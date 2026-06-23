# ==============================================================================
# CloudFront Module – CDN Distribution
# ==============================================================================

# ------------------------------------------------------------------------------
# Origin Access Control for S3
# ------------------------------------------------------------------------------

resource "aws_cloudfront_origin_access_control" "s3" {
  name                              = "${var.name_prefix}-s3-oac"
  description                       = "OAC for EduFlow S3 content bucket"
  origin_access_control_origin_type = "s3"
  signing_behavior                  = "always"
  signing_protocol                  = "sigv4"
}

# ------------------------------------------------------------------------------
# Cache Policies
# ------------------------------------------------------------------------------

# Use AWS managed cache policies
data "aws_cloudfront_cache_policy" "caching_optimized" {
  name = "Managed-CachingOptimized"
}

data "aws_cloudfront_cache_policy" "caching_disabled" {
  name = "Managed-CachingDisabled"
}

data "aws_cloudfront_origin_request_policy" "all_viewer" {
  name = "Managed-AllViewer"
}

# ------------------------------------------------------------------------------
# CloudFront Distribution
# ------------------------------------------------------------------------------

resource "aws_cloudfront_distribution" "main" {
  enabled             = true
  is_ipv6_enabled     = true
  comment             = "EduFlow Platform CDN - ${var.environment}"
  default_root_object = "/"
  price_class         = var.price_class
  web_acl_id          = var.waf_web_acl_arn

  aliases = var.acm_certificate_arn != "" ? [var.domain_name] : []

  # ---- Origin 1: ALB (dynamic content – default) ----
  origin {
    domain_name = var.alb_dns_name
    origin_id   = "alb-origin"

    custom_origin_config {
      http_port              = 80
      https_port             = 443
      origin_protocol_policy = "http-only"
      origin_ssl_protocols   = ["TLSv1.2"]
    }

    custom_header {
      name  = "X-Custom-Header"
      value = var.name_prefix
    }
  }

  # ---- Origin 2: S3 (static content – images, videos, files) ----
  origin {
    domain_name              = var.s3_content_domain_name
    origin_id                = "s3-content-origin"
    origin_access_control_id = aws_cloudfront_origin_access_control.s3.id
  }

  # ---- Default Behavior: ALB (dynamic) ----
  default_cache_behavior {
    allowed_methods  = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "alb-origin"

    cache_policy_id          = data.aws_cloudfront_cache_policy.caching_disabled.id
    origin_request_policy_id = data.aws_cloudfront_origin_request_policy.all_viewer.id

    viewer_protocol_policy = "redirect-to-https"
    compress               = true
  }

  # ---- S3 Content Behavior: /content/* ----
  ordered_cache_behavior {
    path_pattern     = "/content/*"
    allowed_methods  = ["GET", "HEAD", "OPTIONS"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "s3-content-origin"

    cache_policy_id = data.aws_cloudfront_cache_policy.caching_optimized.id

    viewer_protocol_policy = "redirect-to-https"
    compress               = true

    # Cache TTL
    min_ttl     = 0
    default_ttl = 86400    # 1 day
    max_ttl     = 31536000 # 1 year
  }

  # ---- Static assets behavior ----
  ordered_cache_behavior {
    path_pattern     = "/static/*"
    allowed_methods  = ["GET", "HEAD"]
    cached_methods   = ["GET", "HEAD"]
    target_origin_id = "s3-content-origin"

    cache_policy_id = data.aws_cloudfront_cache_policy.caching_optimized.id

    viewer_protocol_policy = "redirect-to-https"
    compress               = true
  }

  # ---- SSL Certificate ----
  viewer_certificate {
    acm_certificate_arn            = var.acm_certificate_arn != "" ? var.acm_certificate_arn : null
    cloudfront_default_certificate = var.acm_certificate_arn == "" ? true : false
    ssl_support_method             = var.acm_certificate_arn != "" ? "sni-only" : null
    minimum_protocol_version       = var.acm_certificate_arn != "" ? "TLSv1.2_2021" : null
  }

  # ---- Restrictions ----
  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  # ---- Custom Error Responses ----
  custom_error_response {
    error_code            = 403
    response_code         = 200
    response_page_path    = "/"
    error_caching_min_ttl = 10
  }

  custom_error_response {
    error_code            = 404
    response_code         = 200
    response_page_path    = "/"
    error_caching_min_ttl = 10
  }

  tags = merge(var.tags, {
    Name = "${var.name_prefix}-cloudfront"
  })
}
