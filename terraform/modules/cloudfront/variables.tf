variable "name_prefix" {
  description = "Prefix for resource names"
  type        = string
}

variable "environment" {
  description = "Deployment environment"
  type        = string
}

variable "domain_name" {
  description = "Domain name for CloudFront aliases"
  type        = string
}

variable "alb_dns_name" {
  description = "DNS name of the ALB"
  type        = string
}

variable "s3_content_domain_name" {
  description = "Regional domain name of the S3 content bucket"
  type        = string
}

variable "acm_certificate_arn" {
  description = "ARN of the ACM certificate (us-east-1) for CloudFront"
  type        = string
  default     = ""
}

variable "waf_web_acl_arn" {
  description = "ARN of the WAF Web ACL"
  type        = string
  default     = ""
}

variable "price_class" {
  description = "CloudFront price class"
  type        = string
  default     = "PriceClass_200"
}

variable "tags" {
  description = "Common tags"
  type        = map(string)
  default     = {}
}
