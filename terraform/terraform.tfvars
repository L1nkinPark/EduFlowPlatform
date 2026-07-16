# ==============================================================================
# EduFlow Platform – Default Terraform Variables (ECS Fargate)
# ==============================================================================

project_name = "eduflow"
environment  = "dev"
aws_region   = "ap-southeast-1"

# Networking
vpc_cidr                  = "10.0.0.0/16"
public_subnet_cidrs       = ["10.0.1.0/24", "10.0.2.0/24"]
private_data_subnet_cidrs = ["10.0.3.0/24", "10.0.4.0/24"]
availability_zones        = ["ap-southeast-1a", "ap-southeast-1b"]

# Domain
domain_name = "eduflow.local"

# ECS Fargate Tasks (ECR Repositories)
fe_image         = "379098698141.dkr.ecr.ap-southeast-1.amazonaws.com/eduflow-dev-frontend:latest"
be_image         = "379098698141.dkr.ecr.ap-southeast-1.amazonaws.com/eduflow-dev-backend:latest"
fe_desired_count = 1
be_desired_count = 1

# RDS MySQL (Tuned to db.t4g.micro & Multi-AZ: Disabled)
rds_instance_class          = "db.t4g.micro"
rds_engine_version          = "8.0"
rds_allocated_storage       = 20
rds_max_allocated_storage   = 50
rds_db_name                 = "eduflow_db"
rds_multi_az                = false
rds_backup_retention_period = 1
rds_deletion_protection     = false

# Monitoring
alarm_email = ""

# SMTP Credentials
smtp_username = "voduchieu42@gmail.com"
smtp_password = "nguv iwqw fnit auea"
