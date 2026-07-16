output "alb_arn" {
  description = "ARN of the Application Load Balancer"
  value       = aws_lb.main.arn
}

output "alb_dns_name" {
  description = "DNS name of the Application Load Balancer"
  value       = aws_lb.main.dns_name
}

output "alb_zone_id" {
  description = "Route 53 zone ID of the ALB"
  value       = aws_lb.main.zone_id
}

output "fe_target_group_arn" {
  description = "ARN of the Frontend target group"
  value       = aws_lb_target_group.fe.arn
}

output "be_target_group_arn" {
  description = "ARN of the Backend target group"
  value       = aws_lb_target_group.be.arn
}

output "fe_target_group_name" {
  description = "Name of the Frontend target group"
  value       = aws_lb_target_group.fe.name
}

output "be_target_group_name" {
  description = "Name of the Backend target group"
  value       = aws_lb_target_group.be.name
}

output "http_listener_arn" {
  description = "ARN of the HTTP listener"
  value       = aws_lb_listener.http.arn
}

output "https_listener_arn" {
  description = "ARN of the HTTPS listener (empty if no certificate)"
  value       = length(aws_lb_listener.https) > 0 ? aws_lb_listener.https[0].arn : ""
}
