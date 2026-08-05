---
title: "Kích hoạt CI/CD"
date: 2026-08-05
weight: 4
chapter: false
pre: "<b>5.4.4.</b>"
description: "Cấu hình GitHub Actions để test, push ECR và rollout revision mới."
---

# Kích hoạt CI/CD

Workflow `.github/workflows/deploy.yml` thực hiện bốn cổng:

1. Backend test với MySQL 8 service.
2. Frontend test và kiểm tra quyền ghi `/app/uploads` trong image.
3. `terraform fmt`, `init -backend=false`, `validate`.
4. Với push lên `main`: build/push hai image và force ECS deployment.

## Secret repository cần có

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`

Tài khoản CI chỉ nên có quyền ECR push và ECS update/describe/log diagnostics cần thiết. Giải pháp production nên chuyển sang GitHub OIDC và role ngắn hạn thay cho access key dài hạn.

## Chạy thủ công

Trong GitHub: **Actions → Test and Deploy to Amazon ECS Fargate → Run workflow**.

Theo dõi đến khi:

- Ba job kiểm tra đều xanh.
- Image có tag commit SHA và `latest` trong ECR.
- Hai service đạt trạng thái stable.

Workflow dùng concurrency để hủy run cũ, giới hạn retry credential và chỉ retry Docker build tối đa ba lần. Nếu rollout thất bại, job tự thu thập service events, stopped tasks, target health và log gần nhất.

{{% notice tip %}}
Sau khi xác nhận workflow hoạt động, bảo vệ nhánh `main` và yêu cầu các job test/validate thành công trước khi merge.
{{% /notice %}}
