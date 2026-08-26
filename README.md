# Lab_01_ing_software

## Introducción
En este laboratorio se desarrolla un módulo básico de gestión de estudiantes utilizando Spring Boot, Java 21 y una base de datos H2 en memoria, aplicando el concepto de arquitectura por capas. El ejercicio permite comprender la separación de responsabilidades entre el controlador, la lógica de negocio, el acceso a datos y el modelo de la aplicación.

A través de una API REST se implementan operaciones para registrar y consultar estudiantes, incorporando validaciones como el formato del correo electrónico y la restricción de correos únicos. Además, se utiliza H2 para almacenar y verificar los datos durante la ejecución del proyecto.

El laboratorio busca entender cómo fluye una solicitud desde la API hasta la base de datos y cómo la arquitectura por capas ayuda a organizar el código y sus responsabilidades.
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
## Conclusiones
- La implementación del módulo de estudiantes permitió comprender de forma práctica cómo funciona una arquitectura por capas, separando las responsabilidades entre Controller, Service, Repository y Model.
- El uso de Spring Boot y JPA facilita la comunicación entre la aplicación y la base de datos H2, permitiendo realizar operaciones de persistencia sin manejar directamente las consultas de bajo nivel.
- Las validaciones implementadas, como el formato del correo y la restricción de email único, muestran la importancia de aplicar reglas de negocio para mantener la integridad de la información.
- La práctica permitió identificar claramente que el Repository constituye la zona de acceso a la base de datos, mientras que el Service concentra la lógica de negocio y el Controller se encarga de exponer los servicios mediante endpoints REST.