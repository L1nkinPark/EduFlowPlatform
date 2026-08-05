---
title: "Splitting Spring Boot frontend and backend in EduFlow"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>3.1.</b>"
description: "Lessons about API contracts, shared JWT configuration, and service-boundary failures."
---

# Splitting Spring Boot frontend and backend in EduFlow

EduFlow uses two Java applications: a Thymeleaf frontend on port 8080 and a REST backend on port 8888. This separation gives each layer an independent lifecycle, but introduces network, authentication, and error-handling boundaries.

## Why separate them?

- The frontend owns HTML, forms, sessions, i18n, and role-specific experience.
- The backend owns business rules, JPA data, authorization, and payment/email integrations.
- Either service can scale or redeploy without repackaging the entire system.

## The contract

The frontend never accesses the database. Data crosses request/response DTOs and `/api/*`. The backend issues JWTs, while the frontend needs the same signing secret to interpret authentication; both containers therefore receive one runtime secret.

```text
Browser -> Frontend session -> Authorization: Bearer <JWT> -> Backend API
```

## Three representative failures

1. **Hard-coded backend URL:** works locally and fails behind the ALB. Use configurable `BACKEND_URL` plus explicit timeouts.
2. **Mismatched JWT secrets:** login succeeds, then later requests return 401. Use one Secrets Manager source.
3. **Backend errors collapse into generic 500 pages:** distinguish timeouts, authentication failures, and business errors at the web boundary.

## Conclusion

Service separation creates value only when API contracts, configuration, and error observability are product features. Otherwise network complexity outweighs deployment flexibility.
