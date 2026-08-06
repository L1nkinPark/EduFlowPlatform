---
title: "Workshop"
date: 2026-08-05
weight: 5
chapter: false
pre: " <b> 5. </b> "
---

# EduFlow deployment workshop on AWS

This section presents the EduFlow deployment process on AWS, CI/CD results, browser testing, and load testing performed on 5 August 2026.

## Deployment results on 5 August 2026

| Item | Result |
|---|---|
| Application website | [EduFlow ALB](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/) returned HTTP `200` |
| Public API | [`/api/public/stats`](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats) returned HTTP `200`; its payload reported 5 courses, 2 instructors, 6 students, and 1 enrollment |
| Deployment domain | `eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com` — AWS Application Load Balancer DNS |
| CI/CD | [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529) succeeded |
| Pipeline duration | `9 minutes 7 seconds` as reported by GitHub Actions |
| Browser smoke test | Homepage, catalog/detail pages, Vietnamese–English switching, and anonymous checkout redirect to `/signin` were checked |
| k6 result | 50 VUs, 1,758 requests, 0 failures, p95 `1.84 seconds`; [JSON summary](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/k6-summary-2026-08-05.json) |
| Online report | [GitHub Pages](https://l1nkinpark.github.io/EduFlowPlatform/) is active and deployed automatically from the Hugo source |

{{% notice info %}}
The HTTP and k6 results were recorded on 5 August 2026. The test profile and machine-readable JSON summary are stored with the source.
{{% /notice %}}

{{% children description="true" /%}}
