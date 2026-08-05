---
title: "Dọn dẹp tài nguyên"
date: 2026-08-05
weight: 6
chapter: false
pre: "<b>5.6.</b>"
description: "Hủy hạ tầng lab bằng Terraform và xác minh không còn tài nguyên tính phí."
---

# Dọn dẹp tài nguyên

{{% notice danger %}}
Các lệnh trong phần này xóa database, container services và dữ liệu lab. Chỉ chạy khi đúng AWS account, workspace và state của workshop. Không dùng cho môi trường staging/production.
{{% /notice %}}

## 1. Xác minh mục tiêu

```powershell
aws sts get-caller-identity
Set-Location terraform
terraform workspace show
terraform state list
```

Account phải là sandbox và workspace phải là workspace đã dùng cho workshop.

## 2. Làm rỗng S3 nếu đã tải dữ liệu

```powershell
$taskBucket=(terraform output -raw s3_bucket)
Write-Output "Bucket cần làm rỗng: $taskBucket"
```

Vì bucket bật versioning, dùng nút **Empty** trong S3 Console để xóa cả current version, noncurrent version và delete marker của đúng bucket này. Terraform không thể xóa bucket còn object.

## 3. Lập và thực thi destroy plan

Đảm bảo các biến `TF_VAR_*` giống lúc apply, sau đó:

```powershell
terraform plan -destroy -out=eduflow-destroy.tfplan
terraform show eduflow-destroy.tfplan
terraform apply eduflow-destroy.tfplan
```

Review phải chỉ chứa tài nguyên tiền tố `eduflow-dev` và bucket ID vừa xác minh.

## 4. Xác nhận

```powershell
terraform state list
aws ecs describe-clusters --clusters eduflow-dev-cluster --region $taskAwsRegion
aws rds describe-db-instances --region $taskAwsRegion --query "DBInstances[?contains(DBInstanceIdentifier, 'eduflow-dev')]"
```

`terraform state list` không còn resource. Kiểm tra thêm console Billing/Cost Explorer sau khi AWS hoàn tất xóa RDS, ALB và ENI.

Cuối cùng dừng/xóa MySQL local nếu không dùng nữa:

```powershell
docker stop eduflow-mysql
docker rm eduflow-mysql
```
