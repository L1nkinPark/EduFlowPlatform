---
title: "Deploy to AWS"
date: 2026-08-05
weight: 4
chapter: false
pre: "<b>5.4.</b>"
description: "Configure Terraform, bootstrap ECR, provision infrastructure, and enable CI/CD."
---

# Deploy to AWS

This section uses `ap-southeast-1` and the default `eduflow-dev-*` names. ECR is bootstrapped first so ECS references existing application images when services are created.

{{% children description="true" /%}}
