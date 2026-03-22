# Terrafor

Esta configuracion crea infraestructura minima en AWS Free Tier para Franquicias API:

- VPC dedicada
- 1 subred publica (EC2)
- 2 subredes privadas (RDS, requisito subnet group)
- Security Group EC2: HTTP 80 (publico) + SSH 22 (IP /32)
- Security Group RDS: MySQL 3306 solo desde EC2
- EC2 Amazon Linux `t2.micro`
- RDS MySQL `db.t3.micro`
- Outputs con endpoints

## Archivos root

- `versions.tf`: version de Terraform y provider AWS
- `provider.tf`: provider AWS con region y tags
- `variables.tf`: variables de entrada
- `main.tf`: composicion de modulos
- `outputs.tf`: endpoints y comandos utiles
- `terraform.tfvars.example`: ejemplo de variables

## Requisitos

1. AWS CLI autenticado (`aws configure`)
2. Terraform >= 1.5
3. Key Pair AWS: `Accenture_test`
4. Copiar `terraform.tfvars.example` a `terraform.tfvars`

## Uso rapido

```bash
cd terraform
cp terraform.tfvars.example terraform.tfvars
# Editar terraform.tfvars y reemplazar:
# - ssh_cidr
# - db_password

terraform init
terraform fmt -recursive
terraform validate
terraform plan
terraform apply
```

## Outputs esperados

- `ec2_public_ip`
- `ec2_public_dns`
- `rds_endpoint`
- `rds_port`
- `app_url`
- `ssh_command`



## Versiones recomendadas de modulos comunes (referencia)

Si luego migras a modulos del registry, se recomienda:

- `terraform-aws-modules/vpc/aws` `~> 5.0`
- `terraform-aws-modules/security-group/aws` `~> 5.0`
- `terraform-aws-modules/ec2-instance/aws` `~> 5.0`
- `terraform-aws-modules/rds/aws` `~> 6.0`

## GitHub Actions (despliegue automatizado)

Se agregaron dos workflows:

- `.github/workflows/terraform-plan.yml`:
	- corre en Pull Request contra `main`
	- ejecuta `fmt`, `init`, `validate`, `plan`
	- publica artefacto con el plan
- `.github/workflows/terraform-deploy.yml`:
	- ejecucion manual (`workflow_dispatch`)
	- operacion `apply` o `destroy`
	- requiere confirmacion via `auto_approve=true`

### Configuracion recomendada (OIDC)

No uses llaves AWS estaticas en GitHub. Usa OIDC con un IAM Role asumible por Actions.

#### Secrets (Repository -> Settings -> Secrets and variables -> Actions)

- `AWS_ROLE_TO_ASSUME`: ARN del rol IAM para GitHub Actions
- `TF_DB_PASSWORD`: password de RDS

#### Variables (Repository -> Settings -> Secrets and variables -> Actions)

- `AWS_REGION` (ejemplo: `us-east-1`)
- `TF_SSH_CIDR` (ejemplo: `181.10.20.30/32`)
- `TF_KEY_PAIR_NAME` (ejemplo: `Accenture_test`)
- `TF_PROJECT_NAME` (opcional, default `franquicias-api`)
- `TF_ENVIRONMENT` (opcional, default `dev`)
- `TF_EC2_INSTANCE_TYPE` (opcional, default `t2.micro`)
- `TF_DB_INSTANCE_CLASS` (opcional, default `db.t3.micro`)
- `TF_DB_NAME` (opcional, default `franquicias_db`)
- `TF_DB_USERNAME` (opcional, default `franquicias_user`)

### Flujo sugerido

1. Crear PR con cambios en `terraform/`.
2. Revisar artefacto del `terraform-plan.yml`.
3. Ejecutar manualmente `terraform-deploy.yml` con:
	 - `operation=apply`
	 - `auto_approve=true`
4. Para limpiar recursos y evitar costos:
	 - `operation=destroy`
	 - `auto_approve=true`
