# ==============================================================================
# Data Sources
# ==============================================================================

data "aws_caller_identity" "current" {}
data "aws_region" "current" {}

# Latest Amazon Linux 2023 AMI
data "aws_ami" "amazon_linux_2023" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-*-x86_64"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  filter {
    name   = "root-device-type"
    values = ["ebs"]
  }
}

# ==============================================================================
# Local Values
# ==============================================================================

locals {
  account_id = data.aws_caller_identity.current.account_id
  region     = data.aws_region.current.name

  # Naming convention: {project}-{environment}-{resource}
  name_prefix = "${var.project_name}-${var.environment}"

  # Common tags applied to all resources
  common_tags = merge(
    {
      Project     = var.project_name
      Environment = var.environment
      ManagedBy   = "terraform"
      CreatedAt   = timestamp()
    },
    var.additional_tags
  )

  # S3 bucket names (must be globally unique)
  content_bucket_name  = "${var.project_name}-content-${local.account_id}-${var.environment}"
  artifact_bucket_name = "${var.project_name}-artifacts-${local.account_id}-${var.environment}"
}
