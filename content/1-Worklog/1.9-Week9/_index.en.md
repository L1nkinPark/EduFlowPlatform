---
title: "Week 9 (14-20 July 2026) - EduFlow, Aegis, and SecHub"
date: 2026-07-20
weight: 9
chapter: false
pre: "<b>1.9.</b>"
description: "Test EduFlow, automate Aegis, and develop SecHub for an OpenAI hackathon."
---

## Date and context

**14 July 2026 - 20 July 2026** — EduFlow; final Aegis work for AABW; **SecHub for an OpenAI hackathon**.

## Actual work

- Added EduFlow registration/payment tests, VNPAY fixes, i18n, ECS Terraform, and k6.
- Added Docker image CI for the Aegis dashboard, SOAR engine, and staging sandbox.
- Completed Vietnamese/English internationalization for SecHub.

## Outcomes and evidence

- EduFlow gained an ALB load-test scenario and a more stable payment flow.
- Aegis service image builds were automated.
- SecHub gained a bilingual user interface for the OpenAI hackathon entry.
- [EduFlow k6 test through the ALB](https://github.com/L1nkinPark/EduFlowPlatform/commit/5e42f3665fb39a1f9d3424f360774e9747d01d6a)
- [Aegis dashboard Docker CI](https://github.com/Little-Boy-s-Aegis/dashboard/commit/7217393847bc9b7951692670cff4322bbb5f3e30)
- [SOAR Docker CI](https://github.com/Little-Boy-s-Aegis/aegis-soar-engine/commit/31d4587ac095546cd1a0ea6ac1aa17374890f28d)
- [SecHub internationalization](https://github.com/Little-Boy-s-SecHub/SecHub/commit/180b1f438edccfde0cf92c686ded3f809a07c06d)

## Challenge and resolution

Payment flows, runtime configuration, and multi-service CI used many different environment variables. I standardized encoding and configuration, added regression tests, and automated image builds.

## Mentor feedback

Not available yet; mentor or organizer feedback will be added later.
