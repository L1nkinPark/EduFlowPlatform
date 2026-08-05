---
title: "EduFlow deployment on AWS"
date: 2026-08-05
weight: 4
chapter: false
pre: "<b>5.4.</b>"
description: "Application URLs and AWS deployment results."
---

# EduFlow deployment on AWS

The application was checked through the default Application Load Balancer DNS in `ap-southeast-1`:

- [EduFlow homepage](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/)
- [Public statistics API](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats)

Both endpoints returned HTTP `200` on 5 August 2026. The application uses the DNS provided by AWS Application Load Balancer.

{{% children description="true" /%}}
