---
title: "EduFlow Deployment Workshop"
date: 2026-08-05
weight: 5
chapter: false
pre: "<b>5.</b>"
---

# Verified EduFlow AWS deployment record

This section only contains data that was checked directly or has public evidence. Items without logs, screenshots, or appropriate access are marked **not verified** instead of being replaced with sample values.

## Verified summary as of 5 August 2026

| Item | Checked result |
|---|---|
| Application website | [EduFlow ALB](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/) returned HTTP `200` |
| Public API | [`/api/public/stats`](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats) returned HTTP `200` |
| Public domain | Default AWS ALB DNS; no evidence of a custom domain |
| CI/CD | [GitHub Actions run #74](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30983018477) succeeded |
| Pipeline duration | `8 minutes 49 seconds` as reported by GitHub Actions |
| k6 result | No result log is stored in the repository or Actions |
| Actual cost | Not verified from the deployment account |
| AWS Console screenshots | None are stored in the repository |
| Manual workshop duration | Not recorded |

{{% notice info %}}
The HTTP measurements are point-in-time availability checks, not load-test results.
{{% /notice %}}

{{% children description="true" /%}}
