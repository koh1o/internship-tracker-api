# Internship Tracker API — текущее состояние

## Актуальность файла

Последнее обновление: **2026-08-14**.

Это актуальная стабильная точка проекта после:

- завершения CRUD для `Company`, `Vacancy` и `Application`;
- реализации бизнес-правил `Application`;
- реализации пагинации, сортировки, динамической фильтрации и статистики;
- перевода схемы PostgreSQL на Flyway;
- добавления реальных integration tests для `ApplicationSpecifications` через Spring Data JPA, Hibernate и PostgreSQL;
- проверки включающих границ дат;
- проверки совместной работы фильтрации, сортировки и пагинации на реальной базе.

В проекте должен находиться один файл с точным названием:

```text
PROJECT_STATUS.md
```

---

## Текущий этап

Завершены этапы:

```text
Application CRUD и бизнес-правила
Application pagination / sorting / filtering
Application statistics
Flyway initial migration
Базовый блок repository integration testing
```

Текущий крупный этап:

```text
Углублённое тестирование
```

Следующий небольшой шаг на следующую сессию:

```text
Определить следующий уровень integration testing:
1. оценить необходимость Testcontainers сейчас;
2. решить, изолировать ли PostgreSQL repository tests через Testcontainers;
3. либо сначала добавить более высокоуровневые integration/controller scenarios.
```

Не переходить к Swagger, Docker, Security или JWT до завершения текущего блока тестирования.

---

## Текущая стабильная точка

Последний рабочий code-коммит:

```text
6bec3ee Test application filtering with pagination and sorting
```

Предыдущий documentation-коммит перед этим обновлением:

```text
ae10ca5 Document Flyway migration setup
```

Последние важные code-коммиты:

```text
abc0fe5 Add initial Flyway migration
c8e939f Add application specification integration test
39fdeea Add application filtering integration tests
f5d8b95 Add comprehensive application filtering integration tests
ebad428 Refactor application repository test setup
b7f096a Test application date filter boundaries
6bec3ee Test application filtering with pagination and sorting
```

Состояние Git после последнего push:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

Всего в проекте **158 тестов**.

Последний полный запуск:

