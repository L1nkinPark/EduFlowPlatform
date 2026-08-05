---
title: "Deployment resources"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.2.</b>"
description: "Source code, CI/CD, and infrastructure configuration used in the workshop."
---

# Deployment resources

## Documentation and tools

- The [EduFlowPlatform](https://github.com/L1nkinPark/EduFlowPlatform) source and commit history.
- [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529) on `main`.
- Direct HTTP probes of the ALB DNS on 5 August 2026.
- Repository Terraform configuration for managing the AWS architecture.

## Security principles

- Account IDs, access keys, passwords, JWTs, OTPs, VNPay secrets, and secret values are not published.
- Sensitive values are supplied through GitHub Actions secrets and AWS runtime configuration.
