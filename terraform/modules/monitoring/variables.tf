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

variable "alarm_email" {
  description = "Email for alarm notifications"
  type        = string
  default     = ""
}

variable "asg_name" {
  description = "Auto Scaling Group name"
  type        = string
}

variable "rds_instance_id" {
  description = "RDS instance identifier"
  type        = string
}

variable "alb_arn_suffix" {
  description = "ALB ARN suffix for metrics"
  type        = string
}

variable "target_group_arn_suffix" {
  description = "Target Group ARN suffix for metrics"
  type        = string
}

variable "tags" {
  description = "Common tags"
  type        = map(string)
  default     = {}
}
