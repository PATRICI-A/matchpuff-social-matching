<div align="center">

# Matching Service — (M02 — Matching Social)

### *"Calcula compatibilidad, conecta estudiantes, construye comunidad"*

---

### Stack Tecnológico

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-47A248?style=for-the-badge&logo=mongodb&logoColor=white)

### Infraestructura & Calidad

![Docker](https://img.shields.io/badge/Docker-Multi--stage-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-CloudAMQP-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![JaCoCo](https://img.shields.io/badge/JaCoCo-%E2%89%A580%25-brightgreen?style=for-the-badge)

### Arquitectura

![Hexagonal](https://img.shields.io/badge/Architecture-Hexagonal-blueviolet?style=for-the-badge)
![REST API](https://img.shields.io/badge/REST-API-009688?style=for-the-badge)
![JWT](https://img.shields.io/badge/Auth-JWT%20HMAC--SHA256-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

</div>

---

## Tabla de Contenidos

1. [Integrantes](#1-integrantes)
2. [Tecnologías Utilizadas](#2-tecnologías-utilizadas)
3. [Descripción del Microservicio](#3-descripción-del-microservicio)
4. [Cómo Funciona](#4-cómo-funciona)
5. [Diagrama de Datos](#5-diagrama-de-datos)
6. [Diagrama de Clases](#6-diagrama-de-clases)
7. [Diagrama de Componentes](#7-diagrama-de-componentes)
8. [Funcionalidades Principales](#8-funcionalidades-principales)
9. [Endpoints](#9-endpoints)
10. [Colas de Mensajería](#10-colas-de-mensajería)
11. [Evidencia de Pruebas](#11-evidencia-de-pruebas)
12. [Evidencia de Cobertura](#12-evidencia-de-cobertura)
13. [Cómo Ejecutar](#13-cómo-ejecutar)
14. [Evidencia CI/CD](#14-evidencia-cicd)
15. [Link Swagger](#15-link-swagger)
16. [Estructura del Código](#16-estructura-del-código)
17. [Código Documentado](#17-código-documentado)
18. [Conexiones Externas](#18-conexiones-externas)
19. [Pipeline de Desarrollo](#19-pipeline-de-desarrollo)
20. [Pipeline de Producción](#20-pipeline-de-producción)
21. [Dockerizado](#21-dockerizado)
22. [Versionamiento](#22-versionamiento)

---

## 1. Integrantes

- Javier Mauricio Romero Deaquiz
- Mariana Malagón
- Andrés Cardozo Martinez
- Jeimmy Vanessa Torres Marín

---

## 2. Tecnologías Utilizadas

| **Tecnología / Herramienta** | **Uso principal en el proyecto** |
|---|---|
| **Java 21 (OpenJDK)** | Lenguaje base con Records, Pattern Matching y Virtual Threads. LTS hasta 2029. |
| **Spring Boot 3.4.5** | Framework principal. Auto-configuración, actuator, métricas y seguridad integrados. |
| **Spring Web** | Exposición de endpoints REST bajo `/api/v1/matches/**` y `/api/v1/categories/**`. |
| **Spring Security** | Autenticación y autorización — validación JWT local por microservicio sin llamada HTTP a M01. |
| **Spring Data MongoDB** | Abstracción del repositorio para las colecciones `matches`, `categories` y `tags`. |
| **Spring Cloud OpenFeign** | Cliente HTTP declarativo para comunicación interna con M01 Profile Service. `ProfileFeignClient` consume `/api/v1/internal/matching/profiles/{id}`. |
| **MapStruct 1.5.5** | Generación automática de mappers entre capas (domain ↔ entity, domain ↔ DTO). |
| **SpringDoc OpenAPI 2.8.6** | Generación automática de Swagger UI y contrato OpenAPI 3 desde anotaciones. |
| **Lombok** | Reducción de código boilerplate (`@Builder`, `@Data`, `@Slf4j`). |
| **JUnit 5** | Framework de pruebas unitarias. |
| **Mockito** | Simulación de dependencias en pruebas sin infraestructura real. |
| **JaCoCo** | Cobertura de código en pruebas unitarias. Mínimo 80% exigido en pipeline CI. |
| **MongoDB Atlas** | Base de datos principal — colecciones `matches`, `categories` y `tags`. |
| **RabbitMQ (CloudAMQP)** | Broker para eventos de match creado (`match.received`) y match respondido (`match.response`). |
| **Docker** | Contenedorización con imagen base `eclipse-temurin:21-jre-alpine`. |
| **GitHub Actions** | Pipelines CI (build & test + JaCoCo) y CD (deploy en Azure Container Instances). |

---

## 3. Descripción del Microservicio

El Matching Service (M02) es el componente de PATRICI.A responsable de calcular la compatibilidad entre perfiles de estudiantes y gestionar el ciclo de vida de los matches en la plataforma. Sus responsabilidades principales son:

- **Cálculo de afinidad:** recibe dos identificadores de usuario, recupera sus perfiles desde M01 vía Feign, calcula un `AffinityScore` compuesto y persiste el resultado como `Match` en MongoDB.
- **Gestión de matches:** crea solicitudes, acepta o rechaza matches y consulta el historial por usuario.
- **Recomendaciones:** ordena candidatos por score de afinidad descendente; opcionalmente filtra por proximidad geográfica consultando el Geolocation Service.
- **Catálogo de categorías y tags:** CRUD completo de intereses para alimentar el algoritmo de afinidad.

**Puerto:** 8080 | **Base de datos:** MongoDB (colecciones `matches`, `categories`, `tags`)

### Algoritmo de Afinidad

```
totalScore = 0.60 × interestScore + 0.40 × scheduleScore

interestScore:
  30% → similaridad de categorías (Jaccard)
  50% → similaridad de nombres de tags (Jaccard)
  10% → coincidencia de género

scheduleScore:
  índice de Jaccard sobre slots discretos de 30 min por día de la semana
```

| RF | Nombre |
|---|---|
| RF-M01 | Crear solicitud de match entre dos usuarios |
| RF-M02 | Aceptar o rechazar match existente |
| RF-M03 | Recomendaciones por score de afinidad |
| RF-M04 | Recomendaciones filtradas por proximidad geográfica |
| RF-M05 | Recomendaciones con puntaje desglosado |
| RF-M06 | Consultas de matches por usuario |
| RF-M07 | CRUD de categorías y tags de interés |

---

## 4. Cómo Funciona

### Arquitectura Hexagonal (Ports & Adapters)

```
┌─────────────────────────────────────────────────────┐
│                  EXTERIOR                           │
│  ┌──────────────┐         ┌──────────────────────┐  │
│  │  Controllers │         │  MongoDB Adapters    │  │
│  │  (REST)      │         │  ProfileFeignClient  │  │
│  │  Port In ──► │         │  RabbitMQ Publisher  │  │
│  └──────┬───────┘         └────────────┬─────────┘  │
│         │          DOMINIO             │ ◄ Port Out  │
│         ▼   ┌────────────────────┐    │             │
│         └──►│  Application       │◄───┘             │
│             │  Use Cases         │                   │
│             └────────────────────┘                   │
└─────────────────────────────────────────────────────┘
```

**Flujo de dependencias:** `Entrypoints / Infrastructure → Application → Domain`

### Patrones de Diseño Utilizados

| Patrón | Ubicación | Descripción |
|---|---|---|
| **Ports & Adapters** | Toda la arquitectura | 3 puertos de entrada (`MatchUseCasePort`, `CategoryUseCasePort`, `RecommendationsUseCasePort`) + 4 de salida (`MatchRepositoryPort`, `CategoryRepositoryPort`, `ProfileServicePort`, `GeolocationServicePort`). |
| **Strategy** | `AffinityCalculatorService` / `AffinityCalculatorServiceImpl` | Lógica de cálculo de afinidad encapsulada detrás de una interfaz intercambiable. Sustituible sin modificar los casos de uso. |
| **Adapter** | `MatchRepositoryAdapter`, `CategoryRepository`, `ProfileServiceAdapter` | Adaptan Spring Data MongoDB y Feign a los puertos de dominio. |
| **Builder** | Todas las entidades y documentos | Lombok `@Builder` + MapStruct para mapeos entre capas. |

### Conexión con Otros Módulos

| Módulo | Protocolo | Dirección | Dato |
|---|---|---|---|
| M01 — Profile Service | HTTP REST via OpenFeign | M02 → M01 | Recupera perfiles completos (`UserMatchProfile`) via `/api/v1/internal/matching/profiles/{id}`. |
| M07 — Geolocation Service | HTTP REST via Feign | M02 → M07 | Consulta usuarios cercanos para recomendaciones geográficas. |
| Notification Service | RabbitMQ (publicación) | M02 → RabbitMQ | Eventos `match.received` y `match.response` en `matching.exchange`. |
| M01 — Auth | JWT (validación local) | M01 → M02 | M02 valida el JWT localmente con clave HMAC-SHA256 compartida. |

---

## 5. Diagrama de Datos

<div align="center">
<img src="docs/diagrama_datos.jpg" alt="Diagrama de Datos" width="600"/>
</div>

El módulo utiliza tres colecciones en MongoDB. El `AffinityScore` se almacena embebido dentro del documento `MatchDocument`, evitando una colección separada y permitiendo consultas O(1) sobre el score sin JOINs. Los campos `requesterId` y `targetId` son referencias UUID a documentos del Profile Service (M01) — el módulo no accede directamente a la colección `users` de M01.

### Colección: `matches`

| Campo | Tipo | Descripción | Restricciones |
|---|---|---|---|
| `idMatch` | UUID | Identificador único del match | PK |
| `requesterId` | UUID | ID del usuario que envía la solicitud | NOT NULL |
| `targetId` | UUID | ID del usuario que recibe la solicitud | NOT NULL |
| `status` | EnumStatus | Estado del match | NOT NULL: `PENDING`, `ACCEPTED`, `REJECTED` |
| `affinityScore` | AffinityScore (embebido) | Score compuesto `{total, interestsScore, academicScore, scheduleScore}` | NOT NULL |
| `createdAt` | Date | Fecha de creación | NOT NULL |
| `updatedAt` | Date | Fecha de última actualización | NOT NULL |

### Colección: `user_match_profiles` (caché interna)

| Campo | Tipo | Descripción |
|---|---|---|
| `id_` | UUID | ID del estudiante |
| `academicData` | AcademicData (embebido) | `{program: String, semester: Integer}` |
| `interests` | Array\<String\> | Lista de intereses/tags |
| `status` | EnumStatus | Estado del perfil |
| `availability` | Array\<String\> | Franjas de disponibilidad horaria |
| `lastSync` | Date | Última sincronización con M01 |

### Colecciones: `categories` y `tags`

Mantienen su propio catálogo para garantizar el bajo acoplamiento con M01.

---

## 6. Diagrama de Clases

<div align="center">
<img src="docs/diagrama_clases.jpg" alt="Diagrama de Clases" width="600"/>
</div>

**Resumen del diseño de dominio:**

- **`Match`** — entidad central: `idMatch`, `requesterId`, `targetId`, `status`, `affinityScore`, `createdAt`, `updatedAt`.
- **`AffinityScore`** — value object embebido: `total`, `interestsScore`, `scheduleScore`. Inmutable.
- **`UserMatchProfile`** — perfil reducido del estudiante para el cálculo: `id`, `program`, `semester`, `interests`, `availability`, `lastSync`.
- **`MatchStatus`** — enum: `REJECTED`, `ACCEPTED`, `PENDING`.

### Clases principales del dominio

| Clase | Tipo | Responsabilidad |
|---|---|---|
| `Match` | Model | Entidad de dominio — ciclo de vida completo del match |
| `AffinityScore` | Value Object | Score compuesto embebido con `total`, `interestsScore`, `scheduleScore` |
| `UserMatchProfile` | Model | Perfil reducido del estudiante para cálculo de afinidad |
| `MatchUseCasePort` | Port In | Contrato del caso de uso de matches |
| `CategoryUseCasePort` | Port In | Contrato del caso de uso de categorías |
| `RecommendationsUseCasePort` | Port In | Contrato del caso de uso de recomendaciones |
| `MatchRepositoryPort` | Port Out | Contrato de persistencia de matches |
| `CategoryRepositoryPort` | Port Out | Contrato de persistencia de categorías y tags |
| `ProfileServicePort` | Port Out | Contrato para obtener perfiles desde M01 |
| `GeolocationServicePort` | Port Out | Contrato para consultar usuarios cercanos |
| `AffinityCalculatorService` | Domain Service | Interfaz intercambiable del algoritmo de afinidad |
| `AffinityCalculatorServiceImpl` | Domain Service | Implementación ponderada: 60% interés + 40% horario |

---

## 7. Diagrama de Componentes

<div align="center">
<img src="docs/diagrama_componentes.jpg" alt="Diagrama de Componentes" width="600"/>
</div>

El diagrama expone tres casos de uso independientes: `MatchingUseCase` orquesta el flujo principal (obtener perfiles → calcular afinidad → persistir match), `RecommendationsUseCase` gestiona las recomendaciones personalizadas y `CategoryUseCase` administra el catálogo de intereses. La conexión clave hacia el exterior se da a través de `ProfileServiceAdapter`, que implementa `ProfileServicePort` invocando a M01 vía Feign.

| Componente | Tipo | Interfaz |
|---|---|---|
| `MatchController` | REST Controller | `POST /api/v1/matches`, `PATCH /api/v1/matches/{id}/status`, `GET /api/v1/matches/user/{userId}`, `GET /api/v1/matches/{id}` |
| `CategoryController` | REST Controller | `POST /api/v1/categories`, `GET /api/v1/categories`, `GET /api/v1/categories/{id}`, `PUT /api/v1/categories/{id}`, `DELETE /api/v1/categories/{id}` |
| `MatchingUseCase` | Application Service | Puerto In: `MatchUseCasePort` |
| `RecommendationsUseCase` | Application Service | Puerto In: `RecommendationsUseCasePort` |
| `CategoryUseCase` | Application Service | Puerto In: `CategoryUseCasePort` |
| `AffinityCalculatorServiceImpl` | Domain Service | Algoritmo ponderado sin dependencias de Spring ni MongoDB |
| `MatchRepositoryAdapter` | Driven Adapter | Puerto Out: `MatchRepositoryPort` → MongoDB |
| `CategoryRepository` | Driven Adapter | Puerto Out: `CategoryRepositoryPort` → MongoDB |
| `ProfileServiceAdapter` | Driven Adapter | Puerto Out: `ProfileServicePort` → Feign → M01 |
| `GeolocationServiceAdapter` | Driven Adapter | Puerto Out: `GeolocationServicePort` → Feign → M07 |
| `RabbitMQFriendshipPublisher` | Driven Adapter | Publica eventos `match.received` y `match.response` |
| `GlobalExceptionHandler` | Exception Handler | Mapeo centralizado de excepciones a códigos HTTP |

---

## 8. Funcionalidades Principales

<div align="center">

| ID | RF | Funcionalidad | Descripción |
|---|---|---|---|
| F01 | RF-M01 | **Crear solicitud de match** | Llama al Profile Service para obtener ambos perfiles, calcula el AffinityScore (60% interés + 40% horario) y persiste el match en PENDING. Publica evento `match.received` en RabbitMQ. |
| F02 | RF-M02 | **Aceptar o rechazar match** | Actualiza el estado del match a ACCEPTED o REJECTED. Si se acepta, notifica al Profile Service para registrar la amistad y publica evento `match.response`. |
| F03 | RF-M03 | **Recomendaciones por afinidad** | Recupera todos los perfiles desde M01 y calcula la afinidad para cada candidato, ordenando de mayor a menor score. |
| F04 | RF-M04 | **Recomendaciones cercanas** | Consulta el Geolocation Service para obtener usuarios dentro del radio indicado y luego calcula la afinidad de cada uno, retornando `userId`, score y distancia en metros. |
| F05 | RF-M05 | **Recomendaciones con puntaje desglosado** | Igual que F03 pero retorna el score desglosado: `interestsScore`, `scheduleScore` y `academicScore` además del `totalScore`. |
| F06 | RF-M06 | **Consultas de matches por usuario** | Cuatro endpoints: todos los matches, solo los enviados, solo los recibidos y búsqueda por ID. Acceso directo a MongoDB sin lógica externa. |
| F07 | RF-M07 | **Gestión de categorías** | CRUD completo: crear, obtener por ID, listar, actualizar y eliminar. Incluye crear categoría con sus tags en una sola petición. |

</div>

---

## 9. Endpoints

### Resumen

| Método | Endpoint | Funcionalidad | Auth requerida | Código exitoso |
|---|---|---|---|---|
| `POST` | `/api/v1/matches` | F01 — Crear match | Bearer JWT | 201 |
| `PATCH` | `/api/v1/matches/{id}/status` | F02 — Aceptar/rechazar match | Bearer JWT | 200 |
| `GET` | `/api/v1/matches/recommendations/{userId}` | F03 — Recomendaciones por afinidad | Bearer JWT | 200 |
| `GET` | `/api/v1/matches/recommendations/{userId}/nearby` | F04 — Recomendaciones cercanas | Bearer JWT | 200 |
| `GET` | `/api/v1/matches/recommendations/{userId}/scores` | F05 — Recomendaciones desglosadas | Bearer JWT | 200 |
| `GET` | `/api/v1/matches/user/{userId}` | F06 — Todos los matches del usuario | Bearer JWT | 200 |
| `GET` | `/api/v1/matches/user/{userId}/sent` | F06 — Matches enviados | Bearer JWT | 200 |
| `GET` | `/api/v1/matches/user/{userId}/received` | F06 — Matches recibidos | Bearer JWT | 200 |
| `GET` | `/api/v1/matches/{id}` | F06 — Match por ID | Bearer JWT | 200 |
| `POST` | `/api/v1/categories` | F07 — Crear categoría | Bearer JWT | 201 |
| `POST` | `/api/v1/categories/with-tags` | F07 — Crear categoría con tags | Bearer JWT | 201 |
| `GET` | `/api/v1/categories` | F07 — Listar categorías | Bearer JWT | 200 |
| `GET` | `/api/v1/categories/{id}` | F07 — Obtener categoría | Bearer JWT | 200 |
| `PUT` | `/api/v1/categories/{id}` | F07 — Actualizar categoría | Bearer JWT | 200 |
| `DELETE` | `/api/v1/categories/{id}` | F07 — Eliminar categoría | Bearer JWT | 204 |

---

### POST /api/v1/matches — Crear Solicitud de Match

<div align="center">
<img src="docs/ds_crear_aceptar.jpg" alt="Diagrama de Secuencia - Crear Match" width="600"/>
</div>

**Request:**
```
POST /api/v1/matches
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

| Campo | Tipo | Origen | Obligatorio | Descripción |
|---|---|---|---|---|
| `senderId` | UUID | body | Sí | ID del usuario que envía la solicitud |
| `receiverId` | UUID | body | Sí | ID del usuario que recibe la solicitud |

```json
// Request body
{
  "senderId": "550e8400-e29b-41d4-a716-446655440001",
  "receiverId": "550e8400-e29b-41d4-a716-446655440002"
}
```

```json
// Response 201 Created
{
  "matchId": "770e8400-e29b-41d4-a716-446655440000",
  "status": "PENDING",
  "affinityScore": {
    "total": 0.74,
    "interestsScore": 0.68,
    "scheduleScore": 0.83
  }
}
```

**Errores:**

| HTTP | Escenario | Código de error |
|:---:|---|---|
| 409 | Match ya existe entre esos usuarios | `DUPLICATE_MATCH` |
| 404 | Uno de los perfiles no existe en M01 | `PROFILE_NOT_FOUND` |
| 401 | JWT inválido o ausente | `TOKEN_INVALID` |

---

### PATCH /api/v1/matches/{id}/status — Aceptar o Rechazar Match

**Request:**
```
PATCH /api/v1/matches/770e8400-.../status
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

```json
// Request body
{ "status": "ACCEPTED" }
```

```json
// Response 200 OK
{
  "matchId": "770e8400-...",
  "status": "ACCEPTED",
  "affinityScore": { "total": 0.74 }
}
```

**Errores:**

| HTTP | Escenario | Código de error |
|:---:|---|---|
| 404 | Match no encontrado | `MATCH_NOT_FOUND` |
| 400 | Match no está en estado PENDING | `INVALID_MATCH_STATUS` |
| 401 | JWT inválido o ausente | `TOKEN_INVALID` |

---

### GET /api/v1/matches/recommendations/{userId} — Recomendaciones por Afinidad

<div align="center">
<img src="docs/ds_recomendaciones.jpg" alt="Diagrama de Secuencia - Recomendaciones" width="600"/>
</div>

**Request:**
```
GET /api/v1/matches/recommendations/550e8400-...
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

```json
// Response 200 OK
[
  "user-id-456",
  "user-id-789"
]
```

---

### GET /api/v1/matches/recommendations/{userId}/nearby — Recomendaciones Cercanas

**Request:**
```
GET /api/v1/matches/recommendations/550e8400-.../nearby?maxDistanceMeters=500
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

```json
// Response 200 OK
[
  {
    "userId": "user-id-456",
    "affinityScore": 0.74,
    "distanceMeters": 87.3
  }
]
```

**Errores:**

| HTTP | Escenario | Código de error |
|:---:|---|---|
| 503 | Geolocation Service no disponible | `SERVICE_UNAVAILABLE` |
| 401 | JWT inválido o ausente | `TOKEN_INVALID` |

---

### GET /api/v1/matches/recommendations/{userId}/scores — Puntaje Desglosado

<div align="center">
<img src="docs/ds_scores_consultas.jpg" alt="Diagrama de Secuencia - Scores y Consultas" width="600"/>
</div>

```json
// Response 200 OK
[
  {
    "userId": "user-id-456",
    "interestsScore": 0.68,
    "scheduleScore": 0.83,
    "academicScore": 0.60,
    "totalScore": 0.74
  }
]
```

---

### GET /api/v1/matches/user/{userId} — Consultas de Matches

Agrupa cuatro endpoints de consulta. Todos acceden directamente a MongoDB sin lógica externa.

```json
// Response 200 OK — GET /api/v1/matches/user/{userId}
[
  {
    "matchId": "770e8400-...",
    "sender": "550e8400-...",
    "receiver": "661f9511-...",
    "status": "ACCEPTED",
    "affinityScore": { "total": 0.74 }
  }
]
```

**Errores:**

| HTTP | Escenario | Código de error |
|:---:|---|---|
| 404 | Match no encontrado (solo para `/matches/{id}`) | `MATCH_NOT_FOUND` |
| 401 | JWT inválido o ausente | `TOKEN_INVALID` |

---

### /api/v1/categories — Gestión de Categorías

<div align="center">
<img src="docs/ds_categorias.jpg" alt="Diagrama de Secuencia - Categorías" width="600"/>
</div>

```json
// POST /api/v1/categories — Request body
{ "name": "Programación" }

// Response 201 Created
{ "categoryId": "cat-123", "name": "Programación" }
```

```json
// POST /api/v1/categories/with-tags — Request body
{
  "name": "Música",
  "tags": ["Rock", "Jazz", "Clásica"]
}

// Response 201 Created
{
  "categoryId": "cat-456",
  "name": "Música",
  "tags": ["Rock", "Jazz", "Clásica"]
}
```

**Errores:**

| HTTP | Escenario | Código de error |
|:---:|---|---|
| 409 | Categoría ya existe | `DUPLICATE_CATEGORY` |
| 404 | Categoría no encontrada | `CATEGORY_NOT_FOUND` |
| 401 | JWT inválido o ausente | `TOKEN_INVALID` |

---

## 10. Colas de Mensajería

**Broker utilizado:** RabbitMQ (CloudAMQP)

### Tópicos / Colas que PUBLICA (produce)

| Exchange | Routing Key | Evento | Payload | Cuándo se publica |
|---|---|---|---|---|
| `matching.exchange` (Topic) | `match.received` | Match creado | `{ senderId, receiverId, matchId, affinityScore }` | Al crear un nuevo match exitosamente (F01) |
| `matching.exchange` (Topic) | `match.response` | Match respondido | `{ senderId, receiverId, matchId, status }` | Al aceptar o rechazar un match (F02) |

### Tópicos / Colas que CONSUME (suscribe)

Este módulo **no consume** colas. Solo publica.

### Comportamiento ante fallo de RabbitMQ

Si RabbitMQ no está disponible, el fallo se registra en logs. La respuesta HTTP al cliente no se ve afectada — la persistencia en MongoDB ya fue exitosa.

---

## 11. Evidencia de Pruebas

### Clases de prueba implementadas

```
src/test/java/.../
├── application/
│   ├── MatchingServiceImplTest.java       → Crear match, calcular afinidad, persistir
│   ├── RecommendationsUseCaseImplTest.java → Recomendaciones por afinidad y por cercanía
│   └── CategoryServiceImplTest.java        → CRUD de categorías y manejo de duplicados
├── domain/
│   ├── MatchTest.java                     → Invariantes del modelo Match
│   ├── AffinityScoreTest.java             → Cálculo del score compuesto
│   └── AffinityCalculatorServiceTest.java → Algoritmo Jaccard de intereses y horario
├── entrypoints/rest/controller/
│   ├── MatchControllerTest.java           → Pruebas @WebMvcTest de endpoints de match
│   └── CategoryControllerTest.java        → Pruebas @WebMvcTest de endpoints de categorías
└── entrypoints/advice/
    └── GlobalExceptionHandlerTest.java    → Mapeos de excepción a código HTTP
```

### Cómo ejecutar las pruebas

```bash
# Pruebas unitarias
./mvnw test

# Prueba específica
./mvnw test -Dtest=MatchingServiceImplTest

# Todas las pruebas + reporte JaCoCo
./mvnw verify

# Reporte de cobertura
./mvnw clean test jacoco:report
# → target/site/jacoco/index.html
```

<div align="center">
<img src="docs/evidencia_pruebas.png" alt="Evidencia pruebas unitarias" width="600"/>
</div>

---

## 12. Evidencia de Cobertura

Cobertura mínima configurada: **≥ 80% de instrucciones**.

<div align="center">
<img src="docs/evidencia_cobertura.png" alt="Reporte de cobertura JaCoCo" width="600"/>
</div>

| Métrica | Objetivo | Obtenido |
|---|---|---|
| Cobertura de instrucciones | ≥ 80% | — |
| Cobertura de ramas | ≥ 60% | — |

---

## 13. Cómo Ejecutar

### Prerrequisitos

- Java 21
- Maven 3.9+
- Docker & Docker Compose

### Opción 1: Local con Maven

```bash
# Clonar el repositorio
git clone https://github.com/PATRICI-A/matching-service.git
cd matching-service

# Ejecutar
./mvnw spring-boot:run
```

**URL:** `http://localhost:8080`  
**Swagger UI:** `http://localhost:8080/swagger-ui.html`

### Opción 2: Docker Compose

```bash
# Levantar servicio + MongoDB
docker compose up --build

# Ver logs
docker compose logs -f matching-service

# Detener
docker compose down -v
```

### Variables de Entorno

| Variable | Valor por defecto | Descripción |
|---|---|---|
| `MONGODB_URI` | URI en `application.yml` | URI de conexión a MongoDB |
| `JWT_SECRET` | `dev-secret-key-must-be-at-least-32-characters` | Secreto compartido con M01 para validar JWT |
| `PROFILE_SERVICE_URL` | `http://localhost:8086` | URL base del Profile Service (M01) |
| `GEO_SERVICE_URL` | `http://localhost:8084` | URL base del Geolocation Service (M07) |
| `RABBITMQ_HOST` | `woodpecker.rmq.cloudamqp.com` | Host de RabbitMQ |
| `RABBITMQ_PORT` | `5671` | Puerto AMQP con SSL |
| `RABBITMQ_USERNAME` | _(secreto)_ | Usuario CloudAMQP |
| `RABBITMQ_PASSWORD` | _(secreto)_ | Contraseña CloudAMQP |
| `PORT` | `8080` | Puerto del servidor |

---

## 14. Evidencia CI/CD

El pipeline `.github/workflows/ci.yml` corre en cada push a `develop` y en PRs:

<div align="center">
<img src="docs/evidencia_cicd.png" alt="Pipeline CI/CD GitHub Actions" width="600"/>
</div>

---

## 15. Link Swagger

| Ambiente | URL |
|---|---|
| **Producción (Azure)** | *(URL del Azure Container Instance)* |
| **Local** | http://localhost:8080/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs |

> Usar **Bearer JWT** en el botón "Authorize" de Swagger UI para probar todos los endpoints.

---

## 16. Estructura del Código

```
matching-service/
├── src/
│   ├── main/
│   │   ├── java/.../matching/
│   │   │   ├── domain/                              # CAPA DE DOMINIO
│   │   │   │   ├── model/
│   │   │   │   │   ├── Match.java
│   │   │   │   │   ├── AffinityScore.java           # Value Object embebido
│   │   │   │   │   ├── UserMatchProfile.java
│   │   │   │   │   ├── Category.java
│   │   │   │   │   └── enums/
│   │   │   │   │       └── MatchStatus.java         # PENDING, ACCEPTED, REJECTED
│   │   │   │   ├── ports/
│   │   │   │   │   ├── in/
│   │   │   │   │   │   ├── MatchUseCasePort.java
│   │   │   │   │   │   ├── CategoryUseCasePort.java
│   │   │   │   │   │   └── RecommendationsUseCasePort.java
│   │   │   │   │   └── out/
│   │   │   │   │       ├── MatchRepositoryPort.java
│   │   │   │   │       ├── CategoryRepositoryPort.java
│   │   │   │   │       ├── ProfileServicePort.java
│   │   │   │   │       └── GeolocationServicePort.java
│   │   │   │   ├── exceptions/
│   │   │   │   │   ├── MatchNotFoundException.java
│   │   │   │   │   ├── DuplicateMatchException.java
│   │   │   │   │   ├── InvalidMatchStatusException.java
│   │   │   │   │   ├── DuplicateCategoryException.java
│   │   │   │   │   └── CategoryNotFoundException.java
│   │   │   │   └── service/
│   │   │   │       ├── AffinityCalculatorService.java     # Interfaz Strategy
│   │   │   │       └── AffinityCalculatorServiceImpl.java # 60% interés + 40% horario
│   │   │   │
│   │   │   ├── application/                         # CAPA DE APLICACIÓN
│   │   │   │   ├── usecase/
│   │   │   │   │   ├── MatchingServiceImpl.java
│   │   │   │   │   ├── RecommendationsUseCaseImpl.java
│   │   │   │   │   └── CategoryServiceImpl.java
│   │   │   │   ├── mapper/
│   │   │   │   │   ├── MatchApplicationMapper.java
│   │   │   │   │   └── CategoryAppMapper.java
│   │   │   │   └── dto/
│   │   │   │       ├── request/
│   │   │   │       └── response/
│   │   │   │
│   │   │   ├── infrastructure/                      # CAPA DE INFRAESTRUCTURA
│   │   │   │   ├── adapters/
│   │   │   │   │   ├── MatchRepositoryAdapter.java
│   │   │   │   │   ├── CategoryRepository.java      # Adapta Mongo + Tags
│   │   │   │   │   ├── ProfileServiceAdapter.java   # Feign → ProfileServicePort
│   │   │   │   │   └── GeolocationServiceAdapter.java
│   │   │   │   ├── persistence/
│   │   │   │   │   ├── entity/
│   │   │   │   │   │   ├── MatchDocument.java
│   │   │   │   │   │   ├── MatchPersistenceMapper.java
│   │   │   │   │   │   ├── CategoryDocument.java
│   │   │   │   │   │   └── TagDocument.java
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── MatchMongoRepository.java
│   │   │   │   │       ├── MongoCategoryRepository.java
│   │   │   │   │       └── MongoTagRepository.java
│   │   │   │   ├── external/
│   │   │   │   │   ├── ProfileFeignClient.java
│   │   │   │   │   ├── ProfilePublicFeignClient.java
│   │   │   │   │   ├── GeolocationFeignClient.java
│   │   │   │   │   └── RabbitMQFriendshipPublisher.java
│   │   │   │   └── config/
│   │   │   │       ├── FeignConfig.java
│   │   │   │       ├── SwaggerConfig.java
│   │   │   │       ├── DevSecurityConfig.java
│   │   │   │       └── ProdSecurityConfig.java
│   │   │   │
│   │   │   └── entrypoints/                         # CAPA DE ENTRADA
│   │   │       ├── rest/controller/
│   │   │       │   ├── MatchController.java
│   │   │       │   └── CategoryController.java
│   │   │       └── advice/
│   │   │           └── GlobalExceptionHandler.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-prod.yml
│   │
│   └── test/
│       └── java/.../
│           ├── application/usecase/
│           ├── domain/
│           ├── entrypoints/rest/controller/
│           └── entrypoints/advice/
│
├── .github/workflows/
│   ├── ci.yml
│   └── cd.yml
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## 17. Código Documentado

Todos los endpoints están documentados con anotaciones OpenAPI:

```java
/**
 * Crea una nueva solicitud de match entre dos usuarios.
 * Llama al Profile Service para obtener ambos perfiles,
 * calcula el AffinityScore (60% interés + 40% horario) y
 * publica evento match.received en RabbitMQ al finalizar.
 */
@Operation(summary = "Crear match", description = "Calcula afinidad y persiste el match en estado PENDING.")
@ApiResponses({
  @ApiResponse(responseCode = "201", description = "Match creado exitosamente"),
  @ApiResponse(responseCode = "409", description = "Match duplicado"),
  @ApiResponse(responseCode = "404", description = "Perfil no encontrado en M01")
})
@SecurityRequirement(name = "bearerAuth")
public ResponseEntity<MatchResponseDto> createMatch(@RequestBody MatchRequest request) { ... }
```

Anotaciones usadas en todos los endpoints:
- `@Tag` — agrupa los endpoints en Swagger UI
- `@Operation` — propósito y comportamiento de cada endpoint
- `@ApiResponses` — todos los códigos de respuesta posibles
- `@SecurityRequirement(name = "bearerAuth")` — todos los endpoints requieren JWT

---

## 18. Conexiones Externas

| Servicio | Tipo | Dirección | Detalle | Manejo de fallo |
|---|---|---|---|---|
| **MongoDB Atlas** | BD documental | M02 ↔ MongoDB | Colecciones `matches`, `categories`, `tags`. Config: `MONGODB_URI`. | Spring lanza excepción; `GlobalExceptionHandler` retorna 500. |
| **Profile Service (M01)** | HTTP REST via Feign | M02 → M01 | Obtiene perfiles via `/api/v1/internal/matching/profiles/{id}`. Config: `PROFILE_SERVICE_URL`. | Feign lanza excepción; retorna 404 o 503 según el caso. |
| **Geolocation Service (M07)** | HTTP REST via Feign | M02 → M07 | Consulta usuarios cercanos para recomendaciones geográficas. Config: `GEO_SERVICE_URL`. | `ServiceUnavailableException` → 503. |
| **RabbitMQ (CloudAMQP)** | Broker AMQP | M02 → RabbitMQ | Publica `match.received` y `match.response`. Config: `RABBITMQ_*`. | Fallo silencioso: se registra en logs, la respuesta HTTP no se ve afectada. |
| **M01 — Auth** | JWT (validación local) | M01 → M02 | Validación local con clave HMAC-SHA256 compartida. Config: `JWT_SECRET`. | Retorna 401 Unauthorized. |

---

## 19. Pipeline de Desarrollo

Perfil: **local / dev** — MongoDB Atlas, sin Docker obligatorio.

```bash
# Levantar en modo desarrollo
./mvnw spring-boot:run

# Ejecutar pruebas
./mvnw test

# Reporte de cobertura
./mvnw clean test jacoco:report
# → target/site/jacoco/index.html
```

El pipeline `.github/workflows/ci.yml` se ejecuta en cada push a `develop` y en PRs:

```yaml
name: CI – Build & Test
on:
  push:
    branches: [develop, feature/**]
  pull_request:
    branches: [main, develop]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: '21', distribution: temurin }
      - run: ./mvnw clean verify -B
      - run: docker build -t matching-service:test .
```

| Paso | Acción | Descripción |
|---|---|---|
| 1 | Checkout | `actions/checkout@v4` |
| 2 | Setup JDK 21 | `actions/setup-java@v4` distribución Temurin |
| 3 | Build & Test | `./mvnw clean verify -B` — compila, tests, JaCoCo ≥ 80% |
| 4 | Docker Build | Construye imagen para validar el Dockerfile |

---

## 20. Pipeline de Producción

Perfil: **producción** — el pipeline CD se activa en push a `main`.

```bash
# Build y levantamiento con Docker Compose
docker compose up --build

# Ver logs
docker compose logs -f matching-service

# Detener
docker compose down -v
```

**Servicios en `docker-compose.yml`:**

| Servicio | Imagen | Puerto |
|---|---|---|
| `matching-service` | Build local (`eclipse-temurin:21-jre-alpine`) | `8080:8080` |

> MongoDB Atlas es servicio cloud — no se levanta localmente con Docker Compose.

```yaml
name: CD – Deploy to Azure
on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: ./mvnw package -DskipTests -B
      - run: docker build -t matching-service:${{ github.sha }} .
      - name: Deploy to Azure Container Instances
        run: |
          # az container create / az webapp deploy
```

| Paso | Acción | Descripción |
|---|---|---|
| 1 | Checkout | `actions/checkout@v4` |
| 2 | Build JAR | `./mvnw package -DskipTests -B` |
| 3 | Docker Build | Construye imagen `eclipse-temurin:21-jre-alpine` |
| 4 | Deploy Azure | Azure Container Instances en push a `main` |

---

## 21. Dockerizado

### Dockerfile (Multi-stage Build)

```dockerfile
# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Etapa 2: Runtime (imagen mínima ~90 MB)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Comandos Docker

```bash
# Primera vez
docker compose up --build

# Sin rebuild
docker compose up

# Detener y eliminar contenedores
docker compose down

# Eliminar también los volúmenes
docker compose down -v
```

---

## 22. Versionamiento

### Estrategia de Ramas (Git Flow)

| Rama | Propósito | Reglas |
|---|---|---|
| `main` | Versión estable lista para producción | Solo merges desde `release/*` y `hotfix/*`. Cada merge crea un tag SemVer `vX.Y.Z`. PR obligatorio. |
| `develop` | Integración continua de trabajo | Recibe merges desde `feature/*` y `release/*`. Rama protegida. |
| `feature/*` | Desarrollo de funcionalidades | Base: `develop`. Se fusiona a `develop` mediante PR revisado. |

### Convenciones de Ramas

```
feature/[nombre-funcionalidad]    → PascalCase, máx 50 chars
hotfix/[descripcion-del-fix]
release/[version]
```

### Convenciones de Commits

```
[tipo]: [descripción específica de la acción]
```

| Tipo | Uso |
|---|---|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de errores |
| `docs` | Cambios en documentación |
| `refactor` | Refactorización sin cambio de comportamiento |
| `test` | Adición o modificación de pruebas |
| `chore` | Cambios de configuración, build o dependencias |

---

<div align="center">

### Equipo Matchpuff

![Module](https://img.shields.io/badge/Module-M02_Matching_Social-orange?style=for-the-badge)
![Course](https://img.shields.io/badge/Course-DOSW-orange?style=for-the-badge)
![Year](https://img.shields.io/badge/Year-2026--1-blue?style=for-the-badge)

**Escuela Colombiana de Ingeniería Julio Garavito**

</div>
