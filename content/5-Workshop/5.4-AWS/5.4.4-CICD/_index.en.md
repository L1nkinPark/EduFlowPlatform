---
title: "Actual CI/CD result"
date: 2026-08-05
weight: 4
chapter: false
pre: "<b>5.4.4.</b>"
description: "Actual jobs and duration from GitHub Actions run #74."
---

# Actual CI/CD result

Evidence: [Test and Deploy to Amazon ECS Fargate #74](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30983018477), run on `main` for merge commit `78ad30b`.

| Job | Result | UTC interval |
|---|---|---|
| Terraform validation | `success` | 06:55:05–06:55:19 |
| Backend tests | `success` | 06:55:05–06:55:56 |
| Frontend tests | `success` | 06:55:06–06:56:03 |
| Build, push, and deploy | `success` | 06:56:06–07:03:49 |

GitHub reports a total run duration of **8 minutes 49 seconds**. This is pipeline duration, not total manual workshop time.

The deploy job recorded successful ECR login, two image builds/pushes, ECS deployment, and service stabilization. No additional current resource state is inferred without access to the matching AWS environment.
