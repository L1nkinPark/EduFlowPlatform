---
title: "Xác minh và kiểm tra bảo mật"
date: 2026-08-05
weight: 5
chapter: false
pre: "<b>5.5.</b>"
description: "Kiểm tra health, ba vai trò, thanh toán, log và ranh giới mạng."
---

# Xác minh triển khai

## 1. Hạ tầng

- ALB trả trang chủ và `/api/public/stats`.
- Hai target group ở trạng thái healthy.
- ECS có một task RUNNING cho mỗi service.
- RDS không public và nằm trong private data subnets.
- S3 public access block bật; ECR scan-on-push bật.

Liệt kê target group:

```powershell
aws elbv2 describe-target-groups `
  --names eduflow-dev-fe-tg eduflow-dev-be-tg `
  --region $taskAwsRegion `
  --query 'TargetGroups[].{name:TargetGroupName,arn:TargetGroupArn}'
```

Dùng từng ARN với `aws elbv2 describe-target-health --target-group-arn <arn>`.

## 2. Chức năng

| Vai trò | Kịch bản bắt buộc |
|---|---|
| Quản trị viên | Xem dashboard, tạo giảng viên, khóa/mở tài khoản |
| Giảng viên | Tạo khóa học, chương, video/tài liệu; xem đơn hàng/học viên |
| Học viên | OTP, đăng nhập, tìm kiếm, promo, checkout, học và lưu tiến độ |

Kiểm tra thêm chuyển ngôn ngữ, định dạng VND, lỗi backend timeout và trang 404/403.

## 3. Bảo mật

- Request học viên đến API admin/instructor phải bị từ chối.
- Giảng viên không chỉnh khóa học của giảng viên khác.
- Secret không xuất hiện trong task environment plaintext, log hoặc HTML.
- Cookie session dùng `HttpOnly`, `SameSite=Lax`; URL không chứa `;jsessionid`.
- Callback VNPay sai chữ ký hoặc sai số tiền không tạo quyền sở hữu khóa học.

## 4. Quan sát

```powershell
aws logs tail /ecs/eduflow-dev-frontend --since 10m --region $taskAwsRegion
aws logs tail /ecs/eduflow-dev-backend --since 10m --region $taskAwsRegion
```

Không đưa password, JWT, VNPay hash secret hoặc OTP vào log khi chụp bằng chứng báo cáo.
