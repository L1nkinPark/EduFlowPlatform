---
title: "Prerequisites"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.2.</b>"
description: "Required tools, AWS privileges, integration values, and security practices."
---

# Prerequisites

## Development machine

| Tool | Version |
|---|---|
| Git | 2.x |
| Java | 17 |
| Docker | 24+ |
| Terraform | 1.5+ |
| AWS CLI | 2.x |

Verify:

```powershell
git --version
java -version
docker --version
terraform version
aws --version
```

The repository includes Maven Wrapper, so a global Maven installation is optional.

## AWS account and privileges

Use an authorized sandbox account/role that can create these resources in `ap-southeast-1`:

- VPC, subnets, routes, Internet Gateway, and security groups.
- ALB, target groups, and listeners.
- ECS, ECR, ECS IAM roles, and CloudWatch Logs.
- RDS MySQL, DB subnet group, and Secrets Manager.
- An S3 bucket with public access blocked.

{{% notice warning %}}
Do not attach an `Action: "*"` policy to a long-lived user. Use an expiring sandbox role or a least-privilege policy approved by the cloud administrator.
{{% /notice %}}

Verify identity and region:

```powershell
aws sts get-caller-identity
aws configure get region
```

## Values to prepare

- SMTP username/app password for OTP (a test account is acceptable).
- VNPay Sandbox terminal code and hash secret.
- Domain/ACM certificate for HTTPS; the workshop works over HTTP when the ARN is empty.
- A local JWT secret of at least 32 bytes. In the current ECS configuration, the backend derives a stable signing key from the database secret.

Do not put secrets in `terraform.tfvars`, screenshots, logs, or Git commits. Section 5.4 uses `TF_VAR_*` environment variables.
