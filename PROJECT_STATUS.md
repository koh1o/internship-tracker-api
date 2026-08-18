# Internship Tracker API — текущее состояние

## Актуальность файла

Последнее обновление: **2026-08-18**.

Это актуальная стабильная точка проекта после:

- завершения CRUD для `Company`, `Vacancy` и `Application`;
- реализации бизнес-правил `Application`;
- реализации pagination / sorting / dynamic filtering / statistics;
- перевода схемы PostgreSQL на Flyway;
- перехода integration tests на PostgreSQL Testcontainers;
- добавления full application integration tests;
- добавления Swagger/OpenAPI;
- добавления Dockerfile и Docker Compose;
- добавления `User` и базового registration flow с BCrypt.

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
```

Текущий крупный этап:

```text
Authentication / Security foundation
```

Регистрация пользователя завершена. Следующий небольшой шаг — реализовать проверку credentials для login:

```text
email + raw password
→ UserRepository.findByEmail(...)
→ PasswordEncoder.matches(...)
→ User или InvalidCredentialsException
```

На следующем шаге **не добавлять JWT раньше времени**. Сначала нужно реализовать и протестировать обычную проверку email/password, затем HTTP login flow, и только после этого переходить к полноценной Security configuration и JWT.

---

## Текущая стабильная точка

Последний рабочий code-коммит:

```text
8ac460d Add user registration flow
```

Предыдущие важные code-коммиты текущего этапа:

```text
cb9c52a Add OpenAPI documentation
5845f8b Add Docker and Compose setup
8ac460d Add user registration flow
```

Последний documentation-коммит:

```text
f1dd1fc Document Testcontainers and integration testing
```

Ранее важные code-коммиты:

```text
abc0fe5 Add initial Flyway migration
90dd213 Add Testcontainers for PostgreSQL tests
dbf959c Add Company integration tests
423b1b9 Add Vacancy integration tests
b316b66 Add Application integration tests
```

Состояние Git после последнего push:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

Всего в проекте: **196 тестов**.

Расчёт после предыдущей стабильной точки в 182 tests:

```text
+ 2 PasswordConfigurationTest
+ 3 UserRepositoryTest
+ 4 UserServiceTest
+ 3 AuthControllerTest
+ 2 AuthIntegrationTest
= 196
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

## User / registration

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
UserResponse
UserMapper
UserService
AuthController
PasswordConfiguration
EmailAlreadyExistsException
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

Используется только:

```text
spring-security-crypto
BCryptPasswordEncoder
PasswordEncoder
```

Полный `spring-boot-starter-security` пока намеренно не подключён, чтобы не включать автоматическую защиту endpoints раньше готовой Security configuration.

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
- Spring Security filter chain и security context — ещё не изучены.
- authentication flow через Spring Security — ещё не реализован.
- JWT structure / signing / validation — ещё не изучены в проекте.

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
- Login ещё не реализован.
- Полный Spring Security ещё не подключён.
- JWT ещё не реализован.
- Данные `Company` / `Vacancy` / `Application` ещё не привязаны к конкретному `User`.
- Нет финального README.
- Нет GitHub Actions.
- Нет `Interview`.
- Нет истории status changes.
- MapStruct не используется без необходимости.

---

## Следующее задание

### Login credentials — Service layer

Первый шаг следующей сессии:

1. Создать `InvalidCredentialsException` с единым сообщением `Invalid email or password`.
2. Добавить в `UserService` метод:

```java
public User authenticate(String email, String password)
```

3. Внутри использовать один `findByEmail(email)`.
4. Если User не найден — `InvalidCredentialsException`.
5. Проверить raw password только через:

```java
passwordEncoder.matches(password, user.getPasswordHash())
```

6. При неверном password вернуть ту же `InvalidCredentialsException`.
7. Покрыть Service unit tests.
8. После этого отдельно перейти к HTTP login endpoint.

Ограничения следующего шага:

```text
не использовать passwordEncoder.encode(...) для login
не делать existsByEmail(...) + findByEmail(...)
не возвращать разные public errors для wrong email и wrong password
не добавлять JWT до готового credential-check flow
```

---

## Вопросы для повторения на следующую сессию

1. Чем authentication отличается от authorization?
2. Почему BCrypt password нельзя проверять через `encode(raw).equals(storedHash)`?
3. Что именно проверяет `PasswordEncoder.matches(raw, hash)`?
4. Почему `UserResponse` не должен содержать `passwordHash`?
5. Зачем одновременно нужны `existsByEmail(...)` и `UNIQUE(email)`?
6. Как race condition может пройти Service-проверку `existsByEmail(...)`, но быть остановлен PostgreSQL?
7. Чем `@WebMvcTest` регистрации отличается от `AuthIntegrationTest`?
8. Почему в integration test не нужно делать Mockito `verify(repository.save(...))`?
9. Почему внутри Docker Compose приложение обращается к PostgreSQL как `db:5432`, а не `localhost:5432`?
10. Чем `EXPOSE 8080` отличается от `ports: "127.0.0.1:8080:8080"`?
11. Почему `docker compose down` сохраняет named volume, а `down -v` удаляет его?
12. Почему после применения `V2__create_users_table.sql` её нельзя бездумно редактировать?

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
git commit -m "Document Swagger Docker and user registration"
git push
git status
```

Ожидаемое состояние после push:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```
