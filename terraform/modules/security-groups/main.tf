# ==============================================================================
# Security Groups Module – ALB SG, FE SG, BE SG, RDS SG
# ==============================================================================
# Security group rules follow the ECS Fargate design diagram:
#   ALB-SG: Inbound 80/443 from Internet → Outbound 0.0.0.0/0
#   FE-SG:  Inbound 8080 from ALB-SG       → Outbound 0.0.0.0/0 (for AWS service endpoints)
#   BE-SG:  Inbound 8888 from ALB-SG       → Outbound 0.0.0.0/0 (to reach RDS and AWS)
#   RDS-SG: Inbound 3306 from BE-SG only   → Outbound: none
# ==============================================================================

# ------------------------------------------------------------------------------
# ALB Security Group
# ------------------------------------------------------------------------------
resource "aws_security_group" "alb" {
  name        = "${var.name_prefix}-alb-sg"
  description = "Security group for the Application Load Balancer"
  vpc_id      = var.vpc_id

  # Inbound HTTP from Internet
  ingress {
    description = "HTTP from Internet"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Inbound HTTPS from Internet
  ingress {
    description = "HTTPS from Internet"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Outbound to VPC/Internet
  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-alb-sg"
  })
}

# ------------------------------------------------------------------------------
# Frontend ECS Service Security Group
# ------------------------------------------------------------------------------
resource "aws_security_group" "fe" {
  name        = "${var.name_prefix}-fe-sg"
  description = "Security group for Frontend ECS Fargate tasks"
  vpc_id      = var.vpc_id

  # Inbound from ALB only
  ingress {
    description     = "Traffic from ALB on 8080"
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id]
  }

  # Outbound to Internet for ECR pulls, Secrets Manager, CloudWatch Logs
  egress {
    description = "Outbound to Internet"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-fe-sg"
  })
}

# ------------------------------------------------------------------------------
# Backend ECS Service Security Group
# ------------------------------------------------------------------------------
resource "aws_security_group" "be" {
  name        = "${var.name_prefix}-be-sg"
  description = "Security group for Backend ECS Fargate tasks"
  vpc_id      = var.vpc_id

  # Inbound from ALB only
  ingress {
    description     = "Traffic from ALB on 8888"
    from_port       = 8888
    to_port         = 8888
    protocol        = "tcp"
    security_groups = [aws_security_group.alb.id]
  }

  # Outbound to VPC/Internet (allows reaching RDS and AWS API endpoints)
  egress {
    description = "Outbound to VPC/Internet"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-be-sg"
  })
}

# ------------------------------------------------------------------------------
# RDS Security Group
# ------------------------------------------------------------------------------
resource "aws_security_group" "rds" {
  name        = "${var.name_prefix}-rds-sg"
  description = "Security group for RDS MySQL instance"
  vpc_id      = var.vpc_id

  # Inbound from Backend ECS Service only
  ingress {
    description     = "MySQL port from BE ECS Service"
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.be.id]
  }

  # RDS database needs no outbound traffic
  tags = merge(var.tags, {
    Name = "${var.name_prefix}-rds-sg"
  })
}
