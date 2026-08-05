---
title: "Tuần 5 (16/06 - 22/06/2026) - Tardis, AABW"
date: 2026-06-22
weight: 5
chapter: false
pre: "<b>1.5.</b>"
description: "Xây dựng Tardis cho hackathon AABW."
---

## Thời gian và bối cảnh

**16/06/2026 - 22/06/2026** — Dự án **Tardis**, hackathon **AABW**.

## Công việc thực tế

- Xây dựng pipeline tiếp nhận webhook từ các nền tảng nhắn tin.
- Thiết kế luồng webhook → RabbitMQ → xử lý/tóm tắt bằng AI → cập nhật thời gian thực.
- Khởi tạo backend Java 21, frontend React và các tình huống kiểm thử webhook.

## Kết quả và bằng chứng

- Hoàn thành phiên bản ban đầu của hệ thống thu nhận, xử lý và phát dữ liệu thời gian thực.
- [Repository Tardis](https://github.com/Little-Boy-s-Tardis/Tardis)
- [Các tình huống kiểm thử webhook](https://github.com/Little-Boy-s-Tardis/Tardis/commit/c66e8cd6e3696a3580bd606d5ddca9ac9c5d9c80)

## Khó khăn và cách xử lý

Môi trường phát triển phụ thuộc PostgreSQL, RabbitMQ và dịch vụ AI. Tôi cấu hình chế độ phát triển với H2, hàng đợi in-memory và phương án dự phòng để có thể chạy, kiểm thử cục bộ khi dịch vụ ngoài chưa sẵn sàng.

## Nhận xét mentor

Chưa có dữ liệu; sẽ bổ sung khi nhận được phản hồi của mentor hoặc ban tổ chức.
