---
title: "Đề xuất"
date: 2026-08-06
weight: 2
chapter: false
pre: "<b>2.</b>"
---

# Nền tảng học trực tuyến EduFlow

<h2 class="proposal-subtitle">Giải pháp quản lý học tập Full-Stack triển khai trên AWS</h2>

### 1. Tóm tắt

EduFlow là nền tảng học trực tuyến quản lý toàn bộ vòng đời khóa học cho ba nhóm người dùng: **học viên**, **giảng viên** và **quản trị viên**. Hệ thống hỗ trợ biên soạn khóa học, tìm kiếm nội dung, thanh toán qua VNPay, học bài và theo dõi tiến độ. Hai ứng dụng Spring Boot được đóng gói thành container, triển khai trên Amazon ECS Fargate và quản lý hạ tầng bằng Terraform.

### 2. Vấn đề

#### Vấn đề cần giải quyết

Nội dung khóa học, đơn hàng, tiến độ học tập và hoạt động của giảng viên thường nằm ở nhiều công cụ tách rời. Dữ liệu tĩnh hoặc dữ liệu mẫu làm trải nghiệm thiếu nhất quán, còn triển khai thủ công dễ tạo sai khác cấu hình và kéo dài thời gian khôi phục. Quyền của từng vai trò cũng phải được kiểm soát thống nhất ở cả giao diện và REST API.

#### Giải pháp

EduFlow tập trung toàn bộ vòng đời khóa học trên một nền tảng. Frontend Thymeleaf quản lý giao diện theo vai trò và phiên trình duyệt; backend Spring Boot REST xử lý quy tắc nghiệp vụ, dữ liệu, xác thực, OTP và thanh toán. MySQL lưu dữ liệu ứng dụng, trong khi AWS cung cấp định tuyến, compute, lưu trữ, secret, log và quy trình triển khai lặp lại.

#### Lợi ích và giá trị đầu tư

- Một quy trình thống nhất kết nối biên soạn khóa học, mua khóa học, học bài và theo dõi tiến độ.
- Dashboard theo vai trò giảm thao tác quản trị thủ công cho giảng viên và quản trị viên.
- Terraform và GitHub Actions giúp tái tạo hạ tầng và phát hành phiên bản nhất quán.
- Kiểm thử tự động, health check và kiểm thử tải giúp giảm rủi ro triển khai.
- Báo cáo Hugo song ngữ giúp tái sử dụng kết quả cho đánh giá kỹ thuật và chia sẻ kiến thức.

### 3. Kiến trúc giải pháp

EduFlow tách lớp trình bày và nghiệp vụ để frontend và backend có thể được kiểm thử, triển khai và vận hành độc lập. Lưu lượng công khai và API đi qua cùng một Application Load Balancer.

#### Kiến trúc ứng dụng

{{< mermaid >}}
graph LR
    USERS[Học viên, Giảng viên, Quản trị viên] --> ALB[Application Load Balancer]
    ALB -->|Route mặc định| FE[Frontend Spring Boot trên ECS 8080]
    ALB -->|Route API| BE[Backend Spring Boot REST trên ECS 8888]
    FE -->|JWT và REST| BE
    BE --> RDS[Amazon RDS MySQL 8]
    FE --> MEDIA[Cloudinary và media upload]
    BE --> SMTP[SMTP và OTP]
    BE --> VNPAY[VNPay Sandbox]
{{< /mermaid >}}

#### Dịch vụ AWS sử dụng

- **Amazon VPC:** tạo ranh giới mạng và tổ chức public/private subnet.
- **Application Load Balancer:** định tuyến mặc định tới frontend và `/api/*` tới backend.
- **Amazon ECS Fargate:** vận hành container frontend và backend.
- **Amazon ECR:** lưu hai container image của ứng dụng.
- **Amazon RDS for MySQL:** lưu người dùng, khóa học, bài học, đơn hàng và tiến độ.
- **Amazon S3:** cung cấp object storage trong cấu hình hạ tầng.
- **AWS Secrets Manager:** cấp database secret và application secret khi runtime.
- **Amazon CloudWatch Logs:** thu thập log của container.

#### Thiết kế thành phần

| Lớp | Thành phần | Trách nhiệm |
|---|---|---|
| Web | Spring Boot, Thymeleaf, i18n | Trang công khai, dashboard theo vai trò, biểu mẫu và phiên trình duyệt |
| API | Spring Boot REST, Security, JWT | Tài khoản, khóa học, bài học, đơn hàng, OTP và tiến độ |
| Dữ liệu | MySQL 8, Spring Data JPA | Người dùng, danh mục, nội dung, đơn hàng và tiến độ học tập |
| Tích hợp | VNPay, SMTP, Cloudinary/media storage | Thanh toán, OTP/email và nội dung học tập |
| Hạ tầng | Docker, ALB, ECS, ECR, RDS, S3 | Đóng gói, định tuyến, compute, cơ sở dữ liệu và lưu trữ |
| Tự động hóa | Terraform, GitHub Actions | Kiểm tra hạ tầng, kiểm thử và triển khai |

