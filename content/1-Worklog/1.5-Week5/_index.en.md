---
title: "Week 5 (16-22 June 2026) - Tardis, AABW"
date: 2026-06-22
weight: 5
chapter: false
pre: "<b>1.5.</b>"
description: "Build Tardis for the AABW hackathon."
---

## Date and context

**16 June 2026 - 22 June 2026** — **Tardis**, an **AABW** hackathon project.

## Actual work

- Built a pipeline for ingesting messaging-platform webhooks.
- Designed the webhook → RabbitMQ → AI processing/summarization → realtime update flow.
- Initialized a Java 21 backend, React frontend, and webhook test scenarios.

## Outcomes and evidence

- Completed the initial realtime ingestion, processing, and broadcast system.
- [Tardis repository](https://github.com/Little-Boy-s-Tardis/Tardis)
- [Webhook test scenarios](https://github.com/Little-Boy-s-Tardis/Tardis/commit/c66e8cd6e3696a3580bd606d5ddca9ac9c5d9c80)

## Challenge and resolution

Development depended on PostgreSQL, RabbitMQ, and an external AI service. I configured an H2 development profile, an in-memory queue, and fallback processing so the application could be tested locally.
