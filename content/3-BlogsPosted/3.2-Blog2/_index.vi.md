---
title: "Blog 2"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>3.2. </b>"
description: "Ba chiến lược tối ưu compute, storage và tài nguyên Dev/Test để kiểm soát chi phí AWS."
---

# Bí quyết tối ưu chi phí hạ tầng AWS lên đến 60% cho Start-up & Business

Trong quá trình triển khai và vận hành hệ thống trên AWS, quản lý và tối ưu chi phí (**Cost Optimization**) luôn là một trong những ưu tiên hàng đầu của kỹ sư Cloud và doanh nghiệp.

Bài viết này chia sẻ ba chiến lược thực tế giúp tối ưu chi phí hạ tầng AWS: **tối ưu compute**, **quản lý storage theo vòng đời dữ liệu** và **tự động tắt/mở tài nguyên Dev/Test**.

{{< figure
    src="../../images/3-BlogsPosted/blog2-aws-cost-optimization.png?featherlight=false"
    alt="Ba chiến lược tối ưu chi phí hạ tầng AWS"
    title="Ba chiến lược tối ưu chi phí hạ tầng AWS"
>}}

> Các tỷ lệ tiết kiệm là mức tối đa hoặc ước tính theo từng kịch bản. Kết quả thực tế phụ thuộc workload, Region, cấu hình, mô hình mua và thời gian sử dụng.

## 1. Tối ưu Compute với AWS Graviton & Savings Plans

Compute như EC2, ECS và EKS thường chiếm phần lớn ngân sách AWS. Hai hướng có thể kết hợp là:

- **Chuyển sang AWS Graviton:** các instance ARM-based như T4g, C6g và M6g dùng bộ xử lý do AWS phát triển. Theo AWS, Graviton có thể mang lại hiệu năng tốt hơn tới 40% với chi phí thấp hơn tới 20% so với instance x86 tương đương cho một số workload. Cần kiểm thử khả năng tương thích kiến trúc ARM, thư viện native và hiệu năng ứng dụng trước khi chuyển production.
- **Tận dụng Savings Plans hoặc Reserved Instances:** workload ổn định 24/7 có thể cam kết mức sử dụng trong một hoặc ba năm. Compute Savings Plans có thể giảm chi phí tới 66% so với On-Demand và vẫn linh hoạt giữa EC2, Fargate và Lambda theo các điều kiện của plan.

## 2. Quản lý Storage thông minh với Amazon S3 Lifecycle

Dữ liệu lưu lâu ngày nhưng không được phân loại là một nguyên nhân khiến hóa đơn S3 tăng dần. Có thể thiết lập S3 Lifecycle Rules theo chu kỳ truy cập:

- **S3 Standard:** dành cho dữ liệu mới hoặc cần đọc/ghi thường xuyên.
- **S3 Standard-IA / One Zone-IA:** dành cho dữ liệu ít truy cập nhưng vẫn cần truy xuất với độ trễ mili giây. Cần cân nhắc minimum storage duration, phí retrieval và yêu cầu khả dụng trước khi chuyển tier.
- **S3 Glacier Flexible Retrieval / Glacier Deep Archive:** dành cho backup, log hoặc dữ liệu lưu trữ dài hạn không cần truy cập tức thời. S3 Glacier Deep Archive có mức lưu trữ rất thấp, có thể từ khoảng `0.00099 USD/GB-tháng` tại một số Region; giá và phí retrieval thay đổi theo Region.

Nếu chưa biết trước chu kỳ truy cập, có thể dùng **S3 Intelligent-Tiering**. Dịch vụ theo dõi access pattern và tự động chuyển object giữa các access tier, kèm phí monitoring/automation cho các object đủ điều kiện.

## 3. Tự động tắt/mở tài nguyên Dev/Test

Môi trường Development và Staging thường chỉ hoạt động trong giờ hành chính. Để tránh trả chi phí compute xuyên đêm và cuối tuần, có thể dùng **AWS Instance Scheduler** hoặc **Lambda kết hợp EventBridge** để:

- Tự động stop EC2/RDS lúc `19:00` hằng ngày.
- Tự động start lại lúc `08:00` sáng hôm sau.

Với lịch làm việc 8 giờ/ngày, 5 ngày/tuần, cách này có thể giảm khoảng 65–70% phần chi phí compute của môi trường không phải production. Storage, backup, Elastic IP/public IPv4 và các tài nguyên liên quan vẫn có thể phát sinh phí. RDS chỉ được dừng tạm thời tối đa bảy ngày trước khi AWS tự khởi động lại.

## Lời kết

Tối ưu chi phí AWS không phải công việc làm một lần rồi kết thúc mà là một quy trình liên tục. Kết hợp **AWS Cost Explorer** để phân tích xu hướng, **AWS Budgets** để cảnh báo và CloudWatch để right-size tài nguyên giúp đội ngũ chủ động kiểm soát ngân sách.

## Nguồn tham khảo

- [AWS Graviton Fast Start](https://aws.amazon.com/ec2/graviton/fast-start/)
- [Compute Savings Plans](https://aws.amazon.com/savingsplans/compute-pricing/)
- [Amazon S3 User Guide — Lifecycle transitions](https://docs.aws.amazon.com/AmazonS3/latest/userguide/lifecycle-transition-general-considerations.html)
- [Amazon S3 User Guide — Intelligent-Tiering](https://docs.aws.amazon.com/AmazonS3/latest/userguide/using-intelligent-tiering.html)
- [Amazon S3 pricing](https://aws.amazon.com/s3/pricing/)
- [Amazon RDS User Guide — Stopping a DB instance temporarily](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_StopInstance.html)

## Bài viết đã đăng

{{% button href="https://www.facebook.com/groups/awsstudygroupfcj/permalink/2234073587357601/" icon="fab fa-facebook" %}}Xem Blog 2 trên AWS Study Group VN{{% /button %}}
