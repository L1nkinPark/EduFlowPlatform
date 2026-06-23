output "db_secret_arn" {
  description = "ARN of the database secret"
  value       = aws_secretsmanager_secret.db.arn
}

output "db_secret_id" {
  description = "ID (name) of the database secret"
  value       = aws_secretsmanager_secret.db.id
}

output "app_secret_arn" {
  description = "ARN of the application secret"
  value       = aws_secretsmanager_secret.app.arn
}

output "app_secret_id" {
  description = "ID (name) of the application secret"
  value       = aws_secretsmanager_secret.app.id
}

output "db_password" {
  description = "Generated database password"
  value       = random_password.db_password.result
  sensitive   = true
}

output "all_secret_arns" {
  description = "List of all secret ARNs for IAM policies"
  value = [
    aws_secretsmanager_secret.db.arn,
    aws_secretsmanager_secret.app.arn
  ]
}
