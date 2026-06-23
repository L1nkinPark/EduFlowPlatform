# ==============================================================================
# Security Groups Module – ALB SG, EC2 SG, RDS SG
# ==============================================================================
# Security group rules follow the system design diagram:
#   ALB SG:  Inbound 80/443 from Internet    → Outbound app_port to EC2 SG
#   EC2 SG:  Inbound app_port from ALB SG    → Outbound 3306 to RDS SG, 443 to S3/Secrets
#   RDS SG:  Inbound 3306 from EC2 SG        → Outbound: none
# ==============================================================================

# ------------------------------------------------------------------------------
# ALB Security Group
# ------------------------------------------------------------------------------

resource "aws_security_group" "alb" {
  name        = "${var.name_prefix}-alb-sg"
  description = "Security group for the Application Load Balancer"
  vpc_id      = var.vpc_id

  # Inbound: HTTP from Internet
  ingress {
    description = "HTTP from Internet"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Inbound: HTTPS from Internet
  ingress {
    description = "HTTPS from Internet"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Outbound: Application port to EC2 instances
  egress {
    description     = "App traffic to EC2 instances"
    from_port       = var.app_port
    to_port         = var.app_port
    protocol        = "tcp"
    security_groups = [aws_security_group.ec2.id]
  }

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-alb-sg"
  })
}

# ------------------------------------------------------------------------------
# EC2 Security Group
# ------------------------------------------------------------------------------

resource "aws_security_group" "ec2" {
  name        = "${var.name_prefix}-ec2-sg"
  description = "Security group for EC2 application instances"
  vpc_id      = var.vpc_id

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-ec2-sg"
  })
}

# Inbound: Application port from ALB
resource "aws_security_group_rule" "ec2_inbound_alb" {
  type                     = "ingress"
  from_port                = var.app_port
  to_port                  = var.app_port
  protocol                 = "tcp"
  description              = "App traffic from ALB"
  security_group_id        = aws_security_group.ec2.id
  source_security_group_id = aws_security_group.alb.id
}

# Outbound: MySQL to RDS
resource "aws_security_group_rule" "ec2_outbound_rds" {
  type                     = "egress"
  from_port                = 3306
  to_port                  = 3306
  protocol                 = "tcp"
  description              = "MySQL traffic to RDS"
  security_group_id        = aws_security_group.ec2.id
  source_security_group_id = aws_security_group.rds.id
}

# Outbound: HTTPS to S3 and Secrets Manager (via VPC endpoints or NAT)
resource "aws_security_group_rule" "ec2_outbound_https" {
  type              = "egress"
  from_port         = 443
  to_port           = 443
  protocol          = "tcp"
  description       = "HTTPS to AWS services (S3, Secrets Manager, CloudWatch)"
  security_group_id = aws_security_group.ec2.id
  cidr_blocks       = ["0.0.0.0/0"]
}

# Outbound: HTTP for package updates
resource "aws_security_group_rule" "ec2_outbound_http" {
  type              = "egress"
  from_port         = 80
  to_port           = 80
  protocol          = "tcp"
  description       = "HTTP for package updates"
  security_group_id = aws_security_group.ec2.id
  cidr_blocks       = ["0.0.0.0/0"]
}

# ------------------------------------------------------------------------------
# RDS Security Group
# ------------------------------------------------------------------------------

resource "aws_security_group" "rds" {
  name        = "${var.name_prefix}-rds-sg"
  description = "Security group for RDS MySQL instances"
  vpc_id      = var.vpc_id

  # Inbound: MySQL from EC2 instances
  ingress {
    description     = "MySQL from EC2 instances"
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.ec2.id]
  }

  # No outbound rules – deny all egress
  # (RDS doesn't need outbound access)

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-rds-sg"
  })
}
