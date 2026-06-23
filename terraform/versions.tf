terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  # Uncomment and configure for remote state storage
  # backend "s3" {
  #   bucket         = "eduflow-terraform-state"
  #   key            = "terraform.tfstate"
  #   region         = "ap-southeast-1"
  #   dynamodb_table = "eduflow-terraform-locks"
  #   encrypt        = true
  # }
}
