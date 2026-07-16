variable "name_prefix" {
  description = "Prefix for resource names"
  type        = string
}

variable "environment" {
  description = "Deployment environment"
  type        = string
}

variable "db_host" {
  description = "Database host address"
  type        = string
  default     = ""
}

variable "db_port" {
  description = "Database port"
  type        = number
  default     = 3306
}

variable "db_username" {
  description = "Database username"
  type        = string
  default     = "admin"
}

variable "db_name" {
  description = "Database name"
  type        = string
  default     = "eduflow_db"
}

variable "tags" {
  description = "Common tags"
  type        = map(string)
  default     = {}
}

variable "smtp_username" {
  description = "SMTP username"
  type        = string
  default     = ""
}

variable "smtp_password" {
  description = "SMTP App Password"
  type        = string
  default     = ""
}
