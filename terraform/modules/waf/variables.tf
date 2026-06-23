variable "name_prefix" {
  description = "Prefix for resource names"
  type        = string
}

variable "rate_limit" {
  description = "Maximum requests per 5-minute period per IP"
  type        = number
  default     = 2000
}

variable "tags" {
  description = "Common tags"
  type        = map(string)
  default     = {}
}
