---
title: "Workshop triển khai EduFlow"
date: 2026-08-05
weight: 5
chapter: false
pre: "<b>5.</b>"
---

# Bản ghi triển khai EduFlow trên AWS

Phần này chỉ ghi dữ liệu đã kiểm tra trực tiếp hoặc có bằng chứng công khai. Nội dung không có log, ảnh hoặc quyền truy cập phù hợp được đánh dấu **chưa xác minh**, không thay bằng số liệu mẫu.

## Tóm tắt đã xác minh ngày 05/08/2026

| Hạng mục | Kết quả đã kiểm tra |
|---|---|
| Website ứng dụng | [ALB EduFlow](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/) trả HTTP `200` |
| API công khai | [`/api/public/stats`](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats) trả HTTP `200`; payload có 5 khóa học, 2 giảng viên, 6 học viên và 1 lượt ghi danh |
| Domain công khai | DNS mặc định của AWS ALB; chưa có bằng chứng về custom domain |
| CI/CD | [GitHub Actions run #76](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30985947529) thành công |
| Thời gian pipeline | `9 phút 07 giây` theo GitHub Actions |
| Kiểm thử trình duyệt | Trang chủ, danh sách/chi tiết khóa học, chuyển Việt–Anh và redirect người chưa đăng nhập sang `/signin` đã được kiểm tra |
| Kết quả k6 | 50 VU, 1.758 request, 0 lỗi, p95 `1,84 giây`; [JSON summary](https://github.com/L1nkinPark/EduFlowPlatform/blob/main/static/evidence/k6-summary-2026-08-05.json) |
| Chi phí thực tế | Chưa xác minh được từ đúng tài khoản triển khai |
| Ảnh AWS Console | Chưa có trong repository |
| Thời gian làm workshop thủ công | Chưa được ghi nhận |
| GitHub Pages của báo cáo | Workflow build thành công nhưng URL Pages trả HTTP `404`; repository chưa bật Pages công khai |

{{% notice info %}}
Các lần đo HTTP riêng lẻ chỉ là kiểm tra khả dụng tại một thời điểm. Kết quả tải
k6 được ghi riêng với profile và summary có thể kiểm tra lại.
{{% /notice %}}

{{% children description="true" /%}}
