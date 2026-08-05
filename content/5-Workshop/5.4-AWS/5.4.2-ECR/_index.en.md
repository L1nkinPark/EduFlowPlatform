---
title: "Build and push images to ECR"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.4.2.</b>"
description: "Container image build and push results from GitHub Actions."
---

# Build and push images to ECR

In [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529), the **Build, push, and deploy** job reported `success` for:

- Configure AWS credentials.
- Log in to Amazon ECR.
- Build and push the frontend image.
- Build and push the backend image.
- Deploy ECS services.
- Wait for ECS services to stabilize.