```text
Tests run: 158
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

Изменения документации коммитятся отдельно от Java-кода.

---

## Среда и Git

- [x] JDK 21 установлен и используется проектом.
- [x] Настроен `JAVA_HOME`.
- [x] Используется Maven Wrapper 3.9.16.
- [x] Установлен и настроен Git.
- [x] Создан публичный GitHub-репозиторий `internship-tracker-api`.
- [x] Ветка `main` связана с `origin/main`.
- [x] Настроен `.gitignore`.
- [x] Секреты не хранятся в Git.
- [x] Пароль PostgreSQL передаётся через `DB_PASSWORD`.
- [x] Перед коммитами проверяются `git status`, обычный diff, staged diff и `diff --check`.
- [x] Code- и documentation-коммиты разделяются.
- [x] После push проверяется чистый working tree.

Полезные команды:

```powershell
git status --short
git --no-pager diff --check
git --no-pager diff
git --no-pager diff --cached --check
git --no-pager diff --cached
git status
```

---

## Spring Boot и PostgreSQL

- [x] Spring Boot 4.1.0.
- [x] Java 21.
- [x] Maven.
- [x] Spring Web.
- [x] Spring Data JPA / Hibernate.
- [x] PostgreSQL.
- [x] Bean Validation.
- [x] `GET /api/hello`.
- [x] Приложение запускается на порту 8080.
- [x] База `internship_tracker` настроена.
- [x] Repository tests могут работать с реальным PostgreSQL.
- [x] Flyway управляет схемой.
- [x] Hibernate проверяет схему через `ddl-auto=validate`.

PostgreSQL CLI:

```text
C:\Program Files\PostgreSQL\18\bin\psql.exe
```

Пароль Spring datasource:

```properties
spring.datasource.password=${DB_PASSWORD}
```

Секреты и реальные `.env` не коммитить.

---

## Company

Полный CRUD завершён.

- [x] Entity и timestamps.
- [x] Repository и repository test.
- [x] Service и unit tests.
- [x] Request/response DTO.
- [x] Ручной Mapper и mapper tests.
- [x] Bean Validation.
- [x] `ResourceNotFoundException`.
- [x] `ErrorResponse` и `GlobalExceptionHandler`.
- [x] Controller и MockMvc tests.

Endpoint:

```text
GET    /api/companies
POST   /api/companies
GET    /api/companies/{id}
PUT    /api/companies/{id}
DELETE /api/companies/{id}
```

---

## Vacancy

Полный CRUD завершён.

- [x] `WorkFormat`: `OFFICE`, `REMOTE`, `HYBRID`, `NOT_SPECIFIED`.
- [x] `Many-to-One` с `Company`.
- [x] `FetchType.LAZY`.
- [x] `EnumType.STRING`.
- [x] `null workFormat → NOT_SPECIFIED`.
- [x] Repository, Service, Controller, DTO, Mapper и tests.
- [x] `404` для отсутствующих `Vacancy` и `Company`.

Endpoint:

```text
POST   /api/vacancies
GET    /api/vacancies
GET    /api/vacancies/{id}
PUT    /api/vacancies/{id}
DELETE /api/vacancies/{id}
```

---

## Application

### CRUD и модель

- [x] `ApplicationStatus`:
  - `PLANNED`;
  - `APPLIED`;
  - `TEST_TASK`;
  - `INTERVIEW`;
  - `OFFER`;
  - `REJECTED`;
  - `WITHDRAWN`.
- [x] `Many-to-One` с `Vacancy`.
- [x] `FetchType.LAZY`.
- [x] `EnumType.STRING`.
- [x] `appliedAt`, `nextContactAt`, `notes`.
- [x] Repository, DTO, Mapper, Service, Controller и tests.
- [x] POST / GET / PUT / DELETE.
- [x] Отдельный `PATCH` для изменения статуса.
- [x] Обычный `PUT` не меняет статус.

Endpoint:

```text
POST   /api/applications
GET    /api/applications
GET    /api/applications/statistics
GET    /api/applications/{id}
PUT    /api/applications/{id}
PATCH  /api/applications/{id}/status
DELETE /api/applications/{id}
```

### Бизнес-правила

- [x] `nextContactAt >= appliedAt`.
- [x] Равные даты разрешены.
- [x] Для статусов кроме `PLANNED` требуется `appliedAt`.
- [x] `PLANNED` может существовать без `appliedAt`.
- [x] Переходы статусов проверяются в Service.
- [x] Повторная установка текущего статуса идемпотентна.
- [x] При неизменном статусе не вызываются `setStatus()` и лишний `save()`.

Переходы:

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

## Пагинация, сортировка и фильтрация Application

### Пагинация

- [x] `Page`, `Pageable`, `PageRequest`.
- [x] Собственный `PagedResponse<T>`.
- [x] Entity и Spring `Page` не возвращаются напрямую клиенту.
- [x] `page >= 0`.
- [x] `1 <= size <= 100`.
- [x] Пустая страница за пределами данных возвращает `200 OK`.

Значения по умолчанию:

```text
page=0
size=10
sortBy=createdAt
direction=DESC
```

### Сортировка

Разрешённые поля:

```text
createdAt
appliedAt
nextContactAt
status
```

- [x] `ASC` и `DESC`.
- [x] Allow-list полей сортировки.
- [x] Неверное поле → управляемый `400`.
- [x] Неверное направление → управляемый `400`.

### Фильтрация

`ApplicationFilter`:

```java
public record ApplicationFilter(
        ApplicationStatus status,
        Long vacancyId,
        Long companyId,
        LocalDateTime appliedAtFrom,
        LocalDateTime appliedAtTo,
        WorkFormat workFormat,
        LocalDateTime nextContactAtFrom,
        LocalDateTime nextContactAtTo
) {
}
```

Поддерживаются:

- [x] `status`.
- [x] `vacancyId`.
- [x] `companyId`.
- [x] `appliedAtFrom`.
- [x] `appliedAtTo`.
- [x] `workFormat`.
- [x] `nextContactAtFrom`.
- [x] `nextContactAtTo`.

Используется:

```text
ApplicationSpecifications
+ Specification<Application>
+ JpaSpecificationExecutor<Application>
```

Условия:

```text
Application.status = status
Application.vacancy.id = vacancyId
Application.vacancy.company.id = companyId
Application.appliedAt >= appliedAtFrom
Application.appliedAt <= appliedAtTo
Application.vacancy.workFormat = workFormat
Application.nextContactAt >= nextContactAtFrom
Application.nextContactAt <= nextContactAtTo
```

Границы диапазонов включающие.

Неверные диапазоны отклоняются до Repository:

```text
Applied at from must not be after applied at to
Next contact at from must not be after next contact at to
```

---

## Application statistics

- [x] `GET /api/applications/statistics`.
- [x] `ApplicationStatisticsResponse`.
- [x] Общее количество.
- [x] Количество для каждого `ApplicationStatus`.
- [x] `EnumMap<ApplicationStatus, Long>`.
- [x] Нулевые статусы присутствуют в ответе.
- [x] `countByStatus(...)`.

Текущая реализация выполняет:

```text
1 x count()
7 x countByStatus(status)
```

Итого: **8 запросов**.

Оптимизация через `GROUP BY status` отложена.

---

## Flyway

Этап завершён.

### Зависимости

Используются:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-flyway</artifactId>
</dependency>

<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-postgresql</artifactId>
</dependency>
```

