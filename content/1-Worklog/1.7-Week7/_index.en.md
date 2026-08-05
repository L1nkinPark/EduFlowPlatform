---
title: "Week 7 (30 June-6 July 2026) - Aegis, AABW"
date: 2026-07-06
weight: 7
chapter: false
pre: "<b>1.7.</b>"
description: "Integrate the Aegis monitoring and incident-response ecosystem for AABW."
---

## Date and context

**30 June 2026 - 6 July 2026** — **Aegis**, an **AABW** hackathon project.

## Actual work

- Developed the simulated bank, monitoring dashboard, SOAR engine, mobile app, and deployment environment.
- Integrated Kafka event delivery and dashboard threat mapping.
- Integrated OPA authentication, forged-JWT handling, and secure mobile token storage.

## Outcomes and evidence

- The banking and security components could exchange events, authenticate, and display alerts.
- [Aegis deployment repository](https://github.com/Little-Boy-s-Aegis/aegis-bank-deployment)
- [Kafka and dashboard threat mapping](https://github.com/Little-Boy-s-Aegis/dashboard/commit/645bdc95651fae87f9a9b2ad43e41145c938733a)
- [SOAR OPA authentication](https://github.com/Little-Boy-s-Aegis/aegis-soar-engine/commit/76e7dcd72728cc8dd6005e197a68c5c835710f9b)
- [Forged-JWT handling](https://github.com/Little-Boy-s-Aegis/aegis-bank-backend/commit/65327897c3d78a51509de81bb563a554af9a5979)
- [Mobile secure storage](https://github.com/Little-Boy-s-Aegis/aegis-bank-mobile-app/commit/d16229362b918a8f831e3e8426d3e205bc28c4a7)

## Challenge and resolution

The system contained many services with different authentication flows. I standardized tokens and HTTP status codes, bound internal services to localhost, and used secure storage on mobile.
