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
| Public API | [`/api/public/stats`](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats) returned HTTP `200`; its payload reported 5 courses, 2 instructors, 6 students, and 1 enrollment |
| Public domain | Default AWS ALB DNS; no evidence of a custom domain |
| CI/CD | [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529) succeeded |
| Pipeline duration | `9 minutes 7 seconds` as reported by GitHub Actions |
| Browser smoke test | Homepage, catalog/detail pages, Vietnamese–English switching, and anonymous checkout redirect to `/signin` were checked |
| k6 result | 50 VUs, 1,758 requests, 0 failures, p95 `1.84 seconds`; [JSON summary](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/k6-summary-2026-08-05.json) |
| Actual cost | Not verified from the deployment account |
| AWS Console screenshots | None are stored in the repository |
| Manual workshop duration | Not recorded |
| Report GitHub Pages | The build workflow succeeded, but the Pages URL returned HTTP `404`; public Pages is not enabled for the repository |

{{% notice info %}}
The individual HTTP measurements are point-in-time availability checks. The k6
load-test profile and machine-readable summary are recorded separately.
{{% /notice %}}

{{% children description="true" /%}}
