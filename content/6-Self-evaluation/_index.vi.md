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
| Backend Spring Boot | REST/JPA/Security/JWT/OTP/VNPay trong mã nguồn; 16 file Java test đã commit; backend job trong [run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529) thành công | Chưa có coverage report và E2E đầy đủ |
| Frontend Spring Boot/Thymeleaf | Template, i18n Việt/Anh và 15 file Java test đã commit; frontend CI thành công; có ảnh và biên bản smoke test trình duyệt | Chưa kiểm thử đăng nhập đầy đủ cho cả ba vai trò |
| Cloud/IaC | 7 module Terraform: `vpc`, `security-groups`, `alb`, `ecs`, `rds`, `s3`, `secrets-manager`; Terraform validation thành công | Chưa xác minh trực tiếp từng resource bằng đúng AWS account |
| CI/CD | Backend, frontend, Terraform và deploy jobs đều `success`; pipeline #76 kéo dài 9 phút 07 giây | Không thay thế kiểm tra trạng thái dài hạn của hệ thống |
| Hiệu năng | K6 50 VU: 1.758 request, 0 lỗi, p95 1,84 giây; JSON summary đã lưu | Chỉ kiểm tra ba API đọc trong 60 giây |
| Bảo mật | Có role/ownership checks, runtime secret configuration, cookie hardening và non-root container trong mã nguồn | Chưa có penetration test hoặc security audit độc lập |
| Tài liệu | Nội dung Hugo song ngữ, worklog có commit evidence và Workshop tách rõ đã/chưa xác minh | Chưa có ảnh AWS Console và phản hồi mentor |

## Việc cần bổ sung, không phải kết quả đã hoàn thành

1. Browser E2E đã đăng nhập cho ba vai trò, OTP và VNPay callback.
2. Coverage artifact cho hai Maven suite.
3. GitHub OIDC thay access key dài hạn.
4. Remote Terraform state có locking.
5. Billing/Cost Explorer và ảnh Console từ đúng môi trường.
6. Metric, alarm, backup/restore test; đưa k6 summary vào GitHub Actions artifact.

Các mục trên là đề xuất tiếp theo, không được tính là chức năng đã triển khai.
