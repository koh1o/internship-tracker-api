# Internship Tracker API — текущее состояние

## Актуальность файла

Последнее обновление: **2026-07-21**.

Это актуальная стабильная точка проекта после реализации обновления данных `Application` через `PUT /api/applications/{id}`.

В источниках проекта должен находиться только один файл с названием `PROJECT_STATUS.md`.

---

## Текущий этап

Выполняется этап реализации `Application`.

Для `Application` уже готовы:

- JPA Entity и enum статусов;
- Repository и repository-тест;
- DTO для создания, ответа и обновления;
- ручной Mapper;
- Service;
- Controller;
- создание;
- получение списка;
- получение по `id`;
- обновление основных данных;
- единая обработка ошибок;
- unit- и controller-тесты.

Следующий шаг — отдельная бизнес-операция изменения статуса:

```text
PATCH /api/applications/{id}/status
```

После неё останется реализовать удаление `Application`, а затем добавить бизнес-правила дат и переходов статусов.

---

## Текущая стабильная точка

Последний рабочий code-коммит:

```text
a3eb4dd Add Application update endpoint
```

Состояние Git после отправки коммита:

```text
On branch main
Your branch is up to date with 'origin/main'.

Changes not staged for commit:
        modified:   PROJECT_STATUS.md
```

Все Java-изменения закоммичены и отправлены на GitHub. Локально изменён только `PROJECT_STATUS.md`.

Всего в проекте **72 теста**.

Последний полный запуск:

```text
Tests run: 72
Failures: 0
Errors: 0
BUILD SUCCESS
```

---

## Реализованный функционал

### Среда и Git

- [x] JDK 21 установлен и используется проектом.
- [x] Настроен `JAVA_HOME`.
- [x] Используется Maven Wrapper 3.9.16.
- [x] Установлен и настроен Git.
- [x] Создан публичный GitHub-репозиторий `internship-tracker-api`.
- [x] Ветка `main` связана с `origin/main`.
- [x] Настроен `.gitignore`.
- [x] В Git не попадают `.idea/`, `target/`, реальные пароли и другие секреты.
- [x] Перед коммитами проверяются тесты, `git status`, diff и staging.
- [x] Для diff используется `git --no-pager diff`.
- [x] Для staged diff используется `git --no-pager diff --cached`.
- [x] Code-коммиты остаются небольшими и осмысленными.
- [x] Изменения `PROJECT_STATUS.md` не смешиваются с code-коммитами.

### Spring Boot и PostgreSQL

- [x] Создан Spring Boot 4.1.0 проект на Java 21 и Maven.
- [x] Добавлен Spring Web.
- [x] Реализован `GET /api/hello`.
- [x] Приложение запускается на порту 8080.
- [x] Добавлены Spring Data JPA и PostgreSQL JDBC Driver.
- [x] Создана база данных `internship_tracker`.
- [x] Создана роль `internship_tracker_app`.
- [x] Пароль базы передаётся через переменную окружения `DB_PASSWORD`.
- [x] Секреты не хранятся в Git.

### Company

Для `Company` завершён полный CRUD:

- [x] Entity и timestamps.
- [x] Repository и repository-тест.
- [x] Service и unit-тесты.
- [x] `CompanyRequest` и `CompanyResponse`.
- [x] Ручной Mapper и mapper-тесты.
- [x] Bean Validation.
- [x] `ResourceNotFoundException`.
- [x] `ErrorResponse` и `GlobalExceptionHandler`.
- [x] Controller и `MockMvc`-тесты.

Реализованные endpoint:

```text
GET    /api/companies
POST   /api/companies
GET    /api/companies/{id}
PUT    /api/companies/{id}
DELETE /api/companies/{id}
```

### Vacancy

Для `Vacancy` завершён полный CRUD:

- [x] Enum `WorkFormat`.
- [x] Значения `OFFICE`, `REMOTE`, `HYBRID`, `NOT_SPECIFIED`.
- [x] Entity `Vacancy`.
- [x] Связь `Many-to-One` с `Company` через `company_id`.
- [x] `FetchType.LAZY`.
- [x] Enum хранится через `EnumType.STRING`.
- [x] Правило `null workFormat → NOT_SPECIFIED`.
- [x] `@PrePersist` и `@PreUpdate` для timestamps.
- [x] `VacancyRepository` и repository-тест.
- [x] `VacancyRequest` и `VacancyResponse`.
- [x] Bean Validation.
- [x] Ручной Mapper и mapper-тесты.
- [x] Service и unit-тесты.
- [x] Controller и `MockMvc`-тесты.
- [x] Успешные сценарии CRUD.
- [x] Validation errors.
- [x] `404 Not Found` для отсутствующей Vacancy.
- [x] `404 Not Found` для отсутствующей Company при создании и обновлении.

Реализованные endpoint:

```text
POST   /api/vacancies
GET    /api/vacancies
GET    /api/vacancies/{id}
PUT    /api/vacancies/{id}
DELETE /api/vacancies/{id}
```

### Application

Для `Application` реализована основная часть CRUD:

