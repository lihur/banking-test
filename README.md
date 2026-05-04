# Banking API — Setup Guide

## Overview

REST API for bank account management supporting deposits, debits, balance retrieval and currency exchange.
Built with Spring Boot 4, PostgreSQL, Flyway, and JWT (RS256) authentication.

---

## Prerequisites

- Java 17+
- PostgreSQL (any recent version)
- Gradle (or use the included `./gradlew` wrapper)

---

## 1. Generate an RSA Keypair

The API uses RS256 JWT authentication. An RSA keypair is required — the public key goes into the application config, the private key is used to sign tokens.

### Linux / macOS

```bash
openssl genrsa -out private.pem 2048
openssl rsa -in private.pem -pubout -out public.pem
```

### Windows

```powershell
openssl genrsa -out private.pem 2048
openssl rsa -in private.pem -pubout -out public.pem
```

---

## 2. Extract the Public Key Value

The application expects the public key as a Base64-encoded string (the raw content of the PEM file, without the header/footer lines).

### Linux / macOS

```bash
grep -v "PUBLIC KEY" public.pem | tr -d '\n'
```

### Windows

```powershell
(Get-Content public.pem | Where-Object { $_ -notmatch "PUBLIC KEY" }) -join ""
```

Copy the resulting single-line string — you will need it in the next step.

---

## 3. Configure the Application

The application requires a database connection and the public key to be configured. You can provide these in any way Spring Boot supports — for example by creating an `application-local.properties` file in `src/main/resources` (already set as the active profile), by setting environment variables, or by any other standard Spring Boot externalized configuration method.

The following properties must be set:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password

app.jwt.public-key=<your Base64 public key from step 2>
```

Example using `application-local.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/banking
spring.datasource.username=postgres
spring.datasource.password=secret

app.jwt.public-key=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA...
```

---

## 4. Run the Application

```bash
./gradlew bootRun
```

Flyway will automatically create the schema and insert test data on first run.

**Test accounts seeded by Flyway:**

| IBAN | User code |
| --- | --- |
| EE471000001020145685 | USER0001 |
| EE382200221020145685 | USER0002 |

---

## 5. Generate a JWT Token

The API requires a Bearer JWT token signed with your private key. The token must contain:

| Claim | Description | Example |
| --- | --- | --- |
| `sub` | User code of the account owner | `USER0001` |
| `exp` | Expiration timestamp (Unix time) | any future timestamp |

**Using [jwt.io](https://jwt.io/):**

1. Open [https://jwt.io](https://jwt.io/)
2. Select algorithm **RS256**
3. Set the payload:

  ```json
  {
  "sub": "USER0001",
  "exp": 9999999999
  }
  ```

4. Paste the contents of `private.pem` into the **Private Key** field
5. Copy the generated token from the **Encoded** panel

Use the token in all API requests as a header:

```
Authorization: Bearer <your token>
```

---

## 6. Explore the API

Swagger UI is available at:

```
http://localhost:8080/swagger-ui
```

Click **Authorize** in the top right and enter your Bearer token to authenticate requests directly from the UI.

---

## Notes

### External Logging

The assignment specified a call to an external system before each debit operation. The originally suggested URL (`https://httpstat.us/`) was offline at the time of development. As a replacement, `https://httpbin.org/post` is used — it accepts POST requests and echoes them back, serving the same purpose of simulating an external system call.

The external logging URL can be overridden via the `app.external-logging.url` property if needed.

### External Logging Behaviour

A failed call to the external logging service will cause the debit request to fail. In a banking context, audit logging is treated as a hard requirement — a transaction that cannot be logged should not be processed.
