---
title: "Verified scope"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>5.1.</b>"
description: "Architecture defined by Terraform and the current evidence scope."
---

# Verified scope

## Architecture defined in source code

The Terraform source defines a VPC, public/private data subnets, security groups, ALB, ECS Fargate, ECR, RDS MySQL, S3, Secrets Manager, and CloudWatch Logs in `ap-southeast-1`. This is a source-code description and is not used to claim that every resource is currently healthy without a matching AWS query.

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

## Directly observed

- The public ALB DNS returned the homepage and statistics API with HTTP `200`.
- The `main` workflow completed backend tests, frontend tests, Terraform validation, image build/push, and ECS deployment.
- The checked URL uses HTTP; there is no evidence of HTTPS or a custom domain.

## Evidence not available

- Step-by-step AWS Console screenshots.
- k6 output and actual p95/error-rate metrics.
- Total manual time from start to completion.
- Cost from the correct deployment account/environment.
