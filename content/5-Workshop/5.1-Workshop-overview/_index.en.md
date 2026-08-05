---
title: "Deployment architecture"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>5.1.</b>"
description: "EduFlow architecture on AWS and deployment results."
---

# Deployment architecture

## System architecture

The EduFlow infrastructure is defined with Terraform and includes a VPC, public/private data subnets, security groups, ALB, ECS Fargate, ECR, RDS MySQL, S3, Secrets Manager, and CloudWatch Logs in `ap-southeast-1`.

```mermaid
flowchart TB
    Internet["Browser / Internet"] --> ALB["AWS Application Load Balancer\nHTTP"]
    ALB -->|"default"| FE["Frontend ECS :8080"]
    ALB -->|"/api/*"| BE["Backend ECS :8888"]
    FE --> BE
    BE --> DB[("RDS MySQL")]
    ECR["ECR images"] --> FE
    ECR --> BE
    SM["Secrets Manager"] -.-> FE
    SM -.-> BE
```

## Deployment results

- The public ALB DNS returned the homepage and statistics API with HTTP `200`.
- The `main` workflow completed backend tests, frontend tests, Terraform validation, image build/push, and ECS deployment.
- A browser smoke test verified public pages, Vietnamese–English switching, and
  redirecting anonymous checkout to the sign-in page.
- k6 completed 1,758 requests with 50 VUs, a 0.00% failure rate, and 1.84-second p95.
- The application is served through the default AWS Application Load Balancer DNS.
