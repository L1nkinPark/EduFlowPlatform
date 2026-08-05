---
title: "Verification and security checks"
date: 2026-08-05
weight: 5
chapter: false
pre: "<b>5.5.</b>"
description: "Verify health, three roles, payments, logs, and network boundaries."
---

# Verify the deployment

## 1. Infrastructure

- The ALB serves the homepage and `/api/public/stats`.
- Both target groups are healthy.
- ECS has one RUNNING task per service.
- RDS is not public and resides in private data subnets.
- S3 public access block and ECR scan-on-push are enabled.

List target groups:

```powershell
aws elbv2 describe-target-groups `
  --names eduflow-dev-fe-tg eduflow-dev-be-tg `
  --region $taskAwsRegion `
  --query 'TargetGroups[].{name:TargetGroupName,arn:TargetGroupArn}'
```

Use each ARN with `aws elbv2 describe-target-health --target-group-arn <arn>`.

## 2. Functionality

| Role | Required scenario |
|---|---|
| Administrator | View dashboard, create instructor, disable/enable account |
| Instructor | Create course, chapter, video/document; view orders/students |
| Student | OTP, sign-in, search, promotion, checkout, learning, and progress |

Also check language switching, VND formatting, backend timeouts, and 404/403 pages.

## 3. Security

- Student requests to administrator/instructor APIs are denied.
- An instructor cannot modify another instructor's course.
- Secrets do not appear in plaintext task environment, logs, or HTML.
- Session cookies use `HttpOnly` and `SameSite=Lax`; URLs contain no `;jsessionid`.
- VNPay callbacks with invalid signatures or amounts do not grant course ownership.

## 4. Observability

```powershell
aws logs tail /ecs/eduflow-dev-frontend --since 10m --region $taskAwsRegion
aws logs tail /ecs/eduflow-dev-backend --since 10m --region $taskAwsRegion
```

Do not include passwords, JWTs, VNPay hash secrets, or OTP values in report evidence.
