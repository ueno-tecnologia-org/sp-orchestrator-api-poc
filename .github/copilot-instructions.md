# Instrucciones para GitHub Copilot

Este documento proporciona pautas para trabajar con este proyecto utilizando GitHub Copilot, siguiendo la arquitectura y las prácticas establecidas.

## Estructura del Proyecto

Este proyecto sigue una arquitectura hexagonal (ports and adapters) con las siguientes capas:

### 1. Dominio (`domain`)
- **Entities**: Modelos de datos puros sin dependencias externas
- **Repositories**: Interfaces para acceso a datos
- **UseCases**: Interfaces que definen los casos de uso de la aplicación

### 2. Negocio (`business`)
- **Ports**: Interfaces que definen cómo el dominio se comunica con el mundo exterior
- **UseCases**: Implementaciones concretas de los casos de uso del dominio

### 3. Infraestructura (`infrastructure`)
- **Adapters**:
  - `inbound`: Adaptadores de entrada (REST, etc.)
  - `outbound`: Adaptadores de salida (MongoDB, etc.)
- **Configurations**: Configuraciones de Spring Boot
- **Data**: Implementaciones concretas de repositorios
- **Interfaces**: Controladores REST y otros puntos de entrada

### 4. Archivos de Configuración de GitHub (`.github`)
- **Workflows**: Definiciones de flujos de trabajo para CI/CD en GitHub Actions
- **Pull Request Templates**: Plantillas predefinidas para pull requests
- **Issue Templates**: Plantillas para la creación de issues
- **Dependabot**: Configuraciones para actualizaciones automáticas de dependencias

## Convenciones de Código

### Principios Generales
1. Sigue los principios SOLID
2. Usa inyección de dependencias
3. Implementa interfaces para definir contratos entre capas
4. Mantén las entidades del dominio libres de dependencias externas

### Kotlin
1. Usa data classes para modelos inmutables
2. Aprovecha las características funcionales de Kotlin (funciones de extensión, lambdas)
3. Utiliza interfaces funcionales con `fun interface` cuando sea apropiado

### Patrones de Diseño
1. **Port-Adapter**: Separa la lógica de negocio de los detalles de implementación
2. **Dependency Inversion**: Las dependencias apuntan hacia el dominio
3. **Repository**: Abstracción para acceso a datos

## Flujo de Trabajo

### Nuevas Características
Al agregar nuevas características, sigue este orden:
1. Define entidades en el dominio
2. Define interfaces de casos de uso en el dominio
3. Implementa casos de uso en la capa de negocio
4. Implementa adaptadores para la infraestructura

### Testing
- Escribe pruebas unitarias para la lógica de dominio
- Usa Mockito para simular dependencias
- Implementa tests de integración para verificar la conectividad con servicios externos

## Tecnologías

- **Spring Boot 3.x**: Framework principal
- **Kotlin 1.9.x**: Lenguaje de programación
- **MongoDB**: Base de datos
- **OpenAPI/Swagger**: Documentación de API
- **New Relic**: Monitoreo

## Convenciones de Nomenclatura

- **Entidades de Dominio**: Nombres descriptivos sin sufijos
- **Casos de Uso**: Verbos que describen la acción + sustantivo + `UseCase`
- **Controladores**: Sustantivo + `Controller`
- **Repositorios**: Sustantivo + `Repository`
- **DTOs**: Sustantivo + propósito + `Request`/`Response`

## Manejo de Errores

- Usa excepciones personalizadas para errores de dominio
- Implementa un manejador global de excepciones para traducir errores a respuestas HTTP apropiadas
- Registra errores con niveles de log adecuados

## API REST

- Sigue principios RESTful
- Usa verbos HTTP apropiados (GET, POST, PUT, DELETE)
- Implementa respuestas con códigos HTTP adecuados
- Documenta endpoints con anotaciones OpenAPI

## Logging

- Usa SLF4J para logging
- Incluye información contextual relevante en los logs
- Evita logging excesivo en rutas críticas