#### Kiến trúc triển khai

{{< figure
    src="../images/eduflow-deployment-architecture.png"
    alt="Kiến trúc triển khai EduFlow trên AWS"
    title="Kiến trúc triển khai EduFlow trên AWS"
    class="architecture-diagram"
>}}

### 4. Triển khai kỹ thuật

#### Các giai đoạn thực hiện

1. Xác định mô hình dữ liệu, REST contract và ba vai trò người dùng.
2. Xây dựng chức năng biên soạn khóa học, danh mục, đơn hàng, bài học và tiến độ.
3. Tích hợp JWT, OTP, VNPay, media storage và nội dung tiếng Việt/Anh.
4. Bổ sung backend/frontend test, browser smoke test, k6 và hardening bảo mật.
5. Đóng gói hai ứng dụng, mô hình hóa AWS bằng Terraform và tự động triển khai qua GitHub Actions.

#### Yêu cầu kỹ thuật

- **Runtime:** Java 17 và Spring Boot 3.3.4.
- **Frontend:** Spring Boot, Thymeleaf, JavaScript và tài nguyên i18n song ngữ.
- **Backend:** Spring REST, Spring Security, JWT, JPA, OTP và tích hợp VNPay.
- **Cơ sở dữ liệu:** MySQL 8 thông qua Spring Data JPA.
- **Cloud:** AWS Region `ap-southeast-1`, ECS Fargate, ALB, ECR, RDS, S3, Secrets Manager và CloudWatch.
- **Tự động hóa:** Maven, Docker, Terraform, GitHub Actions, browser smoke test và k6.

### 5. Tiến độ và mốc thực hiện

- **19/05–08/06/2026:** xây dựng nền tảng DevOps, Kubernetes, Amazon EC2 và IAM.
- **09/06–22/06/2026:** thực hành full-stack, CI/CD và kiến trúc thời gian thực qua KET-Vault và Tardis.
- **23/06–13/07/2026:** xây dựng nền tảng cloud của EduFlow, Terraform modules, sửa container và phát triển bảo mật Aegis.
- **14/07–03/08/2026:** kiểm thử EduFlow, cải thiện VNPay/i18n, k6, dashboard theo vai trò, tích hợp dữ liệu và production hardening.
- **04/08–05/08/2026:** kiểm tra triển khai AWS, kiểm thử trình duyệt, kiểm thử tải và hoàn thiện báo cáo Hugo.

### 6. Dự toán ngân sách

Môi trường phát triển được cấu hình theo quy mô kỳ thực tập và ưu tiên kiểm soát chi phí:

- Một frontend task và một backend task, mỗi task sử dụng **256 CPU units và 512 MiB memory**.
- Amazon RDS MySQL sử dụng **`db.t4g.micro`**, **20 GB** dung lượng ban đầu và chế độ Single-AZ.
- Container image của hai ứng dụng sử dụng chung pipeline build và deploy lên ECR.
- Log, secret và object storage được tách thành các dịch vụ AWS được quản lý.
- Các biến Terraform cho phép thay đổi task count, database size và mức độ sẵn sàng theo nhu cầu.

### 7. Đánh giá rủi ro

| Rủi ro | Ảnh hưởng | Biện pháp xử lý trong giải pháp |
|---|---|---|
| Sai cấu hình JWT | Hai dịch vụ không xác thực được người dùng | Cấp cùng runtime secret và kiểm thử luồng xác thực |
| Sai origin hoặc chữ ký VNPay | Đơn hàng không được xác nhận | Chuẩn hóa amount/encoding, cấu hình return origin và xác minh callback |
| Container không có quyền ghi upload | Giảng viên không lưu được media bài học | Tạo thư mục ghi phù hợp và kiểm tra quyền trong CI |
| Backend khởi động chậm hoặc lỗi mạng | Frontend bị timeout | Cấu hình connection/read timeout và ALB health check |
| Tài nguyên AWS tăng theo tải | Chi phí vận hành tăng | Dùng cấu hình development nhỏ và desired count có thể điều chỉnh |

### 8. Kết quả kỳ vọng

#### Kết quả kỹ thuật

- Ba trải nghiệm theo vai trò sử dụng dữ liệu ứng dụng thực tế.
- Quy trình hoàn chỉnh từ biên soạn, mua khóa học, học bài đến theo dõi tiến độ.
- Hai dịch vụ Spring Boot được kiểm thử độc lập và triển khai sau cùng một ALB.
- Hạ tầng và quy trình triển khai có thể tái tạo bằng Terraform và GitHub Actions.
- Kết quả browser, HTTP, CI/CD và k6 được trình bày tại phần Workshop.

#### Giá trị dự án

EduFlow tạo nền tảng có thể mở rộng cho thương mại khóa học, phân tích học tập, phân phối nội dung và vận hành cloud. Dự án đồng thời thể hiện việc vận dụng kiến thức software engineering, bảo mật, DevOps và AWS trong kỳ thực tập.
