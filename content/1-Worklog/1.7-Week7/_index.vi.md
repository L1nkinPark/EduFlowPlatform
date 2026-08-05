---
title: "Tuần 7 - Thanh toán và mã giảm giá"
date: 2026-08-05
weight: 7
chapter: false
pre: "<b>1.7.</b>"
description: "Làm ổn định checkout VNPay, callback và tính tiền VND."
---

## Mục tiêu

Đảm bảo số tiền, chữ ký và trạng thái đơn hàng nhất quán từ checkout đến callback.

## Công việc

- Bổ sung kiểm tra mã giảm giá theo thời hạn, hạn mức và giá trị đơn tối thiểu.
- Chuẩn hóa số tiền VNPay sang đơn vị nhỏ nhất và định dạng VND ở giao diện.
- Sửa encoding tham số/chữ ký và cấu hình `PAYMENT_RETURN_ORIGIN` sau load balancer.
- Hiển thị lỗi checkout có thể hành động thay vì trang 500 chung.

## Kết quả

Frontend tạo yêu cầu thanh toán qua backend, callback xác minh giao dịch rồi mới ghi nhận quyền sở hữu khóa học. Các ca số tiền được bao phủ bằng unit test riêng.

## Bài học

Tích hợp thanh toán cần một biểu diễn số tiền duy nhất và canonicalization tham số giống hệt ở bước ký lẫn xác minh.
