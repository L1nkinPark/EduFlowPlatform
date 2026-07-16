# ==============================================================================
# Outputs – Key information from the deployed infrastructure
# ==============================================================================

# ------------------------------------------------------------------------------
# Networking
# ------------------------------------------------------------------------------
output "vpc_id" {
  description = "ID of the VPC"
  value       = module.vpc.vpc_id
}

output "public_subnet_ids" {
  description = "Public subnet IDs"
  value       = module.vpc.public_subnet_ids
}

output "private_data_subnet_ids" {
  description = "Private data subnet IDs"
  value       = module.vpc.private_data_subnet_ids
}

# ------------------------------------------------------------------------------
# Load Balancer
# ------------------------------------------------------------------------------
output "alb_dns_name" {
  description = "DNS name of the Application Load Balancer"
  value       = module.alb.alb_dns_name
}

# ------------------------------------------------------------------------------
# Database
# ------------------------------------------------------------------------------
output "rds_endpoint" {
  description = "RDS instance endpoint (host:port)"
  value       = module.rds.db_instance_endpoint
}

output "rds_address" {
  description = "RDS instance hostname"
  value       = module.rds.db_instance_address
}

# ------------------------------------------------------------------------------
# S3 Buckets
# ------------------------------------------------------------------------------
output "s3_content_bucket" {
  description = "Name of the S3 content/backup bucket"
  value       = module.s3.content_bucket_id
}

# ------------------------------------------------------------------------------
# ECS / ECR
# ------------------------------------------------------------------------------
output "ecs_cluster_name" {
  description = "ECS cluster name"
  value       = module.ecs.cluster_name
}

output "frontend_ecr_url" {
  description = "ECR Repository URL for Frontend container"
  value       = module.ecs.frontend_ecr_url
}

output "backend_ecr_url" {
  description = "ECR Repository URL for Backend container"
  value       = module.ecs.backend_ecr_url
}

# ------------------------------------------------------------------------------
# Monitoring
# ------------------------------------------------------------------------------
output "cloudwatch_dashboard_url" {
  description = "CloudWatch dashboard URL"
  value       = "https://${var.aws_region}.console.aws.amazon.com/cloudwatch/home?region=${var.aws_region}#dashboards:name=${local.name_prefix}-dashboard"
}
