# ==============================================================================
# EduFlow Platform – Production Environment
# ==============================================================================
# Usage: terraform plan -var-file=environments/prod.tfvars
# ==============================================================================

project_name = "eduflow"
environment  = "prod"
aws_region   = "ap-southeast-1"

# Networking
vpc_cidr                  = "10.0.0.0/16"
public_subnet_cidrs       = ["10.0.1.0/24", "10.0.4.0/24"]
private_app_subnet_cidrs  = ["10.0.2.0/24", "10.0.5.0/24"]
private_data_subnet_cidrs = ["10.0.3.0/24", "10.0.6.0/24"]
availability_zones        = ["ap-southeast-1a", "ap-southeast-1b"]

# Domain
domain_name     = "eduflow.example.com"
create_dns_zone = true

# EC2 / ASG – Production-grade instances
ec2_instance_type    = "t3.medium"
ec2_key_pair_name    = ""
asg_min_size         = 2
asg_max_size         = 6
asg_desired_capacity = 2
app_port             = 8080

# RDS – Multi-AZ for high availability
rds_instance_class          = "db.t3.medium"
rds_engine_version          = "8.0"
rds_allocated_storage       = 50
rds_max_allocated_storage   = 200
rds_db_name                 = "eduflow_db"
rds_multi_az                = true
rds_backup_retention_period = 7
rds_deletion_protection     = true

# Monitoring
alarm_email = ""
