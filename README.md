# SP Orchestrator API POC

POC de un orquestador de stored procedures que implementa patrones de resiliencia con Resilience4j y cache con Caffeine, siguiendo arquitectura hexagonal.

## 🏗️ Arquitectura

Este proyecto implementa una **arquitectura hexagonal** (ports and adapters) con las siguientes características:

### Capas Implementadas

- **Domain**: Entidades y casos de uso puros sin dependencias externas
- **Business**: Implementación de casos de uso y puertos
- **Infrastructure**: Adaptadores de entrada (REST) y salida (DB, Cache, Resilience)

### Patrones de Resiliencia por Stored Procedure

- ✅ **Circuit Breaker**: Previene cascadas de fallos
- ✅ **Rate Limiting**: Control de tráfico por procedimiento  
- ✅ **Bulkhead**: Aislamiento de recursos
- ✅ **Cache con TTL**: Cache inteligente usando Caffeine

## 🚀 Características Principales

### Stored Procedures Configurados

1. **getUserData**
   - Cache: 10 minutos TTL
   - Rate Limit: 20 req/seg
   - Circuit Breaker: 50% fallo threshold
   - Bulkhead: 10 llamadas concurrentes

2. **getAccountBalance**
   - Cache: 2 minutos TTL
   - Rate Limit: 50 req/seg
   - Circuit Breaker: 60% fallo threshold
   - Bulkhead: 15 llamadas concurrentes

3. **processTransaction**
   - Cache: Deshabilitado (transacciones no deben cachearse)
   - Rate Limit: 5 req/seg
   - Circuit Breaker: 30% fallo threshold
   - Bulkhead: 3 llamadas concurrentes

## 🛠️ Tecnologías Utilizadas

- **Spring Boot 3.1.5** - Framework principal
- **Kotlin 1.9.25** - Lenguaje de programación
- **Resilience4j 2.1.0** - Patrones de resiliencia
- **Caffeine 3.1.8** - Cache de alto rendimiento
- **H2 Database** - Base de datos en memoria para demo
- **OpenAPI/Swagger** - Documentación de API
- **Spring Actuator** - Métricas y monitoreo

## 🏃‍♂️ Inicio Rápido

### Requisitos
- Java 11 o superior
- Gradle (incluido wrapper)

### Ejecución
```bash
# Usando el script de inicio
./start.sh

# O manualmente
./gradlew clean build
java -jar build/libs/sp-orchestrator-api-poc-0.0.1-SNAPSHOT.jar
```

### Endpoints Principales

- **Swagger UI**: http://localhost:8080/sp-orchestrator/swagger-ui.html
- **API Docs**: http://localhost:8080/sp-orchestrator/api-docs
- **H2 Console**: http://localhost:8080/sp-orchestrator/h2-console
- **Actuator**: http://localhost:8080/sp-orchestrator/actuator

## 📋 API Endpoints

### Ejecutar Stored Procedure
```http
POST /sp-orchestrator/api/v1/stored-procedures/execute
Content-Type: application/json

{
  "procedureName": "getUserData",
  "parameters": {
    "userId": "12345"
  },
  "timeoutSeconds": 30
}
```

### Obtener Estadísticas de Cache
```http
GET /sp-orchestrator/api/v1/stored-procedures/procedures/{procedureName}/stats
```

### Limpiar Cache
```http
DELETE /sp-orchestrator/api/v1/stored-procedures/procedures/{procedureName}/cache
```

## 🧪 Ejemplos de Uso

### 1. Obtener Datos de Usuario
```bash
curl -X POST "http://localhost:8080/sp-orchestrator/api/v1/stored-procedures/execute" \
  -H "Content-Type: application/json" \
  -d '{
    "procedureName": "getUserData",
    "parameters": {
      "userId": "12345"
    }
  }'
```

### 2. Consultar Balance de Cuenta
```bash
curl -X POST "http://localhost:8080/sp-orchestrator/api/v1/stored-procedures/execute" \
  -H "Content-Type: application/json" \
  -d '{
    "procedureName": "getAccountBalance",
    "parameters": {
      "accountId": "ACC-789"
    }
  }'
```

### 3. Procesar Transacción
```bash
curl -X POST "http://localhost:8080/sp-orchestrator/api/v1/stored-procedures/execute" \
  -H "Content-Type: application/json" \
  -d '{
    "procedureName": "processTransaction",
    "parameters": {
      "transactionId": "TXN-001",
      "amount": 100.50
    }
  }'
```

## 📊 Monitoreo y Métricas

### Actuator Endpoints
- `/actuator/health` - Estado de la aplicación
- `/actuator/metrics` - Métricas generales
- `/actuator/prometheus` - Métricas en formato Prometheus

### Cache Stats
- Hit rate por stored procedure
- Estadísticas de evictions
- Tamaño actual del cache

## 🔧 Configuración

Las configuraciones se pueden personalizar en `application.properties`:

```properties
# Resilience4j
resilience4j.circuitbreaker.instances.default.failure-rate-threshold=50
resilience4j.ratelimiter.instances.default.limit-for-period=10
resilience4j.bulkhead.instances.default.max-concurrent-calls=5

# Logging
logging.level.com.uenobank.sporchestratorapi=INFO
logging.level.io.github.resilience4j=DEBUG
```

## 🏗️ Estructura de Packages

```
com.uenobank.sporchestratorapi/
├── domain/                     # Entidades y casos de uso puros
│   ├── entities/              # Modelos de dominio
│   ├── repositories/          # Interfaces de repositorios
│   └── usecases/             # Interfaces de casos de uso
├── business/                  # Lógica de negocio
│   ├── ports/                # Puertos para adaptadores
│   └── usecases/             # Implementaciones de casos de uso
└── infrastructure/           # Adaptadores e infraestructura
    ├── adapters/
    │   ├── inbound/          # REST controllers
    │   └── outbound/         # DB, Cache, Resilience
    └── configuration/        # Configuraciones Spring
```

## 🧪 Testing

```bash
# Ejecutar tests
./gradlew test

# Ejecutar con coverage
./gradlew test jacocoTestReport
```

## 📈 Características de Producción

- **Circuit Breaker**: Prevención de cascadas de fallos
- **Rate Limiting**: Control de tráfico granular
- **Bulkhead**: Aislamiento de recursos críticos
- **Cache Inteligente**: TTL configurable por procedimiento
- **Métricas**: Integración con Prometheus
- **Health Checks**: Endpoints de salud
- **Documentación**: OpenAPI/Swagger automática

## 🔄 Próximos Pasos

- [ ] Integración con base de datos real
- [ ] Autenticación y autorización
- [ ] Métricas personalizadas
- [ ] Distribución de cache
- [ ] Retry policies configurables

