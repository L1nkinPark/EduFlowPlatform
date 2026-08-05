---
title: "Bootstrap ECR và đẩy image"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.4.2.</b>"
description: "Tạo hai repository trước, đăng nhập ECR và push container EduFlow."
---

# Bootstrap ECR và đẩy image

Terraform định nghĩa ECR và ECS trong cùng module. Ta tạo riêng hai ECR resource một lần, push image, rồi quay lại full plan.

## 1. Bootstrap repository

Từ thư mục `terraform`:

```powershell
terraform apply `
  -target=module.ecs.aws_ecr_repository.frontend `
  -target=module.ecs.aws_ecr_repository.backend
```

Đọc plan và chỉ nhập `yes` khi đúng hai ECR repository cùng dependency dữ liệu cần thiết được tạo.

{{% notice warning %}}
`-target` chỉ dùng cho bước bootstrap có chủ đích này. Luôn chạy full plan ngay sau khi image đã tồn tại để Terraform đồng bộ toàn bộ graph.
{{% /notice %}}

## 2. Đăng nhập ECR

```powershell
$taskRegistry="$taskAccountId.dkr.ecr.$taskAwsRegion.amazonaws.com"
$taskFeImage="$taskRegistry/eduflow-dev-frontend:latest"
$taskBeImage="$taskRegistry/eduflow-dev-backend:latest"
aws ecr get-login-password --region $taskAwsRegion |
  docker login --username AWS --password-stdin $taskRegistry
```

## 3. Build và push

Từ thư mục gốc repository:

```powershell
docker build -t $taskFeImage .\FE_EduFlow
docker build -t $taskBeImage .\BE_EduFlow
docker push $taskFeImage
docker push $taskBeImage
```

Xác minh:

```powershell
aws ecr describe-images --repository-name eduflow-dev-frontend --region $taskAwsRegion
aws ecr describe-images --repository-name eduflow-dev-backend --region $taskAwsRegion
```
