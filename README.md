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

Se requere un archivo `.env` el cual es administrado por correo electronico. Este archivo no se encuentra en el repositorio por seguridad.

una vez descargado el archivo `.env`, se debe colocar en la raiz del proyecto, al mismo nivel que el `pom.xml` y el `docker-compose-local.yml`.


## Instalación

1. Clonar el repositorio
```bash
git clone https://github.com/lmcadev/franquicias-api.git
```

2. Descargar el archivo `.env` desde el correo electronico proporcionado y colocarlo en la raiz del proyecto.

3. Ejecutar comando de Docker Compose para levantar la API en local, instrucciones detalladas en la seccion "Ejecucion local con Docker" mas abajo.

4. Acceder a la API y a la documentacion Swagger UI en `http://localhost:8080/swagger-ui.html` o realizar la importacion de la coleccion Postman con OPENAPI JSON en `http://localhost:8080/v3/api-docs`

5. generar el token JWT para autenticacion en el caso de swagger, usando el endpoint de login, y pegarlo en el boton Autorize de Swagger UI para probar los endpoints protegidos. En el caso de postman generar el token JWT que se encuentra en /api/auth/login y pegarlo en la seccion de Authorization usando el esquema Bearer Token como una variable.

6. datos de autenticacion para pruebas:
    {
  "email": "admin@correo.com",
  "password": "admin123"
    }

7. explorar los endpoints  y validar que la funcionalidad de gestion de franquicias, sucursales y productos funciona correctamente.

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

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON (perfil local): `http://localhost:8080/v3/api-docs`

## Despliegue AWS Endpoints

- Infraestructura provisionada con Terraform
- EC2 para runtime de API
- RDS MySQL para persistencia
- Swagger UI: `http://ec2-54-227-120-171.compute-1.amazonaws.com/api/v1/swagger-ui.html`
- OpenAPI JSON: `http://ec2-54-227-120-171.compute-1.amazonaws.com/api/v1/api-docs`

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

## Proyecto desarrollado por Luis Miguel Castañeda para Accenture - Franquicias API