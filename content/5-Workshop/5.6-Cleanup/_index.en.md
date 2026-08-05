---
title: "Cleanup and cost status"
date: 2026-08-05
weight: 6
chapter: false
pre: "<b>5.6.</b>"
description: "Only record cleanup and cost when evidence belongs to the correct environment."
---

# Cleanup and cost status

## Cleanup

There is no stored `terraform destroy` log, AWS Console screenshot, or query result from the correct account proving that the infrastructure was removed. The website still returned HTTP `200` when checked, so this report does not claim that the resources were deleted.

## Actual cost

Billing/Cost Explorer data from the deployment account is not available. The report does not use a `$0.00` result obtained from a different AWS account and does not replace actual cost with an AWS Pricing Calculator estimate.

Cost will only be updated when at least one of these is available:

- A Billing/Cost Explorer screenshot for the correct date range and deployment account.
- A Cost and Usage Report or Cost Explorer result filtered/tagged for EduFlow.
- Clear confirmation of credits/free-tier when the billed total is `0`.

## Completion time

Only the **8 minutes 49 seconds** CI/CD pipeline duration is verified. No workshop start/end time was recorded, so total manual time is not reported.
