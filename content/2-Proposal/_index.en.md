---
title: "Proposal"
date: 2026-08-06
weight: 2
chapter: false
pre: " <b> 2. </b> "
---

# EduFlow Online Learning Platform

<h2 class="proposal-subtitle">A Full-Stack Learning Management Solution Deployed on AWS</h2>

### 1. Executive Summary

EduFlow is an online learning platform that manages the complete course lifecycle for **students**, **instructors**, and **administrators**. The system supports course authoring, catalog discovery, VNPay checkout, lesson delivery, and learning-progress tracking. Its two Spring Boot applications are packaged as containers, deployed on Amazon ECS Fargate, and provisioned through Terraform.

### 2. Problem Statement

#### What's the Problem?

Course content, orders, learner progress, and instructor operations are often split across separate tools. Static or mock data makes user journeys inconsistent, while manual deployments introduce configuration drift and slow recovery. Role permissions must also remain consistent across the web interface and REST API.

#### The Solution

EduFlow centralizes the course lifecycle in one platform. A Thymeleaf frontend manages role-specific interfaces and browser sessions, while a Spring Boot REST backend owns business rules, data access, authentication, OTP, and payment processing. MySQL stores application data, and AWS provides routing, compute, storage, secrets, logs, and repeatable delivery.

#### Benefits and Return on Investment

- One workflow connects course authoring, purchase, learning, and progress tracking.
- Role-based dashboards reduce manual administration for instructors and administrators.
- Terraform and GitHub Actions make infrastructure and releases repeatable.
- Automated tests, health checks, and load testing reduce deployment risk.
- The bilingual Hugo report makes project results reusable for technical review and knowledge sharing.

### 3. Solution Architecture

EduFlow separates presentation and business logic so the frontend and backend can be tested, deployed, and operated independently. Public and API traffic enters through one Application Load Balancer.

#### Application Architecture

{{< mermaid >}}
graph LR
    USERS[Students, Instructors, Administrators] --> ALB[Application Load Balancer]
    ALB -->|Default route| FE[Frontend Spring Boot on ECS 8080]
    ALB -->|API route| BE[Backend Spring Boot REST on ECS 8888]
    FE -->|JWT and REST| BE
    BE --> RDS[Amazon RDS MySQL 8]
    FE --> MEDIA[Cloudinary and media uploads]
    BE --> SMTP[SMTP and OTP]
    BE --> VNPAY[VNPay Sandbox]
{{< /mermaid >}}

#### AWS Services Used

- **Amazon VPC:** network boundary and public/private subnet organization.
- **Application Load Balancer:** routes default traffic to the frontend and `/api/*` traffic to the backend.
- **Amazon ECS Fargate:** runs the frontend and backend containers.
- **Amazon ECR:** stores the two application container images.
- **Amazon RDS for MySQL:** stores users, courses, lessons, orders, and progress.
- **Amazon S3:** object storage defined in the infrastructure modules.
- **AWS Secrets Manager:** supplies database and application secrets at runtime.
- **Amazon CloudWatch Logs:** collects container logs.

#### Component Design

| Layer | Components | Responsibility |
|---|---|---|
| Web | Spring Boot, Thymeleaf, i18n | Public pages, role dashboards, forms, and browser sessions |
| API | Spring Boot REST, Security, JWT | Accounts, courses, lessons, orders, OTP, and progress rules |
| Data | MySQL 8, Spring Data JPA | Users, catalog, content, orders, and learning progress |
| Integrations | VNPay, SMTP, Cloudinary/media storage | Payments, OTP/email, and learning media |
| Infrastructure | Docker, ALB, ECS, ECR, RDS, S3 | Packaging, routing, compute, database, and storage |
| Automation | Terraform, GitHub Actions | Infrastructure validation, testing, and deployment |

#### Deployment Architecture

{{< figure
    src="../../images/eduflow-deployment-architecture.png"
    alt="EduFlow deployment architecture on AWS"
    title="EduFlow deployment architecture on AWS"
    class="architecture-diagram"
>}}

### 4. Technical Implementation

#### Implementation Phases

1. Define the domain model, REST contracts, and three user roles.
2. Implement course authoring, catalog, orders, lessons, and progress tracking.
3. Integrate JWT authentication, OTP, VNPay, media storage, and Vietnamese/English content.
4. Add backend/frontend tests, browser smoke tests, k6 load tests, and security hardening.
5. Containerize both applications, model AWS resources with Terraform, and automate deployment through GitHub Actions.

#### Technical Requirements

- **Runtime:** Java 17 and Spring Boot 3.3.4.
- **Frontend:** Spring Boot, Thymeleaf, JavaScript, and bilingual i18n resources.
- **Backend:** Spring REST, Spring Security, JWT, JPA, OTP, and VNPay integration.
- **Database:** MySQL 8 through Spring Data JPA.
- **Cloud:** AWS Region `ap-southeast-1`, ECS Fargate, ALB, ECR, RDS, S3, Secrets Manager, and CloudWatch.
- **Automation:** Maven, Docker, Terraform, GitHub Actions, browser smoke testing, and k6.

### 5. Timeline & Milestones

- **19 May–8 June 2026:** DevOps, Kubernetes, Amazon EC2, and IAM foundations.
- **9–22 June 2026:** Full-stack, CI/CD, and real-time architecture experience through KET-Vault and Tardis.
- **23 June–13 July 2026:** EduFlow cloud foundation, Terraform modules, container fixes, and Aegis security work.
- **14 July–3 August 2026:** EduFlow testing, VNPay/i18n improvements, k6, role dashboards, data integration, and production hardening.
- **4–5 August 2026:** AWS deployment verification, browser testing, load testing, and Hugo report delivery.

### 6. Budget Estimation

The development environment is sized for an internship-scale workload and cost control:

- One frontend task and one backend task, each configured with **256 CPU units and 512 MiB memory**.
- Amazon RDS MySQL uses **`db.t4g.micro`**, **20 GB** initial storage, and Single-AZ mode.
- Application images share the automated ECR build-and-deploy pipeline.
- Logs, secrets, and object storage are separated into managed AWS services.
- Infrastructure variables allow task counts, database size, and availability settings to scale with demand.

### 7. Risk Assessment

| Risk | Impact | Mitigation implemented in the solution |
|---|---|---|
| JWT configuration mismatch | Authentication failure between services | Supply a shared runtime secret and test authentication paths |
| Incorrect VNPay origin or signature | Orders cannot be confirmed | Normalize amount/encoding, configure the return origin, and validate callbacks |
| Container upload permission | Instructors cannot save lesson media | Create a writable upload directory and verify permissions in CI |
| Backend startup or network delay | Frontend requests time out | Configure connection/read timeouts and ALB health checks |
| AWS resource growth | Higher operating cost | Use small development sizing and adjustable desired counts |

### 8. Expected Outcomes

#### Technical Outcomes

- Three role-specific experiences backed by real application data.
- A complete authoring, purchase, lesson, and progress-management workflow.
- Two independently tested Spring Boot services deployed behind one ALB.
- Repeatable infrastructure and deployment through Terraform and GitHub Actions.
- Measurable browser, HTTP, CI/CD, and k6 results documented in the Workshop.

#### Project Value

EduFlow provides a reusable foundation for extending course commerce, learning analytics, content delivery, and cloud operations. The project also demonstrates practical application of software engineering, security, DevOps, and AWS skills during the internship.
