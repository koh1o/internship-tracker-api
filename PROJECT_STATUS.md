# Internship Tracker API — текущее состояние

## Актуальность файла

Последнее обновление: **2026-09-12**.

Это актуальная стабильная точка проекта после:

- завершения CRUD для `Company`, `Vacancy` и `Application`;
- реализации бизнес-правил `Application`;
- реализации pagination / sorting / dynamic filtering / statistics;
- перевода схемы PostgreSQL на Flyway;
- перехода integration tests на PostgreSQL Testcontainers;
- добавления full application integration tests;
- добавления Swagger/OpenAPI;
- добавления Dockerfile и Docker Compose;
- добавления `User` и базового registration flow с BCrypt;
- реализации login credentials flow;
- подключения `spring-boot-starter-security`;
- добавления `SecurityFilterChain`, stateless session policy и Spring Security authentication через `AuthenticationManager`.

В проекте должен находиться один файл с точным названием:

```text
PROJECT_STATUS.md
```

---

## Текущий этап

Завершено:

```text
Company CRUD
Vacancy CRUD
Application CRUD и business rules
Application pagination / sorting / filtering
Application statistics
Flyway migrations
Repository integration testing
Testcontainers
Full application integration testing
Swagger/OpenAPI
Dockerfile / Docker Compose
User registration foundation
User login credentials
Spring Security foundation
```

Текущий крупный этап:

```text
Authentication / JWT
```

Spring Security foundation завершён. Login credentials теперь проверяются стандартным Spring Security authentication flow.

Текущий login flow:

```text
POST /api/auth/login
→ LoginRequest validation
→ UserService.authenticate(...)
→ UsernamePasswordAuthenticationToken
→ AuthenticationManager
→ ProviderManager
→ DaoAuthenticationProvider
├── CustomUserDetailsService
│   └── UserRepository.findByEmail(...)
└── PasswordEncoder
→ successful Authentication или BadCredentialsException
→ UserRepository.findByEmail(...)
→ UserMapper
→ 200 OK + UserResponse
```

При invalid credentials framework exception преобразуется в существующий public contract:

```text
BadCredentialsException
→ InvalidCredentialsException
→ 401 Unauthorized
→ "Invalid email or password"
```

`SecurityFilterChain` уже подключён:

```text
CSRF → disabled
SessionCreationPolicy → STATELESS
/api/auth/register → permitAll
/api/auth/login → permitAll
/v3/api-docs/** → permitAll
/swagger-ui/** → permitAll
/swagger-ui.html → permitAll
anyRequest() → пока permitAll
```

`STATELESS` означает, что authentication не сохраняется в server-side HTTP session. Успешный `/api/auth/login` поэтому пока не делает следующий HTTP request authenticated.

Следующий крупный шаг — JWT: сначала понять структуру token, claims, expiration и signature, затем выбрать актуальный Spring Security способ генерации/валидации по официальной документации. Только после рабочего JWT-механизма переводить остальные endpoints с `permitAll()` на `authenticated()`.

---

## Текущая стабильная точка

Последний рабочий code-коммит:

```text
042cdc1 Integrate Spring Security authentication
```

Предыдущие важные code-коммиты текущего этапа:

```text
cb9c52a Add OpenAPI documentation
5845f8b Add Docker and Compose setup
8ac460d Add user registration flow
4e1bab3 Implement user login credentials
042cdc1 Integrate Spring Security authentication
```

Предыдущий documentation-коммит перед текущим обновлением:

```text
29cd4a5 Document Swagger Docker and user registration
```

Ранее важные code-коммиты:

```text
abc0fe5 Add initial Flyway migration
90dd213 Add Testcontainers for PostgreSQL tests
dbf959c Add Company integration tests
423b1b9 Add Vacancy integration tests
b316b66 Add Application integration tests
```

Состояние Git после code push и до текущего documentation commit:

```text
On branch main
Your branch is up to date with 'origin/main'.

Changes not staged for commit:
    DECISIONS.md
    PROJECT_STATUS.md
```

Code changes уже отправлены в `origin/main`; документационные изменения готовятся отдельным commit.

Всего в проекте: **207 тестов**.

Расчёт:

```text
182 tests до User/auth блока
+ 14 registration tests
= 196

+ 3 первоначальных UserService login tests
+ 3 AuthController login tests
+ 2 первоначальных AuthIntegration login tests
= 204

+ 2 CustomUserDetailsServiceTest
+ 1 AuthIntegration missing-user scenario
= 207
```

---

## Среда

