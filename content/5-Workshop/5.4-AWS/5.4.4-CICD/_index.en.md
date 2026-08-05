---
title: "Enable CI/CD"
date: 2026-08-05
weight: 4
chapter: false
pre: "<b>5.4.4.</b>"
description: "Configure GitHub Actions to test, push to ECR, and roll out the newest revision."
---

# Enable CI/CD

`.github/workflows/deploy.yml` implements four gates:

1. Backend tests with a MySQL 8 service.
2. Frontend tests plus `/app/uploads` write-permission verification in the image.
3. `terraform fmt`, `init -backend=false`, and `validate`.
4. On a push to `main`: build/push both images and force ECS deployments.

## Required repository secrets

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`

The CI principal should have only the ECR push and ECS update/describe/log-diagnostic privileges it requires. A production solution should migrate to GitHub OIDC and a short-lived role instead of long-lived access keys.

## Manual run

In GitHub, open **Actions → Test and Deploy to Amazon ECS Fargate → Run workflow**.

Wait for:

- All three validation jobs to pass.
- ECR images tagged with the commit SHA and `latest`.
- Both services to reach stable state.

The workflow cancels stale runs, bounds credential retries, and retries Docker builds at most three times. When rollout fails, it collects service events, stopped tasks, target health, and recent logs.

{{% notice tip %}}
After confirming the workflow, protect `main` and require test/validation jobs before merge.
{{% /notice %}}
