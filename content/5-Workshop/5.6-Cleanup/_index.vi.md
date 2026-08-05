---
title: "Tình trạng dọn dẹp và chi phí"
date: 2026-08-05
weight: 6
chapter: false
pre: "<b>5.6.</b>"
description: "Chỉ ghi nhận trạng thái dọn dẹp và chi phí khi có bằng chứng đúng môi trường."
---

# Tình trạng dọn dẹp và chi phí

## Dọn dẹp

Chưa có log `terraform destroy`, ảnh AWS Console hoặc kết quả truy vấn từ đúng tài khoản để chứng minh hạ tầng đã được dọn. Website vẫn phản hồi HTTP `200` tại thời điểm kiểm tra, nên báo cáo không ghi tài nguyên đã bị xóa.

## Chi phí thực tế

Chưa có dữ liệu Billing/Cost Explorer từ đúng tài khoản triển khai. Báo cáo không sử dụng số `$0.00` lấy từ tài khoản AWS khác và không thay bằng AWS Pricing Calculator vì đó chỉ là dự toán.

Chi phí chỉ được cập nhật khi có ít nhất một trong các bằng chứng sau:

- Ảnh Billing/Cost Explorer đúng khoảng ngày và đúng tài khoản triển khai.
- Cost and Usage Report hoặc kết quả Cost Explorer có bộ lọc/tag xác định EduFlow.
- Xác nhận rõ credit/free-tier nếu tổng tiền thanh toán bằng `0`.

## Thời gian hoàn thành

Chỉ xác minh được pipeline CI/CD gần nhất kéo dài **9 phút 07 giây**. Chưa có giờ bắt đầu/kết thúc buổi workshop nên không ghi tổng thời gian thao tác thủ công.
