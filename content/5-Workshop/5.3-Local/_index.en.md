---
title: "Recorded test results"
date: 2026-08-05
weight: 3
chapter: false
pre: "<b>5.3.</b>"
description: "Backend and frontend results supported by GitHub Actions evidence."
---

# Recorded test results

The report uses public CI evidence instead of claiming unrecorded local runs. The supporting run is [Test and Deploy to Amazon ECS Fargate #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529).

| Job | Result | Recorded time |
|---|---|---|
| Backend tests | `success` | 06:55:05–06:55:56 UTC |
| Frontend tests | `success` | 06:55:06–06:56:03 UTC |
| Terraform validation | `success` | 06:55:05–06:55:19 UTC |
| Build, push, and deploy | `success` | 06:56:06–07:03:49 UTC |

The test count is not reported because the current public evidence does not expose a stored test-summary artifact.

{{% children description="true" /%}}
