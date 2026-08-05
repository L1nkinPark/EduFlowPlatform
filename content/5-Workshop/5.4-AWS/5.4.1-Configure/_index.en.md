---
title: "Verified deployment configuration"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>5.4.1.</b>"
description: "Non-sensitive values supported by source code and the working URL."
---

# Verified deployment configuration

| Property | Evidence-backed value |
|---|---|
| AWS Region | `ap-southeast-1` |
| Terraform project/environment | `eduflow` / `dev` |
| Checked public protocol | HTTP |
| Public endpoint | `eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com` |
| Custom domain | No evidence available |
| ACM certificate/HTTPS | No evidence available |

SMTP credentials, VNPay credentials, JWT secrets, and database secrets are excluded. All sample emails, domains, and secret strings from the old template were removed because they are not actual deployment data.

Terraform validation for the repository configuration succeeded in [run #74](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30983018477).
