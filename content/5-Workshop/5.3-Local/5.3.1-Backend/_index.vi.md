---
title: "Chạy MySQL và backend"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>5.3.1.</b>"
description: "Khởi tạo database, chạy backend test và kiểm tra Actuator."
---

# Chạy MySQL và backend

## 1. Khởi động MySQL thử nghiệm

```powershell
docker run --name eduflow-mysql `
  -e MYSQL_ROOT_PASSWORD=local-eduflow-password `
  -e MYSQL_DATABASE=eduflow_db `
  -p 3306:3306 `
  -d mysql:8.0
```

Chờ đến khi `docker logs eduflow-mysql` cho biết MySQL sẵn sàng.

## 2. Chạy test backend

```powershell
Set-Location BE_EduFlow
$env:SPRING_DATASOURCE_URL='jdbc:mysql://127.0.0.1:3306/eduflow_db?allowPublicKeyRetrieval=true&useSSL=false'
$env:SPRING_DATASOURCE_USERNAME='root'
$env:SPRING_DATASOURCE_PASSWORD='local-eduflow-password'
$env:JWT_SECRET='replace-with-a-random-local-secret-at-least-32-bytes'
.\mvnw.cmd test
```

## 3. Chạy backend

```powershell
.\mvnw.cmd spring-boot:run
```

Trong terminal khác:

```powershell
Invoke-RestMethod http://localhost:8888/actuator/health
```

Kết quả mong đợi có `status` bằng `UP`.

{{% notice tip %}}
Nếu cổng 3306 đã được MySQL khác sử dụng, dùng database hiện có hoặc đổi port Docker và cập nhật URL datasource.
{{% /notice %}}
