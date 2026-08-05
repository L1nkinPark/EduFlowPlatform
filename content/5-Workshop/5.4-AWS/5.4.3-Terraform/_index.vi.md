---
title: "Tạo hạ tầng và ECS services"
date: 2026-08-05
weight: 3
chapter: false
pre: "<b>5.4.3.</b>"
description: "Lập full plan, apply VPC/ALB/RDS/ECS và xác minh target health."
---

# Tạo hạ tầng đầy đủ

## 1. Gắn image vào Terraform

```powershell
$env:TF_VAR_fe_image=$taskFeImage
$env:TF_VAR_be_image=$taskBeImage
Set-Location terraform
```

## 2. Review plan

```powershell
terraform plan -out=eduflow.tfplan
terraform show eduflow.tfplan
```

Kiểm tra các điểm sau trước khi apply:

- Region `ap-southeast-1`, environment `dev`.
- Hai public subnet và hai private data subnet.
- RDS `db.t4g.micro`, Single-AZ, 20 GB cho lab.
- Hai task definition dùng đúng ECR URL và port 8080/8888.
- Không có secret plaintext trong plan output.

## 3. Apply

```powershell
terraform apply eduflow.tfplan
```

RDS và ECS có thể cần 10-20 phút. Khi hoàn thành:

```powershell
terraform output
$taskAlbDns=(terraform output -raw alb_dns_name)
Invoke-WebRequest "http://$taskAlbDns/" -UseBasicParsing
Invoke-RestMethod "http://$taskAlbDns/api/public/stats"
```

## 4. Kiểm tra ECS

```powershell
aws ecs describe-services `
  --cluster eduflow-dev-cluster `
  --services eduflow-dev-frontend eduflow-dev-backend `
  --region $taskAwsRegion `
  --query 'services[].{name:serviceName,running:runningCount,desired:desiredCount,status:status}'
```

Cả hai service cần `running = desired = 1`. Nếu chưa ổn định, xem events của service và `/ecs/eduflow-dev-frontend`, `/ecs/eduflow-dev-backend` trong CloudWatch Logs.
