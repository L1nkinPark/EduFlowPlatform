---
title: "Triển khai AWS đã kiểm chứng"
date: 2026-08-05
weight: 4
chapter: false
pre: "<b>5.4.</b>"
description: "URL thực tế, bằng chứng CI/CD và giới hạn truy cập AWS."
---

# Triển khai AWS đã kiểm chứng

Ứng dụng được kiểm tra qua DNS mặc định của Application Load Balancer tại `ap-southeast-1`:

- [Trang chủ EduFlow](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/)
- [API thống kê công khai](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats)

Cả hai endpoint trả HTTP `200` khi kiểm tra ngày 05/08/2026. Chưa có bằng chứng về HTTPS/custom domain và không ghi `eduflow.local` như một domain công khai.

{{% children description="true" /%}}
