---
title: "Verified AWS deployment"
date: 2026-08-05
weight: 4
chapter: false
pre: "<b>5.4.</b>"
description: "Actual URL, CI/CD evidence, and AWS access limitations."
---

# Verified AWS deployment

The application was checked through the default Application Load Balancer DNS in `ap-southeast-1`:

- [EduFlow homepage](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/)
- [Public statistics API](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats)

Both endpoints returned HTTP `200` on 5 August 2026. There is no evidence of HTTPS or a custom domain, and `eduflow.local` is not presented as a public domain.

{{% children description="true" /%}}
