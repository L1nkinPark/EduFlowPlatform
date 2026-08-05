---
title: "Tuần 11 - Hạ tầng AWS bằng Terraform"
date: 2026-08-05
weight: 11
chapter: false
pre: "<b>1.11.</b>"
description: "Mô hình hóa VPC, bảo mật, ALB, ECS, RDS, S3 và Secrets Manager."
---

## Mục tiêu

Biến sơ đồ triển khai EduFlow thành hạ tầng có thể tạo lại và review bằng mã.

## Công việc

- Tách module `vpc`, `security-groups`, `alb`, `ecs`, `rds`, `s3`, `secrets-manager`.
- Đặt frontend/backend Fargate sau ALB và route `/api/*` đến backend.
- Cô lập RDS trong private data subnets; truyền bí mật vào task definition.
- Tối giản tài nguyên theo sơ đồ và kích thước môi trường dev để kiểm soát chi phí.

## Kết quả

Terraform tạo đầy đủ networking, database, registry, cluster và services; outputs cung cấp ALB DNS, RDS endpoint, bucket và ECR URL.

## Bài học

Module nên bám theo ranh giới vận hành. Dependency rõ giữa RDS, secret và ECS giúp tránh cấu hình vòng hoặc secret cũ.
