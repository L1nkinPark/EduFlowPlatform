---
title: "Week 3 - Authentication and authorization"
date: 2026-08-05
weight: 3
chapter: false
pre: "<b>1.3.</b>"
description: "Complete JWT, sessions, OTP, and role-based boundaries."
---

## Objective

Protect data and actions by role while keeping the sign-in experience understandable.

## Work completed

- Completed JWT registration/login, refresh tokens, OTP, and password reset.
- Allowed email or username sign-in and normalized frontend cookie/session behavior.
- Restricted instructor creation to administrators and enforced ownership in course APIs.
- Moved the JWT signing secret to environment configuration and AWS Secrets Manager.

## Outcome

Public, authenticated, and role-specific routes have explicit boundaries. Both containers can verify the same tokens without a hard-coded secret.

## Lesson learned

Hiding a UI control is not authorization; enforcement belongs in the backend and must include resource ownership.
