---
title: "Những chi tiết dễ sai khi tích hợp VNPay"
date: 2026-08-05
weight: 3
chapter: false
pre: "<b>3.3.</b>"
description: "Số tiền, encoding, callback URL và ranh giới tin cậy trong thanh toán."
---

# Những chi tiết dễ sai khi tích hợp VNPay

Một payment URL có thể nhìn hợp lệ nhưng vẫn bị VNPay từ chối nếu số tiền hoặc chuỗi ký khác chỉ một ký tự. EduFlow đã phải chuẩn hóa bốn điểm sau.

## 1. Đơn vị tiền

Giá trong hệ thống là VND, còn VNPay nhận số tiền nhân 100. Việc chuyển đổi nằm trong utility riêng thay vì rải ở controller, nhờ đó có thể test giá trị 0, số lẻ không hợp lệ và giới hạn lớn.

## 2. Canonicalization

Danh sách tham số phải được sắp xếp, encode UTF-8 và ghép giống hệt ở lúc tạo chữ ký lẫn xác minh callback. Sự khác nhau giữa dấu `+` và `%20` đủ làm chữ ký sai.

## 3. Return origin sau proxy

Container không tự biết URL công khai của ALB. `PAYMENT_RETURN_ORIGIN` phải được cung cấp từ hạ tầng; không nên tin trực tiếp header do client gửi khi tạo callback URL.

## 4. Callback là nguồn xác nhận

Không cấp quyền học chỉ vì trình duyệt quay về trang thành công. Backend phải xác minh response code, chữ ký, số tiền và trạng thái đơn hàng trước khi ghi nhận mua khóa học.

## Kết luận

Thanh toán cần được xem như giao thức bảo mật, không chỉ là chuyển hướng URL. Utility thuần, test vector và logging không chứa secret giúp việc chẩn đoán an toàn hơn.
