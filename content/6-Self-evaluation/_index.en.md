---
title: "Skills and evidence cross-check"
date: 2026-08-05
weight: 6
chapter: false
pre: "<b>6.</b>"
---

# Skills and evidence cross-check

No self-rating scale or mentor assessment was provided by the participant. Therefore, the old template's “Good/Fair” ratings were removed and this section only lists artifacts that can be checked at the current commit.

| Area | Current evidence | Limitation |
|---|---|---|
| Spring Boot backend | REST/JPA/Security/JWT/OTP/VNPay source; 16 committed Java test files; successful backend job in [run #74](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30983018477) | No coverage report or complete E2E evidence |
| Spring Boot/Thymeleaf frontend | Templates, Vietnamese/English i18n, and 15 committed Java test files; successful frontend and upload-permission checks | No stored browser E2E result |
| Cloud/IaC | Seven Terraform modules: `vpc`, `security-groups`, `alb`, `ecs`, `rds`, `s3`, and `secrets-manager`; successful Terraform validation | Individual resources were not queried through the correct AWS account |
| CI/CD | Backend, frontend, Terraform, and deploy jobs all reported `success`; run #74 took 8 minutes 49 seconds | Does not replace long-term service-state verification |
| Security | Role/ownership checks, runtime secret configuration, cookie hardening, and non-root container source | No independent penetration test or security audit |
| Documentation | Bilingual Hugo content, commit-backed worklog, and Workshop verified/unverified separation | No AWS Console screenshots or mentor feedback |

## Evidence still needed, not completed results

1. Browser E2E for all three roles, OTP, and VNPay callbacks.
2. Coverage artifacts for both Maven suites.
3. GitHub OIDC instead of long-lived access keys.
4. Remote Terraform state with locking.
5. Billing/Cost Explorer and Console screenshots from the correct environment.
6. Stored metrics, alarms, backup/restore tests, and k6 artifacts.

These are next-step proposals and are not counted as implemented features.
