variable "name_prefix" {
  description = "Prefix for resource names"
  type        = string
}

variable "vpc_id" {
  description = "ID of the VPC"
  type        = string
}

variable "app_port" {
  description = "Application port for EC2 instances"
  type        = number
  default     = 8080
}

variable "tags" {
  description = "Common tags for all resources"
  type        = map(string)
  default     = {}
}
