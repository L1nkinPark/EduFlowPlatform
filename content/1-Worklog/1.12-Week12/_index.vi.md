---
title: "Tuần 12 (04/08 - 17/08/2026) - Hoàn thiện EduFlow và báo cáo"
date: 2026-08-05
weight: 12
chapter: false
pre: "<b>1.12.</b>"
description: "Hardening EduFlow, cải thiện CI/CD và xây dựng website báo cáo Hugo."
---

## Thời gian

**04/08/2026 - 17/08/2026**

## Công việc thực tế

- Sửa luồng tạo bài học và tải media trên EduFlow.
- Chuẩn hóa hiển thị VND và nội dung tiếng Việt.
- Xử lý quyền ghi file của container ECS và retry khi Docker build lỗi tạm thời.
- Xây dựng website báo cáo thực tập song ngữ bằng Hugo.

## Kết quả và bằng chứng

- Luồng lesson authoring/upload ổn định hơn; container có thư mục ghi phù hợp.
- CI có retry giới hạn cho lỗi Docker build tạm thời.
- Báo cáo Hugo có thông tin cá nhân, nhật ký 12 tuần và bằng chứng GitHub.
- [Sửa lesson authoring và media upload](https://github.com/L1nkinPark/EduFlowPlatform/commit/85933c0)
- [Chuẩn hóa VND và nội dung tiếng Việt](https://github.com/L1nkinPark/EduFlowPlatform/commit/aa5893d)
- [Sửa quyền khởi động frontend trên ECS](https://github.com/L1nkinPark/EduFlowPlatform/commit/affa848)
- [Retry Docker image build](https://github.com/L1nkinPark/EduFlowPlatform/commit/d19acf2)
- [Thêm website báo cáo Hugo](https://github.com/L1nkinPark/EduFlowPlatform/commit/8587482e88c9fb1cfa42c8b45a59e5a6efb07d87)

## Khó khăn và cách xử lý

Container chạy non-root gặp lỗi ghi file và Docker build đôi khi thất bại do lỗi tạm thời. Tôi tạo thư mục upload với quyền phù hợp và bổ sung retry có giới hạn trong CI.
