output "db_secret_arn" {
  description = "ARN of the database secret"
  value       = aws_secretsmanager_secret.db.arn
}

output "db_secret_id" {
  description = "ID (name) of the database secret"
  value       = aws_secretsmanager_secret.db.id
}

output "db_password" {
  description = "Generated database password"
  value       = random_password.db_password.result
  sensitive   = true
}
