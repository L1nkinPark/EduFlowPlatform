---
title: "Workshop triển khai EduFlow"
date: 2026-08-05
weight: 5
chapter: false
pre: "<b>5.</b>"
---

# Workshop triển khai EduFlow trên AWS

Phần này trình bày quy trình triển khai EduFlow lên AWS, kết quả CI/CD, kiểm thử giao diện và kiểm thử tải thực hiện ngày 05/08/2026.

## Kết quả triển khai ngày 05/08/2026

| Hạng mục | Kết quả |
|---|---|
| Website ứng dụng | [ALB EduFlow](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/) trả HTTP `200` |
| API công khai | [`/api/public/stats`](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats) trả HTTP `200`; payload có 5 khóa học, 2 giảng viên, 6 học viên và 1 lượt ghi danh |
| Domain triển khai | `eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com` — DNS của AWS Application Load Balancer |
| CI/CD | [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529) thành công |
| Thời gian pipeline | `9 phút 07 giây` theo GitHub Actions |
| Kiểm thử trình duyệt | Trang chủ, danh sách/chi tiết khóa học, chuyển Việt–Anh và redirect người chưa đăng nhập sang `/signin` đã được kiểm tra |
| Kết quả k6 | 50 VU, 1.758 request, 0 lỗi, p95 `1,84 giây`; [JSON summary](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/k6-summary-2026-08-05.json) |
| Báo cáo trực tuyến | [GitHub Pages](https://l1nkinpark.github.io/EduFlowPlatform/) hoạt động và được triển khai tự động từ mã nguồn Hugo |

{{% notice info %}}
Kết quả HTTP và tải k6 được ghi nhận ngày 05/08/2026. Profile kiểm thử và JSON summary được lưu cùng mã nguồn.
{{% /notice %}}

{{% children description="true" /%}}
