#!/bin/bash
set -euo pipefail

# ==============================================================================
# EduFlow EC2 Bootstrap Script
# ==============================================================================
# This script runs on first boot to install dependencies, fetch secrets,
# download the application JAR, and start the Spring Boot service.
# ==============================================================================

exec > >(tee /var/log/user_data.log) 2>&1
echo "[$(date)] Starting EduFlow bootstrap..."

# ------------------------------------------------------------------------------
# 1. Install System Dependencies
# ------------------------------------------------------------------------------

dnf update -y
dnf install -y java-17-amazon-corretto jq amazon-cloudwatch-agent

echo "[$(date)] Java version:"
java -version

# ------------------------------------------------------------------------------
# 2. Create Application User & Directories
# ------------------------------------------------------------------------------

useradd -r -s /sbin/nologin eduflow || true
mkdir -p /opt/eduflow/logs
chown -R eduflow:eduflow /opt/eduflow

# ------------------------------------------------------------------------------
# 3. Fetch Database Secrets from AWS Secrets Manager
# ------------------------------------------------------------------------------

echo "[$(date)] Fetching database secrets..."

DB_SECRET=$(aws secretsmanager get-secret-value \
  --secret-id "${db_secret_id}" \
  --region "${aws_region}" \
  --query 'SecretString' \
  --output text)

DB_HOST=$(echo "$DB_SECRET" | jq -r '.host')
DB_PORT=$(echo "$DB_SECRET" | jq -r '.port // "3306"')
DB_USER=$(echo "$DB_SECRET" | jq -r '.username')
DB_PASS=$(echo "$DB_SECRET" | jq -r '.password')
DB_NAME=$(echo "$DB_SECRET" | jq -r '.dbname // "eduflow_db"')

echo "[$(date)] Database host: $DB_HOST"

# ------------------------------------------------------------------------------
# 4. Download Application JAR from S3
# ------------------------------------------------------------------------------

echo "[$(date)] Downloading application JAR..."
aws s3 cp "s3://${artifact_bucket_name}/backend-0.0.1-SNAPSHOT.jar" \
  /opt/eduflow/app.jar \
  --region "${aws_region}"

chown eduflow:eduflow /opt/eduflow/app.jar

# ------------------------------------------------------------------------------
# 5. Create systemd Service
# ------------------------------------------------------------------------------

cat > /etc/systemd/system/eduflow.service << 'SYSTEMD'
[Unit]
Description=EduFlow Spring Boot Application
After=network.target

[Service]
Type=simple
User=eduflow
Group=eduflow
WorkingDirectory=/opt/eduflow
ExecStart=/usr/bin/java \
  -Xms512m -Xmx1024m \
  -XX:+UseG1GC \
  -Djava.security.egd=file:/dev/./urandom \
  -jar /opt/eduflow/app.jar \
  --server.port=${app_port} \
  --spring.datasource.url=jdbc:mysql://$${DB_HOST}:$${DB_PORT}/$${DB_NAME}?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC \
  --spring.datasource.username=$${DB_USER} \
  --spring.datasource.password=$${DB_PASS} \
  --spring.jpa.hibernate.ddl-auto=update \
  --spring.thymeleaf.check-template-location=false \
  --logging.file.path=/opt/eduflow/logs
ExecStop=/bin/kill -TERM $MAINPID
SuccessExitStatus=143
Restart=on-failure
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=eduflow

# Environment variables for secrets
Environment=DB_HOST=$${DB_HOST}
Environment=DB_PORT=$${DB_PORT}
Environment=DB_USER=$${DB_USER}
Environment=DB_PASS=$${DB_PASS}
Environment=DB_NAME=$${DB_NAME}

[Install]
WantedBy=multi-user.target
SYSTEMD

# Write actual env file with secrets (systemd EnvironmentFile)
cat > /opt/eduflow/.env << EOF
DB_HOST=$DB_HOST
DB_PORT=$DB_PORT
DB_USER=$DB_USER
DB_PASS=$DB_PASS
DB_NAME=$DB_NAME
EOF

chmod 600 /opt/eduflow/.env
chown eduflow:eduflow /opt/eduflow/.env

# Update service to use EnvironmentFile
sed -i '/\[Service\]/a EnvironmentFile=/opt/eduflow/.env' /etc/systemd/system/eduflow.service

# ------------------------------------------------------------------------------
# 6. Configure CloudWatch Agent
# ------------------------------------------------------------------------------

cat > /opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json << EOF
{
  "agent": {
    "metrics_collection_interval": 60,
    "run_as_user": "root"
  },
  "logs": {
    "logs_collected": {
      "files": {
        "collect_list": [
          {
            "file_path": "/opt/eduflow/logs/*.log",
            "log_group_name": "${log_group_name}",
            "log_stream_name": "{instance_id}/application",
            "retention_in_days": 30
          },
          {
            "file_path": "/var/log/user_data.log",
            "log_group_name": "${log_group_name}",
            "log_stream_name": "{instance_id}/bootstrap",
            "retention_in_days": 7
          }
        ]
      }
    }
  },
  "metrics": {
    "namespace": "EduFlow/${environment}",
    "metrics_collected": {
      "mem": {
        "measurement": ["mem_used_percent"],
        "metrics_collection_interval": 60
      },
      "disk": {
        "measurement": ["used_percent"],
        "metrics_collection_interval": 300,
        "resources": ["*"]
      }
    },
    "append_dimensions": {
      "InstanceId": "$${aws:InstanceId}",
      "AutoScalingGroupName": "$${aws:AutoScalingGroupName}"
    }
  }
}
EOF

/opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl \
  -a fetch-config \
  -m ec2 \
  -s \
  -c file:/opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json

# ------------------------------------------------------------------------------
# 7. Start Application
# ------------------------------------------------------------------------------

systemctl daemon-reload
systemctl enable eduflow
systemctl start eduflow

echo "[$(date)] EduFlow bootstrap complete!"
echo "[$(date)] Application starting on port ${app_port}"
