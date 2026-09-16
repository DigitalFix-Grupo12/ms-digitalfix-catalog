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