На момент настройки использовались `flyway-core 12.4.0` и `flyway-database-postgresql 12.4.0`.

### Migration

Путь:

```text
src/main/resources/db/migration
```

Первая migration:

```text
V1__create_initial_schema.sql
```

Она создаёт:

```text
companies
vacancies
applications
```

В migration зафиксированы:

- identity primary keys;
- foreign keys;
- `NOT NULL`;
- длины строковых полей;
- enum `CHECK` constraints;
- порядок создания таблиц с учётом FK.

Hibernate больше не создаёт схему автоматически:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Источник истины для схемы:

```text
Flyway migrations
```

Hibernate только валидирует соответствие Entity существующей схеме.

### Проверка

- [x] V1 успешно применена к пустой PostgreSQL database.
- [x] После применения V1 приложение / tests проходят Hibernate validation.
- [x] `flyway_schema_history` содержит успешную V1.
- [x] Полная пересборка из пустой схемы проверена.

### Важный инцидент и правило

После первого применения V1 её файл был переформатирован, из-за чего Flyway обнаружил checksum mismatch.

Для локальной development DB без важных данных было принято решение не использовать `repair`, а удалить созданную учебную схему и `flyway_schema_history`, затем повторно применить текущую V1.

Правило на будущее:

```text
После применения versioned migration не редактировать её без необходимости.
Новые изменения схемы оформлять как V2, V3, ...
```

---

## Тестирование

### Unit tests

Используются:

- JUnit 5;
- Mockito;
- mapper tests;
- Service tests;
- `ApplicationSpecificationsTest` с mocked Criteria API.

`ApplicationSpecificationsTest` проверяет, что Specification правильно строит `Predicate`, но не проверяет реальный SQL.

### Controller tests

Используются:

```text
@WebMvcTest
MockMvc
```

Проверяются:

- HTTP status;
- Content-Type;
- JSON;
- validation errors;
- преобразование query-параметров;
- вызовы Service.

### Repository / integration tests

