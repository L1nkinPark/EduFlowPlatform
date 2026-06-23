output "sns_topic_arn" {
  description = "ARN of the SNS alarms topic"
  value       = aws_sns_topic.alarms.arn
}

output "application_log_group_name" {
  description = "Name of the application CloudWatch log group"
  value       = aws_cloudwatch_log_group.application.name
}

output "dashboard_name" {
  description = "Name of the CloudWatch dashboard"
  value       = aws_cloudwatch_dashboard.main.dashboard_name
}
