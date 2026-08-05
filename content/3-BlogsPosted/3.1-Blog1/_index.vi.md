---
title: "Tách Spring Boot frontend và backend trong EduFlow"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>3.1.</b>"
description: "Bài học về hợp đồng API, JWT dùng chung và lỗi giữa hai dịch vụ."
---

# Tách Spring Boot frontend và backend trong EduFlow

EduFlow dùng hai ứng dụng Java: frontend render Thymeleaf trên cổng 8080 và backend REST API trên cổng 8888. Cách tách này giúp hai lớp có vòng đời độc lập, nhưng cũng tạo thêm ranh giới mạng, xác thực và xử lý lỗi.

## Vì sao tách?

- Frontend tập trung vào HTML, form, session, i18n và trải nghiệm theo vai trò.
- Backend sở hữu quy tắc nghiệp vụ, dữ liệu JPA, phân quyền và tích hợp thanh toán/email.
- Có thể scale hoặc triển khai lại từng dịch vụ mà không đóng gói lại toàn hệ thống.

## Hợp đồng giữa hai bên

Frontend không truy cập database. Mọi dữ liệu đi qua request/response DTO và API `/api/*`. JWT được phát hành bởi backend nhưng frontend cần cùng signing secret để đọc thông tin xác thực; secret vì vậy phải được cấp ở runtime cho cả hai container.

```text
Browser -> Frontend session -> Authorization: Bearer <JWT> -> Backend API
```

## Ba lỗi điển hình

1. **Backend URL hard-code:** hoạt động local nhưng thất bại sau ALB. Giải pháp là `BACKEND_URL` và timeout cấu hình được.
2. **JWT secret lệch nhau:** đăng nhập thành công nhưng request sau bị 401. Giải pháp là một nguồn secret trong Secrets Manager.
3. **Lỗi backend biến thành 500 chung:** frontend cần phân biệt timeout, lỗi xác thực và lỗi nghiệp vụ để hiển thị thông báo phù hợp.

## Kết luận

Tách dịch vụ chỉ tạo giá trị khi hợp đồng API, cấu hình và quan sát lỗi được thiết kế như một phần của sản phẩm. Nếu không, độ phức tạp mạng sẽ lớn hơn lợi ích triển khai.
