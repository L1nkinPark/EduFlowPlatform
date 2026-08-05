---
title: "Điều kiện chuẩn bị"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.2.</b>"
description: "Công cụ, quyền AWS, thông tin tích hợp và nguyên tắc bảo mật cần có."
---

# Điều kiện chuẩn bị

## Máy phát triển

| Công cụ | Phiên bản |
|---|---|
| Git | 2.x |
| Java | 17 |
| Docker | 24+ |
| Terraform | 1.5+ |
| AWS CLI | 2.x |

Kiểm tra:

```powershell
git --version
java -version
docker --version
terraform version
aws --version
```

Repository đã có Maven Wrapper nên không bắt buộc cài Maven toàn cục.

## AWS account và quyền

Sử dụng account/role sandbox được cấp quyền tạo các tài nguyên sau trong `ap-southeast-1`:

- VPC, subnet, route, Internet Gateway và security group.
- ALB, target group và listener.
- ECS, ECR, IAM role cho ECS và CloudWatch Logs.
- RDS MySQL, subnet group và Secrets Manager.
- S3 bucket với public access block.

{{% notice warning %}}
Không sao chép policy `Action: "*"` vào user dùng lâu dài. Hãy dùng role sandbox có thời hạn hoặc policy quyền tối thiểu do quản trị viên cloud phê duyệt.
{{% /notice %}}

Xác minh danh tính và region:

```powershell
aws sts get-caller-identity
aws configure get region
```

## Dữ liệu cần chuẩn bị

- SMTP username/app password dùng cho OTP (có thể dùng tài khoản thử nghiệm).
- VNPay Sandbox terminal code và hash secret.
- Tên miền/chứng chỉ ACM nếu muốn HTTPS; workshop chạy được bằng HTTP khi ARN để trống.
- Một JWT secret tối thiểu 32 byte cho local. Trên ECS hiện tại, backend dẫn xuất khóa ổn định từ secret database.

Không ghi secret vào `terraform.tfvars`, ảnh chụp, log hoặc commit Git. Phần 5.4 dùng biến môi trường `TF_VAR_*`.
