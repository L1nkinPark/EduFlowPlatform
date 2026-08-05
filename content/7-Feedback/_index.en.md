---
title: "Retrospective and feedback"
date: 2026-08-05
weight: 7
chapter: false
pre: "<b>7.</b>"
---

# Retrospective and feedback

## What went well

EduFlow moved from a UI with substantial static content to a real-data platform for administrators, instructors, and students. Purchase-learning-progress, i18n, uploads, OTP, and VNPay connect to the backend; AWS infrastructure and delivery pipelines are also represented as code.

The strongest outcome is traceability: a Git revision maps to ECR images, ECS services have distinct logs, Terraform describes resources, and tests preserve important fixes.

## Remaining friction

- Two Spring Boot services require JWT, URL, and timeout coordination.
- Local uploads on Fargate last only for the task lifetime; production media should live entirely in object storage.
- CI still uses access keys and local Terraform state is not team-ready.
- Unit coverage improved, but does not replace browser-level end-to-end tests.
- Single-AZ RDS and tasks in public subnets are cost-conscious dev choices, not a production HA design.

## Process improvements

1. Start changes with acceptance criteria and regression tests for important defects.
2. Review API contracts and the threat model before authorization or payment changes.
3. Use an immutable staging environment with SHA-tagged images before production updates.
4. Collect real metrics before increasing capacity or adding AWS services.
5. Update the workshop with the code so the documentation remains reproducible.

## Conclusion

The project now has a demonstrable, repeatably deployable MVP foundation. The next maturity step is reliability rather than feature volume: migrations, E2E tests, OIDC, durable object storage, remote state, and observability.

Technical feedback and issues can be submitted through [EduFlowPlatform Issues](https://github.com/L1nkinPark/EduFlowPlatform/issues).
