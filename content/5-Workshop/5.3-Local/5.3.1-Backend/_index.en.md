---
title: "Run MySQL and backend"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>5.3.1.</b>"
description: "Start the database, run backend tests, and verify Actuator."
---

# Run MySQL and backend

## 1. Start a test MySQL instance

```powershell
docker run --name eduflow-mysql `
  -e MYSQL_ROOT_PASSWORD=local-eduflow-password `
  -e MYSQL_DATABASE=eduflow_db `
  -p 3306:3306 `
  -d mysql:8.0
```

Wait until `docker logs eduflow-mysql` reports that MySQL is ready.

## 2. Run backend tests

```powershell
Set-Location BE_EduFlow
$env:SPRING_DATASOURCE_URL='jdbc:mysql://127.0.0.1:3306/eduflow_db?allowPublicKeyRetrieval=true&useSSL=false'
$env:SPRING_DATASOURCE_USERNAME='root'
$env:SPRING_DATASOURCE_PASSWORD='local-eduflow-password'
$env:JWT_SECRET='replace-with-a-random-local-secret-at-least-32-bytes'
.\mvnw.cmd test
```

## 3. Run the backend

```powershell
.\mvnw.cmd spring-boot:run
```

In another terminal:

```powershell
Invoke-RestMethod http://localhost:8888/actuator/health
```

The expected response contains `status` equal to `UP`.

{{% notice tip %}}
If another MySQL instance uses port 3306, use that database or change the Docker port and datasource URL together.
{{% /notice %}}