- [x] Enum `ApplicationStatus`.
- [x] Значения `PLANNED`, `APPLIED`, `TEST_TASK`, `INTERVIEW`, `OFFER`, `REJECTED`, `WITHDRAWN`.
- [x] Entity `Application`.
- [x] Связь `Many-to-One` с `Vacancy` через `vacancy_id`.
- [x] `FetchType.LAZY`.
- [x] Статус хранится через `EnumType.STRING`.
- [x] Поля `appliedAt`, `nextContactAt` и `notes`.
- [x] Ограничение `notes` до 2000 символов.
- [x] `@PrePersist` и `@PreUpdate` для timestamps.
- [x] `ApplicationRepository` и repository-тест.
- [x] `ApplicationRequest`.
- [x] `ApplicationResponse`.
- [x] `ApplicationUpdateRequest`.
- [x] Ручной `ApplicationMapper`.
- [x] Mapper-тесты для создания, ответа и обновления Entity.
- [x] `ApplicationService`.
- [x] Service unit-тесты.
- [x] `ApplicationController`.
- [x] Controller-тесты через `MockMvc`.
- [x] Создание Application.
- [x] Получение списка.
- [x] Получение по `id`.
- [x] Обновление Vacancy, дат и заметок.
- [x] Статус не изменяется через обычный `PUT`.
- [x] `404 Not Found` для отсутствующего Application.
- [x] `404 Not Found` для отсутствующей Vacancy при создании и обновлении.
- [ ] Отдельное изменение статуса через `PATCH`.
- [ ] Удаление Application.
- [ ] Проверка допустимых переходов статуса.
- [ ] Проверка согласованности дат.

Реализованные endpoint:

```text
POST /api/applications
GET  /api/applications
GET  /api/applications/{id}
PUT  /api/applications/{id}
```

Запланированные endpoint:

```text
PATCH  /api/applications/{id}/status
DELETE /api/applications/{id}
```

---

## Текущий контракт Application API

### POST /api/applications

- принимает `ApplicationRequest`;
- возвращает `ApplicationResponse`;
- успешный статус: `201 Created`;
- Vacancy ищется по `vacancyId`;
- при отсутствии Vacancy возвращается `404 Not Found`;
- request содержит начальный `status`.

### GET /api/applications

- возвращает `List<ApplicationResponse>`;
- успешный статус: `200 OK`;
- каждая Entity преобразуется через `ApplicationMapper`;
- пустая база должна возвращать пустой список, а не `404`.

### GET /api/applications/{id}

- возвращает `ApplicationResponse`;
- успешный статус: `200 OK`;
- при отсутствии Application возвращается `404 Not Found`.

### PUT /api/applications/{id}

- принимает `ApplicationUpdateRequest`;
- обновляет Vacancy, `appliedAt`, `nextContactAt` и `notes`;
- не изменяет `status`;
- возвращает `ApplicationResponse`;
- успешный статус: `200 OK`;
- сначала ищется Application;
- затем ищется Vacancy;
- при отсутствии Application или Vacancy возвращается `404 Not Found`;
- Entity изменяется через `ApplicationMapper#updateEntity`;
- сохранение выполняется только после успешных поисков.

### ApplicationRequest

Поля:

```text
Long vacancyId
ApplicationStatus status
LocalDateTime appliedAt
LocalDateTime nextContactAt
String notes
```

Validation:

- `vacancyId` обязателен;
- `status` обязателен;
- `notes` — максимум 2000 символов;
- бизнес-проверки дат пока не добавлены.

### ApplicationUpdateRequest

Поля:

```text
Long vacancyId
LocalDateTime appliedAt
LocalDateTime nextContactAt
String notes
```

Validation:

- `vacancyId` обязателен;
- `notes` — максимум 2000 символов;
- поля `status` намеренно нет;
- изменение статуса будет отдельной бизнес-операцией.

### ApplicationResponse

Поля:

```text
Long id
Long vacancyId
String vacancyTitle
Long companyId
String companyName
ApplicationStatus status
LocalDateTime appliedAt
LocalDateTime nextContactAt
String notes
LocalDateTime createdAt
LocalDateTime updatedAt
```

Response не возвращает Entity `Vacancy` или `Company` напрямую.

---

## Архитектурные правила, которые уже применяются

Поток запроса:

```text
HTTP request → Controller → Service → Repository → PostgreSQL
```

- Controller отвечает за HTTP-запросы и ответы.
- Service отвечает за бизнес-логику.
- Repository отвечает за доступ к данным.
- Entity описывает модель хранения.
- DTO описывает внешний API-контракт.
- Mapper преобразует Entity и DTO.
- Controller не обращается к Repository напрямую.
- Mapper не обращается к Repository.
- Entity не возвращаются клиенту напрямую.
- Используется constructor injection.
- Ошибки обрабатываются централизованно.
- Маппинг пока пишется вручную, без MapStruct.
- Изменение статуса выделяется в отдельную бизнес-операцию.
- Repository не проверяется из Controller-тестов напрямую.
- При ошибочных сценариях тестируется отсутствие лишних взаимодействий.

---

## Что уже понимается уверенно

### Git

