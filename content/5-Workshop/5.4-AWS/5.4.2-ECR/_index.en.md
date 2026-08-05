---
title: "Bootstrap ECR and push images"
date: 2026-08-05
weight: 2
chapter: false
pre: "<b>5.4.2.</b>"
description: "Create the two repositories first, authenticate to ECR, and push EduFlow containers."
---

# Bootstrap ECR and push images

Terraform defines ECR and ECS in one module. Create only the two ECR resources once, push images, then return to a full plan.

## 1. Bootstrap repositories

From the `terraform` directory:

```powershell
terraform apply `
  -target=module.ecs.aws_ecr_repository.frontend `
  -target=module.ecs.aws_ecr_repository.backend
```

Read the plan and enter `yes` only when it creates the two ECR repositories and required data dependencies.

{{% notice warning %}}
Use `-target` only for this intentional bootstrap. Run a full plan immediately after images exist so Terraform reconciles the complete graph.
{{% /notice %}}

## 2. Authenticate to ECR

```powershell
$taskRegistry="$taskAccountId.dkr.ecr.$taskAwsRegion.amazonaws.com"
$taskFeImage="$taskRegistry/eduflow-dev-frontend:latest"
$taskBeImage="$taskRegistry/eduflow-dev-backend:latest"
aws ecr get-login-password --region $taskAwsRegion |
  docker login --username AWS --password-stdin $taskRegistry
```

## 3. Build and push

From the repository root:

```powershell
docker build -t $taskFeImage .\FE_EduFlow
docker build -t $taskBeImage .\BE_EduFlow
docker push $taskFeImage
docker push $taskBeImage
```

Verify:

```powershell
aws ecr describe-images --repository-name eduflow-dev-frontend --region $taskAwsRegion
aws ecr describe-images --repository-name eduflow-dev-backend --region $taskAwsRegion
```
