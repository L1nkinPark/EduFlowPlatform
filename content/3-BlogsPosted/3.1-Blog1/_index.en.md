---
title: "Blog 1"
date: 2026-08-05
weight: 1
chapter: false
pre: "<b>3.1. </b>"
description: "An Amazon RDS overview covering Multi-AZ, backups, recovery, and four practical cost-optimization approaches."
---

# Amazon RDS — High Availability, Backups, and Database Cost Optimization on AWS

Continuing the series on foundational AWS services, this post discusses **Amazon RDS (Relational Database Service)**, focusing on three topics that are often misunderstood by beginners: **Multi-AZ**, **backup and restore**, and **cost optimization**.

{{< figure
    src="../../../images/3-BlogsPosted/blog1-amazon-rds.png?featherlight=false"
    alt="Amazon RDS Multi-AZ, backup and restore, and cost optimization infographic in Vietnamese"
    title="Amazon RDS knowledge summary"
>}}

## 1. What is Amazon RDS?

Amazon RDS is a managed relational database service supporting MySQL, PostgreSQL, MariaDB, Oracle, SQL Server, and the purpose-built Amazon Aurora engine. AWS handles installation, operating-system and database-engine patching, backups, and failover, allowing development teams to focus on schemas, queries, and applications.

## 2. Multi-AZ — do not confuse it with a Read Replica

The most important distinction is that **Multi-AZ is primarily designed for high availability (HA), not read scaling**. Amazon RDS offers two Multi-AZ deployment models:

- **Multi-AZ DB instance deployment:** one standby is placed in another Availability Zone. Data is synchronously replicated to the standby. It does not serve read traffic and is available for promotion during failover.
- **Multi-AZ DB cluster deployment:** one writer and two readers are distributed across three Availability Zones. The readers can serve read traffic, improving fault tolerance and read capacity while providing lower write latency than a Multi-AZ DB instance deployment. Automatic failover is typically completed in under 35 seconds, depending on workload and replica lag.

During hardware, network, or Availability Zone failures—and during certain maintenance operations—RDS automatically fails over to an appropriate standby or reader without manual intervention.

> If the main goal is scaling `SELECT` traffic, evaluate **Read Replicas**. Multi-AZ and Read Replicas serve different needs: **high availability** and **read scaling**.

## 3. Backup and restore — understand it before production

- Automated backups are enabled by default when a DB instance is created in the AWS Management Console. RDS creates a daily snapshot of the complete storage volume during the backup window. When no preferred window is selected, RDS assigns a default 30-minute window.
- Transaction logs are uploaded to Amazon S3 approximately every five minutes.
- The backup retention period can be configured for up to 35 days. Snapshots and transaction logs enable **Point-in-Time Recovery (PITR)** to a point within the retention period, usually within about five minutes of the current time.
- A **manual snapshot** is not automatically removed by the retention policy. It remains until explicitly deleted, making it suitable for milestones such as migrations or major releases.
- Restoring an automated backup, PITR point, or snapshot creates a new DB instance rather than overwriting the original. Disaster Recovery procedures must account for this behavior.

## 4. Four practical ways to optimize RDS costs

1. **Reserved Instances:** suitable for steady 24/7 production workloads. A one- or three-year commitment can save up to 69% compared with On-Demand pricing, depending on the engine, instance, Region, term, and payment option. An RDS Reserved Instance is a billing discount and does not change database operations.
2. **Right-size before purchasing an RI:** observe at least one representative cycle in CloudWatch, including `CPUUtilization`, `FreeableMemory`, connections, IOPS, and latency. Purchasing an RI for an over-provisioned instance combines incorrect sizing with a long-term commitment.
3. **Consider Single-AZ for Dev/Test:** environments that do not require automatic failover can often use Single-AZ to avoid standby costs. Production choices should still follow actual RTO, RPO, and availability requirements.
4. **Stop RDS outside working hours:** Lambda and EventBridge can schedule non-production databases to stop and start. An RDS DB instance can remain stopped for at most seven days before it automatically restarts; storage, backups, and some related resources continue to incur charges.

## 5. Operational notes

- RDS does not provide direct access to the underlying operating system or filesystem. This is the tradeoff between a managed service and customization.
- Automated backups only run while the DB instance is in the `available` state. A database in `storage_full` or another state might miss its backup.
- When deleting a DB instance, select **Retain automated backups** to keep automated backups for the remainder of their retention period. Manual and final snapshots are managed independently.

## References

- [Amazon RDS User Guide — Multi-AZ deployments](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Concepts.MultiAZ.html)
- [Amazon RDS User Guide — Introduction to backups](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_WorkingWithAutomatedBackups.html)
- [Amazon RDS User Guide — Managing automated backups](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_ManagingAutomatedBackups.html)
- [Amazon RDS User Guide — Stopping a DB instance temporarily](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_StopInstance.html)
- [Amazon RDS Reserved Instances](https://aws.amazon.com/rds/reserved-instances/)

## Published post

{{% button href="https://www.facebook.com/groups/awsstudygroupfcj/permalink/2235031937261766/" icon="fab fa-facebook" %}}View Blog 1 in AWS Study Group VN{{% /button %}}
