---
title: "Tuần 8 - Song ngữ và chất lượng giao diện"
date: 2026-08-05
weight: 8
chapter: false
pre: "<b>1.8.</b>"
description: "Hoàn thiện i18n Việt/Anh, typography, điều hướng và thông báo lỗi."
---

## Mục tiêu

Tạo trải nghiệm nhất quán cho người dùng Việt Nam và tiếng Anh trên mọi luồng chính/phụ.

## Công việc

- Chuẩn hóa message bundle `messages`, `messages_vi`, `messages_en`.
- Địa phương hóa header, footer, auth, catalog, hồ sơ, chính sách và trang lỗi.
- Sửa font/ký tự lỗi, căn chỉnh form, scroll trang học và thương hiệu EduFlow.
- Bổ sung kiểm thử parity để phát hiện key dịch thiếu giữa các ngôn ngữ.

## Kết quả

Ngôn ngữ được áp dụng đồng đều thay vì chỉ ở trang chủ. Văn bản tiếng Việt và giá VND hiển thị đúng trong UI sản xuất.

## Bài học

i18n là một hợp đồng dữ liệu; kiểm tra parity tự động hiệu quả hơn rà soát thủ công từng template.
