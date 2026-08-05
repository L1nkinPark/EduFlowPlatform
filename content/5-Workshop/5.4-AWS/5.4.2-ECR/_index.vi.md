---
title: "Build và push image lên ECR"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.4.2.</b>"
description: "Kết quả build và push container image từ GitHub Actions."
---

# Build và push image lên ECR

Trong [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529), job **Build, push, and deploy** ghi nhận `success` cho các bước:

- Configure AWS credentials.
- Log in to Amazon ECR.
- Build and push frontend image.
- Build and push backend image.
- Deploy ECS services.
- Wait for ECS services to stabilize.
