---
title: "Tự đánh giá"
date: 2026-08-05
weight: 6
chapter: false
pre: "<b>6.</b>"
---

# Tự đánh giá dự án

Đánh giá dưới đây dựa trên artifact có trong repository tại ngày 05/08/2026, không thay thế nhận xét của mentor hoặc tổ chức.

| Tiêu chí | Mức tự đánh giá | Bằng chứng |
|---|---|---|
| Phân tích nghiệp vụ | Tốt | Hoàn thiện luồng ba vai trò và vòng đời khóa học bằng dữ liệu thật |
| Backend Spring Boot | Tốt | REST API, JPA, Security/JWT, OTP, VNPay, progress và exception handling |
| Frontend/UX | Khá | Thymeleaf, i18n Việt/Anh, responsive fixes và phản hồi lỗi |
| Kiểm thử | Khá | 19 lớp test backend, 15 lớp test frontend, test bảo mật/tiền/i18n |
| Cloud/IaC | Khá | 7 module Terraform cho VPC, ALB, ECS, RDS, S3 và secrets |
| CI/CD | Khá | Test-gated ECR/ECS delivery, concurrency, bounded retries và diagnostics |
| Bảo mật | Khá | Role/ownership checks, runtime secrets, cookie hardening và non-root image |
| Tài liệu | Tốt | Báo cáo song ngữ và workshop có bước xác minh/dọn dẹp |

## Điểm mạnh

- Theo đuổi lỗi xuyên lớp đến nguyên nhân gốc: UI, API, container, ALB và runtime secret.
- Chuyển dữ liệu giả thành luồng có thể kiểm chứng, giúp dashboard và trải nghiệm đáng tin cậy hơn.
- Biến các lỗi production thành test, validation hoặc diagnostics trong pipeline.

## Điểm cần cải thiện

1. Chuyển state Terraform sang S3 backend có locking cho làm việc nhóm.
2. Thay access key CI dài hạn bằng GitHub OIDC và IAM role ngắn hạn.
3. Bổ sung integration/E2E test tự động cho OTP, VNPay callback và ba vai trò.
4. Thêm migration có phiên bản (Flyway/Liquibase) thay cho `ddl-auto=update` ở production.
5. Cải thiện HA: private app subnets/NAT hoặc VPC endpoints, RDS Multi-AZ và autoscaling khi có tải thật.
6. Bổ sung metric/alarm/SLO và quy trình backup-restore được diễn tập.

## Mục tiêu tiếp theo

Ưu tiên bảo mật chuỗi cung ứng và khả năng phục hồi: OIDC, image scanning gate, migration, test E2E, remote state, alarm và một bài diễn tập khôi phục database.
