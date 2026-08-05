---
title: "Tuần 1 - Khảo sát và xác định phạm vi"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>1.1.</b>"
description: "Khảo sát hệ thống ban đầu, vai trò người dùng và các luồng nghiệp vụ cốt lõi."
---

## Mục tiêu

Hiểu cấu trúc hai ứng dụng Spring Boot và xác định phạm vi MVP cho học viên, giảng viên, quản trị viên.

## Công việc

- Kiểm kê controller, service, entity, template và cấu hình của frontend/backend.
- Lập bản đồ các luồng đăng ký, đăng nhập, khóa học, thanh toán và học bài.
- Xác định các điểm còn dùng dữ liệu tĩnh, route hỏng hoặc thiếu phân quyền.

## Kết quả

Phạm vi được chốt quanh ba vai trò và vòng đời khóa học. Kiến trúc tách frontend `:8080` và backend `:8888` được giữ lại để có thể phát triển, kiểm thử và triển khai độc lập.

## Bài học

Một bản kiểm kê theo luồng người dùng giúp ưu tiên lỗi ảnh hưởng trực tiếp đến giá trị sản phẩm trước khi tối ưu hạ tầng.
