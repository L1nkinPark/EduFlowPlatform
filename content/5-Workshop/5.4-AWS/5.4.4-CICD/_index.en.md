---
title: "Actual CI/CD result"
date: 2026-08-05
weight: 4
chapter: false
pre: "<b>5.4.4.</b>"
description: "Actual jobs and duration from GitHub Actions run #76."
---

# Actual CI/CD result

Evidence: [Test and Deploy to Amazon ECS Fargate #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529), run on `main` for merge commit `ad4e808`.

| Job | Result | UTC interval |
|---|---|---|
| Terraform validation | `success` | 07:40:51–07:41:13 |
| Backend tests | `success` | 07:40:44–07:41:41 |
| Frontend tests | `success` | 07:40:45–07:41:57 |
| Build, push, and deploy | `success` | 07:42:06–07:49:47 |

GitHub reports a total run duration of **9 minutes 7 seconds**. This is pipeline duration, not total manual workshop time.

The deploy job recorded successful ECR login, two image builds/pushes, ECS deployment, and service stabilization. No additional current resource state is inferred without access to the matching AWS environment.
