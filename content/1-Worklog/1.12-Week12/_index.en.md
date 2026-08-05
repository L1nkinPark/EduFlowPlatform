---
title: "Week 12 (4-17 August 2026) - EduFlow completion and report"
date: 2026-08-05
weight: 12
chapter: false
pre: "<b>1.12.</b>"
description: "Harden EduFlow, improve CI/CD, and build the Hugo internship report."
---

## Date

**4 August 2026 - 17 August 2026**. The content below reflects evidence available through **5 August 2026**.

## Actual work

- Fixed EduFlow lesson authoring and media uploads.
- Standardized VND display and Vietnamese content.
- Fixed ECS container write permissions and retried transient Docker build failures.
- Built the bilingual Hugo internship report website.

## Outcomes and evidence

- Lesson authoring/uploads are more stable and the container has a writable upload directory.
- CI uses bounded retries for transient Docker build failures.
- The Hugo report includes personal information, the 12-week worklog, and GitHub evidence.
- [Lesson authoring and media upload fix](https://github.com/L1nkinPark/EduFlowPlatform/commit/85933c0)
- [VND and Vietnamese content normalization](https://github.com/L1nkinPark/EduFlowPlatform/commit/aa5893d)
- [ECS frontend startup permission fix](https://github.com/L1nkinPark/EduFlowPlatform/commit/affa848)
- [Docker image build retry](https://github.com/L1nkinPark/EduFlowPlatform/commit/d19acf2)
- [Hugo report website](https://github.com/L1nkinPark/EduFlowPlatform/commit/8587482e88c9fb1cfa42c8b45a59e5a6efb07d87)

## Challenge and resolution

The non-root container could not write files and Docker builds sometimes failed transiently. I created a writable upload directory and added bounded CI retries.

## Pending update

The period from **6 August to 17 August 2026** had not occurred when this entry was prepared. It will only be updated with actual work and evidence.

## Mentor feedback

Not available yet; it will be added after mentor feedback is received.