- назначение working tree и staging area;
- `git status`, `git add`, `git commit`, `git push`;
- отличие `git diff` от `git diff --cached`;
- добавление в staging только нужных файлов;
- проверка будущего коммита до `git commit`;
- необходимость маленьких осмысленных коммитов;
- отделение `PROJECT_STATUS.md` от code-коммитов;
- работа с новым untracked-файлом;
- проверка отсутствия секретов перед коммитом.

### Java, Spring и архитектура

- назначение Controller, Service, Repository, Entity, DTO и Mapper;
- dependency injection через конструктор;
- `@RestController`, `@Service`, `@Component`;
- `@Valid` и Bean Validation;
- единая обработка `400 Bad Request` и `404 Not Found`;
- почему бизнес-логика не должна находиться в Controller;
- почему Entity не следует возвращать клиенту напрямую;
- различие request DTO и response DTO;
- обновление существующей Entity через Mapper;
- зачем статус исключён из обычного update DTO.

### JPA

- `@Entity`, `@Table`, `@Id`, `@GeneratedValue`;
- `@Column`;
- `@ManyToOne` и `@JoinColumn`;
- `FetchType.LAZY`;
- `EnumType.STRING`;
- `@PrePersist` и `@PreUpdate`;
- `findById()`, `findAll()`, `save()` и `delete()`;
- назначение `Optional` и `orElseThrow()`.

### Тестирование

- структура Arrange, Act, Assert;
- назначение mock;
- `when(...).thenReturn(...)`;
- `thenThrow(...)`;
- `assertSame`, `assertEquals`, `assertThrows`;
- `verify(...)`, `never()`, `verifyNoInteractions(...)`;
- Service unit-тесты;
- mapper-тесты;
- `@WebMvcTest` и `MockMvc`;
- проверка HTTP status, Content-Type и JSON body;
- проверка единого формата ошибок;
- проверка отсутствия `save()` и Mapper-вызовов при ошибках.

---

## Что понимается частично и требует повторения

- различие unit-, controller-, repository- и будущих интеграционных тестов;
- поведение LAZY-связей вне активной JPA-сессии;
- границы бизнес-логики между Entity, Mapper и Service;
- правила допустимых переходов `ApplicationStatus`;
- проектирование отдельного request DTO для `PATCH`;
- проверки согласованности `appliedAt` и `nextContactAt`;
- транзакции и назначение `@Transactional`;
- фильтрация, сортировка и пагинация;
- управление схемой базы через Flyway вместо автоматического Hibernate DDL.

---

## Технический долг и ограничения текущей версии

- Для `Application` ещё нет изменения статуса через отдельный `PATCH`.
- Для `Application` ещё нет удаления.
- Нет правил допустимых переходов статуса.
- Нет бизнес-проверок дат.
- Схема пока не переведена на Flyway.
- Нет пагинации, сортировки и фильтрации.
- Нет Swagger/OpenAPI.
- Нет Dockerfile и `compose.yaml`.
- Нет Testcontainers.
- Нет `Interview`.
- Нет User, Spring Security и JWT.
- Нет GitHub Actions.
- Нет финального README с архитектурой и примерами API.
- Дублирующиеся части CRUD пока не обобщаются намеренно, чтобы сначала понять базовые слои.
- `One-to-Many` коллекции не добавляются без практической необходимости.
- MapStruct не добавляется, пока ручной маппинг остаётся понятным и небольшим.

---

## Следующее задание

Реализовать отдельное изменение статуса Application:

```text
PATCH /api/applications/{id}/status
```

### Предварительный план

1. Создать `ApplicationStatusUpdateRequest`.
2. Добавить validation обязательного `status`.
3. Добавить метод Service для изменения статуса.
4. Найти Application по `id`.
5. Изменить только поле `status`.
6. Сохранить Application.
7. Добавить Controller endpoint.
8. Добавить unit- и controller-тесты.
9. Проверить `404`, validation и отсутствие лишних взаимодействий.
10. Позже добавить правила допустимых переходов между статусами.

Пока не добавлять в этот шаг:

- сложную таблицу переходов статусов;
- автоматическое изменение `appliedAt`;
- историю статусов;
- отдельную Entity для истории;
- Spring Security;
- фильтрацию;
- удаление Application одновременно с PATCH.

---

## Вопросы для повторения перед продолжением

1. Почему `status` отсутствует в `ApplicationUpdateRequest`?
2. Почему изменение статуса считается отдельной бизнес-операцией?
3. Что должен вернуть Service, если Application с нужным `id` отсутствует?
4. Почему Controller не должен сам вызывать `application.setStatus(...)`?
5. Какие взаимодействия не должны происходить, если Application не найден?
6. Чем `PUT /api/applications/{id}` отличается от будущего `PATCH /api/applications/{id}/status`?
7. Почему правила допустимых переходов статуса должны находиться в Service?

---

## Рекомендуемый следующий code-коммит

После реализации, ревью, исправлений и успешных тестов:

```text
Add Application status update endpoint
```

## Рекомендуемый документационный коммит

Для фиксации этого файла отдельным коммитом:

```text
Update project status after Application update endpoint
```
