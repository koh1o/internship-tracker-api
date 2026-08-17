# Internship Tracker API — текущее состояние

## Актуальность файла

Последнее обновление: **2026-08-17**.

Это актуальная стабильная точка проекта после:

- завершения CRUD для `Company`, `Vacancy` и `Application`;
- реализации бизнес-правил `Application`;
- реализации pagination / sorting / filtering / statistics;
- перевода схемы PostgreSQL на Flyway;
- repository integration tests для `ApplicationSpecifications`;
- перехода integration tests на PostgreSQL Testcontainers;
- full application integration tests для `Company`, `Vacancy` и `Application`.

---

## Текущий этап

Завершено:

```text
Company CRUD
Vacancy CRUD
Application CRUD и business rules
Application pagination / sorting / filtering
Application statistics
Flyway initial migration
Repository integration testing
Testcontainers
Full application integration testing
```

Текущий блок тестирования считается завершённым.

Следующий этап:

```text
Swagger/OpenAPI
```

Пока не переходить к Security/JWT. После Swagger по плану идут Dockerfile / Compose, README и только затем Security/JWT.

---

## Текущая стабильная точка

Последний рабочий code-коммит:

```text
b316b66 Add Application integration tests
```

Последние важные code-коммиты текущего блока:

```text
90dd213 Add Testcontainers for PostgreSQL tests
dbf959c Add Company integration tests
423b1b9 Add Vacancy integration tests
b316b66 Add Application integration tests
```

Ранее важные коммиты:

```text
abc0fe5 Add initial Flyway migration
c8e939f Add application specification integration test
39fdeea Add application filtering integration tests
f5d8b95 Add comprehensive application filtering integration tests
ebad428 Refactor application repository test setup
b7f096a Test application date filter boundaries
6bec3ee Test application filtering with pagination and sorting
9abb007 Document integration testing progress
```

Состояние Git после последнего push:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

Всего в проекте: **182 теста**.

---

## Среда

- [x] Java 21.
- [x] Spring Boot 4.1.0.
- [x] Maven Wrapper 3.9.16.
- [x] PostgreSQL 18 для локального запуска.
- [x] Docker Desktop / WSL 2.
- [x] PostgreSQL Testcontainers для tests.
- [x] Git / GitHub.
- [x] Секреты не хранятся в Git.

Локальный datasource использует:

```properties
spring.datasource.password=${DB_PASSWORD}
```

Но test suite больше не зависит от локального PostgreSQL и `DB_PASSWORD`.

---

## Архитектура

Используется слоистая архитектура:

```text
HTTP
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

`CompanyIntegrationTest` покрывает:

- create;
- get existing;
- get missing;
- validation;
- update;
- delete.

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

`VacancyIntegrationTest` покрывает:

- create + проверка связи с `Company`;
- get existing;
- get missing;
- create with missing `Company`;
- update;
- validation;
- delete с проверкой, что `Company` остаётся.

Практически разобран `LazyInitializationException`: чтение lazy-связанных данных вне active Hibernate session может потребовать инициализацию proxy. Для проверки FK в detached Entity использовался `company.id`.

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

`ApplicationIntegrationTest` покрывает 11 representative scenarios:

- valid create;
- get existing;
- get missing;
- create with missing `Vacancy`;
- reject `APPLIED` without `appliedAt`;
- reject `nextContactAt < appliedAt`;
- valid transition `APPLIED → INTERVIEW`;
- invalid transition `APPLIED → OFFER` + unchanged DB state;
- idempotent `APPLIED → APPLIED`;
- PUT с изменением Vacancy/dates/notes без изменения status;
- DELETE с проверкой, что `Vacancy` и `Company` остаются.

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

Date boundaries включающие.

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

Migration:

```text
src/main/resources/db/migration/V1__create_initial_schema.sql
```

Создаёт `companies`, `vacancies`, `applications`.

Правило:

```text
Применённую versioned migration не редактировать.
Изменения схемы делать через V2, V3, ...
```

Flyway также автоматически применяется к PostgreSQL Testcontainer.

---

## Testcontainers

Используются test dependencies:

```text
spring-boot-testcontainers
testcontainers-postgresql
```

Общая конфигурация:

```text
TestcontainersConfiguration
```

PostgreSQL image:

```text
postgres:18-alpine
```

Container объявлен Spring bean и помечен `@ServiceConnection`.

`testcontainers-junit-jupiter` в итоговой конфигурации не используется.

Repository tests сохраняют:

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

MockMvc не поднимает внешний HTTP server.

---

## Что понимается уверенно

- Git: working tree, staging, commit, push, diff, cached diff, untracked files.
- Controller → Service → Repository.
- Entity vs DTO, Mapper, Bean Validation.
- `Many-to-One`, `FetchType.LAZY`, `EnumType.STRING`.
- `Optional`, `Page`, `Pageable`, `Sort` на базовом уровне.
- Specification и `JpaSpecificationExecutor`.
- unit vs controller vs repository integration vs full application integration.
- зачем проверять DB state после POST/PUT/PATCH/DELETE.
- почему `400` может появиться из validation или business logic.
- зачем Testcontainers и базовая роль `@ServiceConnection`.
- Flyway как source of truth и смысл checksum.

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
- Только V1 Flyway migration.
- Нет Swagger/OpenAPI.
- Нет Dockerfile / `compose.yaml`.
- Нет финального README.
- Нет `User`, Security/JWT.
- Нет GitHub Actions.
- Нет `Interview`.
- Нет истории status changes.
- MapStruct не используется без необходимости.

---

## Следующее задание

### Swagger/OpenAPI

Цель:

```text
Добавить автоматически генерируемое описание REST API и Swagger UI.
```

Порядок:

1. Проверить актуальную официальную документацию для текущего Spring Boot.
2. Подключить минимальную зависимость.
3. Запустить приложение.
4. Проверить OpenAPI JSON и Swagger UI.
5. Не перегружать код аннотациями.
6. Добавлять описания API только там, где они реально полезны.
7. Запустить tests.
8. Сделать отдельный commit.

Пока не добавлять Security/JWT.

---

## Вопросы для повторения

1. Чем `@WebMvcTest` отличается от `@SpringBootTest + @AutoConfigureMockMvc`?
2. Почему MockMvc в full integration test не означает controller-only test?
3. Что делает `@ServiceConnection`?
4. Кто создаёт test schema: Flyway или Hibernate?
5. Почему для invalid PATCH недостаточно проверить только `400`?
6. Почему Mockito interaction проверяется unit test, а не full integration test?
7. Почему `DELETE Application` дополнительно проверяет сохранение `Vacancy` и `Company`?
8. Почему lazy proxy может привести к `LazyInitializationException`?
9. Почему untracked файл не виден обычному `git diff --check`?
10. Почему Testcontainers делает tests воспроизводимее локальной test database?

---

## Рекомендуемый documentation-коммит

Перед коммитом:

```powershell
git status --short
git --no-pager diff --check
git --no-pager diff -- PROJECT_STATUS.md DECISIONS.md
```

После review:

```powershell
git add PROJECT_STATUS.md DECISIONS.md
git --no-pager diff --cached --check
git --no-pager diff --cached
git commit -m "Document Testcontainers and integration testing"
git push
git status
```
