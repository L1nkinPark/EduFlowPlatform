---
title: "Tuần 8 (07/07 - 13/07/2026) - Hardening Aegis"
date: 2026-07-13
weight: 8
chapter: false
pre: "<b>1.8.</b>"
description: "Hardening, phân quyền và hoàn thiện tài liệu Aegis cho hackathon AABW."
---

## Thời gian và bối cảnh

**07/07/2026 - 13/07/2026** — Tiếp tục dự án **Aegis**, hackathon **AABW**.

## Công việc thực tế

- Bổ sung kiểm tra quyền quản trị phía backend và IP banning tại gateway.
- Hoàn thiện cấu hình vận hành cho backend, deployment, Terraform và các agent.
- Chuẩn hóa tài liệu thiết lập các repository trong hệ sinh thái.

## Kết quả và bằng chứng

- Quyền quản trị được kiểm tra tại backend; gateway có khả năng chặn IP vi phạm.
- [Backend admin authorization](https://github.com/Little-Boy-s-Aegis/aegis-bank-backend/commit/2140b0e0197fc8e77ff3071b871e7a22e7b05fc4)
- [IP banning và gateway validation](https://github.com/Little-Boy-s-Aegis/aegis-bank-deployment/commit/6718ef5c63fd5f1481e787bf634044b47225480d)
- [Tài liệu backend Aegis](https://github.com/Little-Boy-s-Aegis/aegis-bank-backend/commit/ee06484a47048d1f371f643377b373a7381564df)
- [Tài liệu Terraform Aegis](https://github.com/Little-Boy-s-Aegis/aegis-bank-terraform/commit/310c9c9cf12dda2db7da557057a076c29e984ccf)
- [Kết quả chính thức AABW](https://aabw.genaifund.ai/) ghi nhận Little Boy's
  Aegis thắng hạng mục Financial Services I — Shinhan Future's Lab Vietnam.

## Khó khăn và cách xử lý

Quyền quản trị cần nhất quán ở nhiều tầng và các rule phải được truyền đúng giữa service. Tôi thực thi kiểm tra ở backend lẫn gateway, sau đó chuẩn hóa tài liệu cấu hình liên repository.

## Nhận xét mentor

Chưa có nhận xét mentor. Kết quả cuộc thi được đối chiếu độc lập từ trang kết
quả và thông báo chính thức của ban tổ chức AABW.
