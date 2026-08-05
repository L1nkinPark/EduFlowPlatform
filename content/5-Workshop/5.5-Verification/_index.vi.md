---
title: "Kết quả xác minh thực tế"
date: 2026-08-05
weight: 5
chapter: false
pre: "<b>5.5.</b>"
description: "Các kiểm tra đã chạy thực tế và danh sách chưa đủ bằng chứng."
---

# Kết quả xác minh thực tế

## Kiểm tra HTTP ngày 05/08/2026

| Endpoint | Kết quả | Thời gian một lần đo |
|---|---|---|
| [Trang chủ](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/) | HTTP `200`, `text/html; charset=UTF-8` | khoảng `446 ms` |
| [API thống kê](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats) | HTTP `200`, `application/json` | khoảng `235 ms` |

Hai thời gian trên là một lần đo bằng HTTP request, không phải trung bình, percentile hoặc kết quả performance benchmark.

## Kiểm tra CI

- Backend tests: `success`.
- Frontend tests và runtime upload permission check: `success`.
- Terraform format/init/validate: `success`.
- Build/push image và ECS deployment: `success`.

Nguồn: [GitHub Actions run #74](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30983018477).

## Chưa đủ bằng chứng để đánh dấu đã kiểm tra

- Toàn bộ luồng ba vai trò quản trị viên, giảng viên và học viên.
- Thanh toán VNPay end-to-end.
- Kiểm tra quyền truy cập chéo giữa các vai trò.
- Target group health, ECS task, RDS, ECR, S3 và CloudWatch qua Console/API.
- Kết quả tải k6 với 50 virtual users.