`ApplicationRepositoryTest` использует:

```java
@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
```

Это означает, что тесты используют реальный настроенный PostgreSQL, а не автоматически заменённую embedded DB.

Проверенная цепочка:

```text
ApplicationSpecifications
→ ApplicationRepository
→ Spring Data JPA
→ Hibernate
→ SQL
→ PostgreSQL
```

Покрыто реальными integration tests:

- [x] сохранение `Application`;
- [x] фильтр по `status`;
- [x] фильтр по `companyId`;
- [x] фильтр по `workFormat`;
- [x] диапазон `appliedAt`;
- [x] фильтр по `vacancyId`;
- [x] диапазон `nextContactAt`;
- [x] несколько фильтров одновременно;
- [x] отсутствие фильтров → все записи;
- [x] включение `appliedAtFrom` и `appliedAtTo`;
- [x] включение `nextContactAtFrom` и `nextContactAtTo`;
- [x] filtering + sorting;
- [x] filtering + pagination;
- [x] filtering + sorting + pagination вместе.

Для диапазонов используются negative controls:

```text
before → не входит
from   → входит
inside → входит
to     → входит
after  → не входит
```

Для сортировки порядок проверяется только тогда, когда явно передан `Sort`.

Entity в integration assertions сравниваются по `id`, а не как объекты целиком, чтобы тест не зависел от `equals/hashCode` JPA Entity.

Для повторяющегося setup внутри `ApplicationRepositoryTest` используются private helpers:

```text
saveCompany(...)
saveVacancy(...)
saveApplication(...)
```

Testcontainers пока не используется.

---

## Что понимается уверенно

### Git

- working tree;
- staging area;
- `status`, `add`, `commit`, `push`;
- обычный и staged diff;
- `diff --check`;
- отдельные code/documentation commits;
- проверка чистого working tree.

### Архитектура и Spring

- Controller → Service → Repository;
- Entity vs DTO;
- Mapper;
- Bean Validation;
- централизованная обработка ошибок;
- отдельная бизнес-операция изменения статуса;
- `ApplicationFilter` как parameter object;
- `Specification` как описание условия;
- Repository как место выполнения запроса.

### JPA / Spring Data

- `Many-to-One`;
- `FetchType.LAZY`;
- `EnumType.STRING`;
- `Optional`;
- `Page`, `Pageable`, `PageRequest`, `Sort` на базовом уровне;
- `JpaSpecificationExecutor`;
- `Specification.unrestricted()`;
- `equal`, `greaterThanOrEqualTo`, `lessThanOrEqualTo`, `and`;
- derived query methods.

### Тестирование

- Arrange / Act / Assert;
- Mockito;
- MockMvc;
- различие unit test и integration test на практическом уровне;
- почему mocked Criteria test не заменяет PostgreSQL integration test;
- зачем нужны matching и non-matching rows;
- почему фильтр должен доказывать не только включение, но и исключение;
- почему boundary tests нужны отдельно;
- почему без `Sort` нельзя рассчитывать на порядок SQL result;
- базовая семантика `Page` metadata;
- совместная работа `Specification + Pageable + Sort`.

### Flyway

- Flyway является источником истины для схемы;
- Hibernate `validate` не создаёт таблицы;
- migration хранится в Git;
- checksum защищает применённую migration от незаметного изменения;
- будущие изменения схемы должны идти через V2/V3, а не редактирование V1.

---

## Что понимается частично и требует повторения

- транзакции и `@Transactional` глубже базового уровня;
- persistence context и identity JPA Entity;
- LAZY relationships вне активной сессии;
- `equals/hashCode` для JPA Entity;
- Criteria API глубже используемых операций;
- SQL, который Hibernate генерирует для сложных Specifications;
- nullable sorting и порядок `NULL` в PostgreSQL;
- стабильность сортировки при одинаковом `sortBy`;
- различие `Page`, `Slice` и обычного `List`;
- Testcontainers lifecycle и интеграция со Spring Boot;
- стратегия изоляции integration tests;
- JPQL / projection / `GROUP BY` для статистики;
- граница между repository integration tests и полноценными application integration tests.