- [x] Java 21.
- [x] Spring Boot 4.1.0.
- [x] Maven Wrapper 3.9.16.
- [x] PostgreSQL 18 для локального запуска.
- [x] Docker Desktop / WSL 2.
- [x] PostgreSQL Testcontainers для tests.
- [x] Swagger/OpenAPI через springdoc.
- [x] Dockerfile и Docker Compose.
- [x] Git / GitHub.
- [x] Секреты не хранятся в Git.

Production/local datasource настраивается через environment variables с локальными defaults для URL и username:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://127.0.0.1:5432/internship_tracker}
spring.datasource.username=${DB_USERNAME:internship_tracker_app}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
```

Test suite не зависит от локального PostgreSQL и `DB_PASSWORD`, потому что integration tests используют PostgreSQL Testcontainer.

---

## Архитектура

Используется слоистая архитектура:

```text
HTTP request
→ Controller
→ Service
→ Repository
→ PostgreSQL
```

Пакеты:

```text
controller
service
repository
entity
dto
mapper
exception
configuration
specification
```

Entity напрямую клиенту не возвращаются. Mapping выполняется вручную.

Для auth registration flow текущая цепочка:

```text
POST /api/auth/register
→ AuthController
→ UserService.register(...)
→ PasswordEncoder
→ UserRepository
→ PostgreSQL
```

---

## Company

Полный CRUD завершён.

Endpoints:

```text
GET    /api/companies
POST   /api/companies
GET    /api/companies/{id}
PUT    /api/companies/{id}
DELETE /api/companies/{id}
```

Есть unit, controller, repository и full integration tests.

---

## Vacancy

Полный CRUD завершён.

`WorkFormat`:

```text
OFFICE
REMOTE
HYBRID
NOT_SPECIFIED
```

Связь:

```text
Vacancy Many-to-One Company
FetchType.LAZY
EnumType.STRING
```

Endpoints:

```text
POST   /api/vacancies
GET    /api/vacancies
GET    /api/vacancies/{id}
PUT    /api/vacancies/{id}
DELETE /api/vacancies/{id}
```

---

## Application

`ApplicationStatus`:

```text
PLANNED
APPLIED
TEST_TASK
INTERVIEW
OFFER
REJECTED
WITHDRAWN
```

Связь:

```text
Application Many-to-One Vacancy
FetchType.LAZY
EnumType.STRING
```

Endpoints:

```text
POST   /api/applications
GET    /api/applications
GET    /api/applications/statistics
GET    /api/applications/{id}
PUT    /api/applications/{id}
PATCH  /api/applications/{id}/status
DELETE /api/applications/{id}
```

Business rules:

```text
nextContactAt >= appliedAt
status != PLANNED → appliedAt required
PUT не меняет status
смена status выполняется отдельным PATCH
same status → idempotent
```

Разрешённые переходы:

```text
PLANNED   → APPLIED, WITHDRAWN
APPLIED   → TEST_TASK, INTERVIEW, REJECTED, WITHDRAWN
TEST_TASK → INTERVIEW, REJECTED, WITHDRAWN
INTERVIEW → OFFER, REJECTED, WITHDRAWN
OFFER     → нет переходов
REJECTED  → нет переходов
WITHDRAWN → нет переходов
```

---

## Pagination / sorting / filtering

Для `Application` реализованы pagination, sorting и dynamic filtering через:

```text
ApplicationFilter
Specification<Application>
JpaSpecificationExecutor<Application>
PagedResponse<T>
```

Defaults:

```text
page=0
size=10
sortBy=createdAt
direction=DESC
```

Allow-list sort fields:

```text
createdAt
appliedAt
nextContactAt
status
```

Filters:

```text
status
vacancyId
companyId
appliedAtFrom
appliedAtTo
workFormat
nextContactAtFrom
nextContactAtTo
```

Date boundaries включающие. Противоречивые ranges отклоняются как bad request.

---

## Statistics

Endpoint:

```text
GET /api/applications/statistics
```

Текущая реализация:

```text
1 x count()
7 x countByStatus(...)
```

Итого 8 queries. Оптимизация через `GROUP BY status` отложена.

---

## Flyway

Flyway — source of truth для схемы.

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Versioned migrations:

```text
V1__create_initial_schema.sql
V2__create_users_table.sql
```

`V1` создаёт:

```text
companies
vacancies
applications
```

`V2` создаёт:

```text
users
```

Таблица `users` содержит:

```text
id
email UNIQUE NOT NULL
password_hash NOT NULL
created_at NOT NULL
updated_at NOT NULL
```

Правило:

```text
Применённую versioned migration не редактировать.
Изменения схемы делать через V3, V4, ...
```

`V2` уже применена к локальной development database. Flyway также автоматически применяет `V1` и `V2` к новой PostgreSQL Testcontainer database.

---

## Testcontainers и уровни tests

PostgreSQL image:

```text
postgres:18-alpine
```

Общая test configuration:

```text
TestcontainersConfiguration
PostgreSQLContainer bean
@ServiceConnection
```

Repository integration tests используют реальную PostgreSQL и сохраняют:

```java
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
```

Full integration tests используют:

```java
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
```

Проверяемая цепочка:

```text
MockMvc
→ Controller
→ Service
→ Repository
→ Hibernate
→ PostgreSQL Testcontainer
```

Текущие уровни tests:

```text
unit tests
controller tests
repository integration tests
full application integration tests
```

---

## Swagger / OpenAPI

Подключён:

```text
org.springdoc:springdoc-openapi-starter-webmvc-ui:3.1.0
```

Проверены:

```text
/v3/api-docs
/swagger-ui.html
```

Добавлена `OpenApiConfiguration` с:

```text
title: Internship Tracker API
version: 1.0
description: REST API for tracking companies, vacancies and internship applications
```

На `CompanyController`, `VacancyController` и `ApplicationController` добавлены `@Tag`.

Endpoint изменения статуса `Application` документирован подробнее через `@Operation` и `@ApiResponses`, включая `200`, `400` и `404`.

Принцип: не превращать controllers в набор Swagger-аннотаций; подробные описания добавлять там, где они действительно улучшают контракт API.

---

## Docker / Compose

Добавлены:

```text
Dockerfile
.dockerignore
compose.yaml
```

`Dockerfile` использует multi-stage build:

```text
eclipse-temurin:21-jdk → Maven build
eclipse-temurin:21-jre → runtime image
```

Compose поднимает два services:

```text
app
db
```

Внутри Compose network приложение подключается к PostgreSQL по:

```text
db:5432
```

`db` — DNS-имя Compose service, а не `localhost`.

App публикуется на Windows host как:

```text
127.0.0.1:8080 → container:8080
```

PostgreSQL container наружу на host port `5432` не публикуется, поэтому он не конфликтует с локальным PostgreSQL Windows.

Для PostgreSQL используется named volume:

```text
postgres_data
```

Проверено, что данные сохраняются после:

```text
Ctrl+C

