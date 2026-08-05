---
title: "Week 12 - CI/CD and production hardening"
date: 2026-08-05
weight: 12
chapter: false
pre: "<b>1.12.</b>"
description: "Automate tests, image delivery, ECS rollout, and production hardening."
---

## Objective

Create a safe release path and reduce time spent in a broken production state.

## Work completed

- Added GitHub Actions jobs for backend tests, frontend tests, and Terraform validation.
- Built and pushed both images to ECR by commit SHA before updating ECS services.
- Cancelled stale pipelines, bounded AWS credential retries, and retried transient Docker builds.
- Hardened sessions, JWT, instructor APIs, uploads, Vietnamese content, and checkout errors.

## Outcome

Only the newest revision that passes all gates is deployed. Production configuration incidents became automated checks or documented runtime configuration.

## Lesson learned

CI/CD is more than a deploy command: it encodes safe ordering, bounded waits, and protection against an older revision overwriting a newer fix.
