variable "name_prefix" {
  description = "Prefix for resource names"
  type        = string
}

variable "vpc_id" {
  description = "ID of the VPC"
  type        = string
}

variable "public_subnet_ids" {
  description = "IDs of the public subnets for the ALB"
  type        = list(string)
}

variable "alb_security_group_id" {
  description = "Security group ID for the ALB"
  type        = string
}

variable "app_port" {
  description = "Port on which the application listens"
  type        = number
  default     = 8080
}

variable "health_check_path" {
  description = "Path for ALB health checks"
  type        = string
  default     = "/actuator/health"
}

variable "acm_certificate_arn" {
  description = "ARN of the ACM certificate for HTTPS. Empty string for HTTP only."
  type        = string
  default     = ""
}

variable "enable_deletion_protection" {
  description = "Whether to enable deletion protection for the ALB"
  type        = bool
  default     = false
}

variable "access_logs_bucket" {
  description = "S3 bucket name for ALB access logs. Empty to disable."
  type        = string
  default     = ""
}

variable "tags" {
  description = "Common tags for all resources"
  type        = map(string)
  default     = {}
}
