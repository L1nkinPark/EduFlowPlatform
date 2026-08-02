output "db_secret_arn" {
  description = "ARN of the database secret"
  value       = aws_secretsmanager_secret.db.arn
}

output "db_secret_id" {
  description = "ID (name) of the database secret"
  value       = aws_secretsmanager_secret.db.id
}

output "db_secret_version_id" {
  description = "Current secret version ID used to refresh ECS task definitions"
  value       = aws_secretsmanager_secret_version.db.version_id
}

output "db_password" {
  description = "Generated database password"
  value       = random_password.db_password.result
  sensitive   = true
}
