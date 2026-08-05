---
title: "Tuần 3 - Xác thực và phân quyền"
date: 2026-08-05
weight: 3
chapter: false
pre: "<b>1.3.</b>"
description: "Hoàn thiện JWT, session, OTP và ranh giới quyền theo vai trò."
---

## Mục tiêu

Bảo vệ dữ liệu và chức năng theo vai trò, đồng thời giữ trải nghiệm đăng nhập rõ ràng.

## Công việc

- Hoàn thiện đăng ký/đăng nhập JWT, refresh token, OTP và đặt lại mật khẩu.
- Chấp nhận email hoặc username khi đăng nhập và chuẩn hóa cookie/session frontend.
- Giới hạn tạo tài khoản giảng viên cho quản trị viên; kiểm tra quyền sở hữu ở API khóa học.
- Đưa JWT secret ra biến môi trường và AWS Secrets Manager.

## Kết quả

Các route công khai, route xác thực và chức năng theo vai trò được phân tách rõ. Secret không còn cần được hard-code để hai container xác minh cùng một token.

## Bài học

Ẩn nút trên giao diện không phải phân quyền; kiểm tra bắt buộc phải nằm ở backend và bao gồm cả quyền sở hữu tài nguyên.
