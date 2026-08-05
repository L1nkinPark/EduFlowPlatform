---
title: "Provision infrastructure and ECS services"
date: 2026-08-05
weight: 3
chapter: false
pre: "<b>5.4.3.</b>"
description: "Create a full plan, apply VPC/ALB/RDS/ECS, and verify target health."
---

# Provision the complete infrastructure

## 1. Supply image references to Terraform

```powershell
$env:TF_VAR_fe_image=$taskFeImage
$env:TF_VAR_be_image=$taskBeImage
Set-Location terraform
```

## 2. Review the plan

```powershell
terraform plan -out=eduflow.tfplan
terraform show eduflow.tfplan
```

Check these items before applying:

- Region `ap-southeast-1`, environment `dev`.
- Two public subnets and two private data subnets.
- Single-AZ `db.t4g.micro` RDS with 20 GB for the lab.
- Both task definitions use the correct ECR URLs and ports 8080/8888.
- No plaintext secrets appear in plan output.

## 3. Apply

```powershell
terraform apply eduflow.tfplan
```

RDS and ECS may require 10-20 minutes. When complete:

```powershell
terraform output
$taskAlbDns=(terraform output -raw alb_dns_name)
Invoke-WebRequest "http://$taskAlbDns/" -UseBasicParsing
Invoke-RestMethod "http://$taskAlbDns/api/public/stats"
```

## 4. Verify ECS

```powershell
aws ecs describe-services `
  --cluster eduflow-dev-cluster `
  --services eduflow-dev-frontend eduflow-dev-backend `
  --region $taskAwsRegion `
  --query 'services[].{name:serviceName,running:runningCount,desired:desiredCount,status:status}'
```

Both services should show `running = desired = 1`. If they do not stabilize, inspect service events and the `/ecs/eduflow-dev-frontend` and `/ecs/eduflow-dev-backend` CloudWatch log groups.
