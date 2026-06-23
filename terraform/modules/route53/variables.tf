variable "name_prefix" {
  description = "Prefix for resource names"
  type        = string
}

variable "environment" {
  description = "Deployment environment"
  type        = string
}

variable "domain_name" {
  description = "Domain name"
  type        = string
}

variable "create_zone" {
  description = "Whether to create a new hosted zone"
  type        = bool
  default     = true
}

variable "existing_zone_id" {
  description = "ID of existing hosted zone (if not creating a new one)"
  type        = string
  default     = ""
}

variable "cloudfront_domain_name" {
  description = "CloudFront distribution domain name"
  type        = string
  default     = ""
}

variable "cloudfront_hosted_zone_id" {
  description = "CloudFront distribution hosted zone ID"
  type        = string
  default     = ""
}

variable "alb_dns_name" {
  description = "ALB DNS name"
  type        = string
  default     = ""
}

variable "alb_zone_id" {
  description = "ALB hosted zone ID"
  type        = string
  default     = ""
}

variable "acm_domain_validation_options" {
  description = "ACM certificate domain validation options"
  type = list(object({
    domain_name          = string
    resource_record_name = string
    resource_record_value = string
    resource_record_type = string
  }))
  default = []
}

variable "tags" {
  description = "Common tags"
  type        = map(string)
  default     = {}
}
