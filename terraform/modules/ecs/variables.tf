variable "name_prefix" {
  description = "Prefix for resource names"
  type        = string
}

variable "vpc_id" {
  description = "ID of the VPC"
  type        = string
}

variable "public_subnet_ids" {
  description = "IDs of the public subnets where ECS tasks will run"
  type        = list(string)
}

variable "fe_security_group_id" {
  description = "Security group ID for Frontend ECS tasks"
  type        = string
}

variable "be_security_group_id" {
  description = "Security group ID for Backend ECS tasks"
  type        = string
}

variable "fe_target_group_arn" {
  description = "Target group ARN for Frontend ALB routing"
  type        = string
}

variable "be_target_group_arn" {
  description = "Target group ARN for Backend ALB routing"
  type        = string
}

variable "db_host" {
  description = "RDS DB Hostname"
  type        = string
}

variable "db_name" {
  description = "RDS DB Name"
  type        = string
}

variable "db_username" {
  description = "RDS DB Username"
  type        = string
}

variable "db_password_arn" {
  description = "Secrets Manager ARN for DB password"
  type        = string
}

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

variable "backend_url" {
  description = "URL of the backend API (ALB DNS name)"
  type        = string
  default     = ""
}

variable "tags" {
  description = "Common tags for all resources"
  type        = map(string)
  default     = {}
}
