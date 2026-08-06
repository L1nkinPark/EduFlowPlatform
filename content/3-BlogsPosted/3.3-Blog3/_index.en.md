---
title: "Blog 3"
date: 2026-08-06
weight: 3
chapter: false
pre: "<b>3.3. </b>"
description: "EduFlow's layered security architecture with VPC, ALB, ECS Fargate, RDS, Secrets Manager, CloudWatch, and CI/CD."
---

# EduFlow on AWS: Building Defense in Depth with VPC and ECS Fargate

When designing cloud infrastructure for Web or microservice applications, security must be part of the architecture alongside performance and cost. EduFlow's target model applies **Defense in Depth** and **Least Privilege** to reduce its attack surface and protect user data.

{{< figure
    src="../../../images/3-BlogsPosted/blog3-facebook-post-1.png?width=560px&featherlight=false"
    alt="First screenshot of the Vietnamese Facebook post about EduFlow security architecture on AWS"
    title="Blog 3 Facebook post — part 1"
>}}

## I. Network defense — isolation with VPC

The target architecture separates the public entry point, application tier, and data tier:

- **The ALB is the only public entry point:** the Application Load Balancer receives Internet traffic and forwards valid requests to the frontend and backend target groups. Security Groups restrict allowed ports between layers.
- **Frontend and backend target private subnets:** `FE_EduFlow` and `BE_EduFlow` run as ECS Fargate services without exposing application ports directly to the Internet. In a fully private model, tasks have no public IP and outbound traffic uses NAT Gateways or VPC endpoints.
- **RDS remains in private data subnets:** the database has no direct Internet route, while its Security Group permits MySQL connections only from the backend.

## II. Application and data protection — container security

- **Separate frontend and backend services:** `FE_EduFlow` runs on port `8080`, while `BE_EduFlow` runs on port `8888`. Separate container images and task definitions isolate deployments and make inter-service communication explicit.
- **Isolated database:** Amazon RDS for MySQL stores `eduflow_db` in private data subnets and rejects direct external connections.
- **No hard-coded secrets:** the database password, JWT secret, SMTP credentials, and VNPay credentials are stored in AWS Secrets Manager. The ECS task definition references individual JSON keys and injects their values when the container starts.
- **Least-privilege IAM:** the ECS execution role receives only the permissions required to pull images, write logs, and read task secrets.

{{< figure
    src="../../../images/3-BlogsPosted/blog3-facebook-post-2.png?width=560px&featherlight=false"
    alt="Second screenshot covering Secrets Manager, CloudWatch, and health checks"
    title="Blog 3 Facebook post — part 2"
>}}

## III. Automated monitoring and recovery — observability and health checks

Security also requires fast detection and response when behavior becomes abnormal or a service becomes unstable:

- **CloudWatch Logs:** the frontend and backend use the `awslogs` driver to send container output to separate CloudWatch Log Groups. Centralized logs support troubleshooting, auditing, and alerts.
- **Actuator health checks:** the backend target group calls `/actuator/health`, while the frontend is checked at `/`. The ALB stops routing to targets that fail the configured health threshold, and the ECS service scheduler maintains `desired_count` by replacing failed tasks.
- **No SSH dependency:** Fargate is managed compute, so operations focus on images, task definitions, logs, and metrics instead of direct server access.

## IV. Secure delivery — CI/CD pipeline

EduFlow follows the deployment flow **GitHub → Docker Build → Amazon ECR → ECS Service Update**:

- GitHub Actions runs backend and frontend tests plus `terraform validate` before deployment.
- The pipeline builds both Docker images, tags them with the commit SHA and `latest`, and pushes them to Amazon ECR.
- ECS services receive a new deployment without an operator logging in to a server.
- A consistent pipeline improves traceability and rollback; image scanning and policy checks can be added as further controls.

{{< figure
    src="../../../images/3-BlogsPosted/blog3-facebook-post-3.png?width=560px&featherlight=false"
    alt="Final screenshot of the Vietnamese Facebook post and EduFlow AWS deployment diagram"
    title="Blog 3 Facebook post — part 3"
>}}

## Repository alignment note

{{% notice warning %}}
The post diagram describes the **target architecture with ECS Fargate in private subnets**. The repository's current Terraform still places ECS tasks in `public_subnet_ids` with `assign_public_ip = true`; RDS already uses private data subnets. To fully match the diagram, the implementation needs private application subnets, disabled task public IPs, and outbound connectivity through NAT Gateways or VPC endpoints for ECR, CloudWatch Logs, and Secrets Manager.
{{% /notice %}}

## Conclusion

EduFlow combines multiple controls: ALB and Security Groups at the network edge, isolated containers and RDS, centralized secret management, logs and health checks for incident detection, and CI/CD for repeatable deployment. Defense in Depth does not depend on one service; it emerges when the layers reinforce each other.

## References

- [Amazon ECS — Connect applications to the Internet](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/networking-outbound.html)
- [Amazon ECS — Pass Secrets Manager secrets through environment variables](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/secrets-envvar-secrets-manager.html)
- [Amazon ECS — LogConfiguration](https://docs.aws.amazon.com/AmazonECS/latest/APIReference/API_LogConfiguration.html)
- [Amazon ECS services and unhealthy task replacement](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ecs_services.html)
