---
title: "Self-assessment"
date: 2026-08-05
weight: 6
chapter: false
pre: "<b>6.</b>"
---

# Project self-assessment

This assessment is based on repository artifacts as of 5 August 2026. It does not replace mentor or organization feedback.

| Criterion | Self-rating | Evidence |
|---|---|---|
| Business analysis | Good | Completed three-role and course-lifecycle journeys with real data |
| Spring Boot backend | Good | REST, JPA, Security/JWT, OTP, VNPay, progress, and exception handling |
| Frontend/UX | Fair-to-good | Thymeleaf, Vietnamese/English i18n, responsive fixes, and error feedback |
| Testing | Fair-to-good | 19 backend and 15 frontend test classes plus security/money/i18n coverage |
| Cloud/IaC | Fair-to-good | Seven Terraform modules covering VPC, ALB, ECS, RDS, S3, and secrets |
| CI/CD | Fair-to-good | Test-gated ECR/ECS delivery, concurrency, bounded retries, and diagnostics |
| Security | Fair-to-good | Role/ownership checks, runtime secrets, cookie hardening, and non-root images |
| Documentation | Good | Bilingual report and reproducible workshop with verification/cleanup |

## Strengths

- Followed cross-layer failures to root causes across UI, API, containers, ALB, and runtime secrets.
- Replaced mock data with verifiable journeys, improving dashboard and experience credibility.
- Turned production failures into tests, validation, or pipeline diagnostics.

## Areas for improvement

1. Move Terraform state to an S3 backend with locking for team use.
2. Replace long-lived CI access keys with GitHub OIDC and a short-lived IAM role.
3. Add automated integration/E2E tests for OTP, VNPay callbacks, and all three roles.
4. Add versioned migrations (Flyway/Liquibase) instead of `ddl-auto=update` in production.
5. Improve HA with private app subnets/NAT or VPC endpoints, Multi-AZ RDS, and scaling after real load evidence.
6. Add metrics, alarms, SLOs, and a practiced backup/restore procedure.

## Next goal

Prioritize supply-chain security and recovery: OIDC, image scanning gates, migrations, E2E tests, remote state, alarms, and one database recovery exercise.
