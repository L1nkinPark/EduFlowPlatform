---
title: "Blog 1"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>3.1. </b>"
description: "Tổng quan Amazon RDS về Multi-AZ, backup, khôi phục và bốn hướng tối ưu chi phí."
---

# Amazon RDS — High Availability, Backup & tối ưu chi phí Database trên AWS

Tiếp nối chuỗi bài học về các service nền tảng trên AWS, bài viết này chia sẻ về **Amazon RDS (Relational Database Service)**, đặc biệt là ba mảng dễ bị hiểu nhầm khi mới học: **Multi-AZ**, **Backup/Restore** và **tối ưu chi phí**.

{{< figure
    src="../../images/3-BlogsPosted/blog1-amazon-rds.png?featherlight=false"
    alt="Amazon RDS: Multi-AZ, Backup and Restore, và tối ưu chi phí"
    title="Tổng hợp kiến thức Amazon RDS"
>}}

## 1. RDS là gì?

Amazon RDS là dịch vụ cơ sở dữ liệu quan hệ được quản lý, hỗ trợ MySQL, PostgreSQL, MariaDB, Oracle, SQL Server và engine riêng Amazon Aurora. AWS đảm nhiệm cài đặt, vá lỗi hệ điều hành và database engine, backup và failover; đội phát triển tập trung vào schema, query và ứng dụng.

## 2. Multi-AZ — hiểu đúng để không nhầm với Read Replica

Điểm dễ nhầm nhất là **Multi-AZ không được thiết kế chủ yếu để scale read**, mà để đảm bảo **high availability (HA)**. Amazon RDS có hai kiểu triển khai Multi-AZ:

- **Multi-AZ DB instance deployment:** có một standby ở Availability Zone khác. Dữ liệu được sao chép đồng bộ sang standby. Standby không phục vụ read traffic mà sẵn sàng được promote khi failover.
- **Multi-AZ DB cluster deployment:** gồm một writer và hai reader ở ba Availability Zone. Hai reader có thể phục vụ read traffic, giúp tăng khả năng chịu lỗi và read capacity, đồng thời có write latency thấp hơn so với Multi-AZ DB instance deployment. Thời gian failover tự động thường dưới 35 giây, tùy workload và replica lag.

Khi xảy ra sự cố phần cứng, mạng, Availability Zone hoặc trong một số hoạt động bảo trì, RDS tự động chuyển sang standby/reader phù hợp mà không cần can thiệp thủ công.

> Nếu mục tiêu chính là mở rộng khả năng đọc cho workload có nhiều truy vấn `SELECT`, hãy đánh giá **Read Replica**. Multi-AZ và Read Replica giải quyết hai nhu cầu khác nhau: **HA** và **read scaling**.

## 3. Backup & Restore — cần nắm chắc trước production

- Khi tạo DB instance bằng AWS Management Console, automated backups được bật theo mặc định. RDS tạo snapshot của toàn bộ storage volume mỗi ngày trong backup window. Nếu không tự chọn, RDS gán một backup window mặc định dài 30 phút.
- Transaction log được tải lên Amazon S3 khoảng mỗi 5 phút.
- Backup retention period có thể cấu hình tối đa 35 ngày. Nhờ snapshot và transaction log, có thể thực hiện **Point-in-Time Recovery (PITR)** tới một thời điểm trong khoảng retention, thường gần thời điểm hiện tại trong khoảng 5 phút.
- **Manual snapshot** không tự xóa theo retention; snapshot được giữ đến khi người dùng chủ động xóa. Cách này phù hợp với các mốc quan trọng như trước khi migrate hoặc phát hành phiên bản lớn.
- Restore từ automated backup, PITR hoặc snapshot sẽ tạo một DB instance mới, không ghi đè lên instance cũ. Điểm này cần được đưa vào kịch bản Disaster Recovery.

## 4. Bốn hướng tối ưu chi phí RDS

1. **Reserved Instances:** phù hợp với workload production ổn định 24/7. Cam kết một hoặc ba năm có thể tiết kiệm tới 69% so với On-Demand, tùy engine, instance, Region, kỳ hạn và phương thức thanh toán. RDS Reserved Instance là cơ chế giảm giá khi tính hóa đơn, không thay đổi cách vận hành database.
2. **Right-size trước khi mua RI:** theo dõi ít nhất một chu kỳ đại diện bằng CloudWatch, gồm `CPUUtilization`, `FreeableMemory`, kết nối, IOPS và latency. Mua RI cho một instance đang over-provisioned sẽ khóa cả kích thước chưa phù hợp lẫn cam kết dài hạn.
3. **Cân nhắc Single-AZ cho Dev/Test:** môi trường không yêu cầu failover tự động thường có thể dùng Single-AZ để tránh chi phí standby. Production vẫn cần lựa chọn theo RTO, RPO và yêu cầu sẵn sàng thực tế.
4. **Dừng RDS ngoài giờ làm việc:** với Dev/Test, có thể dùng Lambda và EventBridge để stop/start DB theo lịch. RDS chỉ cho dừng tạm thời tối đa bảy ngày rồi tự khởi động lại; trong thời gian dừng vẫn tính phí storage, backup và một số tài nguyên liên quan.

## 5. Một số lưu ý khi vận hành RDS

- RDS không cho truy cập trực tiếp vào hệ điều hành và filesystem bên dưới. Đây là đánh đổi giữa managed service và mức độ tùy biến.
- Automated backup chỉ chạy khi DB instance ở trạng thái `available`. Nếu instance đang `storage_full` hoặc ở trạng thái khác, backup có thể không diễn ra.
- Khi xóa DB instance, chọn **Retain automated backups** nếu muốn giữ automated backup trong thời gian retention còn lại. Manual snapshot và final snapshot được quản lý độc lập.

## Nguồn tham khảo

- [Amazon RDS User Guide — Multi-AZ deployments](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Concepts.MultiAZ.html)
- [Amazon RDS User Guide — Introduction to backups](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_WorkingWithAutomatedBackups.html)
- [Amazon RDS User Guide — Managing automated backups](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_ManagingAutomatedBackups.html)
- [Amazon RDS User Guide — Stopping a DB instance temporarily](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_StopInstance.html)
- [Amazon RDS Reserved Instances](https://aws.amazon.com/rds/reserved-instances/)

## Bài viết đã đăng

{{% button href="https://www.facebook.com/groups/awsstudygroupfcj/permalink/2235031937261766/" icon="fab fa-facebook" %}}Xem Blog 1 trên AWS Study Group VN{{% /button %}}
