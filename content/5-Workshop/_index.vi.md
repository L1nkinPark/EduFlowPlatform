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
| API công khai | [`/api/public/stats`](http://eduflow-dev-alb-560717424.ap-southeast-1.elb.amazonaws.com/api/public/stats) trả HTTP `200` |
| Domain công khai | DNS mặc định của AWS ALB; chưa có bằng chứng về custom domain |
| CI/CD | [GitHub Actions run #74](https://github.com/L1nkinPark/EduFlowPlatform/actions/runs/30983018477) thành công |
| Thời gian pipeline | `8 phút 49 giây` theo GitHub Actions |
| Kết quả k6 | Chưa có log kết quả trong repository/Actions |
| Chi phí thực tế | Chưa xác minh được từ đúng tài khoản triển khai |
| Ảnh AWS Console | Chưa có trong repository |
| Thời gian làm workshop thủ công | Chưa được ghi nhận |

{{% notice info %}}
Các lần đo HTTP chỉ là kiểm tra khả dụng tại một thời điểm, không thay thế kết quả kiểm thử tải.
{{% /notice %}}

{{% children description="true" /%}}
