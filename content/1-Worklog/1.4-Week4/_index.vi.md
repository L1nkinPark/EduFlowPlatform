---
title: "Tuần 4 (09/06 - 15/06/2026) - KET-Vault, DoraHacks"
date: 2026-06-15
weight: 4
chapter: false
pre: "<b>1.4.</b>"
description: "Phát triển KET-Vault cho cuộc thi hackathon trên DoraHacks."
---

## Thời gian và bối cảnh

**09/06/2026 - 15/06/2026** — Dự án **KET-Vault**, cuộc thi hackathon trên **DoraHacks**.

## Công việc thực tế

- Phát triển hệ thống AI đa tác tử hỗ trợ quản trị DeFi Treasury.
- Tích hợp frontend, FastAPI, smart contract, Docker và GitHub Actions.
- Triển khai lên Railway/Vercel; hoàn thiện quy trình đề xuất, biểu quyết và đọc số dư on-chain.
- Bổ sung Risk Auditor và bước tổng hợp kết quả từ nhiều AI agent.

## Kết quả và bằng chứng

- Hoàn thiện phiên bản full-stack phục vụ cuộc thi với quy trình ra quyết định on-chain.
- [Repository KET-Vault](https://github.com/Littile-Boy-s-KET/KET-Vault)
- [Docker Compose và GitHub Actions](https://github.com/Littile-Boy-s-KET/KET-Vault/commit/086eb767025fa9641e9b968162436a7e802fd175)
- [Sửa lỗi giao dịch proposal bị revert](https://github.com/Littile-Boy-s-KET/KET-Vault/commit/f3a83567f20122f01e3c0f3f1a567f09b8fd5770)
- [Cấu hình Railway, Vercel và CORS](https://github.com/Littile-Boy-s-KET/KET-Vault/commit/cc7c9e5bdc0bb47753925684db33f8bb5ec1134b)
- [Tổng hợp kết quả Risk Auditor](https://github.com/Littile-Boy-s-KET/KET-Vault/commit/b4987d6e8d6dc7016f4aeb04d39b3d538940557d)

## Khó khăn và cách xử lý

Smart contract từng trả về lỗi revert, model AI ngừng hỗ trợ và môi trường Railway gặp lỗi CORS/cổng chạy. Tôi kiểm tra transaction, thay model dự phòng và chuyển cấu hình CORS, `PORT` sang biến môi trường.

## Nhận xét mentor

Chưa có dữ liệu; sẽ bổ sung khi nhận được phản hồi của mentor hoặc ban tổ chức.
