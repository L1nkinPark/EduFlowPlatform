---
title: "Chạy frontend và kiểm tra luồng"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.3.2.</b>"
description: "Chạy frontend test, kết nối backend và build hai container."
---

# Chạy frontend

Giữ backend hoạt động ở `http://localhost:8888`.

## 1. Chạy test frontend

```powershell
Set-Location FE_EduFlow
$env:BACKEND_URL='http://localhost:8888'
$env:JWT_SECRET='replace-with-a-random-local-secret-at-least-32-bytes'
.\mvnw.cmd test
```

Giá trị `JWT_SECRET` phải giống backend.

## 2. Chạy giao diện

```powershell
.\mvnw.cmd spring-boot:run
```

Mở `http://localhost:8080` và kiểm tra:

- Trang chủ/danh mục tải được dữ liệu.
- Đăng ký, đăng nhập và đăng xuất không tạo lỗi 500.
- Chuyển ngôn ngữ Việt/Anh hoạt động.
- Backend ngừng chạy sẽ tạo thông báo lỗi có kiểm soát trong khoảng timeout.

## 3. Build container

Từ thư mục gốc repository:

```powershell
docker build -t eduflow-backend:local .\BE_EduFlow
docker build -t eduflow-frontend:local .\FE_EduFlow
docker run --rm --entrypoint sh eduflow-frontend:local -c 'test -d /app/uploads && test -w /app/uploads'
```

Lệnh cuối phải trả exit code `0`, xác nhận user không phải root vẫn ghi được thư mục upload.
