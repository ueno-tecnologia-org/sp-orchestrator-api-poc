# Ejemplos de Requests para el POC

## Configuración de Postman Collection

### 1. Obtener Datos de Usuario (con cache)
```json
{
  "name": "Get User Data",
  "request": {
    "method": "POST",
    "url": "{{baseUrl}}/api/v1/stored-procedures/execute",
    "header": [
      {
        "key": "Content-Type",
        "value": "application/json"
      }
    ],
    "body": {
      "mode": "raw",
      "raw": "{\n  \"procedureName\": \"getUserData\",\n  \"parameters\": {\n    \"userId\": \"12345\"\n  },\n  \"timeoutSeconds\": 30\n}"
    }
  }
}
```

### 2. Consultar Balance (simulación de errores)
```json
{
  "name": "Get Account Balance",
  "request": {
    "method": "POST",
    "url": "{{baseUrl}}/api/v1/stored-procedures/execute",
    "header": [
      {
        "key": "Content-Type",
        "value": "application/json"
      }
    ],
    "body": {
      "mode": "raw",
      "raw": "{\n  \"procedureName\": \"getAccountBalance\",\n  \"parameters\": {\n    \"accountId\": \"ACC-789\"\n  },\n  \"timeoutSeconds\": 30\n}"
    }
  }
}
```

### 3. Procesar Transacción (sin cache)
```json
{
  "name": "Process Transaction",
  "request": {
    "method": "POST",
    "url": "{{baseUrl}}/api/v1/stored-procedures/execute",
    "header": [
      {
        "key": "Content-Type",
        "value": "application/json"
      }
    ],
    "body": {
      "mode": "raw",
      "raw": "{\n  \"procedureName\": \"processTransaction\",\n  \"parameters\": {\n    \"transactionId\": \"TXN-001\",\n    \"amount\": 100.50\n  },\n  \"timeoutSeconds\": 30\n}"
    }
  }
}
```

## Pruebas de Resiliencia

### Probar Circuit Breaker
1. Ejecuta múltiples veces `getAccountBalance` para activar fallos simulados
2. Observa cómo el circuit breaker se abre después de alcanzar el threshold
3. Verifica que las llamadas posteriores fallan rápidamente

### Probar Rate Limiting
1. Ejecuta múltiples requests rápidos a `processTransaction` (limit: 5/seg)
2. Observa respuestas 429 (Too Many Requests)

### Probar Cache
1. Ejecuta `getUserData` dos veces seguidas
2. La segunda llamada debería ser más rápida (cache hit)
3. Verifica estadísticas en `/procedures/getUserData/stats`

## Variables de Entorno para Postman
```json
{
  "baseUrl": "http://localhost:8080/sp-orchestrator"
}
```

## Scripts de Testing con curl

### Script para probar rate limiting
```bash
#!/bin/bash
echo "Probando rate limiting en processTransaction (5 req/seg)..."
for i in {1..10}; do
  echo "Request $i:"
  curl -s -w "Status: %{http_code}\n" \
    -X POST "http://localhost:8080/sp-orchestrator/api/v1/stored-procedures/execute" \
    -H "Content-Type: application/json" \
    -d "{\"procedureName\": \"processTransaction\", \"parameters\": {\"transactionId\": \"TXN-$i\", \"amount\": 100}}"
  sleep 0.1
done
```

### Script para probar cache
```bash
#!/bin/bash
echo "Probando cache en getUserData..."
echo "Primera llamada (cache miss):"
time curl -s "http://localhost:8080/sp-orchestrator/api/v1/stored-procedures/execute" \
  -H "Content-Type: application/json" \
  -d '{"procedureName": "getUserData", "parameters": {"userId": "cache-test"}}'

echo -e "\nSegunda llamada (cache hit):"
time curl -s "http://localhost:8080/sp-orchestrator/api/v1/stored-procedures/execute" \
  -H "Content-Type: application/json" \
  -d '{"procedureName": "getUserData", "parameters": {"userId": "cache-test"}}'
```
