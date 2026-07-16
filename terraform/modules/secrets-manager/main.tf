# ==============================================================================
# Secrets Manager Module – Database Secrets
# ==============================================================================

# ------------------------------------------------------------------------------
# Random Password for Database
# ------------------------------------------------------------------------------
resource "random_password" "db_password" {
  length           = 32
  special          = true
  override_special = "!#$%&*()-_=+[]{}<>:?"
}

# ------------------------------------------------------------------------------
# Database Secret
# ------------------------------------------------------------------------------
resource "aws_secretsmanager_secret" "db" {
  name                    = "${var.name_prefix}/db"
  description             = "EduFlow database credentials"
  recovery_window_in_days = var.environment == "prod" ? 30 : 0

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-db-secret"
  })
}

resource "aws_secretsmanager_secret_version" "db" {
  secret_id = aws_secretsmanager_secret.db.id
  secret_string = jsonencode({
    host     = var.db_host
    port     = tostring(var.db_port)
    username = var.db_username
    password = random_password.db_password.result
    dbname   = var.db_name
    engine   = "mysql"
  })

  lifecycle {
    ignore_changes = [secret_string]
  }
}
