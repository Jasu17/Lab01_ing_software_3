# Laboratorio 03 — Sistema académico y arquitectura por módulos

Proyecto `demo-academico` de Ingeniería de Software III. Continúa el Laboratorio 02 (CRUD de estudiantes, Opción A) y agrega el flujo Programa, Asignatura, Grupo y Matrícula. Después reorganiza el código por módulos y comunica matrículas con los demás módulos solo mediante ports.

## Requisitos

- JDK 21 o superior. El `pom.xml` compila con release 21 (Spring Boot 3.2.5). En esta máquina `JAVA_HOME` apunta a JDK 11, que no puede compilar el proyecto; hay que apuntarlo a un JDK 21 o superior antes de Maven. Aquí se usó `C:\Program Files\Java\jdk-25.0.2`.
- Maven Wrapper incluido en `DemoAcademicoApplication` (`mvnw.cmd`). No hace falta instalar Maven.

## Cómo iniciar la aplicación

Desde `DemoAcademicoApplication`:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25.0.2"
.\mvnw.cmd spring-boot:run
```

La aplicación escucha en el puerto 8080. H2 queda en memoria.

## Pruebas automatizadas

Desde `DemoAcademicoApplication`:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25.0.2"
.\mvnw.cmd test
```

`MatriculaRulesTest` cubre matrícula exitosa, duplicado con cupo todavía disponible, rechazo por cupo, estudiante inexistente y grupo inexistente. `MatriculaArchitectureTest` comprueba que el módulo de matrículas no importa repositorios de estudiantes ni de grupos.

## Swagger y H2

- Swagger UI: http://localhost:8080/swagger-ui.html
- Consola H2: http://localhost:8080/h2-console
- JDBC: `jdbc:h2:mem:demoacademico`
- Usuario: `sa`
- Contraseña: vacía

## Estructura modular

```
co.edu.demoacademico
├── common          respuestas, excepciones y manejador global
├── estudiantes
├── programas
├── asignaturas
├── grupos
└── matriculas
```

Cada módulo de negocio agrupa modelo, repositorio, servicio, handler, controller y DTOs.

## Ports

- `EstudianteQueryPort.obtenerPorId(Long id)` es el contrato público del módulo de estudiantes. Lo implementa `EstudianteServiceImpl`. Si el estudiante no existe, lanza `NotFoundException`.
- `GrupoQueryPort.obtenerPorId(Long id)` es el contrato público del módulo de grupos. Lo implementa `GrupoServiceImpl`. Si el grupo no existe, lanza `NotFoundException`.

`MatriculaServiceImpl` usa esos dos ports y su propio `MatriculaRepository`. No inyecta `EstudianteRepository` ni `GrupoRepository`.

## Reglas de matrícula

1. El estudiante debe existir.
2. El grupo debe existir.
3. La pareja estudiante-grupo no puede repetirse.
4. El número de matrículas del grupo no puede alcanzar `cupoMax`.
5. Si las cuatro condiciones se cumplen, se guarda la matrícula.

Los errores de negocio responden 400 con `ApiResponse`. Los recursos ausentes responden 404. La validación de DTOs responde 400 con el mensaje "Validación fallida".

## Semilla

`application.yaml` controla la carga inicial:

- `app.seed.enabled`
- `app.seed.cantidad-estudiantes` (si no está, se usa `app.seed.cantidad`)
- `app.seed.cantidad-programas`
- `app.seed.cantidad-asignaturas`
- `app.seed.cantidad-grupos`

Si ya hay datos de ese tipo, el seeder correspondiente no vuelve a insertar. No se generan matrículas.

## Evidencias

El PDF está en `docs/evidencias/Laboratorio_03_Evidencias.pdf`. Las capturas usadas están en `docs/evidencias/capturas/`.
