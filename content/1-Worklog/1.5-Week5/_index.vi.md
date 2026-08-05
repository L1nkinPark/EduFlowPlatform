---
title: "Tuần 5 - Biên soạn nội dung khóa học"
date: 2026-08-05
weight: 5
chapter: false
pre: "<b>1.5.</b>"
description: "Hoàn thiện tạo khóa học, chương và bài học video hoặc tài liệu."
---

## Mục tiêu

Đưa quy trình biên soạn từ form giao diện đến dữ liệu học tập có thể sử dụng.

## Công việc

- Hoàn thiện tạo khóa học với danh mục, mô tả, giá và ảnh bìa.
- Bổ sung chương và bài học theo thứ tự, hỗ trợ `VIDEO` và `DOCUMENT`.
- Thêm upload media qua Cloudinary hoặc thư mục local có giới hạn kích thước.
- Sửa quyền ghi `/app/uploads` cho container frontend trên ECS.

## Kết quả

Giảng viên có thể tạo cấu trúc khóa học đầy đủ và học viên nhận đúng video/tài liệu. Pipeline còn kiểm tra thư mục upload tồn tại và ghi được.

## Bài học

Upload là luồng xuyên nhiều lớp: form, multipart limit, storage, quyền filesystem và URL phân phối đều phải được kiểm thử cùng nhau.
