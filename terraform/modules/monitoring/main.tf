# ==============================================================================
# Monitoring Module – CloudWatch Alarms, Dashboard, SNS Topics
# ==============================================================================

# ------------------------------------------------------------------------------
# SNS Topic for Alarm Notifications
# ------------------------------------------------------------------------------
resource "aws_sns_topic" "alarms" {
  name = "${var.name_prefix}-alarms"

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-alarms-topic"
  })
}

resource "aws_sns_topic_subscription" "email" {
  count = var.alarm_email != "" ? 1 : 0

  topic_arn = aws_sns_topic.alarms.arn
  protocol  = "email"
  endpoint  = var.alarm_email
}

# ------------------------------------------------------------------------------
# CloudWatch Alarms
# ------------------------------------------------------------------------------

# Frontend ECS CPU Utilization > 85%
resource "aws_cloudwatch_metric_alarm" "fe_cpu_high" {
  alarm_name          = "${var.name_prefix}-fe-cpu-high"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = 3
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = 300
  statistic           = "Average"
  threshold           = 85
  alarm_description   = "Frontend ECS CPU utilization exceeds 85%"
  alarm_actions       = [aws_sns_topic.alarms.arn]
  ok_actions          = [aws_sns_topic.alarms.arn]

  dimensions = {
    ClusterName = var.ecs_cluster_name
    ServiceName = var.fe_service_name
  }

  tags = var.tags
}

# Backend ECS CPU Utilization > 85%
resource "aws_cloudwatch_metric_alarm" "be_cpu_high" {
  alarm_name          = "${var.name_prefix}-be-cpu-high"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = 3
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = 300
  statistic           = "Average"
  threshold           = 85
  alarm_description   = "Backend ECS CPU utilization exceeds 85%"
  alarm_actions       = [aws_sns_topic.alarms.arn]
  ok_actions          = [aws_sns_topic.alarms.arn]

  dimensions = {
    ClusterName = var.ecs_cluster_name
    ServiceName = var.be_service_name
  }

  tags = var.tags
}

# RDS CPU Utilization > 85%
resource "aws_cloudwatch_metric_alarm" "rds_cpu_high" {
  alarm_name          = "${var.name_prefix}-rds-cpu-high"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = 3
  metric_name         = "CPUUtilization"
  namespace           = "AWS/RDS"
  period              = 300
  statistic           = "Average"
  threshold           = 85
  alarm_description   = "RDS CPU utilization exceeds 85%"
  alarm_actions       = [aws_sns_topic.alarms.arn]
  ok_actions          = [aws_sns_topic.alarms.arn]

  dimensions = {
    DBInstanceIdentifier = var.rds_instance_id
  }

  tags = var.tags
}

# RDS Free Storage Space < 5 GB
resource "aws_cloudwatch_metric_alarm" "rds_storage_low" {
  alarm_name          = "${var.name_prefix}-rds-storage-low"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = 1
  metric_name         = "FreeStorageSpace"
  namespace           = "AWS/RDS"
  period              = 300
  statistic           = "Average"
  threshold           = 5368709120 # 5 GB in bytes
  alarm_description   = "RDS free storage space is less than 5 GB"
  alarm_actions       = [aws_sns_topic.alarms.arn]

  dimensions = {
    DBInstanceIdentifier = var.rds_instance_id
  }

  tags = var.tags
}

# RDS Free Memory < 128 MB (for db.t4g.micro instance size)
resource "aws_cloudwatch_metric_alarm" "rds_memory_low" {
  alarm_name          = "${var.name_prefix}-rds-memory-low"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = 3
  metric_name         = "FreeableMemory"
  namespace           = "AWS/RDS"
  period              = 300
  statistic           = "Average"
  threshold           = 134217728 # 128 MB in bytes
  alarm_description   = "RDS freeable memory is less than 128 MB"
  alarm_actions       = [aws_sns_topic.alarms.arn]

  dimensions = {
    DBInstanceIdentifier = var.rds_instance_id
  }

  tags = var.tags
}

# ------------------------------------------------------------------------------
# CloudWatch Dashboard
# ------------------------------------------------------------------------------
resource "aws_cloudwatch_dashboard" "main" {
  dashboard_name = "${var.name_prefix}-dashboard"

  dashboard_body = jsonencode({
    widgets = [
      # Row 1: ECS Fargate CPU/Memory
      {
        type   = "metric"
        x      = 0
        y      = 0
        width  = 12
        height = 6
        properties = {
          title   = "ECS - Frontend Fargate CPU/Memory"
          region  = var.aws_region
          metrics = [
            ["AWS/ECS", "CPUUtilization", "ServiceName", var.fe_service_name, "ClusterName", var.ecs_cluster_name, {
              stat   = "Average"
              period = 300
            }],
            ["AWS/ECS", "MemoryUtilization", "ServiceName", var.fe_service_name, "ClusterName", var.ecs_cluster_name, {
              stat   = "Average"
              period = 300
            }]
          ]
          view = "timeSeries"
        }
      },
      {
        type   = "metric"
        x      = 12
        y      = 0
        width  = 12
        height = 6
        properties = {
          title   = "ECS - Backend Fargate CPU/Memory"
          region  = var.aws_region
          metrics = [
            ["AWS/ECS", "CPUUtilization", "ServiceName", var.be_service_name, "ClusterName", var.ecs_cluster_name, {
              stat   = "Average"
              period = 300
            }],
            ["AWS/ECS", "MemoryUtilization", "ServiceName", var.be_service_name, "ClusterName", var.ecs_cluster_name, {
              stat   = "Average"
              period = 300
            }]
          ]
          view = "timeSeries"
        }
      },
      # Row 2: RDS Metrics
      {
        type   = "metric"
        x      = 0
        y      = 6
        width  = 12
        height = 6
        properties = {
          title   = "RDS - CPU & Connections"
          region  = var.aws_region
          metrics = [
            ["AWS/RDS", "CPUUtilization", "DBInstanceIdentifier", var.rds_instance_id, {
              stat   = "Average"
              period = 300
            }],
            ["AWS/RDS", "DatabaseConnections", "DBInstanceIdentifier", var.rds_instance_id, {
              stat   = "Average"
              period = 300
            }]
          ]
          view = "timeSeries"
        }
      },
      {
        type   = "metric"
        x      = 12
        y      = 6
        width  = 12
        height = 6
        properties = {
          title   = "RDS - Free Storage & Memory"
          region  = var.aws_region
          metrics = [
            ["AWS/RDS", "FreeStorageSpace", "DBInstanceIdentifier", var.rds_instance_id, {
              stat   = "Average"
              period = 300
            }],
            ["AWS/RDS", "FreeableMemory", "DBInstanceIdentifier", var.rds_instance_id, {
              stat   = "Average"
              period = 300
            }]
          ]
          view = "timeSeries"
        }
      }
    ]
  })
}
