---
title: "Kết quả kiểm thử frontend"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.3.2.</b>"
description: "Kết quả frontend test và kiểm tra quyền upload từ GitHub Actions."
---

# Kết quả kiểm thử frontend

Trong [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529), job **Frontend tests** có kết quả `success`.

Các bước được GitHub ghi nhận thành công:

- Checkout mã nguồn và thiết lập JDK 17.
- Chạy frontend tests.
- Kiểm tra quyền ghi thư mục upload của frontend runtime.

Job chạy từ **07:40:45 đến 07:41:57 UTC ngày 05/08/2026**.

Ngoài CI, smoke test bằng trình duyệt đã xác minh trang chủ, danh sách/chi tiết
khóa học, chuyển Việt–Anh và redirect người chưa đăng nhập từ **Mua ngay** về
`/signin`. [Ảnh trang chủ](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/eduflow-home-2026-08-05.png) và
[ảnh chi tiết khóa học](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/eduflow-course-detail-2026-08-05.png)
ghi lại kết quả hiển thị trên trình duyệt.
