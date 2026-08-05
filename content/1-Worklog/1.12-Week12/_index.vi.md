---
title: "Tuần 12 - CI/CD và hardening sản xuất"
date: 2026-08-05
weight: 12
chapter: false
pre: "<b>1.12.</b>"
description: "Tự động kiểm thử, build/push image, deploy ECS và xử lý lỗi production."
---

## Mục tiêu

Tạo đường phát hành an toàn và giảm thời gian hệ thống ở trạng thái lỗi.

## Công việc

- Thiết lập GitHub Actions chạy backend test, frontend test và Terraform validation song song theo job.
- Build/push hai image lên ECR bằng commit SHA rồi cập nhật ECS services.
- Hủy pipeline cũ, giới hạn retry AWS credential và retry lỗi Docker build tạm thời.
- Hardening session, JWT, API giảng viên, upload, nội dung tiếng Việt và lỗi checkout sản xuất.

## Kết quả

Chỉ revision mới nhất vượt qua kiểm thử mới được triển khai. Các sự cố cấu hình sản xuất được chuyển thành kiểm tra tự động hoặc cấu hình có tài liệu.

## Bài học

CI/CD không chỉ tự động hóa lệnh deploy; nó mã hóa thứ tự an toàn, giới hạn thời gian chờ và ngăn revision cũ ghi đè bản sửa mới.
