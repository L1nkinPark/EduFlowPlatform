---
title: "Kiểm thử ứng dụng tại local"
date: 2026-08-05
weight: 3
chapter: false
pre: "<b>5.3.</b>"
description: "Chạy MySQL, backend, frontend và xác nhận baseline trước khi lên cloud."
---

# Kiểm thử local

Không nên chẩn đoán lỗi ứng dụng lần đầu trong ECS. Phần này tạo baseline: database chạy, hai Maven suite xanh, health endpoint phản hồi và frontend gọi được backend.

{{% children description="true" /%}}
