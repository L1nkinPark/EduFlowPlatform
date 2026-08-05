---
title: "Nguồn tài liệu triển khai"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.2.</b>"
description: "Mã nguồn, CI/CD và cấu hình hạ tầng sử dụng trong workshop."
---

# Nguồn tài liệu triển khai

## Tài liệu và công cụ

- Mã nguồn và lịch sử commit của [EduFlowPlatform](https://github.com/L1nkinPark/EduFlowPlatform).
- [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529) trên nhánh `main`.
- Kiểm tra HTTP trực tiếp tới DNS ALB ngày 05/08/2026.
- Cấu hình Terraform trong repository để quản lý kiến trúc AWS.

## Nguyên tắc bảo mật

- Không công khai account ID, access key, password, JWT, OTP, VNPay secret hoặc secret value.
- Thông tin nhạy cảm được truyền qua GitHub Actions secrets và cấu hình runtime của AWS.
