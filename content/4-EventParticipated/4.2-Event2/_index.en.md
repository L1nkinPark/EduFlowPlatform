---
title: "Release demo and verification"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>4.2.</b>"
description: "Run an end-to-end three-role scenario before ECS delivery."
---

# Release demo and verification

## Scenario

1. An administrator creates an instructor and reviews the dashboard.
2. The instructor signs in and creates a course, chapter, and video/document lesson.
3. A student registers with OTP, finds the course, and applies a promotion.
4. Checkout runs through VNPay Sandbox and the callback is verified.
5. The student learns, completes a lesson, and checks progress percentage.
6. The UI switches between Vietnamese/English and checks VND, mobile layout, and backend failures.

## Acceptance criteria

- Primary dashboards use no mock records.
- Invalid privileges return an appropriate 401/403 or redirect without exposing data.
- Backend/frontend tests and Terraform validation pass.
- ALB health checks are green and ECS deployment stabilizes.

## Recorded outcome

Demo cycles exposed and resolved upload permissions, Vietnamese fonts, VND amounts, checkout errors, authenticated headers, and instructor API authorization.
