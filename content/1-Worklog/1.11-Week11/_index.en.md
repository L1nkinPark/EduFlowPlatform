---
title: "Week 11 - AWS infrastructure with Terraform"
date: 2026-08-05
weight: 11
chapter: false
pre: "<b>1.11.</b>"
description: "Model VPC, security, ALB, ECS, RDS, S3, and Secrets Manager."
---

## Objective

Turn the EduFlow deployment diagram into reproducible, reviewable infrastructure code.

## Work completed

- Split `vpc`, `security-groups`, `alb`, `ecs`, `rds`, `s3`, and `secrets-manager` modules.
- Placed frontend/backend Fargate services behind an ALB with `/api/*` routed to the backend.
- Isolated RDS in private data subnets and injected secrets into task definitions.
- Reduced resources to match the diagram and dev sizing for cost control.

## Outcome

Terraform creates networking, database, registries, cluster, and services; outputs expose the ALB DNS, RDS endpoint, bucket, and ECR URLs.

## Lesson learned

Modules should follow operational boundaries. Explicit dependencies between RDS, secrets, and ECS prevent cycles and stale credentials.
