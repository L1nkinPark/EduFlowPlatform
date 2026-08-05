---
title: "Week 10 - Containers and dev environment"
date: 2026-08-05
weight: 10
chapter: false
pre: "<b>1.10.</b>"
description: "Package both applications, add health checks, and establish Fly.io development deployment."
---

## Objective

Produce artifacts that run consistently on developer machines, in CI, and in cloud environments.

## Work completed

- Built separate Java 17 Dockerfiles for frontend and backend.
- Configured health endpoints, ports, environment variables, and backend URL.
- Added `fly.toml` files and Fly.io development deployment guidance.
- Fixed build encoding and inter-service 503/timeout issues.

## Outcome

Both applications run as independent containers with runtime configuration rather than hard-coded environment values. Fly.io provides a verification step before AWS.

## Lesson learned

A reusable image contains no environment-specific configuration; the same image should run locally, in staging, and in production through runtime variables.
