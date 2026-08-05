---
title: "Tuần 7 (30/06 - 06/07/2026) - Aegis, AABW"
date: 2026-07-06
weight: 7
chapter: false
pre: "<b>1.7.</b>"
description: "Tích hợp hệ sinh thái giám sát và phản ứng sự cố Aegis cho hackathon AABW."
---

## Thời gian và bối cảnh

**30/06/2026 - 06/07/2026** — Dự án **Aegis**, hackathon **AABW**.

## Công việc thực tế

- Phát triển ứng dụng ngân hàng mô phỏng, dashboard giám sát, SOAR engine, mobile app và môi trường deployment.
- Tích hợp Kafka để truyền sự kiện và ánh xạ mối đe dọa trên dashboard.
- Tích hợp xác thực OPA, xử lý JWT giả mạo và lưu token an toàn trên mobile.

## Kết quả và bằng chứng

- Các thành phần ngân hàng và an ninh có thể trao đổi sự kiện, xác thực và hiển thị cảnh báo.
- [Repository triển khai Aegis](https://github.com/Little-Boy-s-Aegis/aegis-bank-deployment)
- [Kafka và threat mapping trên dashboard](https://github.com/Little-Boy-s-Aegis/dashboard/commit/645bdc95651fae87f9a9b2ad43e41145c938733a)
- [OPA authentication cho SOAR](https://github.com/Little-Boy-s-Aegis/aegis-soar-engine/commit/76e7dcd72728cc8dd6005e197a68c5c835710f9b)
- [Xử lý forged JWT](https://github.com/Little-Boy-s-Aegis/aegis-bank-backend/commit/65327897c3d78a51509de81bb563a554af9a5979)
- [Secure storage trên mobile](https://github.com/Little-Boy-s-Aegis/aegis-bank-mobile-app/commit/d16229362b918a8f831e3e8426d3e205bc28c4a7)

## Khó khăn và cách xử lý

Hệ thống gồm nhiều service với luồng xác thực khác nhau. Tôi chuẩn hóa token và HTTP status, giới hạn service nội bộ về localhost và dùng secure storage trên thiết bị.

## Nhận xét mentor

Chưa có dữ liệu; sẽ bổ sung khi nhận được phản hồi của mentor hoặc ban tổ chức.
