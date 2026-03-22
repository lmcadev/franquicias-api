variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Project name prefix for resources"
  type        = string
  default     = "franquicias-api"
}

variable "environment" {
  description = "Environment name"
  type        = string
  default     = "dev"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "public_subnet_cidr" {
  description = "CIDR block for the public subnet"
  type        = string
  default     = "10.0.1.0/24"
}

variable "private_subnet_1_cidr" {
  description = "CIDR block for the first private subnet"
  type        = string
  default     = "10.0.2.0/24"
}

variable "private_subnet_2_cidr" {
  description = "CIDR block for the second private subnet"
  type        = string
  default     = "10.0.3.0/24"
}

variable "ssh_cidr" {
  description = "CIDR allowed to access EC2 via SSH (use /32 for your IP)"
  type        = string
}

variable "key_pair_name" {
  description = "Existing AWS key pair name for EC2 SSH access"
  type        = string
  default     = "Accenture_test"
}

variable "ec2_instance_type" {
  description = "EC2 instance type (Free Tier)"
  type        = string
  default     = "t2.micro"
}

variable "db_instance_class" {
  description = "RDS instance class (Free Tier)"
  type        = string
  default     = "db.t3.micro"
}

variable "db_name" {
  description = "RDS database name"
  type        = string
  default     = "franquicias_db"
}

variable "db_username" {
  description = "RDS master username"
  type        = string
  default     = "franquicias_user"
}

variable "db_password" {
  description = "RDS master password"
  type        = string
  sensitive   = true
}
