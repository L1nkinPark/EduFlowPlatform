---
title: "Tuần 10 - Container và môi trường thử nghiệm"
date: 2026-08-05
weight: 10
chapter: false
pre: "<b>1.10.</b>"
description: "Đóng gói hai ứng dụng, thêm health check và triển khai dev trên Fly.io."
---

## Mục tiêu

Tạo artifact chạy giống nhau giữa máy phát triển, CI và môi trường cloud.

## Công việc

- Xây Dockerfile riêng cho frontend/backend bằng Java 17.
- Cấu hình health endpoint, port, biến môi trường và backend URL.
- Tạo `fly.toml` cùng hướng dẫn triển khai dev trên Fly.io.
- Khắc phục encoding khi build và timeout/503 giữa hai dịch vụ.

## Kết quả

Hai ứng dụng chạy dưới dạng container độc lập với cấu hình runtime thay cho giá trị hard-code. Môi trường Fly.io cung cấp bước kiểm chứng trước AWS.

## Bài học

Một image tốt không chứa cấu hình môi trường; cùng image phải chạy được ở local, staging và production bằng cách thay biến runtime.
