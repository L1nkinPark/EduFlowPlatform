output "content_bucket_id" {
  description = "ID of the content S3 bucket"
  value       = aws_s3_bucket.content.id
}

output "content_bucket_arn" {
  description = "ARN of the content S3 bucket"
  value       = aws_s3_bucket.content.arn
}

output "content_bucket_domain_name" {
  description = "Regional domain name of the content S3 bucket"
  value       = aws_s3_bucket.content.bucket_regional_domain_name
}

output "artifact_bucket_id" {
  description = "ID of the artifact S3 bucket"
  value       = aws_s3_bucket.artifacts.id
}

output "artifact_bucket_arn" {
  description = "ARN of the artifact S3 bucket"
  value       = aws_s3_bucket.artifacts.arn
}
