---
title: "Tuần 2 - Mô hình dữ liệu và API"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>1.2.</b>"
description: "Chuẩn hóa entity, quan hệ dữ liệu và hợp đồng REST giữa hai dịch vụ."
---

## Mục tiêu

Tạo nền dữ liệu đủ cho khóa học có cấu trúc, giao dịch và theo dõi học tập.

## Công việc

- Rà soát 12 entity chính: Account, Category, SubCategory, Course, Chapter, Lesson, Order, OrderItem, PromoCode, OTP, LessonProgress và ContactMessage.
- Chuẩn hóa response/request model để frontend không phụ thuộc trực tiếp vào entity JPA.
- Bổ sung index và tinh chỉnh HikariCP/Tomcat cho các truy vấn và tải đồng thời.

## Kết quả

Backend cung cấp API theo tài nguyên cho tài khoản, danh mục, khóa học, chương, bài học, đơn hàng và tiến độ. Dữ liệu thật có thể thay thế các khối nội dung giả trên giao diện.

## Bài học

Hợp đồng API ổn định giúp tách thay đổi giao diện khỏi chi tiết lưu trữ và giảm lỗi serialization/quan hệ vòng.
