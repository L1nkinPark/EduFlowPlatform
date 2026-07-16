# ==============================================================================
# ALB Module – Application Load Balancer + Target Groups + Listeners
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

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-alb"
  })
}

# ------------------------------------------------------------------------------
# Target Group – Frontend (Container Port: 8080)
# ------------------------------------------------------------------------------
resource "aws_lb_target_group" "fe" {
  name        = "${var.name_prefix}-fe-tg"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip" # Required for Fargate task integration

  health_check {
    enabled             = true
    healthy_threshold   = 3
    unhealthy_threshold = 3
    timeout             = 5
    interval            = 15
    path                = "/"
    port                = "traffic-port"
    protocol            = "HTTP"
    matcher             = "200,302"
  }

  deregistration_delay = 30

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-fe-tg"
  })
}

# ------------------------------------------------------------------------------
# Target Group – Backend (Container Port: 8888)
# ------------------------------------------------------------------------------
resource "aws_lb_target_group" "be" {
  name        = "${var.name_prefix}-be-tg"
  port        = 8888
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip" # Required for Fargate task integration

  health_check {
    enabled             = true
    healthy_threshold   = 2
    unhealthy_threshold = 3
    timeout             = 5
    interval            = 15
    path                = "/actuator/health"
    port                = "traffic-port"
    protocol            = "HTTP"
    matcher             = "200,401" # Spring security might return 401 on health check depending on configuration
  }

  deregistration_delay = 30

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-be-tg"
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

  # Default Action: Forward to Frontend Target Group
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.fe.arn
  }

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-https-listener"
  })
}

# HTTPS Listener Rule: /api/* -> Backend Target Group
resource "aws_lb_listener_rule" "https_api" {
  count = var.acm_certificate_arn != "" ? 1 : 0

  listener_arn = aws_lb_listener.https[0].arn
  priority     = 10

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.be.arn
  }

  condition {
    path_pattern {
      values = ["/api/*"]
    }
  }
}

# ------------------------------------------------------------------------------
# HTTP Listener (port 80)
# ------------------------------------------------------------------------------
resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.main.arn
  port              = 80
  protocol          = "HTTP"

  # If HTTPS certificate is present: redirect all HTTP traffic to HTTPS (port 443)
  # Otherwise: forward to Frontend target group by default
  default_action {
    type = var.acm_certificate_arn != "" ? "redirect" : "forward"

    dynamic "redirect" {
      for_each = var.acm_certificate_arn != "" ? [1] : []
      content {
        port        = "443"
        protocol    = "HTTPS"
        status_code = "HTTP_301"
      }
    }

    target_group_arn = var.acm_certificate_arn == "" ? aws_lb_target_group.fe.arn : null
  }

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-http-listener"
  })
}

# HTTP Listener Rule: /api/* -> Backend Target Group (Only active when certificate is empty / HTTP only mode)
resource "aws_lb_listener_rule" "http_api" {
  count = var.acm_certificate_arn == "" ? 1 : 0

  listener_arn = aws_lb_listener.http.arn
  priority     = 10

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.be.arn
  }

  condition {
    path_pattern {
      values = ["/api/*"]
    }
  }
}
