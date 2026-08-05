---
title: "Cấu hình AWS và Terraform"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>5.4.1.</b>"
description: "Xác minh account, truyền secret an toàn và validate mã hạ tầng."
---

# Cấu hình AWS và Terraform

## 1. Xác minh phiên AWS

```powershell
$taskAwsRegion='ap-southeast-1'
$env:AWS_REGION=$taskAwsRegion
$taskAccountId=(aws sts get-caller-identity --query Account --output text)
aws sts get-caller-identity
```

Đảm bảo account ID đúng sandbox trước khi tiếp tục.

## 2. Truyền giá trị nhạy cảm

```powershell
$env:TF_VAR_smtp_username='otp-sandbox@example.com'
$env:TF_VAR_smtp_password='<smtp-app-password>'
$env:TF_VAR_vnpay_tmn_code='<8-character-code>'
$env:TF_VAR_vnpay_hash_secret='<sandbox-hash-secret-at-least-16-characters>'
```

Tùy chọn HTTPS:

```powershell
$env:TF_VAR_domain_name='eduflow.example.com'
$env:TF_VAR_acm_certificate_arn=''
```

Để ARN trống cho workshop HTTP. Chứng chỉ ACM phải nằm cùng region với ALB.

## 3. Khởi tạo và kiểm tra

```powershell
Set-Location terraform
terraform fmt -check -recursive
terraform init -input=false
terraform validate
```

{{% notice info %}}
State local phù hợp cho lab cá nhân. Với môi trường nhóm/production, cấu hình backend S3 và cơ chế locking trước khi apply.
{{% /notice %}}
