---
title: "Week 9 - Testing and performance"
date: 2026-08-05
weight: 9
chapter: false
pre: "<b>1.9.</b>"
description: "Expand unit/security tests and establish a repeatable k6 load scenario."
---

## Objective

Reduce service/controller regressions and create a repeatable performance baseline.

## Work completed

- Built 19 backend and 15 frontend test classes for services, controllers, JWT, exceptions, and utilities.
- Added authorization, payment callback, VNPay amount, and i18n parity regression tests.
- Configured MySQL in CI and ran independent Maven suites.
- Added `k6-load-test.js` for the deployed access path.

## Outcome

The pipeline blocks delivery when tests or Terraform validation fail. Critical fixes are paired with regression coverage for their root causes.

## Lesson learned

Test count matters less than boundary coverage: privileges, money, encoding, timeouts, and empty data.
