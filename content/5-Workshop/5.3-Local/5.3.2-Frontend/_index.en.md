---
title: "Run frontend and verify journeys"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.3.2.</b>"
description: "Run frontend tests, connect the backend, and build both containers."
---

# Run the frontend

Keep the backend running at `http://localhost:8888`.

## 1. Run frontend tests

```powershell
Set-Location FE_EduFlow
$env:BACKEND_URL='http://localhost:8888'
$env:JWT_SECRET='replace-with-a-random-local-secret-at-least-32-bytes'
.\mvnw.cmd test
```

Use the same `JWT_SECRET` as the backend.

## 2. Run the web application

```powershell
.\mvnw.cmd spring-boot:run
```

Open `http://localhost:8080` and verify:

- Home/catalog pages load data.
- Registration, sign-in, and logout do not produce generic 500 errors.
- Vietnamese/English switching works.
- Stopping the backend produces a controlled error within the configured timeout.

## 3. Build containers

From the repository root:

```powershell
docker build -t eduflow-backend:local .\BE_EduFlow
docker build -t eduflow-frontend:local .\FE_EduFlow
docker run --rm --entrypoint sh eduflow-frontend:local -c 'test -d /app/uploads && test -w /app/uploads'
```

The final command must exit with code `0`, proving the non-root user can write to the upload directory.
