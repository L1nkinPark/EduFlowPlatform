# ==============================================================================
# Route 53 Module – DNS Records
# ==============================================================================

# ------------------------------------------------------------------------------
# Hosted Zone (conditional creation)
# ------------------------------------------------------------------------------

resource "aws_route53_zone" "main" {
  count = var.create_zone ? 1 : 0

  name    = var.domain_name
  comment = "EduFlow Platform DNS zone - ${var.environment}"

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-dns-zone"
  })
}

# Use existing or newly created zone
locals {
  zone_id = var.create_zone ? aws_route53_zone.main[0].zone_id : var.existing_zone_id
}

# ------------------------------------------------------------------------------
# A Record – CloudFront Distribution (main domain)
# ------------------------------------------------------------------------------

resource "aws_route53_record" "cloudfront" {
  count = local.zone_id != "" ? 1 : 0

  zone_id = local.zone_id
  name    = var.domain_name
  type    = "A"

  alias {
    name                   = var.cloudfront_domain_name
    zone_id                = var.cloudfront_hosted_zone_id
    evaluate_target_health = false
  }
}

# ------------------------------------------------------------------------------
# A Record – ALB (api subdomain, direct API access)
# ------------------------------------------------------------------------------

resource "aws_route53_record" "alb" {
  count = local.zone_id != "" ? 1 : 0

  zone_id = local.zone_id
  name    = "api.${var.domain_name}"
  type    = "A"

  alias {
    name                   = var.alb_dns_name
    zone_id                = var.alb_zone_id
    evaluate_target_health = true
  }
}

# ------------------------------------------------------------------------------
# ACM Certificate DNS Validation Records
# ------------------------------------------------------------------------------

resource "aws_route53_record" "acm_validation" {
  for_each = {
    for dvo in var.acm_domain_validation_options : dvo.domain_name => {
      name   = dvo.resource_record_name
      record = dvo.resource_record_value
      type   = dvo.resource_record_type
    }
  }

  zone_id         = local.zone_id
  name            = each.value.name
  type            = each.value.type
  ttl             = 300
  records         = [each.value.record]
  allow_overwrite = true
}
