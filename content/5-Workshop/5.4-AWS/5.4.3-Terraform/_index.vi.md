---
title: "Hạ tầng Terraform"
date: 2026-08-05
weight: 3
chapter: false
pre: "<b>5.4.3.</b>"
description: "Cấu hình hạ tầng và kết quả Terraform validation."
---

# Hạ tầng Terraform

## Kết quả

- Job **Terraform validation** trong [run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529) thành công.
- Các bước format check, `terraform init -backend=false` và `terraform validate` đều có trạng thái `success`.
- Mã Terraform định nghĩa ALB route mặc định tới frontend và `/api/*` tới backend.
- DNS ALB thực tế trả HTTP `200` cho cả trang chủ và API thống kê.
