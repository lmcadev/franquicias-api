# Franquicias API

API REST reactiva para gestionar franquicias, sucursales y productos, con autenticacion JWT, documentacion OpenAPI y despliegue en AWS usando Terraform.

## Tecnologias usadas

- Java 17
- Spring Boot 4.0.4
- Spring WebFlux (Project Reactor)
- Spring Data R2DBC
- MySQL 8
- JWT (jjwt)
- Spring Security
- Springdoc OpenAPI / Swagger UI
- MapStruct
- JUnit 5, Mockito, Reactor Test
- Docker y Docker Compose
- Terraform (AWS)

## Arquitectura

- Clean Architecture
- Organizacion por feature/dominio
- Monolito modular
- Persistencia reactiva con R2DBC
- Soft delete con columna `deleted_at`

## Requisitos para ejecutar en local

- Docker Desktop 4+
- Docker Compose v2


## Variables de entorno

Se requere un archivo `.env` el cual es administrado

## Ejecucion local con Docker

Desde la raiz del proyecto:

```bash
docker compose -f docker-compose-local.yml down
docker compose -f docker-compose-local.yml up -d --build
```

En Windows PowerShell tambien puedes usar:

```powershell
docker-compose -f docker-compose-local.yml down
docker-compose -f docker-compose-local.yml up -d --build
```

Ver logs de API:

```bash
docker logs -f franquicias-api-local
```

## Endpoints locales utiles

- API base: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON (perfil local): `http://localhost:8080/v3/api-docs`



## Tests

### Pruebas unitarias

Ejecutar suite completa:

En Linux/macOS:

```bash
./mvnw test
```

En Windows PowerShell:

```powershell
.\mvnw.cmd test
```

Criterio de exito:

- `BUILD SUCCESS`
- `Failures: 0`
- `Errors: 0`

### Pruebas funcionales (smoke test local)

Con la API levantada en local, validar endpoints clave.

En Windows PowerShell:

```powershell
$base='http://localhost:8080'
$r0=Invoke-WebRequest -Uri ($base + '/swagger-ui.html') -Method Get -SkipHttpErrorCheck
"SWAGGER_STATUS=$($r0.StatusCode)"

$email='local.test'+(Get-Date -Format 'yyyyMMddHHmmss')+'@correo.com'
$pwd='Pass12345!'

$reg=@{name='Local User';email=$email;password=$pwd} | ConvertTo-Json -Compress
$r1=Invoke-WebRequest -Uri ($base + '/api/auth/register') -Method Post -ContentType 'application/json' -Body $reg -SkipHttpErrorCheck
"REGISTER_STATUS=$($r1.StatusCode)"

$login=@{email=$email;password=$pwd} | ConvertTo-Json -Compress
$r2=Invoke-WebRequest -Uri ($base + '/api/auth/login') -Method Post -ContentType 'application/json' -Body $login -SkipHttpErrorCheck
"LOGIN_STATUS=$($r2.StatusCode)"
```

Resultado esperado:

- `SWAGGER_STATUS=200`
- `REGISTER_STATUS=201`
- `LOGIN_STATUS=200`

## Estructura del proyecto

```text
src/main/java/com/accenture/franquicias_api/
	application/      DTOs, mappers, use cases
	domain/           entidades y contratos de repositorio
	infrastructure/   config, seguridad, persistencia
	presentation/     controllers y manejo de excepciones

src/main/resources/
	application.yml
	application-local.yml
	application-prod.yml
	schema.sql

docker-compose-local.yml
docker-compose-produccion.yml
terraform/
```

## Despliegue AWS

- Infraestructura provisionada con Terraform
- EC2 para runtime de API
- RDS MySQL para persistencia
- Swagger en EC2: `http://ec2-54-227-120-171.compute-1.amazonaws.com/api/v1/swagger-ui/index.html`
- OpenAPI en EC2: `http://ec2-54-227-120-171.compute-1.amazonaws.com/api/v1/api-docs`


## Proyecto desarrollado por Luis Miguel Castañeda para Accenture - Franquicias API