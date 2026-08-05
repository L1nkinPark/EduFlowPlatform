---
title: "Easy-to-miss details in VNPay integration"
date: 2026-08-05
weight: 3
chapter: false
pre: "<b>3.3.</b>"
description: "Amounts, encoding, callback URLs, and trust boundaries in payments."
---

# Easy-to-miss details in VNPay integration

A payment URL can look valid and still be rejected when an amount or signature string differs by one character. EduFlow standardized four areas.

## 1. Amount units

The application stores VND while VNPay expects the amount multiplied by 100. Conversion lives in a dedicated utility rather than controllers, making zero, invalid fractional, and large values testable.

## 2. Canonicalization

Parameters must be sorted, UTF-8 encoded, and joined identically during signing and callback verification. A `+` versus `%20` difference is sufficient to invalidate a signature.

## 3. Return origin behind a proxy

A container does not inherently know the public ALB URL. Infrastructure supplies `PAYMENT_RETURN_ORIGIN`; callback construction should not blindly trust client-provided headers.

## 4. The callback confirms payment

Do not grant course access because a browser reaches a success page. The backend verifies response code, signature, amount, and order state before recording ownership.

## Conclusion

Treat payments as a security protocol, not a URL redirect. Pure utilities, test vectors, and secret-free logging make diagnosis safer.
