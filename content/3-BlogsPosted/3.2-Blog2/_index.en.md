---
title: "From architecture diagram to ECS Fargate with Terraform"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>3.2.</b>"
description: "How EduFlow turns an AWS architecture into reproducible infrastructure modules."
---

# From architecture diagram to ECS Fargate with Terraform

EduFlow infrastructure does not aim to use the most AWS services. It maps each operational need to one clear component.

## Module boundaries

| Module | Role |
|---|---|
| `vpc` | VPC, public subnets, and private data subnets |
| `security-groups` | Permit only ALB → apps and backend → RDS |
| `alb` | TLS/HTTP entry point, health checks, and `/api/*` routing |
| `ecs` | Cluster, ECR, task definitions, and Fargate services |
| `rds` | MySQL, subnet group, backups, and monitoring role |
| `secrets-manager` | Database, SMTP, JWT, and VNPay runtime values |
| `s3` | Asset/backup bucket with public access blocked |

## Delivery flow

GitHub Actions runs tests and `terraform validate`, signs in to ECR, builds both images tagged with the commit SHA, pushes them, and forces new ECS deployments. The SHA provides exact traceability; `latest` supports simple operations.

## Cost decisions

The dev environment uses low desired counts, Single-AZ RDS, and no modules absent from the architecture. This is intentional: control MVP cost while preserving `multi_az` and desired-count inputs for future availability requirements.

## Conclusion

Good Terraform is executable documentation. Module names, inputs/outputs, and security groups should communicate the architecture without reading every resource.
