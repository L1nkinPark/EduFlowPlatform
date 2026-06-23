# ==============================================================================
# EduFlow Platform – Development Environment
# ==============================================================================
# Usage: terraform plan -var-file=environments/dev.tfvars
# ==============================================================================

project_name = "eduflow"
environment  = "dev"
aws_region   = "ap-southeast-1"

# Networking
vpc_cidr                  = "10.0.0.0/16"
public_subnet_cidrs       = ["10.0.1.0/24", "10.0.4.0/24"]
private_app_subnet_cidrs  = ["10.0.2.0/24", "10.0.5.0/24"]
private_data_subnet_cidrs = ["10.0.3.0/24", "10.0.6.0/24"]
availability_zones        = ["ap-southeast-1a", "ap-southeast-1b"]

# Domain
domain_name     = "dev.eduflow.example.com"
create_dns_zone = true

# EC2 / ASG – Smaller instances for dev
ec2_instance_type    = "t3.small"
ec2_key_pair_name    = ""
asg_min_size         = 1
asg_max_size         = 2
asg_desired_capacity = 1
app_port             = 8080

# RDS – Single-AZ for cost savings in dev
rds_instance_class          = "db.t3.micro"
rds_engine_version          = "8.0"
rds_allocated_storage       = 20
rds_max_allocated_storage   = 50
rds_db_name                 = "eduflow_db"
rds_multi_az                = false
rds_backup_retention_period = 1
rds_deletion_protection     = false

# Monitoring
alarm_email = ""
