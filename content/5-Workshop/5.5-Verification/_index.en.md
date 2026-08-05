---
title: "Test results"
date: 2026-08-05
weight: 5
chapter: false
pre: "<b>5.5.</b>"
description: "EduFlow HTTP, browser, load, and CI/CD results."
---

# Test results

## HTTP checks on 5 August 2026

| Endpoint | Result | One measured request |
|---|---|---|
| [Homepage](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/) | HTTP `200`, `text/html; charset=UTF-8` | approximately `498 ms` |
| [Statistics API](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats) | HTTP `200`, `application/json` | approximately `137 ms` |

The HTTP results were recorded on 5 August 2026.

The statistics payload reported **5 courses, 2 instructors, 6 students, and 1
enrollment**. [Stored JSON](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/public-stats-2026-08-05.json).

## Browser smoke test

- The Vietnamese homepage rendered navigation, search, featured courses, and prices.
- VI/EN switched public labels between Vietnamese and English.
- The course catalog and a course-detail page rendered successfully.
- An anonymous visitor selecting **Buy Now** was redirected to `/signin`.

[Homepage screenshot](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/eduflow-home-2026-08-05.png) ·
[Course-detail screenshot](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/eduflow-course-detail-2026-08-05.png)

## k6 load test

The read-only scenario ramped to **50 virtual users**, held 50 VUs for 30
seconds, and then ramped down. It called `/api/courses`, `/api/categories`, and
`/api/public/stats`.

| Metric | Result |
|---|---|
| HTTP requests | 1,758 |
| Checks | 2,930/2,930 passed (100%) |
| HTTP failure rate | 0.00% |
| Average / median | 665.06 ms / 498.24 ms |
| p90 / p95 | 1.49 s / 1.84 s |
| Maximum | 3.13 s |

All thresholds passed. The [JSON summary](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/k6-summary-2026-08-05.json) and
[verification record](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/verification-2026-08-05.md) are stored with the source.

## CI checks

- Backend tests: `success`.
- Frontend tests and runtime upload permission check: `success`.
- Terraform format/init/validate: `success`.
- Image build/push and ECS deployment: `success`.

Source: [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529), total duration **9 minutes 7 seconds**.
