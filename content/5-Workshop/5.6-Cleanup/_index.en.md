---
title: "Clean up resources"
date: 2026-08-05
weight: 6
chapter: false
pre: "<b>5.6.</b>"
description: "Destroy lab infrastructure with Terraform and verify that billable resources are gone."
---

# Clean up resources

{{% notice danger %}}
These commands delete the lab database, container services, and data. Run them only against the confirmed AWS account, workspace, and workshop state. Never use them for staging or production.
{{% /notice %}}

## 1. Confirm the target

```powershell
aws sts get-caller-identity
Set-Location terraform
terraform workspace show
terraform state list
```

The account must be the sandbox and the workspace must be the one used for this workshop.

## 2. Empty S3 if data was uploaded

```powershell
$taskBucket=(terraform output -raw s3_bucket)
Write-Output "Bucket to empty: $taskBucket"
```

Because versioning is enabled, use **Empty** in the S3 Console for this exact bucket to remove current versions, noncurrent versions, and delete markers. Terraform cannot delete a non-empty bucket.

## 3. Create and apply a destroy plan

Keep the same `TF_VAR_*` values used during apply, then run:

```powershell
terraform plan -destroy -out=eduflow-destroy.tfplan
terraform show eduflow-destroy.tfplan
terraform apply eduflow-destroy.tfplan
```

The review should contain only `eduflow-dev` resources and the bucket ID you confirmed.

## 4. Confirm removal

```powershell
terraform state list
aws ecs describe-clusters --clusters eduflow-dev-cluster --region $taskAwsRegion
aws rds describe-db-instances --region $taskAwsRegion --query "DBInstances[?contains(DBInstanceIdentifier, 'eduflow-dev')]"
```

`terraform state list` should contain no resources. Check Billing/Cost Explorer after AWS finishes deleting RDS, ALB, and ENIs.

Finally stop and remove the local MySQL container if it is no longer needed:

```powershell
docker stop eduflow-mysql
docker rm eduflow-mysql
```
