output "ec2_public_ip" {
  description = "Public IP of the EC2 instance"
  value       = module.ec2.public_ip
}

output "ec2_public_dns" {
  description = "Public DNS of the EC2 instance"
  value       = module.ec2.public_dns
}

output "rds_endpoint" {
  description = "RDS endpoint address"
  value       = module.rds.endpoint
}

output "rds_port" {
  description = "RDS endpoint port"
  value       = module.rds.port
}

output "app_url" {
  description = "HTTP URL to reach the EC2 instance"
  value       = "http://${module.ec2.public_dns}"
}

output "ssh_command" {
  description = "SSH command to connect to EC2"
  value       = "ssh -i <path-to-key.pem> ec2-user@${module.ec2.public_dns}"
}
