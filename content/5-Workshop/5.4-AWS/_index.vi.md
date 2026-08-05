---
title: "Triển khai lên AWS"
date: 2026-08-05
weight: 4
chapter: false
pre: "<b>5.4.</b>"
description: "Cấu hình Terraform, bootstrap ECR, tạo hạ tầng và bật CI/CD."
---

# Triển khai lên AWS

Phần này dùng `ap-southeast-1` và tên mặc định `eduflow-dev-*`. Quy trình bootstrap ECR trước để ECS luôn tham chiếu image ứng dụng tồn tại khi tạo service.

{{% children description="true" /%}}
