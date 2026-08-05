---
title: "Review kiến trúc và threat model"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>4.1.</b>"
description: "Phiên review ranh giới mạng, dữ liệu, secret và quyền theo vai trò."
---

# Review kiến trúc và threat model

## Mục tiêu

Đối chiếu sơ đồ AWS, Terraform và luồng ứng dụng để phát hiện đường truy cập không cần thiết hoặc cấu hình bí mật không nhất quán.

## Checklist

- Chỉ ALB nhận lưu lượng từ Internet.
- Frontend/backend chỉ nhận lưu lượng từ security group phù hợp.
- RDS nằm trong private data subnets và chỉ backend kết nối cổng 3306.
- JWT, database, SMTP và VNPay được cấp bằng secret/runtime variable.
- API quản trị/giảng viên kiểm tra cả role và quyền sở hữu.
- Callback thanh toán không tin trạng thái trình duyệt.

## Kết quả ghi nhận

Review dẫn đến việc ổn định JWT secret trên ECS, siết API khóa học giảng viên, hardening session và bổ sung timeout giữa frontend/backend.

## Đầu ra

Một checklist có thể dùng lại trước mỗi thay đổi Terraform, security rule hoặc luồng xác thực.
