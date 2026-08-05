---
title: "Nguồn bằng chứng và giới hạn"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.2.</b>"
description: "Nguồn dữ liệu dùng để xác minh và những nội dung chưa đủ bằng chứng."
---

# Nguồn bằng chứng và giới hạn

## Nguồn đã sử dụng

- Mã nguồn và lịch sử commit của [EduFlowPlatform](https://github.com/L1nkinPark/EduFlowPlatform).
- [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529) trên nhánh `main`.
- Kiểm tra HTTP trực tiếp tới DNS ALB ngày 05/08/2026.
- Cấu hình Terraform trong repository để mô tả kiến trúc mong muốn.

## Quy tắc ghi nhận

- Kết quả CI chỉ được ghi là thành công khi job/step công khai có trạng thái `success`.
- Kết quả HTTP chỉ xác nhận endpoint phản hồi tại thời điểm đo.
- Cấu hình Terraform không tự chứng minh resource AWS đang `healthy`.
- Không công khai account ID, access key, password, JWT, OTP, VNPay secret hoặc secret value.

## Giới hạn hiện tại

Phiên AWS CLI hiện có không cho phép xác minh tài nguyên của môi trường đã triển khai. Vì vậy báo cáo không khẳng định trạng thái ECS task, target group, RDS, ECR hoặc S3 từ AWS Console và không ghi chi phí bằng dữ liệu của tài khoản khác.
