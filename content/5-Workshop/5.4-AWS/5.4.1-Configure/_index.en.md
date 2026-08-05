---
title: "Deployment configuration"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>5.4.1.</b>"
description: "Configuration values used for the EduFlow environment on AWS."
---

# Deployment configuration

| Property | Value |
|---|---|
| AWS Region | `ap-southeast-1` |
| Terraform project/environment | `eduflow` / `dev` |
| Checked public protocol | HTTP |
| Public endpoint | `eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com` |

SMTP credentials, VNPay credentials, JWT secrets, and database secrets are managed through deployment-environment secrets and are not displayed in the report.

Terraform validation for the repository configuration succeeded in [run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529).
