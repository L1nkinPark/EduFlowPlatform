---
title: "Tổng kết và phản hồi"
date: 2026-08-05
weight: 7
chapter: false
pre: "<b>7.</b>"
---

# Tổng kết và phản hồi

## Điều đã làm tốt

EduFlow đã chuyển từ một giao diện chứa nhiều nội dung tĩnh thành nền tảng có dữ liệu thật cho quản trị viên, giảng viên và học viên. Luồng mua-học-tiến độ, i18n, upload, OTP và VNPay được kết nối với backend; hạ tầng AWS và pipeline cũng được lưu dưới dạng mã.

Điểm có giá trị nhất là khả năng truy vết: một revision Git tương ứng image ECR, ECS service có log riêng, Terraform mô tả tài nguyên và test bảo vệ các lỗi đã sửa.

## Điều còn vướng

- Kiến trúc hai Spring Boot service tạo thêm đồng bộ JWT, URL và timeout.
- Upload local trên Fargate chỉ tồn tại theo vòng đời task; cần chuyển hoàn toàn sang object storage cho production.
- CI hiện dùng access key; Terraform state local chưa phù hợp nhóm.
- Mức bao phủ unit test đã tốt hơn nhưng chưa thay thế kiểm thử trình duyệt end-to-end.
- RDS Single-AZ và task chạy public subnet là lựa chọn tiết kiệm cho dev, chưa phải kiến trúc HA production.

## Đề xuất cải tiến quy trình

1. Mỗi thay đổi bắt đầu bằng acceptance criteria và test hồi quy cho lỗi quan trọng.
2. Review API contract và threat model trước khi thay đổi quyền hoặc thanh toán.
3. Dùng môi trường staging bất biến với image SHA trước khi cập nhật production.
4. Thu thập metric thực tế trước khi tăng tài nguyên hoặc thêm dịch vụ AWS.
5. Cập nhật workshop cùng code để tài liệu luôn chạy lại được.

## Kết luận

Dự án đã đạt nền tảng MVP có thể trình diễn và triển khai lặp lại. Bước trưởng thành tiếp theo không phải thêm nhiều tính năng, mà là tăng độ tin cậy: migration, E2E, OIDC, object storage bền vững, remote state và observability.

Phản hồi hoặc issue kỹ thuật có thể gửi tại [EduFlowPlatform Issues](https://github.com/L1nkinPark/EduFlowPlatform/issues).
