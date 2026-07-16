# ==============================================================================
# EduFlow Platform – Development Environment (dev.tfvars)
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
domain_name = "dev.eduflow.local"

# ECS Fargate Tasks
fe_image         = "nginx:alpine"
be_image         = "nginx:alpine"
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
