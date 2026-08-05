---
title: "Terraform and infrastructure evidence"
date: 2026-08-05
weight: 3
chapter: false
pre: "<b>5.4.3.</b>"
description: "Separate Terraform configuration, validation results, and unverified AWS state."
---

# Terraform and infrastructure evidence

## Verified

- The **Terraform validation** job in [run #74](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30983018477) succeeded.
- Format check, `terraform init -backend=false`, and `terraform validate` all reported `success`.
- Terraform source defines the default ALB route to the frontend and `/api/*` to the backend.
- The actual ALB DNS returned HTTP `200` for the homepage and statistics API.

## Not directly verified through AWS API/Console

- Current ECS running and desired task counts.
- Target health for each target group.
- RDS status, class, storage, and public-access configuration.
- S3 public access block and the ECR image list.

These items are not marked successful based only on Terraform source or a local state file.
