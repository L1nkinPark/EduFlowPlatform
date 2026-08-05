---
title: "Week 7 - Payments and promotions"
date: 2026-08-05
weight: 7
chapter: false
pre: "<b>1.7.</b>"
description: "Stabilize VNPay checkout, callbacks, and VND amount handling."
---

## Objective

Keep amounts, signatures, and order state consistent from checkout through callback.

## Work completed

- Validated promotions by date, usage limits, and minimum order value.
- Normalized VNPay amounts to the smallest unit and standardized VND display.
- Fixed parameter/signature encoding and configured `PAYMENT_RETURN_ORIGIN` behind the load balancer.
- Replaced generic 500 pages with actionable checkout errors.

## Outcome

The frontend requests payment through the backend, and the callback verifies the transaction before course ownership is recorded. Dedicated unit tests cover amount conversion cases.

## Lesson learned

Payment integrations need one amount representation and identical parameter canonicalization during signing and verification.
