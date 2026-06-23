# ==============================================================================
# ALB Module – Application Load Balancer + Target Group
# ==============================================================================

# ------------------------------------------------------------------------------
# Application Load Balancer
# ------------------------------------------------------------------------------

resource "aws_lb" "main" {
  name               = "${var.name_prefix}-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [var.alb_security_group_id]
  subnets            = var.public_subnet_ids

  enable_deletion_protection = var.enable_deletion_protection

  # Access logging to S3 (optional)
  dynamic "access_logs" {
    for_each = var.access_logs_bucket != "" ? [1] : []
    content {
      bucket  = var.access_logs_bucket
      prefix  = "alb-logs"
      enabled = true
    }
  }

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-alb"
  })
}

# ------------------------------------------------------------------------------
# Target Group
# ------------------------------------------------------------------------------

resource "aws_lb_target_group" "app" {
  name     = "${var.name_prefix}-tg"
  port     = var.app_port
  protocol = "HTTP"
  vpc_id   = var.vpc_id

  health_check {
    enabled             = true
    healthy_threshold   = 3
    unhealthy_threshold = 3
    timeout             = 10
    interval            = 30
    path                = var.health_check_path
    port                = "traffic-port"
    protocol            = "HTTP"
    matcher             = "200"
  }

  stickiness {
    type            = "lb_cookie"
    cookie_duration = 86400
    enabled         = true
  }

  deregistration_delay = 60

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-tg"
  })
}

# ------------------------------------------------------------------------------
# HTTPS Listener (port 443)
# ------------------------------------------------------------------------------

resource "aws_lb_listener" "https" {
  count = var.acm_certificate_arn != "" ? 1 : 0

  load_balancer_arn = aws_lb.main.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn   = var.acm_certificate_arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.app.arn
  }

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-https-listener"
  })
}

# ------------------------------------------------------------------------------
# HTTP Listener (port 80) – Redirect to HTTPS
# ------------------------------------------------------------------------------

resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.main.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type = var.acm_certificate_arn != "" ? "redirect" : "forward"

    # Redirect to HTTPS if certificate is available
    dynamic "redirect" {
      for_each = var.acm_certificate_arn != "" ? [1] : []
      content {
        port        = "443"
        protocol    = "HTTPS"
        status_code = "HTTP_301"
      }
    }

    # Forward to target group if no certificate (dev)
    target_group_arn = var.acm_certificate_arn == "" ? aws_lb_target_group.app.arn : null
  }

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-http-listener"
  })
}
