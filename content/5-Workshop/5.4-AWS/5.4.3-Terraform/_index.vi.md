---
title: "Bằng chứng Terraform và hạ tầng"
date: 2026-08-05
weight: 3
chapter: false
pre: "<b>5.4.3.</b>"
description: "Phân biệt cấu hình Terraform, kết quả validation và trạng thái AWS chưa xác minh."
---

# Bằng chứng Terraform và hạ tầng

## Đã xác minh

- Job **Terraform validation** trong [run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529) thành công.
- Các bước format check, `terraform init -backend=false` và `terraform validate` đều có trạng thái `success`.
- Mã Terraform định nghĩa ALB route mặc định tới frontend và `/api/*` tới backend.
- DNS ALB thực tế trả HTTP `200` cho cả trang chủ và API thống kê.

## Chưa xác minh trực tiếp từ AWS API/Console

- Số task ECS đang chạy và desired count hiện tại.
- Target health của từng target group.
- Trạng thái, class, storage và public-access của RDS.
- Public access block của S3 và danh sách ECR image.

Các mục chưa xác minh không được ghi thành kết quả thành công chỉ dựa trên Terraform source hoặc state local.
