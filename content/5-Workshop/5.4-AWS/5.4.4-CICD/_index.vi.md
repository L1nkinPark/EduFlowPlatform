---
title: "Kết quả CI/CD thực tế"
date: 2026-08-05
weight: 4
chapter: false
pre: "<b>5.4.4.</b>"
description: "Các job và thời lượng thực tế của GitHub Actions run #74."
---

# Kết quả CI/CD thực tế

Bằng chứng: [Test and Deploy to Amazon ECS Fargate #74](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30983018477), chạy trên nhánh `main` cho merge commit `78ad30b`.

| Job | Kết quả | Khoảng thời gian UTC |
|---|---|---|
| Terraform validation | `success` | 06:55:05–06:55:19 |
| Backend tests | `success` | 06:55:05–06:55:56 |
| Frontend tests | `success` | 06:55:06–06:56:03 |
| Build, push, and deploy | `success` | 06:56:06–07:03:49 |

GitHub hiển thị tổng thời lượng run là **8 phút 49 giây**. Đây là thời gian pipeline, không phải tổng thời gian thực hiện workshop thủ công.

Job deploy ghi nhận thành công việc đăng nhập ECR, build/push hai image, triển khai ECS và chờ service ổn định. Báo cáo không suy ra thêm trạng thái hiện tại của từng resource nếu không có quyền truy vấn AWS tương ứng.
