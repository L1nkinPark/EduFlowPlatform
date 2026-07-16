output "alb_security_group_id" {
  description = "ID of the ALB security group"
  value       = aws_security_group.alb.id
}

output "fe_security_group_id" {
  description = "ID of the Frontend security group"
  value       = aws_security_group.fe.id
}

output "be_security_group_id" {
  description = "ID of the Backend security group"
  value       = aws_security_group.be.id
}

output "rds_security_group_id" {
  description = "ID of the RDS security group"
  value       = aws_security_group.rds.id
}
