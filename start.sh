#!/bin/bash

echo "🚀 Iniciando SP Orchestrator API POC..."
echo "📋 Características implementadas:"
echo "   ✅ Circuit Breaker con Resilience4j"
echo "   ✅ Rate Limiting por stored procedure"
echo "   ✅ Bulkhead pattern para aislamiento"
echo "   ✅ Cache con TTL usando Caffeine"
echo "   ✅ Arquitectura hexagonal"
echo ""

# Compilar el proyecto
echo "🔨 Compilando proyecto..."
./gradlew clean build -x test

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
    echo ""
    echo "🌐 Iniciando aplicación..."
    echo "📍 Swagger UI: http://localhost:8080/sp-orchestrator/swagger-ui.html"
    echo "📍 API Docs: http://localhost:8080/sp-orchestrator/api-docs"
    echo "📍 H2 Console: http://localhost:8080/sp-orchestrator/h2-console"
    echo "📍 Actuator: http://localhost:8080/sp-orchestrator/actuator"
    echo ""

    # Ejecutar la aplicación
    java -jar build/libs/sp-orchestrator-api-poc-0.0.1-SNAPSHOT.jar
else
    echo "❌ Error en la compilación"
    exit 1
fi
