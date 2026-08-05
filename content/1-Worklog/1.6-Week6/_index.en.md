---
title: "Week 6 (23-29 June 2026) - EduFlow cloud foundation"
date: 2026-06-29
weight: 6
chapter: false
pre: "<b>1.6.</b>"
description: "Build Terraform, a Fly.io environment, and container fixes for EduFlow."
---

## Date

**23 June 2026 - 29 June 2026**

## Actual work

- Initialized EduFlowPlatform AWS infrastructure with Terraform.
- Set up a Fly.io test deployment.
- Fixed UTF-8 build encoding and frontend/backend 503 and health timeout issues.

## Outcomes and evidence

- Established an Infrastructure-as-Code baseline and a verification environment before AWS deployment.
- [AWS Terraform IaC](https://github.com/L1nkinPark/EduFlowPlatform/commit/f868bc80f04b2d23142258e1861b1f0b534f32c8)
- [Fly.io environment](https://github.com/L1nkinPark/EduFlowPlatform/commit/e176301fef4f3feeb548688a307794302d6bb9a9)
- [UTF-8 build fix](https://github.com/L1nkinPark/EduFlowPlatform/commit/ae76f62ee501c10223ac4b1a0382b65d06ceb4b1)
- [503 and health timeout fix](https://github.com/L1nkinPark/EduFlowPlatform/commit/cec32b82f7aebc872d246d530eb8d33470b5ff7a)

## Challenge and resolution

The Java container used the wrong encoding and health checks failed while the backend was still starting. I enforced UTF-8, adjusted timeouts, and corrected cross-service health configuration.
