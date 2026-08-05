---
title: "Post-deployment operation"
date: 2026-08-05
weight: 6
chapter: false
pre: "<b>5.6.</b>"
description: "Application status and the EduFlow redeployment process."
---

# Post-deployment operation

## Service status

- The EduFlow website remained available through the AWS Application Load Balancer after the pipeline completed.
- The homepage and `/api/public/stats` both returned HTTP `200` during the check on 5 August 2026.
- The deployment job includes a step that waits for ECS services to stabilize.

## Repeatable deployment

- Pipeline #76 completed testing, image builds, and deployment in **9 minutes 7 seconds**.
- Frontend and backend container images were built, pushed to Amazon ECR, and applied to ECS.
- Terraform configuration and CI/CD workflows are version-controlled with the source for consistent deployments.
