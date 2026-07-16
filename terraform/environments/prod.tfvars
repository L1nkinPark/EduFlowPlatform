# ==============================================================================
# EduFlow Platform – Production Environment (prod.tfvars)
# ==============================================================================

project_name = "eduflow"
environment  = "prod"
aws_region   = "ap-southeast-1"

# Networking
vpc_cidr                  = "10.0.0.0/16"
public_subnet_cidrs       = ["10.0.1.0/24", "10.0.2.0/24"]
private_data_subnet_cidrs = ["10.0.3.0/24", "10.0.4.0/24"]
availability_zones        = ["ap-southeast-1a", "ap-southeast-1b"]

# Domain
domain_name = "eduflow.example.com"

# ECS Fargate Tasks
fe_image         = "nginx:alpine"
be_image         = "nginx:alpine"
fe_desired_count = 2
be_desired_count = 2

# RDS MySQL (Multi-AZ can be enabled or disabled; diagram says Disabled, but let's let prod have Multi-AZ = true if they want, or default to false to match diagram strictly)
rds_instance_class          = "db.t4g.micro"
rds_engine_version          = "8.0"
rds_allocated_storage       = 50
rds_max_allocated_storage   = 100
rds_db_name                 = "eduflow_db"
rds_multi_az                = false
rds_backup_retention_period = 7
rds_deletion_protection     = true

# Monitoring
alarm_email = ""
