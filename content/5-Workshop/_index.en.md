---
title: "EduFlow Deployment Workshop"
date: 2026-08-05
weight: 5
chapter: false
pre: "<b>5.</b>"
---

# Deploy EduFlow on AWS

This workshop tests both Spring Boot applications, packages them with Docker, provisions infrastructure with Terraform, and deploys the frontend/backend to Amazon ECS Fargate behind an Application Load Balancer.

{{% notice warning %}}
The workshop creates billable AWS resources. Use only an authorized sandbox account, monitor cost, and complete the cleanup section.
{{% /notice %}}

{{% children description="true" /%}}