---

## Технический долг и ограничения

- Пагинация, сортировка и фильтрация пока реализованы только для `Application`.
- Сохранены старые перегрузки `getAllApplications(...)`; позднее можно сократить.
- Статистика выполняет 8 запросов; `GROUP BY` отложен.
- Repository integration tests зависят от локально запущенного PostgreSQL.
- Testcontainers пока не используется.
- Нет отдельной дополнительной сортировки по `id` для одинаковых значений основного sort field.
- `PagedResponse<T>` не содержит `first`, `last`, `hasNext`.
- Method validation возвращает первое найденное сообщение.
- `fieldErrors` для query-параметров пока пустой.
- `spring.jpa.open-in-view` пока не отключён явно.
- В тестах есть предупреждение Mockito о dynamic Java agent; сборку не ломает.
- В `Company.java` остался `import jakarta.persistence.*;`; исправить отдельным cleanup-коммитом.
- Есть только V1 Flyway migration; будущие изменения схемы должны идти новыми versioned migrations.
- Нет Swagger/OpenAPI.
- Нет Dockerfile и `compose.yaml`.
- Нет Testcontainers.
- Нет `Interview`.
- Нет `User`, Spring Security и JWT.
- Нет GitHub Actions.
- Нет финального README.
- Нет истории изменения статусов.
- CRUD-код намеренно не обобщается раньше времени.
- `One-to-Many` коллекции не добавляются без необходимости.
- MapStruct пока не используется.

---

## Следующее задание

На следующей сессии сначала выбрать следующий уровень тестирования.

### Вариант A — Testcontainers

Цель:

```text
Сделать PostgreSQL integration tests воспроизводимыми и независимыми от вручную настроенной локальной базы.
```

Нужно будет понять:

- что такое containerized test dependency;
- как запускается PostgreSQL container на время tests;
- откуда Spring получает datasource properties;
- как Flyway применяется к test database;
- чем это лучше зависимости от локального PostgreSQL;
- какие есть затраты по времени запуска и сложности.

### Вариант B — более высокий уровень integration testing

Цель:

```text
Проверить несколько полных application flows через Spring context и HTTP / database together.
```

Перед выбором не писать код. Сначала определить, какой риск в проекте важнее закрыть следующим.

### Ограничения

Пока не нужно:

- добавлять Security/JWT;
- добавлять frontend;
- переходить на микросервисы;
- добавлять Kafka/Redis/Kubernetes;
- оптимизировать статистику без отдельной задачи;
- менять V1 migration;
- добавлять новые abstraction layers для tests без необходимости.

---

## Вопросы для повторения на следующую сессию

1. Чем unit test `ApplicationSpecificationsTest` отличается от repository integration test?
2. Почему наличие нескольких реальных Java-классов само по себе ещё не делает тест интеграционным?
3. Зачем в filter integration test создавать как matching, так и non-matching records?
4. Почему `from` и `to` проверяются отдельными boundary scenarios?
5. Почему нельзя полагаться на порядок `findAll(specification)` без `Sort`?
6. Что означает `PageRequest.of(1, 2, ...)`?
7. Чем `page.getTotalElements()` отличается от `page.getContent().size()`?
8. Почему в integration tests JPA Entity удобнее сравнивать по `id`?
9. Кто сейчас создаёт схему: Hibernate или Flyway?
10. Что произойдёт, если изменить уже применённую V1 migration?
11. Чем локальный PostgreSQL integration test отличается от Testcontainers test?
12. Какой следующий уровень tests даст проекту больше пользы и почему?

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
git commit -m "Document integration testing progress"
git push
git status
```

Ожидаемое состояние:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```
