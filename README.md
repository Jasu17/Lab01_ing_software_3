# Lab 04 — Microservicios con Spring Boot, JWT y Docker

## Descripción

Solución académica compuesta por dos microservicios desarrollados con Spring Boot 3.2.5 y Java 21:

- **consulta-service:** gestión y consulta de estudiantes mediante una API REST protegida.
- **auth-service:** autenticación de usuarios mediante BCrypt y generación/validación de tokens JWT.

La comunicación entre servicios se realiza mediante OpenFeign. Ambos utilizan bases de datos H2 independientes.

## Requisitos

- Java 21
- Maven
- Docker y Docker Compose (para ejecución contenerizada)

## Ejecución local

Abrir dos terminales desde la raíz del repositorio.

**1. Iniciar auth-service:**

```bash
cd microservicios-demoacademico/auth-service
mvn clean package -DskipTests
mvn spring-boot:run
```

**2. Iniciar consulta-service:**

```bash
cd microservicios-demoacademico/consulta-service
mvn clean package -DskipTests
mvn spring-boot:run
```

Los servicios deben ejecutarse en el siguiente orden: primero `auth-service` (8081) y después `consulta-service` (8082).

## Ejecución con Docker Compose

Desde la carpeta `microservicios-demoacademico`, compilar ambos proyectos:

```bash
cd microservicios-demoacademico/auth-service
mvn clean package -DskipTests

cd ../consulta-service
mvn clean package -DskipTests
```

Levantar los contenedores desde la carpeta `microservicios-demoacademico`:

```bash
docker compose up --build -d
```

Verificar el estado:

```bash
docker compose ps
```

Detener los servicios:

```bash
docker compose down
```

## URLs de acceso

| Servicio | Swagger UI | H2 Console |
|---|---|---|
| auth-service | http://localhost:8081/swagger-ui.html | http://localhost:8081/h2-console |
| consulta-service | http://localhost:8082/swagger-ui.html | http://localhost:8082/h2-console |

**Configuración de conexión H2:**

| Campo | auth-service | consulta-service |
|---|---|---|
| JDBC URL | `jdbc:h2:mem:authdb` | `jdbc:h2:mem:demoacademico` |
| Driver | `org.h2.Driver` | `org.h2.Driver` |
| Usuario | `sa` | `sa` |
| Contraseña | Vacía | Vacía |

Nota: H2 Console tiene restricciones para conexiones remotas. En Docker, el acceso desde el navegador puede requerir habilitar explícitamente `web-allow-others`.

## Usuarios de prueba

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | `Admin2026*` | ADMIN |
| docente | `Docente2026*` | DOCENTE |
| estudiante1 | `Estu2026*` | ESTUDIANTE |

Los usuarios se crean automáticamente mediante `AuthDataSeeder` cuando la base de datos está vacía.

## Flujo de autenticación en Swagger

1. Abrir Swagger de `auth-service`:
   http://localhost:8081/swagger-ui.html
2. Ejecutar `POST /auth/login` con un usuario y contraseña válidos.
3. Copiar únicamente el valor del campo `token` de la respuesta.
4. Ejecutar `GET /auth/validate` y enviar el token como parámetro para verificar su validez.
5. Abrir Swagger de `consulta-service`:
   http://localhost:8082/swagger-ui.html
6. Pulsar **Authorize**.
7. Pegar únicamente el token copiado, sin escribir el prefijo `Bearer`.
8. Pulsar **Authorize** y cerrar la ventana emergente.
9. Ejecutar `GET /api/estudiantes`.

Swagger agregará automáticamente el encabezado:

```http
Authorization: Bearer <token>
```

Si no se autoriza la petición o se utiliza un token inválido, el servicio debe rechazar el acceso.

## Evidencias
El PDF está en `docs/evidencias/Laboratorio_04_Evidencias.pdf`. Las capturas usadas están en `docs/evidencias/capturas/`.

---

**Tecnologías:** Java 21 · Spring Boot · Spring Security · Spring Data JPA · H2 · BCrypt · JWT · OpenFeign · Swagger/OpenAPI · Docker Compose.