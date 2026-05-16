# Proyecto-Yogurt
Sistema de Producción y Monitoreo de Yogurt 

Aplicación desarrollada con Spring Boot para gestionar y monitorear el proceso de producción de yogurt, permitiendo administrar recetas, controlar lotes y supervisar métricas en tiempo real.

# Funcionalidades principales
- Gestión de recetas
- Control de lotes de producción
- Monitoreo de temperatura
- Seguimiento de estados del proceso
- Dashboard de métricas
- API REST documentada con Swagger/OpenAP

# Tecnologías utilizadas
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Lombok
- Swagger / OpenAPI
- Maven

# Arquitectura del proyecto
El proyecto sigue una arquitectura en capas basada en buenas prácticas de Spring Boot y principios SOLID.

Controller > Service > Repository > Database
Capas 
Controllers

Gestionan las solicitudes HTTP y las respuestas de la API.

Services

Contienen la lógica de negocio del sistema.

Repositories

Acceso y persistencia de datos mediante Spring Data JPA.

DTOs

Objetos utilizados para transportar datos entre cliente y servidor.

Entities

Representan el modelo de dominio y las tablas de la base de datos.

# Ejecucion
- Por la consola estando en la carpeta del proyecto "demo": .\mvnw.cmd spring-boot:run
- Puerto de la aplicacion: http://localhost:8082
- Documentacion de la API: http://localhost:8082/swagger-ui/index.html

