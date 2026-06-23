variable "name_prefix" {
  description = "Prefix for resource names"
  type        = string
}

variable "environment" {
  description = "Deployment environment"
  type        = string
}

variable "aws_region" {
  description = "AWS region"
  type        = string
}

variable "ami_id" {
  description = "AMI ID for EC2 instances"
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.medium"
}

variable "key_pair_name" {
  description = "EC2 key pair name for SSH access"
  type        = string
  default     = ""
}

variable "ec2_security_group_id" {
  description = "Security group ID for EC2 instances"
  type        = string
}

variable "private_app_subnet_ids" {
  description = "Private application subnet IDs"
  type        = list(string)
}

variable "target_group_arn" {
  description = "ARN of the ALB target group"
  type        = string
}

variable "alb_arn_suffix" {
  description = "ARN suffix of the ALB (for scaling policy)"
  type        = string
  default     = ""
}

variable "target_group_arn_suffix" {
  description = "ARN suffix of the target group (for scaling policy)"
  type        = string
  default     = ""
}

variable "min_size" {
  description = "Minimum ASG size"
  type        = number
  default     = 2
}

variable "max_size" {
  description = "Maximum ASG size"
  type        = number
  default     = 6
}

variable "desired_capacity" {
  description = "Desired ASG capacity"
  type        = number
  default     = 2
}

variable "app_port" {
  description = "Application port"
  type        = number
  default     = 8080
}

variable "artifact_bucket_name" {
  description = "S3 bucket name for application artifacts"
  type        = string
}

variable "content_bucket_name" {
  description = "S3 bucket name for content storage"
  type        = string
}

variable "db_secret_id" {
  description = "Secrets Manager secret ID for database credentials"
  type        = string
}

variable "secrets_arns" {
  description = "List of Secrets Manager ARNs the EC2 instances can access"
  type        = list(string)
  default     = []
}

variable "log_group_name" {
  description = "CloudWatch log group name for application logs"
  type        = string
  default     = "/eduflow/application"
}

variable "tags" {
  description = "Common tags"
  type        = map(string)
  default     = {}
}
