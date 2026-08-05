# EduFlow verification evidence — 05/08/2026

Time zone: Asia/Bangkok (UTC+7).

## Public deployment

- `GET http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/`
  returned HTTP `200` with `text/html; charset=UTF-8` in one measured request
  of approximately `498 ms`.
- `GET /api/public/stats` returned HTTP `200` with `application/json` in one
  measured request of approximately `137 ms`.
- The public statistics payload reported 5 courses, 2 instructors, 6 students,
  and 1 enrollment.
- `GET /api/courses` and `GET /api/categories` both returned HTTP `200` before
  the load test.

These are point-in-time availability checks, not uptime guarantees.

## Browser smoke test

The deployed application was checked through a real browser:

- Vietnamese homepage rendered with navigation, search, featured courses,
  course prices, and sign-in/sign-up actions.
- Language switching changed the public navigation and page labels between
  Vietnamese and English.
- The course catalog and a course-detail page rendered successfully.
- Selecting **Buy Now** as an anonymous visitor redirected to `/signin`.

Screenshots:

- `eduflow-home-2026-08-05.png`
- `eduflow-course-detail-2026-08-05.png`

The test did not submit registration, payment, OTP, or other state-changing
forms.

## k6 load test

Command:

```text
k6 run --summary-export=static/evidence/k6-summary-2026-08-05.json k6-load-test.js
```

Profile: ramp to 50 virtual users for 20 seconds, hold 50 virtual users for 30
seconds, then ramp down for 10 seconds. The script sent read-only requests to
`/api/courses`, `/api/categories`, and `/api/public/stats`.

Results:

- 586 completed iterations.
- 1,758 HTTP requests.
- 2,930/2,930 checks passed (100%).
- HTTP request failure rate: 0.00% (0/1,758).
- Average response time: 665.06 ms.
- Median response time: 498.24 ms.
- p90 response time: 1.49 s.
- p95 response time: 1.84 s.
- Maximum response time: 3.13 s.
- Thresholds passed: checks >99%, failures <5%, p95 <8 s.

Machine-readable output: `k6-summary-2026-08-05.json`.

## GitHub Actions

[Test and Deploy to Amazon ECS Fargate #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529)
completed successfully for commit `ad4e808e71bc66ba1439c01facfbe95a1d9a114f`.

- Backend tests: success.
- Frontend tests: success.
- Terraform validation: success.
- Build, push, and deploy: success.
- Total workflow duration: 9 minutes 7 seconds.

## Evidence limitations

- The GitHub Pages workflow succeeded, but
  `https://l1nkinpark.github.io/EduFlowPlatform/` returned HTTP `404` and the
  public GitHub Pages API did not report an enabled Pages site.
- The locally available AWS CLI identity did not expose the deployed
  `eduflow-dev` ECS/ALB resources, so it was not used to claim Console-level
  resource state or cost.
- No AWS Billing/Cost Explorer data, AWS Console screenshots, VNPay end-to-end
  payment, mentor feedback, or independent security audit was available.
