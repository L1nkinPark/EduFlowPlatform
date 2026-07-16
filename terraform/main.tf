# ==============================================================================
# EduFlow Platform – Root Module
# ==============================================================================
# This is the main entry point that orchestrates all infrastructure modules
# matching the ECS Fargate system design diagram.
# ==============================================================================

# ------------------------------------------------------------------------------
# Providers
# ------------------------------------------------------------------------------
provider "aws" {
  region = var.aws_region

  default_tags {
    tags = local.common_tags
  }
}

# ==============================================================================
# 1. NETWORKING – VPC, Subnets, IGW
# ==============================================================================
module "vpc" {
  source = "./modules/vpc"

  name_prefix               = local.name_prefix
  vpc_cidr                  = var.vpc_cidr
  public_subnet_cidrs       = var.public_subnet_cidrs
  private_app_subnet_cidrs  = [] # Fargate tasks are deployed in public subnets in this design
  private_data_subnet_cidrs = var.private_data_subnet_cidrs
  availability_zones        = var.availability_zones
  tags                      = local.common_tags
}

# ==============================================================================
# 2. SECURITY GROUPS – ALB SG, FE SG, BE SG, RDS SG
# ==============================================================================
module "security_groups" {
  source = "./modules/security-groups"

  name_prefix = local.name_prefix
  vpc_id      = module.vpc.vpc_id
  tags        = local.common_tags
}

# ==============================================================================
# 3. SECRETS MANAGER – DB Secret
# ==============================================================================
module "secrets_manager" {
  source = "./modules/secrets-manager"

  name_prefix = local.name_prefix
  environment = var.environment
  db_host     = module.rds.db_instance_address
  db_port     = 3306
  db_username = "admin"
  db_name     = var.rds_db_name
  tags        = local.common_tags
}

# ==============================================================================
# 4. S3 – Storage Bucket (Static Assets & Backups)
# ==============================================================================
module "s3" {
  source = "./modules/s3"

  bucket_name = local.bucket_name
  domain_name = var.domain_name
  tags        = local.common_tags
}

# ==============================================================================
# 5. RDS – MySQL Database (Single-AZ for Cost Savings)
# ==============================================================================
module "rds" {
  source = "./modules/rds"

  name_prefix             = local.name_prefix
  environment             = var.environment
  private_data_subnet_ids = module.vpc.private_data_subnet_ids
  rds_security_group_id   = module.security_groups.rds_security_group_id
  engine_version          = var.rds_engine_version
  instance_class          = var.rds_instance_class
  allocated_storage       = var.rds_allocated_storage
  max_allocated_storage   = var.rds_max_allocated_storage
  db_name                 = var.rds_db_name
  db_username             = "admin"
  db_password             = module.secrets_manager.db_password
  multi_az                = var.rds_multi_az
  backup_retention_period = var.rds_backup_retention_period
  deletion_protection     = var.rds_deletion_protection
  tags                    = local.common_tags
}

# ==============================================================================
# 6. ALB – Application Load Balancer
# ==============================================================================
module "alb" {
  source = "./modules/alb"

  name_prefix                = local.name_prefix
  vpc_id                     = module.vpc.vpc_id
  public_subnet_ids          = module.vpc.public_subnet_ids
  alb_security_group_id      = module.security_groups.alb_security_group_id
  acm_certificate_arn        = var.acm_certificate_arn
  enable_deletion_protection = var.environment == "prod"
  tags                       = local.common_tags
}

# ==============================================================================
# 7. ECS – Elastic Container Service (Fargate Tasks for Frontend & Backend)
# ==============================================================================
module "ecs" {
  source = "./modules/ecs"

  name_prefix          = local.name_prefix
  vpc_id               = module.vpc.vpc_id
  public_subnet_ids    = module.vpc.public_subnet_ids
  fe_security_group_id = module.security_groups.fe_security_group_id
  be_security_group_id = module.security_groups.be_security_group_id
  fe_target_group_arn  = module.alb.fe_target_group_arn
  be_target_group_arn  = module.alb.be_target_group_arn
  db_host              = module.rds.db_instance_address
  db_name              = var.rds_db_name
  db_username          = "admin"
  db_password_arn      = module.secrets_manager.db_secret_arn
  fe_image             = var.fe_image
  be_image             = var.be_image
  fe_desired_count     = var.fe_desired_count
  be_desired_count     = var.be_desired_count
  tags                 = local.common_tags
}
