---
title: "Week 4 (9-15 June 2026) - KET-Vault, DoraHacks"
date: 2026-06-15
weight: 4
chapter: false
pre: "<b>1.4.</b>"
description: "Develop KET-Vault for a DoraHacks competition."
---

## Date and context

**9 June 2026 - 15 June 2026** — **KET-Vault**, a **DoraHacks** hackathon project.

## Actual work

- Developed a multi-agent AI system for DeFi treasury governance.
- Integrated the frontend, FastAPI, smart contracts, Docker, and GitHub Actions.
- Deployed to Railway/Vercel and completed on-chain proposal, voting, and balance flows.
- Added a Risk Auditor and synthesis of multiple AI-agent results.

## Outcomes and evidence

- Delivered a hackathon-ready full-stack version with an on-chain decision workflow.
- [KET-Vault repository](https://github.com/Littile-Boy-s-KET/KET-Vault)
- [Docker Compose and GitHub Actions](https://github.com/Littile-Boy-s-KET/KET-Vault/commit/086eb767025fa9641e9b968162436a7e802fd175)
- [On-chain proposal revert fix](https://github.com/Littile-Boy-s-KET/KET-Vault/commit/f3a83567f20122f01e3c0f3f1a567f09b8fd5770)
- [Railway, Vercel, and CORS configuration](https://github.com/Littile-Boy-s-KET/KET-Vault/commit/cc7c9e5bdc0bb47753925684db33f8bb5ec1134b)
- [Risk Auditor synthesis](https://github.com/Littile-Boy-s-KET/KET-Vault/commit/b4987d6e8d6dc7016f4aeb04d39b3d538940557d)

## Challenge and resolution

The smart contract reverted, an AI model was decommissioned, and Railway had CORS/port issues. I inspected the transactions, selected a fallback model, and moved CORS and `PORT` settings into environment variables.

## Mentor feedback

Not available yet; mentor or organizer feedback will be added later.
