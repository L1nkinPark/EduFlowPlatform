# ==============================================================================
# General
# ==============================================================================

variable "project_name" {
  description = "Name of the project, used for resource naming and tagging"
  type        = string
  default     = "eduflow"
}

variable "environment" {
  description = "Deployment environment (dev, staging, prod)"
  type        = string
  default     = "dev"

  validation {
    condition     = contains(["dev", "staging", "prod"], var.environment)
    error_message = "Environment must be one of: dev, staging, prod."
  }
}

variable "aws_region" {
  description = "AWS region for resource deployment"
  type        = string
  default     = "ap-southeast-1"
}

# ==============================================================================
# Networking
# ==============================================================================

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "public_subnet_cidrs" {
  description = "CIDR blocks for public subnets (one per AZ)"
  type        = list(string)
  default     = ["10.0.1.0/24", "10.0.2.0/24"]
}

variable "private_data_subnet_cidrs" {
  description = "CIDR blocks for private data subnets (one per AZ)"
  type        = list(string)
  default     = ["10.0.3.0/24", "10.0.4.0/24"]
}

variable "availability_zones" {
  description = "List of availability zones to use"
  type        = list(string)
  default     = ["ap-southeast-1a", "ap-southeast-1b"]
}

# ==============================================================================
# Domain & SSL
# ==============================================================================

variable "domain_name" {
  description = "Primary domain name for the application"
  type        = string
  default     = "eduflow.example.com"
}

variable "acm_certificate_arn" {
  description = "ARN of the ACM certificate for HTTPS (empty for HTTP only)"
  type        = string
  default     = ""
}

# ==============================================================================
# ECS Fargate
# ==============================================================================

variable "fe_image" {
  description = "Docker image for the Frontend task"
  type        = string
  default     = "nginx:alpine"
}

variable "be_image" {
  description = "Docker image for the Backend task"
  type        = string
  default     = "nginx:alpine"
}

variable "fe_desired_count" {
  description = "Desired number of Frontend tasks"
  type        = number
  default     = 1
}

variable "be_desired_count" {
  description = "Desired number of Backend tasks"
  type        = number
  default     = 1
}

# ==============================================================================
# RDS
# ==============================================================================

variable "rds_instance_class" {
  description = "RDS instance class"
  type        = string
  default     = "db.t4g.micro"
}

variable "rds_engine_version" {
  description = "MySQL engine version for RDS"
  type        = string
  default     = "8.0"
}

variable "rds_allocated_storage" {
  description = "Initial allocated storage in GB"
  type        = number
  default     = 20
}

variable "rds_max_allocated_storage" {
  description = "Maximum allocated storage in GB for autoscaling"
  type        = number
  default     = 50
}

variable "rds_db_name" {
  description = "Name of the database to create"
  type        = string
  default     = "eduflow_db"
}

variable "rds_multi_az" {
  description = "Whether to enable Multi-AZ deployment for RDS"
  type        = bool
  default     = false
}

variable "rds_backup_retention_period" {
  description = "Number of days to retain RDS backups"
  type        = number
  default     = 1
}

variable "rds_deletion_protection" {
  description = "Whether to enable deletion protection on the RDS instance"
  type        = bool
  default     = false
}

# ==============================================================================
# Monitoring
# ==============================================================================

variable "alarm_email" {
  description = "Email address for CloudWatch alarm notifications"
  type        = string
  default     = ""
}

# ==============================================================================
# Tags
# ==============================================================================

variable "additional_tags" {
  description = "Additional tags to apply to all resources"
  type        = map(string)
  default     = {}
}

# ==============================================================================
# SMTP Credentials
# ==============================================================================

variable "smtp_username" {
  description = "SMTP username (email)"
  type        = string
  default     = "voduchieu42@gmail.com"
}

variable "smtp_password" {
  description = "SMTP App Password"
  type        = string
  sensitive   = true
}
