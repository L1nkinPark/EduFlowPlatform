---
title: "Tuần 11 (28/07 - 03/08/2026) - Kiểm thử TrueTrace và EduFlow"
date: 2026-08-03
weight: 11
chapter: false
pre: "<b>1.11.</b>"
description: "Tăng độ ổn định cho TrueTrace của Qoder và hardening EduFlow."
---

## Thời gian và bối cảnh

**28/07/2026 - 03/08/2026** — Tiếp tục TrueTrace cho hackathon Qoder và hardening EduFlow.

## Công việc thực tế

- Bổ sung unit test cho TrueTrace backend, dashboard, agent rules và luồng end-to-end.
- Cải thiện xử lý khi AI agent đưa ra quyết định khóa tài khoản.
- Hardening đăng nhập, OTP, JWT, checkout và tích hợp AWS trên EduFlow.

## Kết quả và bằng chứng

- TrueTrace có test cho EventPublisher, dashboard Go và rule engine.
- EduFlow giới hạn số lần thử lấy AWS credentials để tránh chờ vô hạn.
- [EventPublisher tests](https://github.com/Little-Boy-s-TrueTrace/truetrace-backend/commit/25da6dcfa3c7be618641e3e50b70ef305359bef3)
- [Dashboard Go tests](https://github.com/Little-Boy-s-TrueTrace/truetrace-dashboard/commit/050e7230e182c41647cdd87dbbc243735f9ad8da)
- [Agent rules tests](https://github.com/Little-Boy-s-TrueTrace/truetrace-agent-engine/commit/7673758375988cdc7b61cfdae256ae5b7d5494d7)
- [Giới hạn AWS credential retries trên EduFlow](https://github.com/L1nkinPark/EduFlowPlatform/commit/6853a95657c5edb45884e3a3ba191cd62176add1)

## Khó khăn và cách xử lý

Lỗi tạm thời từ AWS, pipeline cũ và quyết định sai của AI làm luồng end-to-end thiếu ổn định. Tôi giới hạn retry, tăng kiểm thử và bổ sung cách xử lý lỗi rõ ràng.
