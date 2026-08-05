---
title: "Week 2 (26 May-1 June 2026) - Cloud and data systems"
date: 2026-06-01
weight: 2
chapter: false
pre: "<b>1.2.</b>"
description: "Expand cloud templates, practise EC2, and improve HistoryMindAI data."
---

## Date

**26 May 2026 - 1 June 2026**

## Actual work

- Added Redis PV/PVC, Uptime Kuma, and documentation organized by AWS, Azure, and GCP.
- Practised Amazon EC2 and documented deployment use cases.
- Improved dynamic categories, RLS policies, and entity relationships in HistoryMindAI.

## Outcomes and evidence

- Expanded the DevOps templates with Redis storage, monitoring, and cloud documentation.
- [Redis PV/PVC](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/5f84f053370f6f277d81487e45a9003b8be7bbd8)
- [AWS, GCP, and Azure documentation structure](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/2b105d30785d290cfcff21029e9170393816efb9)
- [Amazon EC2 hands-on guide](https://github.com/L1nkinPark/devops-ci-cd-templates/commit/b7cc735bd9c1cbc41ce3667f414092a7f0be3c5e)
- [HistoryMindAI pronoun and entity relationship fix](https://github.com/HistoryMindAI/vietnam_history_dataset/commit/cb3cd76a086a5e7e5e932115da88a37113867508)
- [RLS policy documentation](https://github.com/Little-Boy-s/Little-Boy-s/commit/8cc2852b34556bca5f036a0cc1f770ba30782544)

## Challenge and resolution

Data and configuration were spread across multiple repositories. I separated documentation by cloud platform, normalized the folder structure, and corrected RLS policies and entity relationships.
