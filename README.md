<div align="center">

# 🐾 Matchpuff — Microservicio de Social Matching

### *"Gestiona la lógica de matching, conexiones, recomendaciones e interacciones entre usuarios en la plataforma Matchpuff"*

---

### 🛠️ Stack Tecnológico

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-Latest-47A248?style=for-the-badge&logo=mongodb&logoColor=white)

### ☁️ Infraestructura & Calidad

![Azure ECS](https://img.shields.io/badge/Azure_ECS-Deploy-FF9900?style=for-the-badge&logo=amazonAzure&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Container-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

### 🏗️ Arquitectura

![Hexagonal](https://img.shields.io/badge/Architecture-Hexagonal-blueviolet?style=for-the-badge)
![Clean Architecture](https://img.shields.io/badge/Clean-Architecture-blue?style=for-the-badge)
![REST API](https://img.shields.io/badge/REST-API-009688?style=for-the-badge)

</div>

---

## 📑 Tabla de Contenidos

1. [👤 Integrantes](#1--integrantes)
2. [🎯 Objetivo del Microservicio](#2--objetivo-del-microservicio)
3. [⚡ Funcionalidades Principales](#3--funcionalidades-principales)
4. [📋 Estrategia de Versionamiento y Branches](#4--manejo-de-estrategia-de-versionamiento-y-branches)
    - [4.1 Convenciones para crear ramas](#41-convenciones-para-crear-ramas)
    - [4.2 Convenciones para crear commits](#42-convenciones-para-crear-commits)
5. [⚙️ Tecnologías Utilizadas](#5--tecnologias-utilizadas)
6. [🧩 Funcionalidad](#6--funcionalidad)
7. [📊 Diagramas](#7--diagramas)
8. [⚠️ Manejo de Errores](#8--manejo-de-errores)
9. [🧪 Evidencia de Pruebas y Ejecución](#9--evidencia-de-las-pruebas-y-como-ejecutarlas)
10. [🗂️ Organización del Código](#10--codigo-de-la-implementacion-organizado-en-las-respectivas-carpetas)
11. [🚀 Ejecución del Proyecto](#11--ejecucion-del-proyecto)
12. [☁️ CI/CD y Despliegue en Azure](#12--evidencia-de-cicd-y-despliegue-en-azure)
13. [🤝 Contribuciones](#13--contribuciones)

---

## 1. 👤 Integrantes:

Javier Mauricio Romero Deaquiz


Mariana Malagón


Andrés Cardozo Martinez


Jeimmy Vanessa Torres Marín


## 2. 🎯 Objetivo del microservicio

El microservicio de Social Matching tiene como objetivo gestionar toda la lógica de conexión y afinidad entre usuarios dentro de la plataforma Matchpuff. Este servicio administra la creación, actualización y eliminación de matches entre estudiantes, el cálculo automatizado de puntuaciones de afinidad basadas en intereses compartidos (tags/categorías) y compatibilidad de horarios, la generación de recomendaciones personalizadas, y la gestión del catálogo de categorías y etiquetas disponibles en la plataforma, garantizando conexiones relevantes y de valor entre los usuarios.

---

## 3. ⚡ Funcionalidades principales

<div align="center">

<table>
  <thead>
    <tr>
      <th>💡 Funcionalidad</th>
      <th>Descripción</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>Gestión de Matches</strong></td>
      <td>Crea, consulta, actualiza y elimina matches entre pares de usuarios. Gestiona el ciclo de vida del match con estados PENDING, ACCEPTED y REJECTED.</td>
    </tr>
    <tr>
      <td><strong>Cálculo de Afinidad</strong></td>
      <td>Calcula automáticamente un score de afinidad al crear un match, combinando compatibilidad interes comunes .</td>
    </tr>
    <tr>
      <td><strong>Recomendaciones</strong></td>
      <td>Genera recomendaciones de usuarios con alta afinidad para un estudiante dado, basándose en el algoritmo de cálculo de afinidad.</td>
    </tr>
    <tr>
      <td><strong>Gestión de Categorías</strong></td>
      <td>Administra el catálogo de categorías de intereses disponibles en la plataforma, permitiendo crear, consultar, actualizar y eliminar categorías.</td>
    </tr>
    <tr>
      <td><strong>Gestión de Tags</strong></td>
      <td>Administra las etiquetas de interés asociadas a categorías, permitiendo crear, consultar, actualizar y eliminar tags dentro de cada categoría.</td>
    </tr>
    <tr>
      <td><strong>Integración con Perfiles</strong></td>
      <td>Se comunica con el microservicio de Perfiles vía Feign Client para obtener los datos de los usuarios necesarios para calcular la afinidad.</td>
    </tr>
    <tr>
      <td><strong>Actualización de Estado de Match</strong></td>
      <td>Permite a los usuarios aceptar o rechazar matches pendientes, actualizando el estado del match en tiempo real.</td>
    </tr>
  </tbody>
</table>

</div>


## 4. 📋 Manejo de Estrategia de versionamiento y branches

### Estrategia de Ramas (Git Flow)

### Ramas y propósito
- Manejaremos GitFlow, el modelo de ramificación para el control de versiones de Git

#### `main`
- **Propósito:** rama **estable** con la versión final (lista para demo/producción).
- **Reglas:**
    - Solo recibe merges desde `release/*` y `hotfix/*`.
    - Cada merge a `main` debe crear un **tag** SemVer (`vX.Y.Z`).
    - Rama **protegida**: PR obligatorio, 1–2 aprobaciones, checks de CI en verde.

#### `develop`
- **Propósito:** integración continua de trabajo; base de nuevas funcionalidades.
- **Reglas:**
    - Recibe merges desde `feature/*` y también desde `release/*` al finalizar un release.
    - Rama **protegida** similar a `main`.

#### `feature/*`
- **Propósito:** desarrollo de una funcionalidad, refactor o spike.
- **Base:** `develop`.
- **Cierre:** se fusiona a `develop` mediante **PR**


#### `release/*`
- **Propósito:** congelar cambios para estabilizar pruebas, textos y versiones previas al deploy.
- **Base:** `develop`.
- **Cierre:** merge a `main` (crear **tag** `vX.Y.Z`) **y** merge de vuelta a `develop`.
- **Ejemplo de nombre:**  
  `release/1.3.0`

#### `hotfix/*`
- **Propósito:** corregir un bug **crítico** detectado en `main`.
- **Base:** `main`.
- **Cierre:** merge a `main` (crear **tag** de **PATCH**) **y** merge a `develop` para mantener paridad.
- **Ejemplos de nombre:**  
  `hotfix/fix-affinity-calculation`, `hotfix/fix-match-status`


---

### 4.1 Convenciones para **crear ramas**

#### `feature/*`
**Formato:**
```
feature/[nombre-funcionalidad]
```

**Ejemplos:**
- `feature/gestionMatches`
- `feature/calculoAfinidad`

**Reglas de nomenclatura:**
- Usar **PascalCase** (palabras separadas por mayúscula)
- Máximo 50 caracteres en total
- Descripción clara y específica de la funcionalidad

#### `release/*`
**Formato:**
```
release/[version]
```
**Ejemplo:** `release/1.0.0`

#### `hotfix/*`
**Formato:**
```
hotfix/[descripcion-breve-del-fix]
```
**Ejemplos:**
- `hotfix/corregirCalculoAfinidad`
- `hotfix/fixEstadoMatch`

---

### 4.2 Convenciones para **crear commits**

#### **Formato:**
```
[tipo]: [descripción específica de la acción]
```

#### **Tipos de commit:**
- `feat`: Nueva funcionalidad
- `fix`: Corrección de errores
- `docs`: Cambios en documentación

## 5. ⚙️ Tecnologías Utilizadas


| **Tecnología / Herramienta** | **Uso principal en el proyecto** |
|------------------------------|----------------------------------|
| **Java 21 (OpenJDK)** | Lenguaje de programación base del microservicio backend, con soporte a records, switch expressions y mejoras modernas. |
| **Spring Boot 3.4.5** | Framework principal para construir el microservicio, exponiendo APIs REST y gestionando configuración e inyección de dependencias. |
| **Spring Web** | Exposición de endpoints REST (controladores HTTP) dentro de la arquitectura hexagonal. |
| **Spring Security** | Configuración de seguridad del microservicio; protege endpoints en el perfil de producción. |
| **Spring Data MongoDB** | Integración del microservicio con MongoDB usando el patrón Repository y puertos/adaptadores. |
| **MongoDB** | Base de datos NoSQL principal para las colecciones `matches` y `categories` con subdocumentos de afinidad y tags. |
| **OpenFeign (Spring Cloud)** | Cliente HTTP declarativo para comunicación con el microservicio de Perfiles y obtención de datos de usuarios. |
| **MapStruct 1.5.5** | Generación automática de mappers entre capas (DTO ↔ Dominio ↔ Persistencia). |
| **Apache Maven** | Gestión de dependencias, empaquetado del microservicio y automatización de builds en los pipelines CI/CD. |
| **Lombok** | Reducción de código repetitivo con anotaciones como `@Getter`, `@Builder`, `@Data` y `@RequiredArgsConstructor`. |
| **JUnit 5** | Framework de pruebas unitarias para validar la lógica de dominio y casos de uso en el microservicio. |
| **Mockito** | Simulación de dependencias (puertos, repositorios, Feign clients) en pruebas unitarias sin acceder a infraestructura real. |
| **Spring Security Test** | Soporte para pruebas de controladores con contexto de seguridad simulado. |
| **Swagger (OpenAPI 3 / springdoc 2.8.6)** | Generación automática de documentación y prueba interactiva de los endpoints REST. |
| **Spring Boot Actuator** | Exposición de endpoints de salud (`/actuator/health`) para monitoreo y healthchecks de Docker. |
| **Docker** | Contenerización del microservicio con build multi-stage y soporte HTTPS (SSL/TLS). |
| **Docker Compose** | Orquestación local de la aplicación para desarrollo y pruebas. |
| **Azure ECS (Fargate)** | Plataforma cloud donde se despliega el contenedor Docker del microservicio en producción. |
| **Amazon ECR** | Registro de contenedores Docker donde se almacenan las imágenes del microservicio. |
| **GitHub Actions** | Automatización de CI/CD: compilación, pruebas, análisis de cobertura y despliegue en Azure. |
| **SonarCloud** | Análisis estático de calidad de código y cobertura de pruebas. |
| **JaCoCo** | Generación de reportes de cobertura de pruebas integrados al pipeline CI. |


> 🧠 **Stack tecnológico seleccionado** para asegurar **escalabilidad**, **modularidad**, **seguridad**, **trazabilidad** y **mantenibilidad**, aplicando buenas prácticas de ingeniería de software.

## 6. 🧩 Funcionalidades

---

### 🔑 Funcionalidades principales

### 1️⃣ Crear Match

Permite crear un match entre dos usuarios estudiantes en el sistema. Al crear el match, se calcula automáticamente el score de afinidad entre ambos perfiles.

**Endpoint principal:**  
`POST /api/v1/matches`

---

### 📦 Estructura de la Solicitud (Request)

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | ⚠️ Restricciones | 📝 Descripción |
|---|---|:---:|---|
| requesterId | UUID | Obligatorio | ID del usuario que envía la solicitud de match. |
| targetId | UUID | Obligatorio | ID del usuario destinatario del match. |

</div>

---

### 📦 Estructura de la Respuesta (Response)

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | 📝 Descripción |
|---|---|---|
| idMatch | UUID | Identificador único del match. |
| requesterId | UUID | ID del usuario que inició el match. |
| targetId | UUID | ID del usuario destinatario. |
| status | Enum | Estado actual del match (`PENDING`, `ACCEPTED`, `REJECTED`). |
| affinityScore | Object | Objeto con los scores de afinidad calculados. |
| affinityScore.score | Double | Score total de afinidad (0.0 – 1.0). |
| affinityScore.interestScore | Double | Score de compatibilidad de intereses (0.0 – 1.0). |
| affinityScore.scheduleScore | Double | Score de compatibilidad de horarios (0.0 – 1.0). |
| createdAt | LocalDateTime | Fecha y hora de creación del match. |
| updatedAt | LocalDateTime | Fecha y hora de última actualización. |

</div>


---

### ✅ Ejemplo de Uso Exitoso

1. El cliente envía un POST con los IDs del solicitante y destinatario.
2. El sistema consulta los perfiles de ambos usuarios al microservicio de Perfiles vía Feign Client.
3. Se calcula el score de afinidad combinando intereses y horarios.
4. Se persiste el match en MongoDB con estado `PENDING`.
5. Se retorna `201 CREATED` con los datos del nuevo match incluyendo el score de afinidad.

**Request (Solicitud):**
```json
POST /api/v1/matches

{
  "requesterId": "550e8400-e29b-41d4-a716-446655440000",
  "targetId": "550e8400-e29b-41d4-a716-446655440001"
}
```

**Response (Respuesta):**
```json
{
  "idMatch": "550e8400-e29b-41d4-a716-446655440002",
  "requesterId": "550e8400-e29b-41d4-a716-446655440000",
  "targetId": "550e8400-e29b-41d4-a716-446655440001",
  "status": "PENDING",
  "affinityScore": {
    "score": 0.73,
    "interestScore": 0.80,
    "scheduleScore": 0.62
  },
  "createdAt": "2026-05-13T17:52:38",
  "updatedAt": "2026-05-13T17:52:38"
}
```

---

### 🖼️ Diagrama de Secuencia

*(Adjunta aquí el diagrama de secuencia para crear match)*

<details>
<summary><strong>🟢 Explicación del Flujo</strong></summary>

El proceso inicia cuando el cliente envía un POST al `MatchController` con los IDs de los dos usuarios. El controlador delega al `MatchingServiceImpl`, que consulta los perfiles de ambos usuarios a través del `ProfileServiceAdapter` (Feign Client). Con los perfiles obtenidos, el `AffinityCalculatorImpl` calcula el score de afinidad ponderando intereses compartidos y compatibilidad de horarios. El match se persiste en MongoDB a través del `MatchRepositoryAdapter` y se retorna la respuesta con el score calculado.

</details>

---

### 📊 Tipos de errores manejados

<div align="center">

| 🔢 **Código HTTP** | ⚠️ **Escenario** | 💬 **Mensaje de Error** |
|:------------------:|:----------------|:------------------------|
| ![400](https://img.shields.io/badge/400-Bad_Request-red?style=flat) | IDs de usuario nulos | `"El ID del solicitante es requerido"` |
| ![404](https://img.shields.io/badge/404-Not_Found-orange?style=flat) | Usuario no encontrado en el servicio de Perfiles | `"User not found: <userId>"` |

</div>

---

### 2️⃣ Obtener Match por ID

Retorna los datos completos de un match según su identificador único.

**Endpoint principal:**  
`GET /api/v1/matches/{matchId}`

---

### 📦 Estructura de la Respuesta (Response)

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | 📝 Descripción |
|---|---|---|
| (objeto) | MatchResponse | Datos completos del match incluyendo IDs, estado y score de afinidad. |

</div>

---

### ✅ Ejemplo de Uso Exitoso

1. El cliente envía un GET con el `matchId` como path variable.
2. El sistema busca el match en MongoDB.
3. Se retorna `200 OK` con los datos completos del match.

**Request (Solicitud):**
```
GET /api/v1/matches/550e8400-e29b-41d4-a716-446655440002
```

---

### 🖼️ Diagrama de Secuencia

*(Adjunta aquí el diagrama de secuencia para obtener match por ID)*

---

### 📊 Tipos de errores manejados

<div align="center">

| 🔢 **Código HTTP** | ⚠️ **Escenario** | 💬 **Mensaje de Error** |
|:------------------:|:----------------|:------------------------|
| ![404](https://img.shields.io/badge/404-Not_Found-orange?style=flat) | Match no existe | `"Match not found: <matchId>"` |

</div>

---

### 3️⃣ Obtener Matches por Usuario

Retorna la lista completa de matches de un usuario específico.

**Endpoint principal:**  
`GET /api/v1/matches/user/{userId}`

---

### 📦 Estructura de la Respuesta (Response)

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | 📝 Descripción |
|---|---|---|
| (lista) | List\<MatchResponse\> | Lista de todos los matches del usuario (como solicitante o destinatario). |

</div>

---

### ✅ Ejemplo de Uso Exitoso

1. El cliente envía un GET con el `userId` como path variable.
2. El sistema busca todos los matches donde el usuario participa en MongoDB.
3. Se retorna `200 OK` con la lista de matches.

**Request (Solicitud):**
```
GET /api/v1/matches/user/550e8400-e29b-41d4-a716-446655440000
```

---

### 🖼️ Diagrama de Secuencia

*(Adjunta aquí el diagrama de secuencia para obtener matches por usuario)*

---

### 📊 Tipos de errores manejados

<div align="center">

| 🔢 **Código HTTP** | ⚠️ **Escenario** | 💬 **Mensaje de Error** |
|:------------------:|:----------------|:------------------------|
| ![404](https://img.shields.io/badge/404-Not_Found-orange?style=flat) | Usuario no existe | `"User not found: <userId>"` |

</div>

---

### 4️⃣ Actualizar Estado del Match

Permite al usuario destinatario aceptar o rechazar un match pendiente.

**Endpoint principal:**  
`PUT /api/v1/matches/{matchId}`

---

### 📦 Estructura de la Solicitud (Request)

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | ⚠️ Restricciones | 📝 Descripción |
|---|---|:---:|---|
| status | Enum | Obligatorio | Nuevo estado del match: `ACCEPTED` o `REJECTED`. |

</div>

---

### ✅ Ejemplo de Uso Exitoso

1. El cliente envía un PUT con el nuevo estado del match.
2. El sistema actualiza el estado del match en MongoDB.
3. Se retorna `200 OK` con el match actualizado.

**Request (Solicitud):**
```json
PUT /api/v1/matches/550e8400-e29b-41d4-a716-446655440002

{
  "status": "ACCEPTED"
}
```

---

### 🖼️ Diagrama de Secuencia

*(Adjunta aquí el diagrama de secuencia para actualizar estado de match)*

---

### 📊 Tipos de errores manejados

<div align="center">

| 🔢 **Código HTTP** | ⚠️ **Escenario** | 💬 **Mensaje de Error** |
|:------------------:|:----------------|:------------------------|
| ![400](https://img.shields.io/badge/400-Bad_Request-red?style=flat) | Estado nulo o inválido | `"El estado del match es requerido"` |
| ![404](https://img.shields.io/badge/404-Not_Found-orange?style=flat) | Match no existe | `"Match not found: <matchId>"` |

</div>

---

### 5️⃣ Eliminar Match

Permite eliminar permanentemente un match del sistema.

**Endpoint principal:**  
`DELETE /api/v1/matches/{matchId}`

---

### ✅ Ejemplo de Uso Exitoso

1. El cliente envía un DELETE con el `matchId` como path variable.
2. El sistema elimina el match de MongoDB.
3. Se retorna `204 No Content`.

**Request (Solicitud):**
```
DELETE /api/v1/matches/550e8400-e29b-41d4-a716-446655440002
```

---

### 🖼️ Diagrama de Secuencia

*(Adjunta aquí el diagrama de secuencia para eliminar match)*

---

### 📊 Tipos de errores manejados

<div align="center">

| 🔢 **Código HTTP** | ⚠️ **Escenario** | 💬 **Mensaje de Error** |
|:------------------:|:----------------|:------------------------|
| ![404](https://img.shields.io/badge/404-Not_Found-orange?style=flat) | Match no existe | `"Match not found: <matchId>"` |

</div>

---

### 6️⃣ Gestión de Categorías

Permite crear, consultar, actualizar y eliminar las categorías de intereses disponibles en la plataforma.

**Endpoints principales:**  
`POST /api/v1/categories` — Crear categoría  
`GET /api/v1/categories` — Listar categorías  
`GET /api/v1/categories/{categoryId}` — Obtener categoría por ID  
`PUT /api/v1/categories/{categoryId}` — Actualizar categoría  
`DELETE /api/v1/categories/{categoryId}` — Eliminar categoría

---

### 📦 Estructura de la Solicitud (Request — Crear/Actualizar)

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | ⚠️ Restricciones | 📝 Descripción |
|---|---|:---:|---|
| name | String | Obligatorio, máximo 100 caracteres | Nombre de la categoría. |

</div>

---

### ✅ Ejemplo de Uso Exitoso

1. El cliente envía un POST con los datos de la categoría.
2. El sistema valida y persiste la categoría en MongoDB.
3. Se retorna `201 CREATED` con los datos de la nueva categoría.

**Request (Solicitud):**
```json
POST /api/v1/categories

{
  "name": "Tecnología"
}
```

**Response (Respuesta):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440010",
  "name": "Tecnología"
}
```

---

### 🖼️ Diagrama de Secuencia

*(Adjunta aquí el diagrama de secuencia para gestión de categorías)*

---

### 📊 Tipos de errores manejados

<div align="center">

| 🔢 **Código HTTP** | ⚠️ **Escenario** | 💬 **Mensaje de Error** |
|:------------------:|:----------------|:------------------------|
| ![400](https://img.shields.io/badge/400-Bad_Request-red?style=flat) | Datos inválidos | Errores de validación del request |
| ![404](https://img.shields.io/badge/404-Not_Found-orange?style=flat) | Categoría no existe | `"Category not found: <categoryId>"` |

</div>

---

### 7️⃣ Gestión de Tags dentro de Categorías

Permite crear, consultar, actualizar y eliminar etiquetas de interés asociadas a una categoría.

**Endpoints principales:**  
`POST /api/v1/categories/{categoryId}/tags` — Crear tag  
`GET /api/v1/categories/{categoryId}/tags` — Listar tags de una categoría  
`PUT /api/v1/categories/{categoryId}/tags/{tagId}` — Actualizar tag  
`DELETE /api/v1/categories/{categoryId}/tags/{tagId}` — Eliminar tag

---

### 📦 Estructura de la Solicitud (Request — Crear/Actualizar Tag)

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | ⚠️ Restricciones | 📝 Descripción |
|---|---|:---:|---|
| name | String | Obligatorio, máximo 50 caracteres | Nombre del tag. |
| category | String | Obligatorio, máximo 100 caracteres | Nombre de la categoría a la que pertenece. |

</div>

---

### ✅ Ejemplo de Uso Exitoso

1. El cliente envía un POST con los datos del tag bajo una categoría.
2. El sistema valida y persiste el tag en MongoDB asociado a la categoría.
3. Se retorna `201 CREATED` con los datos del nuevo tag.

**Request (Solicitud):**
```json
POST /api/v1/categories/550e8400-e29b-41d4-a716-446655440010/tags

{
  "name": "Inteligencia Artificial",
  "category": "Tecnología"
}
```

**Response (Respuesta):**
```json
{
  "name": "Inteligencia Artificial",
  "category": "Tecnología"
}
```

---

### 🖼️ Diagrama de Secuencia

*(Adjunta aquí el diagrama de secuencia para gestión de tags)*

---

### 📊 Tipos de errores manejados

<div align="center">

| 🔢 **Código HTTP** | ⚠️ **Escenario** | 💬 **Mensaje de Error** |
|:------------------:|:----------------|:------------------------|
| ![400](https://img.shields.io/badge/400-Bad_Request-red?style=flat) | Datos inválidos | Errores de validación del request |
| ![404](https://img.shields.io/badge/404-Not_Found-orange?style=flat) | Categoría o tag no existe | `"Category not found"` / `"Tag not found"` |

</div>

---

## 7. 📊 Diagramas

Esta sección muestra los diagramas clave del microservicio de Social Matching, ilustrando su arquitectura, componentes principales y despliegue.

---

### 🏗️ Diagrama de Componentes — Vista General



---

### 🔍 Diagrama de Componentes — Vista Específica



**Arquitectura Hexagonal:**  
El microservicio de Social Matching separa controladores, casos de uso, lógica de negocio y adaptadores externos para mantener modularidad y escalabilidad.

**Flujo principal:**

- **MatchController / CategoryController**
    - Reciben solicitudes HTTP y las delegan a los puertos de entrada correspondientes.

**Lógica de Negocio (Dominio):**

- **Casos de Uso (Application Layer)**
    - `MatchingServiceImpl` implementa `MatchUseCasePort` y orquesta la creación, consulta, actualización y eliminación de matches.
    - `RecommendationsUseCaseImpl` implementa `RecommendationsUseCasePort` y genera recomendaciones personalizadas de usuarios.
    - `CategoryServiceImpl` implementa `CategoryUseCasePort` y gestiona el catálogo de categorías y tags.
    - `AffinityCalculatorImpl` calcula el score de afinidad entre dos perfiles combinando intereses y horarios.

**Integración y Adaptadores:**

- **Persistencia:**
    - `MatchRepositoryAdapter` implementa `MatchRepositoryPort` y persiste matches en MongoDB.
    - `CategoryRepository` implementa `CategoryRepositoryPort` y persiste categorías y tags en MongoDB.

- **Comunicación Externa:**
    - `ProfileServiceAdapter` implementa `ProfileServicePort` usando `ProfileFeignClient` para consultar datos de usuarios al microservicio de Perfiles.

- **Seguridad:**
    - `ProdSecurityConfig` y `DevSecurityConfig` configuran la seguridad según el perfil activo de Spring.

- **Manejo de Errores:**
    - `GlobalExceptionHandler` centraliza el manejo de excepciones de dominio y errores de validación.

> El microservicio de Social Matching gestiona toda la lógica de conexión entre usuarios de Matchpuff, integrándose con el microservicio de Perfiles para acceder a los datos necesarios para el cálculo de afinidad.


### 🔌 Servicios Externos Integrados

El microservicio se integra con otros sistemas del ecosistema Matchpuff.

<div align="center">

| 🌍 **Servicio** | ⚙️ **Operación** | 📋 **Propósito** |
|:---------------|:----------------|:-----------------------|
| **Microservicio de Perfiles** | Consulta de perfil por ID | Obtener datos de carrera, semestre, tags y horarios para calcular afinidad |

</div>

**Dominio y Mapeo:**

- Las entidades `Match`, `AffinityScore`, `UserMatchProfile`, `Category` y `Tag` encapsulan la lógica central.
- Los enums `MatchStatus`, `CareerEnum`, `GenderEnum` y `DayOfWeekEnum` garantizan valores controlados en el dominio.

> El diagrama ilustra cómo el dominio de social matching se mantiene aislado de la infraestructura, permitiendo cambiar la base de datos o los adaptadores externos sin afectar las reglas de negocio.


---
### 📊 Diagrama de base de datos

*(Adjunta aquí el diagrama de persistencia)*

El microservicio de Social Matching utiliza **MongoDB** como motor de base de datos NoSQL. Contiene dos colecciones principales: `matches` y `categories`.

#### 📋 Colección: `matches`

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | 📝 Descripción | ⚠️ Restricciones |
|:---|:---|:---|:---|
| **_id** | `UUID` | Identificador único del match | Primary Key |
| **requesterId** | `UUID` | ID del usuario que solicitó el match | NOT NULL |
| **targetId** | `UUID` | ID del usuario destinatario | NOT NULL |
| **status** | `String` | Estado del match (PENDING, ACCEPTED, REJECTED) | NOT NULL |
| **affinityScore** | `AffinityScoreDocument` | Subdocumento con los scores de afinidad | Embebido |
| **createdAt** | `LocalDateTime` | Fecha de creación del match | NOT NULL |
| **updatedAt** | `LocalDateTime` | Fecha de última actualización | NOT NULL |

</div>

##### Subdocumento: `AffinityScoreDocument`

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | 📝 Descripción |
|:---|:---|:---|
| **score** | `Double` | Score total de afinidad (0.0 – 1.0) |
| **interestScore** | `Double` | Score de compatibilidad de intereses (0.0 – 1.0) |
| **scheduleScore** | `Double` | Score de compatibilidad de horarios (0.0 – 1.0) |

</div>

#### 📋 Colección: `categories`

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | 📝 Descripción | ⚠️ Restricciones |
|:---|:---|:---|:---|
| **_id** | `UUID` | Identificador único de la categoría | Primary Key |
| **name** | `String` | Nombre de la categoría | NOT NULL, Unique, Máx. 100 chars |

</div>

#### 📋 Colección: `tags`

<div align="center">

| 🏷️ Campo | 🗃️ Tipo | 📝 Descripción | ⚠️ Restricciones |
|:---|:---|:---|:---|
| **_id** | `UUID` | Identificador único del tag | Primary Key |
| **name** | `String` | Nombre del tag | NOT NULL, Máx. 50 chars |
| **categoryID** | `UUID` | ID de la categoría a la que pertenece | NOT NULL |

</div>

---

### 📦 Diagrama de Clases del Dominio

*(Adjunta aquí el diagrama de clases)*

**Resumen del diseño de dominio:**

La arquitectura de dominio se centra en las entidades de matching y afinidad.

- **Entidad Principal:** `Match` contiene los IDs de los usuarios, el estado del match, la puntuación de afinidad y las marcas de tiempo.
- **Cálculo de Afinidad:** `AffinityScore` agrupa los tres scores (total, intereses, horarios). El `AffinityCalculatorImpl` aplica pesos: `interestScore = 30% categoría + 50% nombre-tag + 10% género`, `totalScore = 60% interestScore + 40% scheduleScore`.
- **Perfil de Matching:** `UserMatchProfile` encapsula los datos del usuario necesarios para el cálculo: carrera, semestre, tags, horarios, género y preferencias de género.
- **Catálogo:** `Category` y `Tag` modelan el catálogo de intereses con validaciones de negocio en sus constructores.
- **Enumeraciones:** `MatchStatus`, `CareerEnum`, `GenderEnum` y `DayOfWeekEnum` garantizan valores controlados en el dominio.

> Este diseño asegura que la lógica de afinidad sea extensible y que el catálogo de categorías/tags pueda crecer sin afectar las reglas de negocio centrales.


---

### 📦 DTOs Principales

<div align="center">
<div style="background:#111; color:#fff; border-radius:12px; padding:24px 12px; box-shadow:0 2px 12px #0002;">

<table style="border:2px solid #4A90E2; border-radius:8px;">
  <caption style="font-size:1.15em; font-weight:bold; color:#4A90E2; padding:8px;">📨 <u>Request DTOs</u></caption>
  <thead style="background:#222; color:#fff;">
    <tr>
      <th style="padding:8px;">DTO</th>
      <th style="padding:8px;">Atributos Principales</th>
      <th style="padding:8px;">Descripción</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><b>MatchRequest</b></td>
      <td>requesterId, targetId</td>
      <td>Solicitud para crear un nuevo match entre dos usuarios. Ambos IDs son obligatorios.</td>
    </tr>
    <tr>
      <td><b>MatchUpdateRequest</b></td>
      <td>status</td>
      <td>Solicitud para actualizar el estado de un match (ACCEPTED o REJECTED).</td>
    </tr>
    <tr>
      <td><b>AffinityScoreRequest</b></td>
      <td>score, interestScore, scheduleScore</td>
      <td>Solicitud para definir manualmente un score de afinidad. Valores entre 0.0 y 1.0.</td>
    </tr>
    <tr>
      <td><b>AffinityScoreUpdateRequest</b></td>
      <td>score, interestScore, scheduleScore</td>
      <td>Solicitud para actualizar un score de afinidad existente.</td>
    </tr>
    <tr>
      <td><b>UserMatchProfileRequest</b></td>
      <td>career, semester, tags, schedules, gender, genderPreferences, lastSync</td>
      <td>Solicitud con el perfil de matching de un usuario. Semestre 1–10, tags y horarios obligatorios.</td>
    </tr>
    <tr>
      <td><b>UserMatchProfileUpdateRequest</b></td>
      <td>career, semester, tags, schedules, gender, genderPreferences, lastSync</td>
      <td>Solicitud para actualizar el perfil de matching de un usuario.</td>
    </tr>
    <tr>
      <td><b>TagRequest</b></td>
      <td>name, category</td>
      <td>Solicitud para crear o asociar un tag de interés. Nombre máx. 50 chars, categoría máx. 100 chars.</td>
    </tr>
    <tr>
      <td><b>ScheduleRequest</b></td>
      <td>dayOfWeek, name, startTime, endTime</td>
      <td>Solicitud para definir una franja de disponibilidad horaria. La hora de inicio debe ser antes que la de fin.</td>
    </tr>
  </tbody>
</table>

<br>

<table style="border:2px solid #43A047; border-radius:8px;">
  <caption style="font-size:1.15em; font-weight:bold; color:#43A047; padding:8px;">📤 <u>Response DTOs</u></caption>
  <thead style="background:#222; color:#fff;">
    <tr>
      <th style="padding:8px;">DTO</th>
      <th style="padding:8px;">Atributos Principales</th>
      <th style="padding:8px;">Descripción</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><b>MatchResponse</b></td>
      <td>idMatch, requesterId, targetId, status, affinityScore, createdAt, updatedAt</td>
      <td>Respuesta completa de un match con su score de afinidad calculado.</td>
    </tr>
    <tr>
      <td><b>AffinityScoreResponse</b></td>
      <td>score, interestScore, scheduleScore</td>
      <td>Desglose de los componentes del score de afinidad entre dos usuarios.</td>
    </tr>
    <tr>
      <td><b>UserMatchProfileResponse</b></td>
      <td>id, career, semester, tags, schedules, gender, genderPreferences, lastSync</td>
      <td>Respuesta del perfil de matching de un usuario con todos sus datos de afinidad.</td>
    </tr>
    <tr>
      <td><b>RecommendationResponse</b></td>
      <td>userId, affinityScore</td>
      <td>Respuesta de recomendación con el ID del usuario recomendado y su score de afinidad.</td>
    </tr>
    <tr>
      <td><b>TagResponse</b></td>
      <td>name, category</td>
      <td>Respuesta con el nombre y categoría de un tag de interés.</td>
    </tr>
    <tr>
      <td><b>ScheduleResponse</b></td>
      <td>dayOfWeek, name, startTime, endTime</td>
      <td>Respuesta con los datos de una franja de disponibilidad horaria.</td>
    </tr>
    <tr>
      <td><b>CategoryResponse</b></td>
      <td>id, name</td>
      <td>Respuesta con el identificador y nombre de una categoría de intereses.</td>
    </tr>
    <tr>
      <td><b>CategoryWithTagsResponse</b></td>
      <td>id, name, tags</td>
      <td>Respuesta de una categoría con su lista completa de tags asociados.</td>
    </tr>
  </tbody>
</table>

<br>

<table style="border:2px solid #F0AD4E; border-radius:8px;">
  <caption style="font-size:1.15em; font-weight:bold; color:#F0AD4E; padding:8px;">⚙️ <u>Enums del Dominio</u></caption>
  <thead style="background:#222; color:#fff;">
    <tr>
      <th style="padding:8px;">Enum</th>
      <th style="padding:8px;">Valores</th>
      <th style="padding:8px;">Descripción</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><b>MatchStatus</b></td>
      <td>PENDING, ACCEPTED, REJECTED</td>
      <td>Estado del ciclo de vida de un match entre dos usuarios.</td>
    </tr>
    <tr>
      <td><b>CareerEnum</b></td>
      <td>SYSTEMS_ENGINEERING, COMPUTER_SCIENCE, INFORMATION_TECHNOLOGY, ADMINISTRATION, BUSINESS</td>
      <td>Carrera universitaria del estudiante, usada en el cálculo de afinidad.</td>
    </tr>
    <tr>
      <td><b>GenderEnum</b></td>
      <td>MALE, FEMALE, PREFER_NOT_TO_SAY</td>
      <td>Género del usuario, considerado como factor de afinidad (peso 10%).</td>
    </tr>
    <tr>
      <td><b>DayOfWeekEnum</b></td>
      <td>MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY</td>
      <td>Días de disponibilidad usados en el cálculo del score de horarios.</td>
    </tr>
  </tbody>
</table>

</div>
</div>

---

### 🗄️ Diagrama de Despliegue

*(Adjunta aquí el diagrama de despliegue)*

---

#### 🚀 Despliegue e Infraestructura

El microservicio de **Social Matching** se ejecuta como un contenedor Docker en **Azure**, respaldado por una arquitectura robusta de CI/CD.

- **Ejecución:** Contenedor Docker en Azure ECS con imagen almacenada en Amazon ECR.
- **Base de datos:** **MongoDB** con URI inyectada como variable de entorno `MONGO_URI`.
- **Comunicación interna:** Feign Client hacia el microservicio de Perfiles con URL configurada vía `PROFILE_SERVICE_URL`.
- **HTTPS:** El servicio corre sobre SSL/TLS en el puerto configurado con soporte de producción.
- **CI/CD (GitHub Actions):**
    - Pruebas unitarias (JUnit 5) en cada PR a `develop` y `main`.
    - Despliegue automático a Azure ECS en merges a `main`.
    - Análisis de calidad de código con SonarCloud.
- **Construcción:** Dockerfile multi-stage (Maven Build → JRE 21 Alpine Runtime).
- **Configuración:** Variables de entorno gestionadas desde Azure Secrets/Environment.

<div align="center">	

| 🌐 **Componente** | 📝 **Descripción** |
|------------------|-------------------|
| Azure ECS Fargate | Hosting del contenedor Docker del microservicio |
| Amazon ECR | Registro privado de imágenes Docker |
| MongoDB Atlas / Azure | Base de datos NoSQL para matches y categorías |
| Microservicio de Perfiles | Proveedor de datos de usuario vía Feign Client |
| GitHub Actions | Automatización de CI/CD y calidad de código |
| Swagger UI | Documentación interactiva en `/swagger-ui.html` |

</div>



---

## 8. ⚠️ Manejo de Errores

El microservicio de **Social Matching** implementa un **mecanismo centralizado de manejo de errores** que garantiza uniformidad, claridad y seguridad en todas las respuestas enviadas al cliente cuando ocurre un fallo.

---

### 🧠 Estrategia general de manejo de errores

El sistema utiliza una **clase global** `GlobalExceptionHandler` con la anotación `@RestControllerAdvice` que intercepta todas las excepciones lanzadas desde los controladores REST. Cada excepción de dominio se transforma en una respuesta **JSON estandarizada** con el código HTTP apropiado.


---

### ⚙️ Global Exception Handler

El **Global Exception Handler** captura y maneja todas las excepciones del sistema de forma centralizada. Utiliza métodos con `@ExceptionHandler` para procesar cada tipo de error.

**✨ Características principales:**

- ✅ **Centraliza** la captura de excepciones desde todos los controladores
- ✅ **Retorna mensajes JSON consistentes** con el mismo formato estructurado (message, status)
- ✅ **Asigna códigos HTTP** según la naturaleza del error (400, 404, 409, 500)
- ✅ **Define mensajes descriptivos** que ayudan tanto al desarrollador como al usuario
- ✅ **Mantiene la aplicación limpia**, eliminando bloques try-catch redundantes
- ✅ **Mejora la trazabilidad** y facilita la depuración en entornos de prueba y producción


---

### 🧩 Excepciones de dominio manejadas

<div align="center">

| ⚠️ **Excepción** | 🔢 **HTTP** | 💬 **Escenario** |
|:----------------|:----------:|:----------------|
| `UserMatchProfile` (base) | Variable (404, 400, etc.) | Excepción base de dominio con código HTTP configurable por caso de uso |
| `InvalidInputException` | 400 | Datos de entrada inválidos a nivel de dominio (horario inválido, tag con nombre en blanco, etc.) |
| `NotFoundException` | 404 | Recurso no encontrado (match, categoría o tag inexistente) |
| `MethodArgumentNotValidException` | 400 | Validación de campos del DTO fallida (`@NotNull`, `@NotBlank`, `@Size`, `@DecimalMin`, etc.) |
| `Exception` (genérica) | 500 | Error inesperado del servidor |

</div>

---

### ✅ Beneficios del manejo centralizado

<div align="center">

| 🎯 **Beneficio** | 📋 **Descripción** |
|:-----------------|:-------------------|
| **🎯 Uniformidad** | Todas las respuestas de error tienen el mismo formato JSON estandarizado |
| **🔧 Mantenibilidad** | Agregar nuevas excepciones no requiere modificar cada controlador |
| **🔒 Seguridad** | Oculta los detalles internos del servidor y evita exponer trazas sensibles |
| **📍 Trazabilidad** | Cada error incluye código HTTP y descripción del fallo; los errores inesperados se loguean con nivel ERROR |
| **🤝 Integración fluida** | Facilita la comunicación con frontend y herramientas como Postman/Swagger |

</div>

---

> Gracias a este enfoque, el microservicio de Social Matching logra un manejo de errores **robusto**, **escalable** y **seguro**, garantizando una experiencia de usuario más confiable y profesional.

---


---

## 9. 🧪 Evidencia de las pruebas y cómo ejecutarlas

El microservicio de **Social Matching** implementa una **estrategia integral de pruebas** que garantiza la calidad, funcionalidad y confiabilidad del código mediante pruebas unitarias cubiertas con JaCoCo.

---

### 🎯 Tipos de pruebas implementadas

<div align="center">

| 🧪 **Tipo de Prueba** | 📋 **Descripción** | 🛠️ **Herramientas** |
|:---------------------|:-------------------|:--------------------|
| **Pruebas Unitarias de Casos de Uso** | Validan el funcionamiento aislado de `MatchingServiceImpl`, `CategoryServiceImpl` y `RecommendationsUseCaseImpl` con mocks de puertos | ![JUnit](https://img.shields.io/badge/JUnit_5-25A162?style=flat&logo=junit5&logoColor=white) ![Mockito](https://img.shields.io/badge/Mockito-C5D928?style=flat) |
| **Pruebas del Algoritmo de Afinidad** | Verifican el cálculo correcto del score de afinidad en `AffinityCalculatorImpl` con distintas combinaciones de perfiles | ![JUnit](https://img.shields.io/badge/JUnit_5-25A162?style=flat&logo=junit5&logoColor=white) |
| **Pruebas de Dominio** | Verifican la lógica de negocio pura en las entidades `Match`, `Category`, `Tag` y `Schedule` | ![JUnit](https://img.shields.io/badge/JUnit_5-25A162?style=flat&logo=junit5&logoColor=white) |
| **Pruebas de Controlador** | Validan los endpoints REST de `MatchController` y `CategoryController` con MockMvc | ![Spring Test](https://img.shields.io/badge/Spring_Test-6DB33F?style=flat&logo=spring&logoColor=white) |
| **Pruebas de Mappers** | Verifican el mapeo correcto entre capas usando `MatchApplicationMapper` y `CategoryAppMapper` | ![JUnit](https://img.shields.io/badge/JUnit_5-25A162?style=flat&logo=junit5&logoColor=white) |
| **Pruebas de Adaptadores** | Validan `MatchRepositoryAdapter` y `CategoryRepository` con mocks de dependencias externas | ![Mockito](https://img.shields.io/badge/Mockito-C5D928?style=flat) |
| **Pruebas de Mappers de Persistencia** | Verifican el mapeo entre dominio y documentos MongoDB en `MatchPersistenceMapper` y `CategoryPersistenceMapper` | ![JUnit](https://img.shields.io/badge/JUnit_5-25A162?style=flat&logo=junit5&logoColor=white) |

</div>

---

### 🚀 Cómo ejecutar las pruebas

#### **1️⃣ Ejecutar todas las pruebas unitarias**

```bash
mvn test
```

#### **2️⃣ Ejecutar pruebas con reporte de cobertura JaCoCo**

```bash
mvn clean verify
```

#### **3️⃣ Ejecutar una prueba específica**

```bash
mvn test -Dtest=MatchingServiceImplTest
```

#### **4️⃣ Ejecutar pruebas desde IntelliJ IDEA**

1. Click derecho sobre la carpeta `src/test/java`
2. Selecciona **"Run 'Tests in...'**
3. Ver resultados en el panel inferior

---

### 🧪 Clases de prueba implementadas

<div align="center">

| 🧪 **Clase de Prueba** | 📋 **Qué valida** |
|:-----------------------|:------------------|
| `MatchingServiceImplTest` | Creación, consulta, actualización de estado y eliminación de matches; integración con cálculo de afinidad |
| `CategoryServiceImplTest` | CRUD de categorías y tags; validaciones de negocio en el catálogo de intereses |
| `RecommendationsUseCaseImplTest` | Generación de recomendaciones de usuarios basadas en el score de afinidad |
| `AffinityCalculatorImplTest` | Cálculo de scores de intereses, horarios y total bajo distintas combinaciones de perfiles |
| `MatchControllerTest` | Todos los endpoints REST de `MatchController` con MockMvc |
| `CategoryControllerTest` | Todos los endpoints REST de `CategoryController` con MockMvc |
| `MatchApplicationMapperTest` | Mapeos entre request/response REST ↔ dominio para matches y affinity scores |
| `CategoryAppMapperTest` | Mapeos entre request/response REST ↔ dominio para categorías y tags |
| `MatchRepositoryAdapterTest` | Adaptador de persistencia de matches con mocks del repositorio MongoDB |
| `CategoryRepositoryTest` | Adaptador de persistencia de categorías y tags con mocks del repositorio MongoDB |
| `MatchPersistenceMapperTest` | Mapeos entre dominio ↔ documentos de MongoDB para matches y affinity scores |
| `CategoryPersistenceMapperTest` | Mapeos entre dominio ↔ documentos de MongoDB para categorías y tags |
| `CategoryTest` | Validaciones de negocio en la entidad `Category` |
| `TagTest` | Validaciones de negocio en la entidad `Tag` (nombre, categoría, longitud) |
| `GlobalExceptionHandlerTest` | Manejo centralizado de excepciones del microservicio |

</div>

---

### 🧪 Ejemplo de prueba unitaria

```java
@Test
@DisplayName("Crear match calcula afinidad y persiste con estado PENDING")
void createMatch_shouldCalculateAffinityAndSaveAsPending() {
    MatchRequest request = new MatchRequest();
    request.setRequesterId(UUID.randomUUID());
    request.setTargetId(UUID.randomUUID());

    when(profileServicePort.getUserProfile(request.getRequesterId())).thenReturn(requesterProfile);
    when(profileServicePort.getUserProfile(request.getTargetId())).thenReturn(targetProfile);
    when(affinityCalculator.calculate(requesterProfile, targetProfile)).thenReturn(affinityScore);
    when(matchRepository.save(any())).thenReturn(match);

    MatchResponse result = matchingService.createMatch(request);

    verify(affinityCalculator).calculate(requesterProfile, targetProfile);
    verify(matchRepository).save(any());
    assertNotNull(result);
    assertEquals(MatchStatus.PENDING, result.getStatus());
}
```

---

### 🖼️ Evidencias de ejecución

*(Adjunta aquí la captura del reporte JaCoCo)*

---

### ✅ Criterios de aceptación de pruebas

Para considerar el sistema correctamente probado, se debe cumplir:

- ✅ **Todas las pruebas en estado PASSED** (sin fallos)
- ✅ **Cero errores de compilación** en el código de pruebas
- ✅ **Pruebas de casos felices y casos de error** implementadas
- ✅ **Lógica de dominio** cubierta con pruebas de entidades y algoritmo de afinidad
- ✅ **Reporte JaCoCo** generado en `target/site/jacoco/index.html`

---

## 10. 🗂️ Código de la implementación organizado en las respectivas carpetas

El microservicio de **Social Matching** sigue una **arquitectura hexagonal (puertos y adaptadores)** que separa las responsabilidades en capas bien definidas, promoviendo la escalabilidad, testabilidad y mantenibilidad del código.

---

### 📂 Estructura general del proyecto (Scaffolding)

```
matchpuff-social-matching/
│
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/matchpuff/matchingservice/matching_service/
│   │   │   │
│   │   │   ├── 📁 application/                              # 🔵 CAPA DE APLICACIÓN
│   │   │   │   ├── 📁 dto/
│   │   │   │   │   ├── 📁 request/   (MatchRequest, MatchUpdateRequest,
│   │   │   │   │   │                  AffinityScoreRequest, AffinityScoreUpdateRequest,
│   │   │   │   │   │                  UserMatchProfileRequest, UserMatchProfileUpdateRequest,
│   │   │   │   │   │                  TagRequest, ScheduleRequest)
│   │   │   │   │   └── 📁 response/  (MatchResponse, AffinityScoreResponse,
│   │   │   │   │                      UserMatchProfileResponse, RecommendationResponse,
│   │   │   │   │                      TagResponse, ScheduleResponse,
│   │   │   │   │                      CategoryResponse, CategoryWithTagsResponse)
│   │   │   │   ├── 📁 mapper/        (MatchApplicationMapper, CategoryAppMapper)
│   │   │   │   ├── 📁 service/       (MatchingService, AffinityCalculatorService)
│   │   │   │   └── 📁 usecase/       (MatchingServiceImpl, CategoryServiceImpl,
│   │   │   │                          RecommendationsUseCaseImpl,
│   │   │   │                          AffinityCalculatorServiceImpl)
│   │   │   │
│   │   │   ├── 📁 domain/                                   # 🟢 CAPA DE DOMINIO
│   │   │   │   ├── 📁 exceptions/    (UserMatchProfile, InvalidInputException,
│   │   │   │   │                      NotFoundException)
│   │   │   │   ├── 📁 model/         (Match, AffinityScore, UserMatchProfile,
│   │   │   │   │                      Category, Tag, Schedule)
│   │   │   │   │   └── 📁 enums/     (MatchStatus, CareerEnum, GenderEnum, DayOfWeekEnum)
│   │   │   │   └── 📁 ports/
│   │   │   │       ├── 📁 in/        (MatchUseCasePort, CategoryUseCasePort,
│   │   │   │       │                  RecommendationsUseCasePort)
│   │   │   │       └── 📁 out/       (MatchRepositoryPort, CategoryRepositoryPort,
│   │   │   │                          ProfileServicePort)
│   │   │   │
│   │   │   ├── 📁 entrypoints/                              # 🟠 ENTRADA (DRIVING ADAPTERS)
│   │   │   │   ├── 📁 advice/        (GlobalExceptionHandler, ErrorResponse)
│   │   │   │   └── 📁 rest/
│   │   │   │       └── 📁 controller/ (MatchController, CategoryController)
│   │   │   │
│   │   │   └── 📁 infrastructure/                           # 🟠 INFRAESTRUCTURA (DRIVEN ADAPTERS)
│   │   │       ├── 📁 adapters/
│   │   │       │   ├── 📁 adapter/   (MatchRepositoryAdapter, CategoryRepository)
│   │   │       │   └── 📁 persistence/
│   │   │       │       ├── 📁 entity/   (MatchDocument, AffinityScoreDocument,
│   │   │       │       │                 CategoryDocument, TagDocument)
│   │   │       │       ├── 📁 mapper/   (MatchPersistenceMapper, CategoryPersistenceMapper)
│   │   │       │       └── 📁 repository/ (MatchMongoRepository, MongoCategoryRepository,
│   │   │       │                           MongoTagRepository)
│   │   │       ├── 📁 config/        (DevSecurityConfig, ProdSecurityConfig,
│   │   │       │                      FeignConfig, SwaggerConfig,
│   │   │       │                      StartupDependencyCheck)
│   │   │       └── 📁 external/
│   │   │           └── 📁 profile/   (ProfileServiceAdapter)
│   │   │               ├── 📁 client/  (ProfileFeignClient)
│   │   │               └── 📁 dto/     (UserMatchProfileDto)
│   │   │
│   │   └── 📁 resources/
│   │       └── 📄 application.yml
│   │
│   └── 📁 test/                                             # 🧪 PRUEBAS
│       └── 📁 java/.../
│           ├── 📁 application/mapper/   (MatchApplicationMapperTest, CategoryAppMapperTest)
│           ├── 📁 application/service/  (AffinityCalculatorImplTest)
│           ├── 📁 application/usecase/  (MatchingServiceImplTest, CategoryServiceImplTest,
│           │                             RecommendationsUseCaseImplTest)
│           ├── 📁 domain/model/         (CategoryTest, TagTest)
│           ├── 📁 entrypoints/advice/   (GlobalExceptionHandlerTest)
│           ├── 📁 entrypoints/rest/controller/ (MatchControllerTest, CategoryControllerTest)
│           └── 📁 infrastructure/adapters/    (MatchRepositoryAdapterTest, CategoryRepositoryTest,
│                                               MatchPersistenceMapperTest,
│                                               CategoryPersistenceMapperTest)
│
├── 📁 .github/workflows/                                     # 🔄 CI/CD
│   ├── 📄 ci.yml
│   ├── 📄 cd-azure.yml
│   └── 📄 sonar.yml
├── 📄 Dockerfile
├── 📄 docker-compose.yml
├── 📄 pom.xml
└── 📄 README.md
```

---

> ℹ️ El código fuente está organizado siguiendo estrictamente la arquitectura hexagonal para garantizar la separación de responsabilidades y facilitar el mantenimiento y la extensión del sistema.

### 🏛️ Arquitectura Hexagonal Implementada

<div align="center">

| 🎨 **Capa** | 📋 **Responsabilidad** | 🔗 **Dependencias** |
|:-----------|:----------------------|:-------------------|
| **🟢 Domain** | Lógica de negocio pura, entidades (`Match`, `AffinityScore`, `Category`, `Tag`), enums y puertos (interfaces) | ❌ Ninguna (independiente) |
| **🔵 Application** | Casos de uso (`MatchingServiceImpl`, `CategoryServiceImpl`, `RecommendationsUseCaseImpl`, `AffinityCalculatorImpl`), DTOs y mappers | ✅ Solo `Domain` |
| **🟠 Entrypoints** | Controladores REST y manejador global de excepciones | ✅ `Domain` + `Application` |
| **🟠 Infrastructure** | Adaptadores MongoDB, Feign Client para Perfiles, configuración de seguridad y Spring | ✅ `Domain` + `Application` |

</div>

**Flujo de dependencias:** `Entrypoints / Infrastructure → Application → Domain`

---

### 🎯 Principios de diseño aplicados

<div align="center">

| ✅ **Principio** | 📋 **Implementación** |
|:----------------|:---------------------|
| **Separación de responsabilidades** | Cada capa tiene un propósito único y bien definido |
| **Inversión de dependencias** | Las capas externas dependen de interfaces (puertos) definidas en el dominio |
| **Independencia del framework** | La lógica de negocio y el algoritmo de afinidad no dependen de Spring ni de MongoDB |
| **Patrón Ports & Adapters** | Los casos de uso consumen puertos; la infraestructura los implementa |
| **Testabilidad** | Fácil crear pruebas unitarias mockeando puertos; controladores con MockMvc |
| **Mantenibilidad** | Cambios en una capa no afectan a las demás |

</div>  

---

## 11. 🚀 Ejecución del Proyecto

### 📋 Prerrequisitos
- **Java 21**
- **Maven 3.9+**
- **Docker & Docker Compose** (para ejecución containerizada)
- **MongoDB** (URI de conexión requerida)
- **Microservicio de Perfiles** en ejecución (URL requerida para la integración vía Feign)

### 🛠️ Opción 1: Ejecución Local (Maven)

```bash
# 1. Clonar repositorio
git clone https://github.com/<org>/matchpuff-social-matching.git

# 2. Copiar variables de entorno
cp .env.example .env
# Editar .env con tus credenciales reales

# 3. Ejecutar aplicación (perfil dev — sin SSL requerido)
mvn spring-boot:run
```

📍 **URL Local (dev):** `http://localhost:8080`  
📚 **Documentación API:** `http://localhost:8080/swagger-ui.html`

### 🐳 Opción 2: Ejecución con Docker Compose

```bash
# Asegúrate de tener el .env configurado
docker compose up --build
```

Esto levanta:
- `matching-service`: La aplicación en el puerto configurado

### ⚙️ Variables de Entorno

| Variable | Valor por defecto | Descripción |
|:---------|:-----------------|:------------|
| `MONGO_URI` | *(requerido)* | URI de conexión a MongoDB |
| `MONGODB_DATABASE` | `matchingservice` | Nombre de la base de datos |
| `PROFILE_SERVICE_URL` | *(requerido)* | URL base del microservicio de Perfiles |
| `PROFILE_SERVICE_PATH` | *(requerido)* | Path del endpoint de consulta de perfiles |
| `PORT` | `8080` | Puerto del servidor |
| `APP_STARTUP_FAIL_FAST` | `true` | Si la app falla al iniciar cuando no hay conexión a MongoDB |

## 12. ☁️ CI/CD y Despliegue en Azure

El proyecto implementa un **pipeline automatizado** con **GitHub Actions** para garantizar la calidad del código y el despliegue continuo en **Azure ECS**.

---

### 🔗 Enlaces de Despliegue

*(Adjunta aquí la URL del servicio desplegado en Azure)*

<div align="center">

| 🌍 Ambiente | 📝 Estado |
|:-----------|:---------|
| **🟢 Producción (Azure ECS)** | ![Active](https://img.shields.io/badge/Status-Active-success?style=flat) |

</div>

---

### 🔄 Pipeline de Automatización

El flujo de trabajo ejecuta los siguientes pasos en cada push o PR:

**CI (en PRs a `develop` y `main`, y pushes a `develop`):**

1. **Build & Test** — Compila el proyecto con Maven y ejecuta `mvn clean verify`.
2. **Cobertura JaCoCo** — Genera y sube el reporte de cobertura como artefacto de GitHub Actions.
3. **Docker Validation** — Construye la imagen Docker para verificar que el Dockerfile es correcto.
4. **SonarCloud** — Analiza la calidad y cobertura del código (pipeline separado `sonar.yml`).

**CD (en merges a `main`):**

1. **Login a Azure** — Configura credenciales de Azure con `Azure-actions/configure-Azure-credentials`.
2. **Build & Push a ECR** — Construye la imagen Docker y la sube a Amazon ECR.
3. **Deploy a ECS** — Actualiza la task definition y fuerza un nuevo despliegue en ECS con `Azure-actions/amazon-ecs-deploy-task-definition`.

---

### ☁️ Infraestructura

<div align="center">

| Componente | Servicio | Propósito |
|:-----------|:---------|:----------|
| **Compute** | ![Azure ECS](https://img.shields.io/badge/Azure_ECS-FF9900?logo=amazonAzure&logoColor=white) | Ejecución del contenedor Docker del microservicio en Fargate |
| **Registry** | ![ECR](https://img.shields.io/badge/Amazon_ECR-FF9900?logo=amazonAzure&logoColor=white) | Registro privado de imágenes Docker |
| **Database** | ![MongoDB](https://img.shields.io/badge/MongoDB-47A248?logo=mongodb&logoColor=white) | Persistencia de matches, categorías y tags |
| **Integration** | ![Feign](https://img.shields.io/badge/OpenFeign-6DB33F?logo=spring&logoColor=white) | Comunicación con el microservicio de Perfiles |
| **CI/CD** | ![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?logo=github-actions&logoColor=white) | Automatización de pruebas y despliegue continuo |
| **Quality** | ![SonarCloud](https://img.shields.io/badge/SonarCloud-F3702A?logo=sonarcloud&logoColor=white) | Análisis estático y cobertura de código |
| **API Docs** | ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black) | Documentación interactiva de endpoints REST |

</div>

---

### 📊 Evidencias de Despliegue

*(Adjunta aquí las capturas de pantalla de Azure ECS y el pipeline de GitHub Actions)*

---

## 13. 🤝 Contribuciones y Metodología

El equipo **Matchpuff** aplicó la metodología **Scrum** con sprints semanales para garantizar una entrega incremental de valor y mejora continua.

### 👥 Equipo Scrum

| Rol | Responsabilidad |
|:---|:---|
| **Product Owner** | Priorización del Backlog y maximización de valor. |
| **Scrum Master** | Facilitador del proceso y eliminación de impedimentos. |
| **Developers** | Diseño, implementación y pruebas de funcionalidades. |

### 🔄 Eventos y Artefactos

- **Sprints Semanales**: Ciclos cortos de desarrollo.
- **Daily Scrum**: Sincronización diaria (15 min).
- **Sprint Review & Retrospective**: Demostración de incrementos y mejora de procesos.
- **Backlogs**: Gestión de tareas en GitHub Projects.

### 🎯 Valores del Equipo
Compromiso, Coraje, Enfoque, Apertura y Respeto fueron los pilares para afrontar desafíos técnicos como la arquitectura hexagonal con Spring Boot 3.4.5, la integración con Feign Client hacia el microservicio de Perfiles, el diseño del algoritmo de afinidad multicriteria, el despliegue en Azure ECS con HTTPS y la construcción de un microservicio de social matching escalable y extensible.

---

<div align="center">

### 🐾 Equipo **Matchpuff**

![Team](https://img.shields.io/badge/Team-Matchpuff-blueviolet?style=for-the-badge&logo=github&logoColor=white)
![Course](https://img.shields.io/badge/Course-DOSW-orange?style=for-the-badge)
![Year](https://img.shields.io/badge/Year-2026--1-blue?style=for-the-badge)

> 💡 **Matchpuff Social Matching Service** es el microservicio encargado de gestionar las conexiones, la afinidad y las recomendaciones entre usuarios de la plataforma, siendo el núcleo social sobre el que se construyen las interacciones de Matchpuff.

**🎓 Escuela Colombiana de Ingeniería Julio Garavito**

</div>

---