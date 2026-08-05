---
title: "Architecture and threat-model review"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>4.1.</b>"
description: "Review network, data, secret, and role boundaries."
---

# Architecture and threat-model review

## Objective

Compare the AWS diagram, Terraform, and application flow to find unnecessary access paths and inconsistent secret configuration.

## Checklist

- Only the ALB accepts Internet traffic.
- Frontend/backend accept traffic only from the intended security groups.
- RDS sits in private data subnets and only the backend reaches port 3306.
- JWT, database, SMTP, and VNPay values arrive through secrets/runtime variables.
- Administrator/instructor APIs enforce both role and ownership.
- Payment callbacks do not trust browser state.

## Recorded outcome

The review resulted in stable ECS JWT configuration, tighter instructor course APIs, hardened sessions, and explicit frontend/backend timeouts.

## Deliverable

A reusable checklist for every Terraform, security-rule, or authentication-flow change.
