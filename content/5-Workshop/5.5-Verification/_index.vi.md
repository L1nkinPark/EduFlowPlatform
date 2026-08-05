---
title: "Kết quả kiểm thử"
date: 2026-08-05
weight: 5
chapter: false
pre: "<b>5.5.</b>"
description: "Kết quả HTTP, trình duyệt, tải và CI/CD của EduFlow."
---

# Kết quả kiểm thử

## Kiểm tra HTTP ngày 05/08/2026

| Endpoint | Kết quả | Thời gian một lần đo |
|---|---|---|
| [Trang chủ](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/) | HTTP `200`, `text/html; charset=UTF-8` | khoảng `498 ms` |
| [API thống kê](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats) | HTTP `200`, `application/json` | khoảng `137 ms` |

Kết quả HTTP được ghi nhận ngày 05/08/2026.

Payload thống kê tại thời điểm kiểm tra có **5 khóa học, 2 giảng viên, 6 học
viên và 1 lượt ghi danh**. [Bản JSON đã lưu](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/public-stats-2026-08-05.json).

## Kiểm thử trình duyệt

- Trang chủ tiếng Việt hiển thị điều hướng, tìm kiếm, khóa học nổi bật và giá.
- Nút VI/EN chuyển nhãn giao diện công khai giữa tiếng Việt và tiếng Anh.
- Danh sách khóa học và trang chi tiết khóa học hiển thị thành công.
- Người chưa đăng nhập chọn **Mua ngay** được chuyển tới `/signin`.

[Ảnh trang chủ](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/eduflow-home-2026-08-05.png) ·
[Ảnh chi tiết khóa học](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/eduflow-course-detail-2026-08-05.png)

## Kiểm thử tải k6

Kịch bản chỉ đọc tăng dần tới **50 virtual users**, giữ 50 VU trong 30 giây rồi
giảm tải. Ba endpoint được gọi là `/api/courses`, `/api/categories` và
`/api/public/stats`.

| Chỉ số | Kết quả |
|---|---|
| HTTP requests | 1.758 |
| Checks | 2.930/2.930 đạt (100%) |
| Tỷ lệ lỗi HTTP | 0,00% |
| Trung bình / trung vị | 665,06 ms / 498,24 ms |
| p90 / p95 | 1,49 giây / 1,84 giây |
| Lớn nhất | 3,13 giây |

Tất cả threshold đều đạt. [Summary JSON](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/k6-summary-2026-08-05.json) và
[biên bản kiểm chứng](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/verification-2026-08-05.md) được lưu cùng mã nguồn.

## Kiểm tra CI

- Backend tests: `success`.
- Frontend tests và runtime upload permission check: `success`.
- Terraform format/init/validate: `success`.
- Build/push image và ECS deployment: `success`.

Nguồn: [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529), tổng thời gian **9 phút 07 giây**.
