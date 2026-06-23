# ==============================================================================
# ASG Module – Launch Template + Auto Scaling Group
# ==============================================================================

# ------------------------------------------------------------------------------
# IAM Role & Instance Profile for EC2
# ------------------------------------------------------------------------------

resource "aws_iam_role" "ec2_app" {
  name = "${var.name_prefix}-ec2-app-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })

  tags = var.tags
}

# S3 read access (download app artifacts + content)
resource "aws_iam_role_policy" "ec2_s3" {
  name = "${var.name_prefix}-ec2-s3-policy"
  role = aws_iam_role.ec2_app.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "s3:GetObject",
          "s3:ListBucket",
          "s3:PutObject",
          "s3:DeleteObject"
        ]
        Resource = [
          "arn:aws:s3:::${var.artifact_bucket_name}",
          "arn:aws:s3:::${var.artifact_bucket_name}/*",
          "arn:aws:s3:::${var.content_bucket_name}",
          "arn:aws:s3:::${var.content_bucket_name}/*"
        ]
      }
    ]
  })
}

# Secrets Manager read access
resource "aws_iam_role_policy" "ec2_secrets" {
  name = "${var.name_prefix}-ec2-secrets-policy"
  role = aws_iam_role.ec2_app.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "secretsmanager:GetSecretValue",
          "secretsmanager:DescribeSecret"
        ]
        Resource = var.secrets_arns
      }
    ]
  })
}

# CloudWatch Logs & Metrics
resource "aws_iam_role_policy_attachment" "ec2_cloudwatch" {
  role       = aws_iam_role.ec2_app.name
  policy_arn = "arn:aws:iam::aws:policy/CloudWatchAgentServerPolicy"
}

# SSM for remote management (optional, recommended over SSH)
resource "aws_iam_role_policy_attachment" "ec2_ssm" {
  role       = aws_iam_role.ec2_app.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_instance_profile" "ec2_app" {
  name = "${var.name_prefix}-ec2-app-profile"
  role = aws_iam_role.ec2_app.name

  tags = var.tags
}

# ------------------------------------------------------------------------------
# Launch Template
# ------------------------------------------------------------------------------

resource "aws_launch_template" "app" {
  name          = "${var.name_prefix}-app-lt"
  image_id      = var.ami_id
  instance_type = var.instance_type

  iam_instance_profile {
    arn = aws_iam_instance_profile.ec2_app.arn
  }

  vpc_security_group_ids = [var.ec2_security_group_id]

  # Key pair for SSH (optional)
  key_name = var.key_pair_name != "" ? var.key_pair_name : null

  # User data script for bootstrapping
  user_data = base64encode(templatefile("${path.module}/user_data.sh", {
    app_port             = var.app_port
    artifact_bucket_name = var.artifact_bucket_name
    db_secret_id         = var.db_secret_id
    aws_region           = var.aws_region
    environment          = var.environment
    log_group_name       = var.log_group_name
  }))

  # EBS root volume
  block_device_mappings {
    device_name = "/dev/xvda"
    ebs {
      volume_size           = 30
      volume_type           = "gp3"
      encrypted             = true
      delete_on_termination = true
    }
  }

  # Enable detailed monitoring
  monitoring {
    enabled = true
  }

  # IMDSv2 (security best practice)
  metadata_options {
    http_endpoint               = "enabled"
    http_tokens                 = "required"
    http_put_response_hop_limit = 2
  }

  tag_specifications {
    resource_type = "instance"
    tags = merge(var.tags, {
      Name = "${var.name_prefix}-app-instance"
    })
  }

  tag_specifications {
    resource_type = "volume"
    tags = merge(var.tags, {
      Name = "${var.name_prefix}-app-volume"
    })
  }

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-app-lt"
  })

  lifecycle {
    create_before_destroy = true
  }
}

# ------------------------------------------------------------------------------
# Auto Scaling Group
# ------------------------------------------------------------------------------

resource "aws_autoscaling_group" "app" {
  name                = "${var.name_prefix}-app-asg"
  min_size            = var.min_size
  max_size            = var.max_size
  desired_capacity    = var.desired_capacity
  vpc_zone_identifier = var.private_app_subnet_ids
  target_group_arns   = [var.target_group_arn]

  # Health check using ALB target group
  health_check_type         = "ELB"
  health_check_grace_period = 300

  # Use the latest launch template version
  launch_template {
    id      = aws_launch_template.app.id
    version = "$Latest"
  }

  # Instance refresh for rolling deployments
  instance_refresh {
    strategy = "Rolling"
    preferences {
      min_healthy_percentage = 50
      instance_warmup        = 120
    }
  }

  # Warm pool for faster scaling (optional)
  # warm_pool {
  #   pool_state                  = "Stopped"
  #   min_size                    = 1
  #   max_group_prepared_capacity = 2
  # }

  tag {
    key                 = "Name"
    value               = "${var.name_prefix}-app-asg"
    propagate_at_launch = false
  }

  dynamic "tag" {
    for_each = var.tags
    content {
      key                 = tag.key
      value               = tag.value
      propagate_at_launch = false
    }
  }

  lifecycle {
    create_before_destroy = true
    ignore_changes        = [desired_capacity]
  }
}

# ------------------------------------------------------------------------------
# Auto Scaling Policies
# ------------------------------------------------------------------------------

# Target Tracking: CPU Utilization
resource "aws_autoscaling_policy" "cpu_target_tracking" {
  name                   = "${var.name_prefix}-cpu-target-tracking"
  autoscaling_group_name = aws_autoscaling_group.app.name
  policy_type            = "TargetTrackingScaling"

  target_tracking_configuration {
    predefined_metric_specification {
      predefined_metric_type = "ASGAverageCPUUtilization"
    }
    target_value     = 70.0
    disable_scale_in = false
  }
}

# Target Tracking: ALB Request Count per Target
resource "aws_autoscaling_policy" "request_count_tracking" {
  name                   = "${var.name_prefix}-request-count-tracking"
  autoscaling_group_name = aws_autoscaling_group.app.name
  policy_type            = "TargetTrackingScaling"

  target_tracking_configuration {
    predefined_metric_specification {
      predefined_metric_type = "ALBRequestCountPerTarget"
      resource_label         = "${var.alb_arn_suffix}/${var.target_group_arn_suffix}"
    }
    target_value     = 1000.0
    disable_scale_in = false
  }
}
