# ==============================================================================
# EduFlow Platform – Root Module
# ==============================================================================
# This is the main entry point that orchestrates all infrastructure modules
# matching the system design diagram.
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

# WAF & CloudFront ACM must be in us-east-1
provider "aws" {
  alias  = "us_east_1"
  region = "us-east-1"

  default_tags {
    tags = local.common_tags
  }
}

# ==============================================================================
# 1. NETWORKING – VPC, Subnets, IGW, NAT
# ==============================================================================

module "vpc" {
  source = "./modules/vpc"

  name_prefix               = local.name_prefix
  vpc_cidr                  = var.vpc_cidr
  public_subnet_cidrs       = var.public_subnet_cidrs
  private_app_subnet_cidrs  = var.private_app_subnet_cidrs
  private_data_subnet_cidrs = var.private_data_subnet_cidrs
  availability_zones        = var.availability_zones
  tags                      = local.common_tags
}

# ==============================================================================
# 2. SECURITY GROUPS – ALB SG, EC2 SG, RDS SG
# ==============================================================================

module "security_groups" {
  source = "./modules/security-groups"

  name_prefix = local.name_prefix
  vpc_id      = module.vpc.vpc_id
  app_port    = var.app_port
  tags        = local.common_tags
}

# ==============================================================================
# 3. SECRETS MANAGER – DB & App Secrets
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
# 4. S3 – Content & Artifact Buckets
# ==============================================================================

module "s3" {
  source = "./modules/s3"

  content_bucket_name        = local.content_bucket_name
  artifact_bucket_name       = local.artifact_bucket_name
  domain_name                = var.domain_name
  cloudfront_distribution_arn = module.cloudfront.distribution_arn
  tags                       = local.common_tags
}

# ==============================================================================
# 5. RDS – MySQL Multi-AZ
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
  app_port                   = var.app_port
  health_check_path          = "/actuator/health"
  acm_certificate_arn        = aws_acm_certificate.alb[0].arn
  enable_deletion_protection = var.environment == "prod"
  tags                       = local.common_tags
}

# ==============================================================================
# 7. ASG – Auto Scaling Group + Launch Template
# ==============================================================================

module "asg" {
  source = "./modules/asg"

  name_prefix            = local.name_prefix
  environment            = var.environment
  aws_region             = var.aws_region
  ami_id                 = data.aws_ami.amazon_linux_2023.id
  instance_type          = var.ec2_instance_type
  key_pair_name          = var.ec2_key_pair_name
  ec2_security_group_id  = module.security_groups.ec2_security_group_id
  private_app_subnet_ids = module.vpc.private_app_subnet_ids
  target_group_arn       = module.alb.target_group_arn
  alb_arn_suffix         = ""
  target_group_arn_suffix = ""
  min_size               = var.asg_min_size
  max_size               = var.asg_max_size
  desired_capacity       = var.asg_desired_capacity
  app_port               = var.app_port
  artifact_bucket_name   = local.artifact_bucket_name
  content_bucket_name    = local.content_bucket_name
  db_secret_id           = module.secrets_manager.db_secret_id
  secrets_arns           = module.secrets_manager.all_secret_arns
  log_group_name         = module.monitoring.application_log_group_name
  tags                   = local.common_tags
}

# ==============================================================================
# 8. WAF – Web Application Firewall (us-east-1 for CloudFront)
# ==============================================================================

module "waf" {
  source = "./modules/waf"

  providers = {
    aws = aws.us_east_1
  }

  name_prefix = local.name_prefix
  rate_limit  = 2000
  tags        = local.common_tags
}

# ==============================================================================
# 9. CLOUDFRONT – CDN Distribution
# ==============================================================================

module "cloudfront" {
  source = "./modules/cloudfront"

  name_prefix            = local.name_prefix
  environment            = var.environment
  domain_name            = var.domain_name
  alb_dns_name           = module.alb.alb_dns_name
  s3_content_domain_name = module.s3.content_bucket_domain_name
  acm_certificate_arn    = aws_acm_certificate.cloudfront[0].arn
  waf_web_acl_arn        = module.waf.web_acl_arn
  tags                   = local.common_tags
}

# ==============================================================================
# 10. ROUTE 53 – DNS
# ==============================================================================

module "route53" {
  source = "./modules/route53"

  name_prefix                   = local.name_prefix
  environment                   = var.environment
  domain_name                   = var.domain_name
  create_zone                   = var.create_dns_zone
  cloudfront_domain_name        = module.cloudfront.distribution_domain_name
  cloudfront_hosted_zone_id     = module.cloudfront.distribution_hosted_zone_id
  alb_dns_name                  = module.alb.alb_dns_name
  alb_zone_id                   = module.alb.alb_zone_id
  acm_domain_validation_options = aws_acm_certificate.cloudfront[0].domain_validation_options
  tags                          = local.common_tags
}

# ==============================================================================
# 11. MONITORING – CloudWatch Alarms, Dashboard, Log Groups
# ==============================================================================

module "monitoring" {
  source = "./modules/monitoring"

  name_prefix            = local.name_prefix
  environment            = var.environment
  aws_region             = var.aws_region
  alarm_email            = var.alarm_email
  asg_name               = module.asg.autoscaling_group_name
  rds_instance_id        = module.rds.db_instance_id
  alb_arn_suffix         = ""
  target_group_arn_suffix = ""
  tags                   = local.common_tags
}

# ==============================================================================
# ACM Certificates
# ==============================================================================

# Certificate for ALB (regional)
resource "aws_acm_certificate" "alb" {
  count = 1

  domain_name       = var.domain_name
  validation_method = "DNS"

  subject_alternative_names = [
    "*.${var.domain_name}"
  ]

  lifecycle {
    create_before_destroy = true
  }

  tags = merge(local.common_tags, {
    Name = "${local.name_prefix}-alb-cert"
  })
}

# Certificate for CloudFront (must be us-east-1)
resource "aws_acm_certificate" "cloudfront" {
  count    = 1
  provider = aws.us_east_1

  domain_name       = var.domain_name
  validation_method = "DNS"

  subject_alternative_names = [
    "*.${var.domain_name}"
  ]

  lifecycle {
    create_before_destroy = true
  }

  tags = merge(local.common_tags, {
    Name = "${local.name_prefix}-cf-cert"
  })
}

# ACM Certificate Validation
resource "aws_acm_certificate_validation" "alb" {
  count = 1

  certificate_arn         = aws_acm_certificate.alb[0].arn
  validation_record_fqdns = module.route53.acm_validation_record_fqdns
}

resource "aws_acm_certificate_validation" "cloudfront" {
  count    = 1
  provider = aws.us_east_1

  certificate_arn         = aws_acm_certificate.cloudfront[0].arn
  validation_record_fqdns = module.route53.acm_validation_record_fqdns
}
