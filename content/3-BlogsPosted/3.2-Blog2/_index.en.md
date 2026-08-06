---
title: "Blog 2"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>3.2. </b>"
description: "Three strategies for optimizing compute, storage, and Dev/Test resources to control AWS costs."
---

# Reducing AWS Infrastructure Costs by up to 60% for Start-ups and Businesses

When deploying and operating systems on AWS, **cost optimization** is a major priority for both Cloud engineers and businesses.

This post presents three practical strategies for AWS infrastructure cost optimization: **compute optimization**, **storage lifecycle management**, and **automated scheduling for Dev/Test resources**.

{{< figure
    src="../../../images/3-BlogsPosted/blog2-aws-cost-optimization.png?featherlight=false"
    alt="Three AWS infrastructure cost-optimization strategies infographic in Vietnamese"
    title="Three AWS infrastructure cost-optimization strategies"
>}}

> Savings percentages are maximums or estimates for specific scenarios. Actual results depend on the workload, Region, configuration, purchase model, and usage schedule.

## 1. Optimize compute with AWS Graviton and Savings Plans

Compute services such as EC2, ECS, and EKS often represent a large share of an AWS bill. Two complementary approaches are:

- **Migrate to AWS Graviton:** ARM-based instance families such as T4g, C6g, and M6g use processors designed by AWS. According to AWS, Graviton can provide up to 40% higher performance at up to 20% lower cost than comparable x86 instances for certain workloads. Test ARM compatibility, native dependencies, and application performance before moving production workloads.
- **Use Savings Plans or Reserved Instances:** steady 24/7 workloads can commit to a consistent level of usage for one or three years. Compute Savings Plans can reduce costs by up to 66% compared with On-Demand pricing while retaining flexibility across EC2, Fargate, and Lambda under the plan's conditions.

## 2. Manage storage with Amazon S3 Lifecycle

Unclassified long-lived data can make S3 costs increase over time. S3 Lifecycle Rules can align storage classes with access patterns:

- **S3 Standard:** for new or frequently read and written data.
- **S3 Standard-IA / One Zone-IA:** for infrequently accessed data that still requires millisecond retrieval. Consider minimum storage duration, retrieval fees, and availability requirements before transitioning objects.
- **S3 Glacier Flexible Retrieval / Glacier Deep Archive:** for backups, logs, and long-term archives that do not need real-time access. S3 Glacier Deep Archive storage can start at approximately `USD 0.00099/GB-month` in some Regions; pricing and retrieval charges vary by Region.

When access patterns are unknown, consider **S3 Intelligent-Tiering**. It monitors access patterns and automatically moves eligible objects between access tiers, with a monitoring and automation charge.

## 3. Automatically stop and start Dev/Test resources

Development and staging environments are often only used during business hours. **AWS Instance Scheduler** or **Lambda with EventBridge** can reduce overnight and weekend compute charges by scheduling resources to:

- Stop EC2 and RDS at `19:00` each day.
- Start them again at `08:00` the next morning.

For an eight-hour day and five-day work week, this approach can reduce the compute portion of non-production costs by approximately 65–70%. Storage, backups, Elastic IP/public IPv4 addresses, and related resources might still incur charges. An RDS DB instance can remain stopped for only seven days before AWS automatically restarts it.

## Conclusion

AWS cost optimization is a continuous process rather than a one-time task. Combine **AWS Cost Explorer** for trend analysis, **AWS Budgets** for alerts, and CloudWatch for right-sizing to maintain active control of the budget.

## References

- [AWS Graviton Fast Start](https://aws.amazon.com/ec2/graviton/fast-start/)
- [Compute Savings Plans](https://aws.amazon.com/savingsplans/compute-pricing/)
- [Amazon S3 User Guide — Lifecycle transitions](https://docs.aws.amazon.com/AmazonS3/latest/userguide/lifecycle-transition-general-considerations.html)
- [Amazon S3 User Guide — Intelligent-Tiering](https://docs.aws.amazon.com/AmazonS3/latest/userguide/using-intelligent-tiering.html)
- [Amazon S3 pricing](https://aws.amazon.com/s3/pricing/)
- [Amazon RDS User Guide — Stopping a DB instance temporarily](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_StopInstance.html)

## Published post

{{% button href="https://www.facebook.com/groups/awsstudygroupfcj/permalink/2234073587357601/" icon="fab fa-facebook" %}}View Blog 2 in AWS Study Group VN{{% /button %}}
