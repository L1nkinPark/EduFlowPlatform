---
title: "Tuần 9 (14/07 - 20/07/2026) - EduFlow, Aegis và SecHub"
date: 2026-07-20
weight: 9
chapter: false
pre: "<b>1.9.</b>"
description: "Kiểm thử EduFlow, tự động hóa Aegis và phát triển SecHub cho hackathon OpenAI."
---

## Thời gian và bối cảnh

**14/07/2026 - 20/07/2026** — EduFlow; hoàn thiện Aegis cho AABW; phát triển **SecHub cho hackathon của OpenAI**.

## Công việc thực tế

- Bổ sung kiểm thử đăng ký/thanh toán, sửa VNPAY, i18n, Terraform ECS và k6 cho EduFlow.
- Thiết lập CI tạo Docker image cho dashboard, SOAR và staging sandbox của Aegis.
- Hoàn thiện i18n Việt/Anh cho SecHub.

## Kết quả và bằng chứng

- EduFlow có kịch bản kiểm thử tải qua ALB và luồng thanh toán ổn định hơn.
- Các service Aegis được tự động hóa quá trình build image.
- SecHub có giao diện song ngữ phục vụ bài thi hackathon OpenAI.
- [k6 kiểm thử EduFlow qua ALB](https://github.com/L1nkinPark/EduFlowPlatform/commit/5e42f3665fb39a1f9d3424f360774e9747d01d6a)
- [CI Docker image cho Aegis dashboard](https://github.com/Little-Boy-s-Aegis/dashboard/commit/7217393847bc9b7951692670cff4322bbb5f3e30)
- [CI Docker image cho SOAR](https://github.com/Little-Boy-s-Aegis/aegis-soar-engine/commit/31d4587ac095546cd1a0ea6ac1aa17374890f28d)
- [Hoàn thiện i18n cho SecHub](https://github.com/Little-Boy-s-SecHub/SecHub/commit/180b1f438edccfde0cf92c686ded3f809a07c06d)

## Khó khăn và cách xử lý

Luồng thanh toán, runtime configuration và CI đa service sử dụng nhiều biến môi trường khác nhau. Tôi chuẩn hóa encoding/cấu hình, bổ sung kiểm thử hồi quy và tự động hóa build image.

## Nhận xét mentor

Chưa có dữ liệu; sẽ bổ sung khi nhận được phản hồi của mentor hoặc ban tổ chức.
