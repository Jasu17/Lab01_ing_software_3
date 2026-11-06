# Especificación técnica. Proyecto S3

Contrato de ejecución verificado: Java 21, Spring Boot 3.2.5, cinco artefactos Maven independientes, imagen `eclipse-temurin:21-jre`, red Compose `proyecto-s3_default`.

## 1. Mapa de red

Cada servicio escucha en `0.0.0.0` del contenedor. El host publica el mismo puerto. La base H2 es `jdbc:h2:mem` dentro del proceso. No hay contenedor de base de datos.

| Servicio | Puerto | Base | Variables inyectadas por Compose |
|---|---:|---|---|
| auth-service | 8081 | `authdb` | Ninguna |
| estudiantes-service | 8082 | `estudiantesdb` | `AUTH_SERVICE_URL`, `MATRICULAS_SERVICE_URL` |
| cursos-service | 8083 | `cursosdb` | `AUTH_SERVICE_URL`, `MATRICULAS_SERVICE_URL` |
| matriculas-service | 8084 | `matriculasdb` | `AUTH_SERVICE_URL`, `ESTUDIANTES_SERVICE_URL`, `CURSOS_SERVICE_URL` |
| calificaciones-service | 8085 | `calificacionesdb` | `AUTH_SERVICE_URL`, `MATRICULAS_SERVICE_URL` |

Valores dentro de la red:

| Variable | Valor |
|---|---|
| `AUTH_SERVICE_URL` | `http://auth-service:8081` |
| `ESTUDIANTES_SERVICE_URL` | `http://estudiantes-service:8082` |
| `CURSOS_SERVICE_URL` | `http://cursos-service:8083` |
| `MATRICULAS_SERVICE_URL` | `http://matriculas-service:8084` |

Sin esas variables, cada `application.yaml` usa `http://localhost:<puerto>`. Ese default vale para `mvn spring-boot:run` en el host. Dentro de un contenedor, `localhost` es el propio contenedor.

`depends_on` ordena el arranque y no espera a que el puerto acepte tráfico. Estudiantes y cursos no declaran a matrículas en `depends_on`: el ciclo de llamadas existe, el ciclo de arranque no.

```mermaid
graph TD
    Client[Cliente REST / Swagger]

    Client -->|8081| Auth[auth-service]
    Client -->|8082| Est[estudiantes-service]
    Client -->|8083| Cur[cursos-service]
    Client -->|8084| Mat[matriculas-service]
    Client -->|8085| Cal[calificaciones-service]

    Auth --- AuthDb[(H2 authdb)]
    Est --- EstDb[(H2 estudiantesdb)]
    Cur --- CurDb[(H2 cursosdb)]
    Mat --- MatDb[(H2 matriculasdb)]
    Cal --- CalDb[(H2 calificacionesdb)]

    Est -->|GET /auth/validate| Auth
    Cur -->|GET /auth/validate| Auth
    Mat -->|GET /auth/validate| Auth
    Cal -->|GET /auth/validate| Auth

    Mat -->|Feign Bearer GET estudiante| Est
    Mat -->|Feign Bearer GET curso| Cur
    Est -->|Feign Bearer conteo activas| Mat
    Cur -->|Feign Bearer conteo activas| Mat
    Cal -->|Feign Bearer matricula y /mias| Mat
```

Aristas de negocio que el grafo de cliente no sustituye:

| Origen | Destino | Uso |
|---|---|---|
| estudiantes, cursos, matriculas, calificaciones | auth-service | Validación del Bearer en cada petición protegida |
| matriculas-service | estudiantes-service, cursos-service | Existencia y `cupoMaximo` en el alta |
| estudiantes-service, cursos-service | matriculas-service | Conteo de matrículas activas antes de borrar; cursos también al reducir el cupo |
| calificaciones-service | matriculas-service | Matrícula activa en el alta y en la corrección; `/mias` para las notas del estudiante |

## 2. Imagen y arranque

Los cinco `Dockerfile` coinciden salvo `EXPOSE`. Copian `target/*.jar` a `/app/app.jar` y ejecutan `java -jar`. Maven no corre en esa imagen. El JAR se produce antes, con JDK 21.

JWT: HS256, expiración 3 600 000 ms, secreto en `application.yaml` de auth-service. Compose no lo sobrescribe.

Semilla con base vacía: `admin`, `docente`, `estudiante` (este último con `estudianteId` 1); 50 estudiantes; cursos `IS3-001` (cupo 30), `BD2-001` (cupo 25), `RED-001` (cupo 2).

## 3. Alta de matrícula

Seis HTTP síncronos. El duplicado y el cupo se resuelven después, en la base local de matrículas.

```mermaid
sequenceDiagram
    participant C as Cliente
    participant M as matriculas-service
    participant A as auth-service
    participant E as estudiantes-service
    participant U as cursos-service

    C->>M: POST /api/matriculas
    M->>A: GET /auth/validate
    A-->>M: claims
    M->>E: GET /api/estudiantes/id
    E->>A: GET /auth/validate
    A-->>E: claims
    E-->>M: estudiante
    M->>U: GET /api/cursos/id
    U->>A: GET /auth/validate
    A-->>U: claims
    U-->>M: curso y cupoMaximo
    M-->>C: 201
```

La página `docs/arquitectura-red.html` dibuja el mismo mapa para abrirlo en el navegador.
