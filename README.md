# Laboratorio: Contenerización de Microservicios con Spring Boot y Docker

Este repositorio documenta el desarrollo y la implementación práctica del laboratorio enfocado en la contenerización de aplicaciones desarrolladas con **Spring Boot**, abriendo desde enfoques ineficientes hasta el estándar de la industria (*Multi-stage builds*), gestión de volúmenes para persistencia y aislamiento de redes en arquitecturas de microservicios.

## Estructura y Fases del Laboratorio

El laboratorio se estructuró de manera progresiva para analizar el impacto del diseño de contenedores en términos de tamaño, seguridad, rendimiento y despliegue:

### 1. Análisis de Enfoques de Contenerización
* **Enfoque Ineficiente (`Dockerfile.bad`):** 
  * Se realizó la compilación de la aplicación directamente dentro de la imagen de Docker.
  * *Resultado:* Imágenes sobredimensionadas (superiores a `1.19 GB`), alta superficie de ataque debido a la inclusión del SDK completo, Maven, código fuente y dependencias intermedias.
* **Compilación Separada (`Dockerfile.good`):**
  * Se separó el proceso compilando localmente el archivo ejecutable (`.jar`) y utilizando Docker exclusivamente para ejecutar el artefacto sobre una imagen base ligera (`Alpine`).
  * *Resultado:* Reducción drástica del tamaño de la imagen (~660 MB sin comprimir / ~235 MB comprimidos) y mayor seguridad en el entorno de ejecución.
* **Enfoque Óptimo Multi-stage (`Dockerfile.multi`):**
  * Se implementó un único `Dockerfile` dividido en dos etapas: una primera etapa (`build`) encargada de compilar con Maven y una segunda etapa de producción (`runtime`) basada en Alpine que hereda únicamente el artefacto ejecutable (`app.jar`).
  * *Resultado:* Automatización completa del proceso de construcción de manera reproducible y limpia, sin contaminar el entorno host del desarrollador.

### 2. Persistencia de Datos con Volúmenes
* Se implementaron volúmenes de Docker (`datos-spring-lab`) para evitar la pérdida de información inherente a la naturaleza efímera de los contenedores.
* Se validó la durabilidad de los datos al escribir y leer archivos de prueba a través de contenedores independientes que comparten el mismo volumen de almacenamiento persistente.

### 3. Redes y Comunicación entre Conteneres
* Se creó una red virtual privada en Docker (`red-spring-lab`) para aislar y conectar los servicios de forma segura.
* Se probó la comunicación interna entre contenedores (como microservicios y bases de datos MySQL) mediante resolución de nombres utilizando el DNS interno de Docker (por ejemplo, conectando al host `db-lab`), evitando la dependencia de direcciones IP estáticas.

### 4. Caso Integrador y Orquestación
* Se analizaron escenarios empresariales donde la optimización de tamaño, la seguridad de las imágenes, la persistencia de bases de datos y la correcta comunicación de red son críticas para entornos de producción.
* Se consolidó la estructura mediante prácticas estandarizadas para el despliegue concurrente utilizando orquestación de contenedores.

---

## Tabla Comparativa de Enfoques

| Criterio | Compilación en Docker (`bad`) | Compilación Separada (`good`) | Enfoque Multi-stage (`multi`) |
| :--- | :--- | :--- | :--- |
| **Tamaño de Imagen** | Muy alto (> 1 GB) | Optimizado (~660 MB / 235 MB) | Optimizado (~660 MB / 235 MB) |
| **Seguridad** | Baja (expone herramientas y código) | Alta (solo binario de ejecución) | Alta (entorno limpio y aislado) |
| **Reproducibilidad** | Depende del entorno local | Baja (requiere build previo manual) | Alta (automatizada por Docker) |
| **Adecuación Producción**| No recomendada | Aceptable | Estándar ideal de la industria |

---

## Conclusiones Clave
1. **Optimización de Imágenes:** El uso de compilaciones *multi-stage* y bases minimalistas como Alpine resulta indispensable para reducir costos de almacenamiento, acelerar descargas en registros y minimizar la superficie de ataque.
2. **Persistencia y Estado:** Los contenedores deben mantenerse apátridas (*stateless*); toda información transaccional o de bases de datos debe recaer estrictamente sobre volúmenes externos gestionados.
3. **Networking Seguro:** El aislamiento por redes virtuales y la comunicación por nombres de host garantizan arquitecturas limpias, escalables y protegidas contra exposiciones innecesarias al exterior.
