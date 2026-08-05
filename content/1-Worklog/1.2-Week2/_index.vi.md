---
title: "Tuần 2 (26/05 - 01/06/2026) - Cloud và hệ thống dữ liệu"
date: 2026-06-01
weight: 2
chapter: false
pre: "<b>1.2.</b>"
description: "Mở rộng template cloud, thực hành EC2 và cải thiện dữ liệu HistoryMindAI."
---

## Thời gian

**26/05/2026 - 01/06/2026**

## Công việc thực tế

- Bổ sung Redis PV/PVC, Uptime Kuma và tổ chức tài liệu theo AWS, Azure, GCP.
- Thực hành Amazon EC2 và ghi lại các use case triển khai.
- Cải thiện danh mục động, chính sách RLS và quan hệ thực thể trong HistoryMindAI.

## Kết quả và bằng chứng

- Bộ template DevOps được mở rộng với lưu trữ Redis, giám sát và tài liệu cloud.
- [Redis PV/PVC](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/5f84f053370f6f277d81487e45a9003b8be7bbd8)
- [Cấu trúc tài liệu AWS, GCP và Azure](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/2b105d30785d290cfcff21029e9170393816efb9)
- [Bài thực hành Amazon EC2](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/b7cc735bd9c1cbc41ce3667f414092a7f0be3c5e)
- [Sửa quan hệ đại từ và thực thể HistoryMindAI](https://github.com/HistoryMindAI/vietnam_history_dataset/commit/cb3cd76a086a5e7e5e932115da88a37113867508)
- [Tài liệu chính sách RLS](https://github.com/Little-Boy-s/Little-Boy-s/commit/8cc2852b34556bca5f036a0cc1f770ba30782544)

## Khó khăn và cách xử lý

Dữ liệu và cấu hình nằm ở nhiều repository nên khó theo dõi. Tôi tách tài liệu theo từng nền tảng cloud, chuẩn hóa cấu trúc thư mục và điều chỉnh chính sách RLS cùng quan hệ thực thể.
