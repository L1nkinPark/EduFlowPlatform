variable "content_bucket_name" {
  description = "Name of the S3 content bucket"
  type        = string
}

variable "artifact_bucket_name" {
  description = "Name of the S3 artifact bucket"
  type        = string
}

variable "domain_name" {
  description = "Domain name for CORS configuration"
  type        = string
}

variable "cloudfront_distribution_arn" {
  description = "ARN of the CloudFront distribution for bucket policy"
  type        = string
  default     = ""
}

variable "tags" {
  description = "Common tags"
  type        = map(string)
  default     = {}
}
