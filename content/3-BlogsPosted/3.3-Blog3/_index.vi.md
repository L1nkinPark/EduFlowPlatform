---
title: "Blog 3"
date: 2026-08-06
weight: 3
chapter: false
pre: "<b>3.3. </b>"
description: "Kiến trúc bảo mật đa lớp của EduFlow với VPC, ALB, ECS Fargate, RDS, Secrets Manager, CloudWatch và CI/CD."
---

# Kiến trúc EduFlow trên AWS: Xây dựng hệ thống bảo mật đa lớp với VPC & ECS Fargate

Khi thiết kế hạ tầng cho ứng dụng Web/Microservices trên đám mây, bên cạnh hiệu năng và chi phí, **bảo mật** là yêu cầu cần được đưa vào ngay từ kiến trúc. Với EduFlow, mô hình mục tiêu áp dụng **Defense-in-Depth (bảo mật đa lớp)** và **Least Privilege (quyền tối thiểu)** để giảm bề mặt tấn công và bảo vệ dữ liệu người dùng.

{{< figure
    src="../../images/3-BlogsPosted/blog3-facebook-post-1.png?width=560px&featherlight=false"
    alt="Phần đầu bài đăng Facebook về kiến trúc bảo mật EduFlow trên AWS"
    title="Ảnh bài đăng Blog 3 — phần 1"
>}}

## I. Lớp phòng thủ mạng — Network Isolation with VPC

Điểm đầu tiên của kiến trúc mục tiêu là tách rõ ranh giới giữa public entry point, application tier và data tier:

- **Chỉ ALB là điểm truy cập công khai:** Application Load Balancer tiếp nhận traffic Internet và chuyển tiếp request hợp lệ tới target group của frontend/backend. Security Group giới hạn các cổng được phép giữa từng lớp.
- **Frontend và Backend hướng tới Private Subnet:** `FE_EduFlow` và `BE_EduFlow` chạy bằng ECS Fargate, không cần mở trực tiếp port ứng dụng ra Internet. Với mô hình private hoàn chỉnh, task không có public IP và outbound traffic đi qua NAT Gateway hoặc VPC endpoints.
- **RDS nằm trong private data subnets:** database không có đường truy cập trực tiếp từ Internet; Security Group chỉ cho phép kết nối MySQL từ backend.

## II. Bảo mật ứng dụng và dữ liệu — Container Security & Data Protection

- **Tách biệt Frontend và Backend:** `FE_EduFlow` chạy ở port `8080`, còn `BE_EduFlow` chạy ở port `8888`. Hai service được đóng gói thành container và triển khai bằng task definition riêng, giúp kiểm soát giao tiếp liên dịch vụ và vòng đời triển khai.
- **Cơ sở dữ liệu biệt lập:** Amazon RDS MySQL lưu dữ liệu `eduflow_db` trong private data subnets và không chấp nhận kết nối trực tiếp từ bên ngoài.
- **Không hard-code secret:** database password, JWT secret, SMTP credential và VNPay credential được lưu trong AWS Secrets Manager. ECS task definition tham chiếu từng JSON key và inject giá trị khi container khởi động.
- **IAM theo quyền tối thiểu:** ECS execution role chỉ được cấp các quyền cần thiết để pull image, ghi log và đọc secret phục vụ task.

{{< figure
    src="../../images/3-BlogsPosted/blog3-facebook-post-2.png?width=560px&featherlight=false"
    alt="Phần giữa bài đăng Facebook về Secrets Manager, CloudWatch và health check"
    title="Ảnh bài đăng Blog 3 — phần 2"
>}}

## III. Giám sát tự động và tự phục hồi — Observability & Health Checks

Bảo mật còn bao gồm khả năng phát hiện và phản ứng nhanh khi có hành vi bất thường hoặc service mất ổn định:

- **CloudWatch Logs:** frontend và backend sử dụng `awslogs` log driver để gửi log container vào các CloudWatch Log Group riêng. Log tập trung hỗ trợ điều tra lỗi, audit và thiết lập cảnh báo.
- **Actuator Health Check:** target group của backend gọi `/actuator/health`; frontend được kiểm tra qua `/`. ALB ngừng định tuyến tới target không đạt ngưỡng health check, còn ECS service scheduler duy trì `desired_count` bằng cách thay thế task bị lỗi.
- **Không phụ thuộc SSH:** Fargate là nền tảng managed compute; vận hành tập trung vào image, task definition, log và metric thay vì SSH trực tiếp vào máy chủ.

## IV. Quy trình triển khai an toàn — Secure CI/CD Pipeline

Luồng triển khai của EduFlow đi theo chuỗi **GitHub → Docker Build → Amazon ECR → ECS Service Update**:

- GitHub Actions chạy backend/frontend test và `terraform validate` trước job deploy.
- CI pipeline build hai Docker image, gắn tag theo commit SHA và `latest`, sau đó push lên Amazon ECR.
- ECS service được cập nhật bằng deployment mới, không cần đăng nhập thủ công vào máy chủ.
- Quy trình nhất quán giúp truy vết phiên bản và rollback; các bước image scan hoặc policy check có thể được bổ sung vào pipeline.

{{< figure
    src="../../images/3-BlogsPosted/blog3-facebook-post-3.png?width=560px&featherlight=false"
    alt="Phần cuối bài đăng Facebook và sơ đồ kiến trúc triển khai EduFlow trên AWS"
    title="Ảnh bài đăng Blog 3 — phần 3"
>}}

## Lưu ý đối chiếu repository

{{% notice warning %}}
Sơ đồ trong bài đăng mô tả **kiến trúc mục tiêu với ECS Fargate trong private subnets**. Terraform hiện tại của repository vẫn đặt ECS tasks trong `public_subnet_ids` và bật `assign_public_ip = true`; RDS đã nằm trong private data subnets. Để implementation khớp hoàn toàn với sơ đồ, cần tạo private application subnets, tắt public IP cho task và cung cấp outbound access qua NAT Gateway hoặc VPC endpoints cho ECR, CloudWatch Logs và Secrets Manager.
{{% /notice %}}

## Kết luận

Kiến trúc EduFlow kết hợp nhiều lớp kiểm soát: ALB và Security Group ở biên mạng, container và RDS tách biệt, secret được quản lý tập trung, log/health check phục vụ phát hiện sự cố và CI/CD chuẩn hóa triển khai. Defense-in-Depth không phụ thuộc vào một dịch vụ duy nhất mà đến từ việc các lớp bổ trợ lẫn nhau.

## Nguồn tham khảo

- [Amazon ECS — Connect applications to the Internet](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/networking-outbound.html)
- [Amazon ECS — Pass Secrets Manager secrets through environment variables](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/secrets-envvar-secrets-manager.html)
- [Amazon ECS — LogConfiguration](https://docs.aws.amazon.com/AmazonECS/latest/APIReference/API_LogConfiguration.html)
- [Amazon ECS services and unhealthy task replacement](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ecs_services.html)
