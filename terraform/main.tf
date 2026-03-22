data "aws_availability_zones" "available" {
  state = "available"
}

module "network" {
  source = "./modules/network"

  project_name          = var.project_name
  environment           = var.environment
  vpc_cidr              = var.vpc_cidr
  public_subnet_cidr    = var.public_subnet_cidr
  private_subnet_1_cidr = var.private_subnet_1_cidr
  private_subnet_2_cidr = var.private_subnet_2_cidr
  az_1                  = data.aws_availability_zones.available.names[0]
  az_2                  = data.aws_availability_zones.available.names[1]
}

module "security_group" {
  source = "./modules/security_group"

  project_name = var.project_name
  environment  = var.environment
  vpc_id       = module.network.vpc_id
  ssh_cidr     = var.ssh_cidr
}

module "ec2" {
  source = "./modules/ec2"

  project_name  = var.project_name
  environment   = var.environment
  subnet_id     = module.network.public_subnet_id
  ec2_sg_id     = module.security_group.ec2_sg_id
  key_pair_name = var.key_pair_name
  instance_type = var.ec2_instance_type
}

module "rds" {
  source = "./modules/rds"

  project_name       = var.project_name
  environment        = var.environment
  private_subnet_ids = module.network.private_subnet_ids
  rds_sg_id          = module.security_group.rds_sg_id
  db_instance_class  = var.db_instance_class
  db_name            = var.db_name
  db_username        = var.db_username
  db_password        = var.db_password
}
