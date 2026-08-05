---
title: "Cấu hình triển khai đã xác minh"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>5.4.1.</b>"
description: "Các giá trị không nhạy cảm có bằng chứng từ mã nguồn và URL đang hoạt động."
---

# Cấu hình triển khai đã xác minh

| Thuộc tính | Giá trị có bằng chứng |
|---|---|
| AWS Region | `ap-southeast-1` |
| Project/environment trong Terraform | `eduflow` / `dev` |
| Giao thức public đã kiểm tra | HTTP |
| Public endpoint | `eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com` |
| Custom domain | Chưa có bằng chứng |
| Chứng chỉ ACM/HTTPS | Chưa có bằng chứng |

SMTP credential, VNPay credential, JWT secret và database secret không được đưa vào báo cáo. Toàn bộ email, domain và chuỗi secret mẫu của template cũ đã được loại bỏ vì không phải dữ liệu triển khai thực tế.

Terraform validation của cấu hình repository đã thành công trong [run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529).
