---
title: "Configure AWS and Terraform"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>5.4.1.</b>"
description: "Verify the account, pass secrets safely, and validate infrastructure code."
---

# Configure AWS and Terraform

## 1. Verify the AWS session

```powershell
$taskAwsRegion='ap-southeast-1'
$env:AWS_REGION=$taskAwsRegion
$taskAccountId=(aws sts get-caller-identity --query Account --output text)
aws sts get-caller-identity
```

Confirm that the account ID is the intended sandbox before proceeding.

## 2. Supply sensitive values

```powershell
$env:TF_VAR_smtp_username='otp-sandbox@example.com'
$env:TF_VAR_smtp_password='<smtp-app-password>'
$env:TF_VAR_vnpay_tmn_code='<8-character-code>'
$env:TF_VAR_vnpay_hash_secret='<sandbox-hash-secret-at-least-16-characters>'
```

Optional HTTPS configuration:

```powershell
$env:TF_VAR_domain_name='eduflow.example.com'
$env:TF_VAR_acm_certificate_arn=''
```

Keep the ARN empty for the HTTP workshop. An ACM certificate must be in the same region as the ALB.

## 3. Initialize and validate

```powershell
Set-Location terraform
terraform fmt -check -recursive
terraform init -input=false
terraform validate
```

{{% notice info %}}
Local state is acceptable for an individual lab. Configure an S3 backend and locking before a shared or production apply.
{{% /notice %}}
