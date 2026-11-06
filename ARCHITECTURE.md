# Arquitectura

## Seguridad

La sesión es stateless. `auth-service` emite un JWT HS256 con expiración de una hora. Los claims son `sub` (usuario), `role`, `iat`, `exp` y, solo si el usuario tiene un estudiante asociado, `estudianteId`. Los demás servicios no verifican la firma en local: `AuthValidationFilter` llama a `GET /auth/validate` y publica una autoridad igual al rol (`ADMIN`, `DOCENTE`, `ESTUDIANTE`).

`/auth/**` en auth-service es público. La comprobación de credenciales y de firma ocurre en `AuthServiceImpl`. Las contraseñas se guardan con BCrypt.

## Filtro y `filterChain.doFilter`

```java
try {
    validation = authClient.validate(authHeader);
} catch (FeignException.Unauthorized ex) {
    reject(..., "TOKEN_INVALID");
    return;
} catch (FeignException ex) {
    reject(..., "TOKEN_VALIDATION_ERROR");
    return;
}
filterChain.doFilter(request, response);
```

El `try` envuelve solo `GET /auth/validate`. Un 401 de auth-service se responde como `TOKEN_INVALID`. Cualquier otra `FeignException` (red o proceso caído) se responde como `TOKEN_VALIDATION_ERROR`.

`doFilter` queda fuera. `BusinessException` y `RemoteServiceException` no son `FeignException` y siguen hasta `GlobalExceptionHandler` (400 y 502). Si `doFilter` estuviera dentro del `try`, una `FeignException` no traducida por una capa inferior se etiquetaría como fallo del token.

## Alta de matrícula

Seis HTTP síncronos, en este orden:

1. Cliente `POST /api/matriculas`.
2. Filtro de matrículas `GET /auth/validate`.
3. `GET /api/estudiantes/{id}` con el Bearer reenviado.
4. Filtro de estudiantes `GET /auth/validate`.
5. `GET /api/cursos/{id}` con el mismo Bearer.
6. Filtro de cursos `GET /auth/validate`.

El duplicado activo y el cupo se evalúan después, en la base local de matrículas. Auth caído en el paso 2 responde 401 al cliente. Estudiantes o cursos caídos responden 502. Un id inexistente responde 400.

## Matriz de permisos

| Operación | ADMIN | DOCENTE | ESTUDIANTE |
|---|---|---|---|
| Estudiantes, lectura | sí | sí | no |
| Estudiantes, escritura | sí | no | no |
| Cursos, lectura | sí | sí | sí |
| Cursos, escritura | sí | no | no |
| Matrículas, lectura general | sí | sí | no |
| Matrículas, alta y anulación | sí | no | no |
| `GET /api/matriculas/mias` | no | no | sí |
| Calificaciones, lectura por matrícula | sí | sí | no |
| Calificaciones, alta y corrección | sí | sí | no |
| `GET /api/calificaciones/mias` | no | no | sí |

La regla de `/mias` se declara antes del comodín `/**`. Spring Security aplica la primera coincidencia.
