---
title: "Đối chiếu năng lực và bằng chứng"
date: 2026-08-05
weight: 6
chapter: false
pre: "<b>6.</b>"
---

# Đối chiếu năng lực và bằng chứng

Không có thang điểm tự đánh giá hoặc nhận xét mentor do người thực hiện cung cấp. Vì vậy phần này bỏ các mức “Tốt/Khá” của template cũ và chỉ liệt kê artifact có thể kiểm tra tại commit hiện tại.

| Lĩnh vực | Bằng chứng hiện có | Giới hạn |
|---|---|---|
| Backend Spring Boot | REST/JPA/Security/JWT/OTP/VNPay trong mã nguồn; 16 file Java test đã commit; backend job trong [run #74](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30983018477) thành công | Chưa có coverage report và E2E đầy đủ |
| Frontend Spring Boot/Thymeleaf | Template, i18n Việt/Anh và 15 file Java test đã commit; frontend test và upload-permission check thành công | Chưa có kết quả browser E2E lưu trữ |
| Cloud/IaC | 7 module Terraform: `vpc`, `security-groups`, `alb`, `ecs`, `rds`, `s3`, `secrets-manager`; Terraform validation thành công | Chưa xác minh trực tiếp từng resource bằng đúng AWS account |
| CI/CD | Backend, frontend, Terraform và deploy jobs đều `success`; pipeline #74 kéo dài 8 phút 49 giây | Không thay thế kiểm tra trạng thái dài hạn của hệ thống |
| Bảo mật | Có role/ownership checks, runtime secret configuration, cookie hardening và non-root container trong mã nguồn | Chưa có penetration test hoặc security audit độc lập |
| Tài liệu | Nội dung Hugo song ngữ, worklog có commit evidence và Workshop tách rõ đã/chưa xác minh | Chưa có ảnh AWS Console và phản hồi mentor |

## Việc cần bổ sung, không phải kết quả đã hoàn thành

1. Browser E2E cho ba vai trò, OTP và VNPay callback.
2. Coverage artifact cho hai Maven suite.
3. GitHub OIDC thay access key dài hạn.
4. Remote Terraform state có locking.
5. Billing/Cost Explorer và ảnh Console từ đúng môi trường.
6. Metric, alarm, backup/restore test và kết quả k6 được lưu làm artifact.

Các mục trên là đề xuất tiếp theo, không được tính là chức năng đã triển khai.
