---
title: "Cấu hình triển khai"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>5.4.1.</b>"
description: "Các giá trị cấu hình sử dụng cho môi trường EduFlow trên AWS."
---

# Cấu hình triển khai

| Thuộc tính | Giá trị |
|---|---|
| AWS Region | `ap-southeast-1` |
| Project/environment trong Terraform | `eduflow` / `dev` |
| Giao thức public đã kiểm tra | HTTP |
| Public endpoint | `eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com` |

SMTP credential, VNPay credential, JWT secret và database secret được quản lý qua secret của môi trường triển khai và không hiển thị trong báo cáo.

Terraform validation của cấu hình repository đã thành công trong [run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529).
