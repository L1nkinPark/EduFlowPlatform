---
title: "Evidence sources and limitations"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.2.</b>"
description: "Sources used for verification and items without sufficient evidence."
---

# Evidence sources and limitations

## Sources used

- The [EduFlowPlatform](https://github.com/L1nkinPark/EduFlowPlatform) source and commit history.
- [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529) on `main`.
- Direct HTTP probes of the ALB DNS on 5 August 2026.
- Repository Terraform configuration for the intended architecture.

## Recording rules

- A CI outcome is recorded as successful only when the public job/step reports `success`.
- An HTTP result only confirms that the endpoint responded at the measured time.
- Terraform configuration alone does not prove that an AWS resource is `healthy`.
- Account IDs, access keys, passwords, JWTs, OTPs, VNPay secrets, and secret values are not published.

## Current limitation

The available AWS CLI session cannot verify resources in the deployment environment. Therefore, this report does not claim current ECS task, target group, RDS, ECR, or S3 Console state and does not use cost data from a different AWS account.
