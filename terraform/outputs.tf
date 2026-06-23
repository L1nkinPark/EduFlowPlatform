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

output "private_app_subnet_ids" {
  description = "Private application subnet IDs"
  value       = module.vpc.private_app_subnet_ids
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
# CloudFront
# ------------------------------------------------------------------------------

output "cloudfront_domain_name" {
  description = "CloudFront distribution domain name"
  value       = module.cloudfront.distribution_domain_name
}

output "cloudfront_distribution_id" {
  description = "CloudFront distribution ID"
  value       = module.cloudfront.distribution_id
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
  description = "Name of the S3 content bucket"
  value       = module.s3.content_bucket_id
}

output "s3_artifact_bucket" {
  description = "Name of the S3 artifact bucket"
  value       = module.s3.artifact_bucket_id
}

# ------------------------------------------------------------------------------
# DNS
# ------------------------------------------------------------------------------

output "route53_name_servers" {
  description = "Name servers for the Route 53 hosted zone"
  value       = module.route53.name_servers
}

output "application_url" {
  description = "Application URL"
  value       = "https://${var.domain_name}"
}

output "api_url" {
  description = "Direct API URL"
  value       = "https://api.${var.domain_name}"
}

# ------------------------------------------------------------------------------
# Monitoring
# ------------------------------------------------------------------------------

output "cloudwatch_dashboard_url" {
  description = "CloudWatch dashboard URL"
  value       = "https://${var.aws_region}.console.aws.amazon.com/cloudwatch/home?region=${var.aws_region}#dashboards:name=${module.monitoring.dashboard_name}"
}

# ------------------------------------------------------------------------------
# Auto Scaling
# ------------------------------------------------------------------------------

output "asg_name" {
  description = "Auto Scaling Group name"
  value       = module.asg.autoscaling_group_name
}
