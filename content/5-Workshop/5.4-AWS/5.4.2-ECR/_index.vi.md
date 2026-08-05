---
title: "Bằng chứng build và push ECR"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.4.2.</b>"
description: "Kết quả build/push image có thể kiểm tra từ GitHub Actions."
---

# Bằng chứng build và push ECR

Trong [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529), job **Build, push, and deploy** ghi nhận `success` cho các bước:

- Configure AWS credentials.
- Log in to Amazon ECR.
- Build and push frontend image.
- Build and push backend image.
- Deploy ECS services.
- Wait for ECS services to stabilize.

Repository không lưu ảnh chụp ECR Console hoặc danh sách image digest/tag của lần chạy này. Vì vậy báo cáo không ghi số lượng image, digest hoặc thời gian push ngoài trạng thái thành công của workflow.
