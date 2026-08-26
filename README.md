# Lab_01_ing_software

## Introducción

## Consultas
Lista de Usuarios en el sistema:
```bash
curl http://localhost:8080/api/estudiantes
```
```json
[
  {"id":1,"nombre":"Ana Pérez","email":"ana@demo.com"},
  {"id":2,"nombre":"Juan Gómez","email":"juan@demo.com"},
  {"id":3,"nombre":"Carlos Ruiz","email":"carlos@demo.com"}
]
```
Endpoint de búsqueda por Email
```bash
curl http://localhost:8080/api/estudiantes/buscar\?email\=juan@demo.com
```
```json
{
  "id":2,
  "nombre":"Juan Gómez",
  "email":"juan@demo.com"
}
```
Manejo de error con Email Existente:
```bash
curl -X POST http://localhost:8080/api/estudiantes -H "Content-Type: application/json" -d "{\"nombre\":\"Carlos Ruiz\",\"email\":\"carlos@demo.com\"}"
```
```json
{
  "mensaje":"Error de registro",
  "codigo":409,
  "detalle":"El email 'carlos@demo.com' ya está registrado. Por favor, use un correo electrónico diferente.",
  "timestamp":"2026-08-26T18:27:06.175096065"
}
```
Usuario No encontrado:
```bash
curl http://localhost:8080/api/estudiantes/buscar\?email\=juan@dem.com
```
```
null
```
## Limitaciones Observadas
## Conclusiones