---
title: "Tự đánh giá"
date: 2026-08-05
weight: 6
chapter: false
pre: " <b> 6. </b> "
---

# Kết quả và năng lực đạt được

Trong kỳ thực tập, dự án EduFlow giúp củng cố năng lực phát triển phần mềm full-stack, triển khai hạ tầng đám mây, tự động hóa CI/CD, kiểm thử và xây dựng tài liệu kỹ thuật.

| Lĩnh vực | Kết quả đạt được |
|---|---|
| Backend Spring Boot | Xây dựng REST API với JPA, Spring Security, JWT, OTP và tích hợp VNPay; 16 file Java test đã được commit; backend job trong [run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529) hoàn thành thành công. |
| Frontend Spring Boot/Thymeleaf | Phát triển giao diện Thymeleaf, hỗ trợ quốc tế hóa tiếng Việt/Anh và 15 file Java test; frontend CI và smoke test trình duyệt hoàn thành thành công. |
| Cloud/IaC | Tổ chức hạ tầng thành 7 module Terraform gồm `vpc`, `security-groups`, `alb`, `ecs`, `rds`, `s3` và `secrets-manager`; Terraform validation hoàn thành thành công. |
| CI/CD | Tự động hóa kiểm thử backend, frontend, kiểm tra Terraform, build/push container image và triển khai ECS; pipeline #76 hoàn thành trong 9 phút 07 giây. |
| Hiệu năng | Thực hiện k6 với 50 virtual users, ghi nhận 1.758 request, 0 lỗi và p95 1,84 giây; kết quả được lưu dưới dạng JSON summary. |
| Bảo mật | Áp dụng kiểm tra vai trò và quyền sở hữu, cấu hình secret khi runtime, tăng cường bảo vệ cookie và vận hành container bằng người dùng non-root. |
| Tài liệu | Hoàn thiện báo cáo Hugo song ngữ, nhật ký 12 tuần, bài viết kỹ thuật, nội dung hackathon và workshop triển khai. |
