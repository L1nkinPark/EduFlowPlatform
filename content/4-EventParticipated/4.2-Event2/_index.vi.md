---
title: "Demo và kiểm thử phát hành"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>4.2.</b>"
description: "Phiên chạy end-to-end ba vai trò trước khi phát hành ECS."
---

# Demo và kiểm thử phát hành

## Kịch bản

1. Quản trị viên tạo tài khoản giảng viên và xem dashboard.
2. Giảng viên đăng nhập, tạo khóa học, chương và bài học tài liệu/video.
3. Học viên đăng ký qua OTP, tìm khóa học và áp mã giảm giá.
4. Checkout qua VNPay Sandbox và xác minh callback.
5. Học bài, đánh dấu hoàn thành và kiểm tra phần trăm tiến độ.
6. Chuyển Việt/Anh, kiểm tra VND, layout mobile và lỗi backend.

## Tiêu chí đạt

- Không dùng dữ liệu giả trong các dashboard chính.
- Quyền sai trả 401/403 hoặc chuyển hướng phù hợp, không lộ dữ liệu.
- Test backend/frontend và Terraform validation đều thành công.
- Health check ALB xanh và deployment ECS ổn định.

## Kết quả ghi nhận

Các vòng demo đã phát hiện và sửa lỗi upload, font tiếng Việt, số tiền VND, checkout error, header xác thực và quyền API giảng viên.
