variable "project_name" {
  type = string
}

variable "environment" {
  type = string
}

variable "subnet_id" {
  type = string
}

variable "ec2_sg_id" {
  type = string
}

variable "key_pair_name" {
  type = string
}

variable "instance_type" {
  type    = string
  default = "t2.micro"
}
