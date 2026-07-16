output "cluster_name" {
  description = "Name of the ECS Cluster"
  value       = aws_ecs_cluster.main.name
}

output "frontend_ecr_url" {
  description = "ECR Repository URL for Frontend container"
  value       = aws_ecr_repository.frontend.repository_url
}

output "backend_ecr_url" {
  description = "ECR Repository URL for Backend container"
  value       = aws_ecr_repository.backend.repository_url
}
