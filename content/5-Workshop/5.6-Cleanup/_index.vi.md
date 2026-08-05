---
title: "Vận hành sau triển khai"
date: 2026-08-05
weight: 6
chapter: false
pre: "<b>5.6.</b>"
description: "Trạng thái ứng dụng và quy trình tái triển khai EduFlow."
---

# Vận hành sau triển khai

## Trạng thái dịch vụ

- Website EduFlow tiếp tục hoạt động qua AWS Application Load Balancer sau khi pipeline hoàn thành.
- Trang chủ và API `/api/public/stats` cùng phản hồi HTTP `200` trong lần kiểm tra ngày 05/08/2026.
- Quy trình chờ ECS service ổn định được tích hợp trong job triển khai.

## Khả năng tái triển khai

- Pipeline #76 hoàn thành kiểm thử, build image và triển khai trong **9 phút 07 giây**.
- Container image của frontend và backend được build, push lên Amazon ECR và cập nhật cho ECS.
- Cấu hình Terraform và workflow CI/CD được quản lý cùng mã nguồn, hỗ trợ triển khai nhất quán.
