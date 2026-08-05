---
title: "Actual verification results"
date: 2026-08-05
weight: 5
chapter: false
pre: "<b>5.5.</b>"
description: "Checks actually performed and items without sufficient evidence."
---

# Actual verification results

## HTTP checks on 5 August 2026

| Endpoint | Result | One measured request |
|---|---|---|
| [Homepage](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/) | HTTP `200`, `text/html; charset=UTF-8` | approximately `446 ms` |
| [Statistics API](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats) | HTTP `200`, `application/json` | approximately `235 ms` |

These timings are single HTTP measurements, not averages, percentiles, or performance benchmarks.

## CI checks

- Backend tests: `success`.
- Frontend tests and runtime upload permission check: `success`.
- Terraform format/init/validate: `success`.
- Image build/push and ECS deployment: `success`.

Source: [GitHub Actions run #74](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30983018477).

## Not sufficiently evidenced

- Complete administrator, instructor, and student journeys.
- End-to-end VNPay payment.
- Cross-role authorization checks.
- Target health, ECS tasks, RDS, ECR, S3, and CloudWatch through Console/API.
- A 50-virtual-user k6 result.
