---
title: "Tuần 1 (19/05 - 25/05/2026) - DevOps và Kubernetes"
date: 2026-05-25
weight: 1
chapter: false
pre: "<b>1.1.</b>"
description: "Xây dựng bộ template Kubernetes cho hệ thống frontend, backend và MariaDB."
---

## Thời gian

**19/05/2026 - 25/05/2026**

## Công việc thực tế

- Xây dựng bộ template DevOps/Kubernetes có thể tái sử dụng cho frontend, backend và MariaDB.
- Cấu hình `Deployment`, `Service`, `Ingress`, `ConfigMap`, resource request/limit và Persistent Volume qua NFS.
- Viết hướng dẫn triển khai full-stack và cấu hình truy cập MariaDB bằng NodePort.

## Kết quả và bằng chứng

- Hoàn thiện cấu trúc triển khai full-stack trên Kubernetes và tài liệu thao tác đi kèm.
- [Full-stack Deployment, Service và Ingress](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/b59844316c34150bd2124ecbc8034da6c56a75ab)
- [Hướng dẫn triển khai MariaDB, backend và frontend](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/49d89f34cfe29a632e81cf6688273e0b08c4d546)
- [Cấu hình resource request và limit](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/257943281d884672a9d4a93c5b602c1b15bdd222)
- [Sửa ConfigMap volume mount bằng subPath](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/ac40e27457d74d6558b36e96a3dc90c12e98b78f)

## Khó khăn và cách xử lý

ConfigMap ban đầu được mount sai đường dẫn nên container không đọc đúng cấu hình. Tôi chuyển sang dùng `subPath`, chuẩn hóa volume mount và bổ sung kiểm tra CI để phát hiện lỗi cấu hình sớm.

## Nhận xét mentor

Chưa có dữ liệu; sẽ bổ sung khi nhận được phản hồi của mentor.
