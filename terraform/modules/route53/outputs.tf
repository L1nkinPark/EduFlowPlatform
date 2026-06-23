output "zone_id" {
  description = "Route 53 hosted zone ID"
  value       = local.zone_id
}

output "name_servers" {
  description = "Name servers for the hosted zone"
  value       = var.create_zone ? aws_route53_zone.main[0].name_servers : []
}

output "acm_validation_record_fqdns" {
  description = "FQDNs of ACM validation records"
  value       = [for record in aws_route53_record.acm_validation : record.fqdn]
}
