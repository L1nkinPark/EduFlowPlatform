---
title: "Kết quả CI/CD thực tế"
date: 2026-08-05
weight: 4
chapter: false
pre: "<b>5.4.4.</b>"
description: "Các job và thời lượng thực tế của GitHub Actions run #76."
---

# Kết quả CI/CD thực tế

Bằng chứng: [Test and Deploy to Amazon ECS Fargate #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529), chạy trên nhánh `main` cho merge commit `ad4e808`.

| Job | Kết quả | Khoảng thời gian UTC |
|---|---|---|
| Terraform validation | `success` | 07:40:51–07:41:13 |
| Backend tests | `success` | 07:40:44–07:41:41 |
| Frontend tests | `success` | 07:40:45–07:41:57 |
| Build, push, and deploy | `success` | 07:42:06–07:49:47 |

GitHub hiển thị tổng thời lượng pipeline là **9 phút 07 giây**.

Job deploy hoàn thành đăng nhập ECR, build/push hai image, triển khai ECS và chờ service ổn định.
