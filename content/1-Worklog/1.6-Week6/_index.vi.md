---
title: "Tuần 6 (23/06 - 29/06/2026) - Khởi tạo EduFlow trên cloud"
date: 2026-06-29
weight: 6
chapter: false
pre: "<b>1.6.</b>"
description: "Xây dựng Terraform, môi trường Fly.io và sửa lỗi container cho EduFlow."
---

## Thời gian

**23/06/2026 - 29/06/2026**

## Công việc thực tế

- Khởi tạo hạ tầng AWS của EduFlowPlatform bằng Terraform.
- Thiết lập môi trường triển khai thử nghiệm trên Fly.io.
- Xử lý lỗi encoding UTF-8 khi build và lỗi 503/health timeout giữa frontend với backend.

## Kết quả và bằng chứng

- Có nền tảng Infrastructure as Code và môi trường kiểm chứng trước khi triển khai AWS.
- [Terraform IaC cho AWS](https://github.com/L1nkinPark/EduFlowPlatform/commit/f868bc80f04b2d23142258e1861b1f0b534f32c8)
- [Môi trường Fly.io](https://github.com/L1nkinPark/EduFlowPlatform/commit/e176301fef4f3feeb548688a307794302d6bb9a9)
- [Sửa UTF-8 trong quá trình build](https://github.com/L1nkinPark/EduFlowPlatform/commit/ae76f62ee501c10223ac4b1a0382b65d06ceb4b1)
- [Sửa lỗi 503 và health timeout](https://github.com/L1nkinPark/EduFlowPlatform/commit/cec32b82f7aebc872d246d530eb8d33470b5ff7a)

## Khó khăn và cách xử lý

Container Java bị sai encoding và health check thất bại khi backend khởi động chậm. Tôi ép encoding UTF-8, điều chỉnh timeout và cập nhật cấu hình health check giữa hai dịch vụ.
