# API Fintech

API REST que simula un sistema bancario simple: gestiona **usuarios** y **cuentas** (de ahorro y corrientes). Trabajo práctico de la Clase 14 de la Diplomatura en Desarrollo de Software FinTech (UTN BA).

## Tecnologías

- Java 21
- Spring Boot 4.1.1 (Spring Web, Spring Data JPA, Validation)
- Maven
- MySQL 8.4 (en Docker)
- springdoc-openapi 3.0.3 (Swagger UI)
- JUnit 5 y Mockito para los tests
- Lombok

## Modelo de dominio

- `Account`: clase abstracta con id, número de cuenta, saldo (`BigDecimal`), fecha de creación y titular.
- `SavingsAccount` y `CheckingAccount`: subclases de `Account`.
- `Operable`: interfaz con `deposit` y `withdraw`.
- `AccountType`: enum con los tipos de cuenta (`SAVINGS`, `CHECKING`).
- `User`: titular de las cuentas. Un usuario puede tener muchas cuentas (relación 1 a N).

**Polimorfismo:** el método `withdraw` se comporta distinto según el tipo de cuenta. La cuenta de ahorro rechaza el retiro si el saldo no alcanza, y la cuenta corriente permite quedar en descubierto hasta un límite de 500.

## Arquitectura

Arquitectura por capas dentro de `com.mikko.apifintech`:

- `controller`: endpoints REST
- `service`: lógica de negocio
- `repository`: interfaces de Spring Data JPA
- `model`: entidades, interfaz y enum
- `dto`: records de entrada y salida con validaciones
- `mapper`: conversión entre entidades y DTO
- `exception`: excepciones personalizadas y `GlobalExceptionHandler`

## Cómo ejecutarlo

Requisitos: Java 21 y Docker.

```bash
# 1. Levantar MySQL
docker-compose up -d

# 2. Iniciar la aplicación
./mvnw spring-boot:run
```

La API queda en `http://localhost:8080`. Las tablas se crean solas (`ddl-auto=update`).

## Documentación

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI (JSON):** http://localhost:8080/v3/api-docs
- **Colección de Postman:** `postman/fintech-api.postman_collection.json` (importala en Postman y definí la variable `baseUrl` como `http://localhost:8080`).

## Endpoints

### Usuarios

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | /usuarios | Crea un usuario |
| GET | /usuarios | Lista todos los usuarios |
| GET | /usuarios/{id} | Obtiene un usuario por id |

### Cuentas

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | /cuentas | Lista todas las cuentas |
| POST | /cuentas | Crea una cuenta (SAVINGS o CHECKING) para un usuario |
| GET | /cuentas/{id} | Obtiene una cuenta por id |
| PUT | /cuentas/{id} | Actualiza el número de cuenta |
| DELETE | /cuentas/{id} | Elimina una cuenta |
| POST | /cuentas/{id}/depositos | Deposita dinero |
| POST | /cuentas/{id}/retiros | Retira dinero |

## Manejo de errores

Las excepciones personalizadas se traducen a respuestas HTTP en `GlobalExceptionHandler`, que devuelve un `ErrorResponse` uniforme (fecha, código, error, mensaje y ruta):

| Excepción | Código HTTP |
|-----------|-------------|
| UserNotFoundException / AccountNotFoundException | 404 |
| DuplicateEmailException / DuplicateAccountNumberException | 409 |
| InsufficientFundsException | 422 |
| Errores de validación, JSON inválido o argumento ilegal | 400 |

## Tests

```bash
./mvnw test
```

Son 20 tests: tests unitarios de `AccountService` y `UserService` con Mockito, tests de dominio puro del polimorfismo y un test de carga del contexto de Spring.

## Decisiones técnicas

- **SINGLE_TABLE para la herencia:** todas las cuentas viven en una sola tabla, `accounts`, con una columna discriminadora `account_type`. Es más simple y evita joins. El costo es que algunas columnas, como `overdraft_limit`, quedan nulas en las cuentas de ahorro.
- **BigDecimal para el dinero:** evita los errores de redondeo de `double`.
- **DTO como records:** separan la API de las entidades y concentran las validaciones.
- **GlobalExceptionHandler:** centraliza el manejo de errores y devuelve una respuesta uniforme.
- **Docker Compose para MySQL:** cualquiera levanta la base con un solo comando, sin instalarla.