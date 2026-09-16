# ms-digitalfix-catalog (puerto 8083)

Catalogo de servicios tecnicos y repuestos (JPA + H2, datos semilla).

| Metodo | Ruta | Descripcion |
|---|---|---|
| GET | /api/catalog/services?tipo=SERVICIO\|REPUESTO | Lista |
| GET | /api/catalog/services/{id} | Detalle |
## Arquitectura

```
Angular (MSAL) -> AWS API Gateway -> ms-digitalfix-bff :8080 (valida JWT Entra ID + rol)
                                         |-> ms-digitalfix-workorders :8082 -> audit
                                         |-> ms-digitalfix-catalog    :8083
                                         |-> ms-digitalfix-report     :8084 -> workorders
                                         |-> ms-digitalfix-audit      :8085
```

Los microservicios de dominio **no validan JWT**: solo escuchan en la red interna
del host (el Security Group expone unicamente el 8080 del BFF). El BFF propaga la
identidad del usuario en el header `X-User-Name`.

## Ejecutar

```
mvn clean package
java -jar target/ms-digitalfix-catalog-0.0.1-SNAPSHOT.jar
```

Health: `GET /actuator/health`. Base de datos: H2 en memoria (se reinicia con el servicio).

## Perfiles de base de datos

| Perfil | Base de datos | Uso |
|---|---|---|
| (por defecto) | H2 en memoria | Desarrollo local y tests |
| `cloud` | Amazon RDS PostgreSQL, schema `catalog` | EC2 (`SPRING_PROFILES_ACTIVE=cloud`) |

Variables del perfil `cloud`: `DB_HOST`, `DB_PORT` (5432), `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`. En la EC2 se obtienen de SSM Parameter Store; nunca se guardan en el repo. El schema se crea al arrancar (`hibernate.hbm2ddl.create_namespaces`).

## Ejemplos

Directo al microservicio (local, sin token):

```bash
# catálogo completo (7 ítems)
curl http://localhost:8083/api/catalog/services

# solo repuestos (el filtro no distingue mayúsculas)
curl "http://localhost:8083/api/catalog/services?tipo=repuesto"

# detalle de un ítem; un id inexistente devuelve 404
curl http://localhost:8083/api/catalog/services/1
```

Respuesta de ejemplo:

```json
{ "id": 1, "nombre": "Cambio de tablero eléctrico", "tipo": "SERVICIO", "stock": 12, "tarifa": 45000 }
```

A través del API Gateway (roles Admin o Supervisor):

```bash
curl -H "Authorization: Bearer $TOKEN" \
  https://7s6qn2mb8h.execute-api.us-east-1.amazonaws.com/api/catalog/services?tipo=SERVICIO
```

Sin token el gateway responde 401; con un rol sin permiso (Cliente, Auditor) el BFF responde 403.
