---
title: "Từ sơ đồ đến ECS Fargate bằng Terraform"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>3.2.</b>"
description: "Cách EduFlow chuyển kiến trúc AWS thành các module hạ tầng có thể tái tạo."
---

# Từ sơ đồ đến ECS Fargate bằng Terraform

Mục tiêu hạ tầng của EduFlow không phải dùng nhiều dịch vụ nhất, mà là ánh xạ rõ mỗi nhu cầu vận hành vào một thành phần AWS.

## Ranh giới module

| Module | Vai trò |
|---|---|
| `vpc` | VPC, public subnets và private data subnets |
| `security-groups` | Chỉ cho phép ALB → frontend/backend và backend → RDS |
| `alb` | TLS/HTTP entry point, health check và route `/api/*` |
| `ecs` | Cluster, ECR, task definitions và Fargate services |
| `rds` | MySQL, subnet group, backup và monitoring role |
| `secrets-manager` | Database, SMTP, JWT/VNPay runtime values |
| `s3` | Asset/backup bucket với public access bị chặn |

## Luồng triển khai

GitHub Actions chạy test và `terraform validate`, đăng nhập ECR, build hai image gắn commit SHA, push image rồi buộc ECS tạo deployment mới. Tag SHA giúp truy vết chính xác code đang chạy, còn `latest` phục vụ thao tác vận hành đơn giản.

## Quyết định chi phí

Môi trường dev dùng desired count nhỏ, RDS Single-AZ và không thêm các module không có trong sơ đồ. Đây là đánh đổi có chủ đích: giảm chi phí cho MVP, đồng thời giữ biến `multi_az` và desired count để nâng cấp khi yêu cầu sẵn sàng cao xuất hiện.

## Kết luận

Terraform tốt là tài liệu có thể thực thi. Tên module, input/output và security group phải kể lại được kiến trúc mà không cần đọc toàn bộ resource.