docker compose down

docker compose up
```

`docker compose down -v` удалит named volume вместе с данными.

`db` имеет healthcheck через `pg_isready`, а `app` ожидает `service_healthy`.

`DB_PASSWORD` передаётся через environment variable и не хранится в Git.

---

## User / registration / login credentials

Добавлена Entity `User`:

```text
id
email
passwordHash
createdAt
updatedAt
```

Добавлены:

```text
UserRepository
RegistrationRequest
LoginRequest
UserResponse
UserMapper
UserService
AuthController
PasswordConfiguration
EmailAlreadyExistsException
InvalidCredentialsException
```

Registration endpoint:

```text
POST /api/auth/register
```

Успешная регистрация возвращает:

```text
201 Created
```

Повторный email возвращает:

```text
409 Conflict
```

Request принимает raw password:

```json
{
  "email": "student@example.com",
  "password": "secret123"
}
```

Raw password не сохраняется. `UserService` вызывает:

```java
passwordEncoder.encode(password)
```

и сохраняет только `passwordHash`.

`UserResponse` не содержит ни raw password, ни password hash.

Используется:

```text
spring-boot-starter-security
BCryptPasswordEncoder
PasswordEncoder
```

Отдельная dependency `spring-security-crypto` заменена на Spring Boot security starter. `PasswordEncoder` и BCrypt продолжают использоваться через зависимости starter.

Email uniqueness защищается на двух уровнях:

```text
UserService.existsByEmail(...) → понятная business error
UNIQUE(email) в PostgreSQL     → окончательная integrity guarantee
```

Registration tests:

```text
PasswordConfigurationTest — BCrypt properties
UserRepositoryTest         — find / exists / UNIQUE
UserServiceTest            — registration business logic
AuthControllerTest         — HTTP / validation / 409
AuthIntegrationTest        — full flow + real BCrypt + PostgreSQL
```

`AuthIntegrationTest` доказывает, что сохранённый password hash:

```text
не равен raw password
и PasswordEncoder.matches(raw, hash) == true
```

Login endpoint:

```text
POST /api/auth/login
```

Успешные credentials возвращают:

```text
200 OK + UserResponse
```

Missing user и wrong password возвращают одинаковый ответ:

```text
401 Unauthorized
Invalid email or password
```

Добавлены Spring Security components:

```text
SecurityConfiguration
CustomUserDetailsService
AuthenticationManager
ProviderManager
DaoAuthenticationProvider
UsernamePasswordAuthenticationToken
SessionCreationPolicy.STATELESS
```

`CustomUserDetailsService` реализует:

```text
UserDetailsService.loadUserByUsername(username)
→ UserRepository.findByEmail(username)
→ JPA User
→ Spring Security UserDetails
```

В текущем проекте роль `username` выполняет email.

Password при login больше не проверяется вручную в `UserService`. Текущий flow:

```text
UserService
→ UsernamePasswordAuthenticationToken.unauthenticated(email, password)
→ AuthenticationManager.authenticate(...)
→ DaoAuthenticationProvider
→ CustomUserDetailsService
→ PasswordEncoder
```

`DaoAuthenticationProvider` использует `PasswordEncoder` для проверки raw password против сохранённого BCrypt hash.

При invalid credentials `BadCredentialsException` преобразуется в `InvalidCredentialsException`, поэтому публичный HTTP contract остаётся прежним.

После successful authentication `UserService` временно делает второй `userRepository.findByEmail(email)`, чтобы вернуть JPA `User` для существующего `UserMapper`. Это сознательный временный компромисс до введения более удобного principal/auth response.

Security configuration:

```text
CSRF disabled
SessionCreationPolicy.STATELESS
register/login/Swagger → permitAll
остальные requests → пока permitAll
```

Login/security tests:

```text
CustomUserDetailsServiceTest — найденный / отсутствующий user
UserServiceTest              — AuthenticationManager success / invalid credentials / missing JPA user after auth
AuthControllerTest           — HTTP contract
AuthIntegrationTest          — real BCrypt + PostgreSQL + Spring Security authentication
```

Всего tests после блока: `207`.

Текущий `/api/auth/login` всё ещё не выдаёт JWT и не сохраняет authenticated state между запросами. Это ожидаемо при `STATELESS`: следующий request должен будет самостоятельно принести bearer token.

---

## Что понимается уверенно

- Git: working tree, staging, commit, push, diff, cached diff, untracked files.
- Controller → Service → Repository.
- Entity vs DTO, Mapper, Bean Validation.
- `Many-to-One`, `FetchType.LAZY`, `EnumType.STRING` на практическом уровне.
- `Optional`, `Page`, `Pageable`, `Sort` на базовом уровне.
- Specification и `JpaSpecificationExecutor` на уровне текущего проекта.
- unit vs controller vs repository integration vs full application integration.
- зачем проверять DB state после изменяющих HTTP-операций.
- Flyway как source of truth и правило неизменности применённых migrations.
- базовая роль Testcontainers и `@ServiceConnection`.
- image vs container, named volume, Compose services.
- что `localhost` относится к текущему network environment.
- зачем password хранится как hash, а не plaintext.
- почему BCrypt для login проверяется через `matches(...)`, а не через `encode(...) + equals(...)`.
- почему response пользователя не должен содержать `passwordHash`.
- различие authentication и authorization на базовом уровне.
- почему missing email и wrong password должны давать одинаковый public error.
- различие `400 Bad Request` для invalid request и `401 Unauthorized` для failed authentication.
- роль `SecurityFilterChain` перед MVC Controller.
- разницу между `permitAll()` и `SessionCreationPolicy.STATELESS`.
- базовую схему session-based authentication: cookie содержит session id, а `SecurityContext` хранится server-side.
- зачем `CustomUserDetailsService` преобразует JPA `User` в Spring Security `UserDetails`.
- базовую цепочку `AuthenticationManager → ProviderManager → DaoAuthenticationProvider`.
- почему `UserService` больше не должен вручную вызывать `PasswordEncoder.matches(...)` для login.

---

## Что понимается частично

- `@Transactional` глубже базового уровня.
- persistence context и Entity lifecycle.
- LAZY relationships вне active session.
- `equals/hashCode` для JPA Entity.
- Criteria API глубже используемых операций.
- SQL сложных Specifications.
- nullable sorting.
- `Page` vs `Slice`.
- Testcontainers lifecycle глубже Spring-managed bean.
- JPQL / projection / `GROUP BY`.
- Docker networking глубже практической схемы `host ↔ app ↔ db`.
- внутреннее устройство Spring Security filter chain глубже используемой конфигурации.
- `SecurityContext` и lifecycle `Authentication` между filters глубже базового уровня.
- `AuthenticationManager` / `AuthenticationProvider` глубже текущей цепочки.
- authorities / roles (`hasAuthority`, `hasRole`, `ROLE_...`) ещё не используются.
- JWT structure / claims / signing / validation — ещё не изучены в проекте.

---

## Технический долг

- Pagination / sorting / filtering пока только для `Application`.
- Статистика делает 8 queries.
- Сохранены старые overloads `getAllApplications(...)`.
- Нет tie-break sorting по `id`.
- `PagedResponse<T>` не содержит `first`, `last`, `hasNext`.
- `spring.jpa.open-in-view` явно не отключён.
- Mockito dynamic agent warning остаётся.
- В `Company.java` есть `import jakarta.persistence.*;` — отдельный cleanup.
- В integration tests повторяется setup `Company → Vacancy → Application`; helpers можно добавить позднее.
- Email пока не нормализуется (`trim` / lowercase).
- Password policy пока ограничивается `@NotBlank`; минимальная длина ещё не определена.
- `existsByEmail` даёт понятный `409`, но race condition при параллельной регистрации пока может закончиться DB constraint exception.
- Login endpoint пока только проверяет credentials и не создаёт authenticated state между запросами.
- `anyRequest().permitAll()` пока временно оставляет основной API публичным до появления JWT validation.
- После successful authentication выполняется второй `findByEmail(...)` для получения JPA `User`.
- Authority `"USER"` пока захардкожена в `CustomUserDetailsService`; полноценная role model отложена.
- JWT ещё не реализован.
- Данные `Company` / `Vacancy` / `Application` ещё не привязаны к конкретному `User`.
- Нет финального README.
- Нет GitHub Actions.
- Нет `Interview`.
- Нет истории status changes.
- MapStruct не используется без необходимости.

---

## Следующее задание

### JWT foundation

Следующий блок разработки:

1. Разобрать JWT с нуля: `header.payload.signature`.
2. Понять, что payload обычно можно прочитать без секрета и почему JWT не является шифрованием.
3. Разобрать claims: `sub`, `iat`, `exp` и минимальные данные, которые действительно нужны проекту.
4. Понять назначение cryptographic signature: token нельзя считать доверенным только потому, что его удалось декодировать.
5. По актуальной официальной документации Spring Security выбрать способ выдачи и проверки JWT для Spring Boot 4.1.x.
6. Определить конфигурацию signing key / secret через environment variables; секрет не хранить в Git.
7. Только после этого реализовать token generation и validation.
8. После рабочего JWT поменять основной access rule с временного `permitAll()` на `authenticated()` и проверить `401` для request без token.

Ограничения следующего шага:

```text
не добавлять JWT library случайно без проверки актуальной Spring Security документации
не хранить signing secret в Git
не класть password или passwordHash в JWT
не считать Base64 encoding шифрованием
не переключать anyRequest() на authenticated() раньше рабочего token validation
не добавлять refresh tokens / roles / OAuth раньше базового access token
```

---

## Вопросы для повторения на следующую сессию

1. Где находится `SecurityFilterChain` относительно Controller?
2. Чем `permitAll()` отличается от `SessionCreationPolicy.STATELESS`?
3. Почему успешный `/api/auth/login` сейчас не делает следующий request authenticated?
4. Что хранится server-side в session-based authentication, а что обычно лежит в session cookie?
5. Зачем нужен `CustomUserDetailsService`?
6. Чем JPA `User` отличается от Spring Security `UserDetails`?
7. За что отвечают `AuthenticationManager`, `ProviderManager` и `DaoAuthenticationProvider`?
8. Кто теперь вызывает `PasswordEncoder.matches(...)` при login?
9. Почему invalid email и wrong password по-прежнему должны давать одинаковый `401`?
10. Почему мы пока оставили `.anyRequest().permitAll()`?
11. Что должен будет принести каждый protected request после перехода на stateless JWT?
12. Почему JWT нужно сначала проверять cryptographically, а не просто декодировать?

---

## Рекомендуемый documentation-коммит

После замены обоих файлов:

```powershell
git status --short
git --no-pager diff --check
git --no-pager diff -- PROJECT_STATUS.md DECISIONS.md
```

Затем:

```powershell
git add PROJECT_STATUS.md DECISIONS.md
git --no-pager diff --cached --check
git --no-pager diff --cached --stat
git status --short
git commit -m "Document Spring Security authentication"
git push
git status
```

Ожидаемое состояние после push:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```
