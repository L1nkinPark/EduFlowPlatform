---
title: "Tuần 9 - Kiểm thử và hiệu năng"
date: 2026-08-05
weight: 9
chapter: false
pre: "<b>1.9.</b>"
description: "Mở rộng unit test, kiểm tra bảo mật và tạo kịch bản tải k6."
---

## Mục tiêu

Giảm hồi quy ở service/controller và có baseline hiệu năng có thể lặp lại.

## Công việc

- Xây 19 lớp test backend và 15 lớp test frontend cho service, controller, JWT, exception và utility.
- Thêm test phân quyền API khóa học, callback thanh toán, tiền VNPay và i18n parity.
- Cấu hình MySQL test trong CI và chạy hai Maven suite độc lập.
- Tạo `k6-load-test.js` cho luồng truy cập triển khai.

## Kết quả

Pipeline chặn triển khai nếu test hoặc Terraform validation thất bại. Các lỗi quan trọng có test hồi quy gắn với nguyên nhân đã sửa.

## Bài học

Số lượng test không quan trọng bằng việc bao phủ ranh giới: quyền, số tiền, encoding, timeout và dữ liệu rỗng.
