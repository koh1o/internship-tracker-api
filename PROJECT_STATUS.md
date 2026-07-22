# Internship Tracker API — текущее состояние

## Актуальность файла

Последнее обновление: **2026-07-22**.

Это актуальная стабильная точка проекта после завершения CRUD для `Application`, отдельного изменения статуса и основных бизнес-правил дат и переходов статусов.

В источниках проекта должен находиться только один файл с названием `PROJECT_STATUS.md`.

---

## Текущий этап

Завершён основной модуль `Application`.

Для `Company`, `Vacancy` и `Application` реализован CRUD. Для `Application` дополнительно готовы отдельная операция изменения статуса, проверки согласованности дат и явные правила переходов статусов.

Следующий крупный этап:

```text
Фильтрация, сортировка и пагинация
```

Начинать рекомендуется с пагинации списка `Application`, затем добавить сортировку и фильтры по статусу, компании, формату работы и датам.

---

## Текущая стабильная точка

Последний рабочий code-коммит:

```text
6a882b5 Validate applied date for Application status
```

Последние завершённые коммиты:

```text
985bdf3 Add Application status update endpoint
aa61b8f Add Application delete endpoint
9edfa59 Add Application date validation
733e469 Add Application status transition validation
7c83dfc Handle unchanged Application status
a76960f Add Application status transition rules
6a882b5 Validate applied date for Application status
```

Состояние Git после отправки последнего code-коммита:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

Всего в проекте **94 теста**.

Последний полный запуск:

```text
Tests run: 94
Failures: 0
Errors: 0
BUILD SUCCESS
```

После редактирования этого файла локально будет изменён только `PROJECT_STATUS.md`. Его нужно закоммитить отдельно от Java-кода.

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
- [x] Пароль PostgreSQL передаётся через переменную окружения `DB_PASSWORD`.
- [x] Перед коммитами проверяются тесты, `git status`, diff и staging.
- [x] Для diff используется `git --no-pager diff`.
- [x] Для staged diff используется `git --no-pager diff --cached`.
- [x] Code-коммиты небольшие и осмысленные.
- [x] Изменения `PROJECT_STATUS.md` не смешиваются с code-коммитами.

### Spring Boot и PostgreSQL

- [x] Создан Spring Boot 4.1.0 проект на Java 21 и Maven.
- [x] Добавлен Spring Web.
- [x] Реализован `GET /api/hello`.
- [x] Приложение запускается на порту 8080.
- [x] Добавлены Spring Data JPA и PostgreSQL JDBC Driver.
- [x] Создана база данных `internship_tracker`.
- [x] Создана роль `internship_tracker_app`.
- [x] Секреты не хранятся в Git.

### Company

Для `Company` завершён полный CRUD:

- [x] Entity и timestamps.
- [x] Repository и repository-тест.
- [x] Service и unit-тесты.
- [x] Request/response DTO.
- [x] Ручной Mapper и mapper-тесты.
- [x] Bean Validation.
- [x] `ResourceNotFoundException`.
- [x] `ErrorResponse` и `GlobalExceptionHandler`.
- [x] Controller и `MockMvc`-тесты.

Endpoint:

```text
GET    /api/companies
POST   /api/companies
GET    /api/companies/{id}
PUT    /api/companies/{id}
DELETE /api/companies/{id}
```

### Vacancy

Для `Vacancy` завершён полный CRUD:

- [x] Enum `WorkFormat`: `OFFICE`, `REMOTE`, `HYBRID`, `NOT_SPECIFIED`.
- [x] Entity `Vacancy`.
- [x] Связь `Many-to-One` с `Company` через `company_id`.
- [x] `FetchType.LAZY`.
- [x] Enum хранится через `EnumType.STRING`.
- [x] Правило `null workFormat → NOT_SPECIFIED`.
- [x] Repository и repository-тест.
- [x] Request/response DTO и Bean Validation.
- [x] Ручной Mapper и mapper-тесты.
- [x] Service и unit-тесты.
- [x] Controller и `MockMvc`-тесты.
- [x] `404 Not Found` для отсутствующей Vacancy.
- [x] `404 Not Found` для отсутствующей Company при создании и обновлении.

Endpoint:

```text
POST   /api/vacancies
GET    /api/vacancies
GET    /api/vacancies/{id}
PUT    /api/vacancies/{id}
DELETE /api/vacancies/{id}
```

### Application

Для `Application` завершены CRUD и основные бизнес-операции:

- [x] Enum `ApplicationStatus`.
- [x] Статусы `PLANNED`, `APPLIED`, `TEST_TASK`, `INTERVIEW`, `OFFER`, `REJECTED`, `WITHDRAWN`.
- [x] Entity `Application`.
- [x] Связь `Many-to-One` с `Vacancy` через `vacancy_id`.
- [x] `FetchType.LAZY`.
- [x] Статус хранится через `EnumType.STRING`.
- [x] Поля `appliedAt`, `nextContactAt`, `notes`.
- [x] Ограничение `notes` до 2000 символов.
- [x] Repository и repository-тест.
- [x] `ApplicationRequest`, `ApplicationUpdateRequest`, `ApplicationStatusUpdateRequest`, `ApplicationResponse`.
- [x] Ручной `ApplicationMapper` и mapper-тесты.
- [x] Service и unit-тесты.
- [x] Controller и `MockMvc`-тесты.
- [x] Создание.
- [x] Получение списка.
- [x] Получение по `id`.
- [x] Обновление Vacancy, дат и заметок.
- [x] Удаление.
- [x] Отдельное изменение статуса через `PATCH`.
- [x] Статус не изменяется через обычный `PUT`.
- [x] Идемпотентная повторная установка того же статуса без лишнего `save()`.
- [x] `404 Not Found` для отсутствующего Application.
- [x] `404 Not Found` для отсутствующей Vacancy при создании и обновлении.
- [x] Единый `400 Bad Request` для ошибок бизнес-валидации.

Endpoint:

```text
POST   /api/applications
GET    /api/applications
GET    /api/applications/{id}
PUT    /api/applications/{id}
PATCH  /api/applications/{id}/status
DELETE /api/applications/{id}
```

---

## Бизнес-правила Application

### Согласованность дат

- `nextContactAt` не может быть раньше `appliedAt`.
- Одинаковые значения `appliedAt` и `nextContactAt` разрешены.
- Если одна из дат отсутствует, проверка порядка дат не нарушается.
- Для любого статуса, кроме `PLANNED`, поле `appliedAt` обязательно.
- `PLANNED` может существовать без `appliedAt`.
- Нельзя удалить `appliedAt` через `PUT`, если текущий статус уже не `PLANNED`.
- Нельзя перейти из `PLANNED` в активный статус, если `appliedAt` не заполнен.

Сообщения ошибок:

```text
Next contact date must not be before applied date
Applied date is required for status APPLIED
```

Второе сообщение формируется динамически для конкретного статуса.

### Переходы статусов

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

Повторная установка текущего статуса разрешена:

```text
REJECTED → REJECTED
```

В этом случае Service возвращает текущий `Application`, не вызывает `setStatus()` и не выполняет лишний `save()`.

Запрещённый переход возвращает бизнес-ошибку вида:

```text
Cannot change status from PLANNED to INTERVIEW
```

---

## Текущий контракт Application API

### POST /api/applications

- принимает `ApplicationRequest`;
- возвращает `ApplicationResponse`;
- успешный статус: `201 Created`;
- Vacancy ищется по `vacancyId`;
- при отсутствии Vacancy возвращается `404 Not Found`;
- проверяются порядок дат и соответствие `status`/`appliedAt`.

### GET /api/applications

- возвращает `List<ApplicationResponse>`;
- успешный статус: `200 OK`;
- пустая база возвращает пустой список;
- пагинация, сортировка и фильтрация пока не добавлены.

### GET /api/applications/{id}

- возвращает `ApplicationResponse`;
- успешный статус: `200 OK`;
- при отсутствии Application возвращается `404 Not Found`.

### PUT /api/applications/{id}

- принимает `ApplicationUpdateRequest`;
- обновляет Vacancy, `appliedAt`, `nextContactAt`, `notes`;
- не изменяет `status`;
- возвращает `ApplicationResponse`;
- успешный статус: `200 OK`;
- проверяет порядок дат;
- запрещает удалить `appliedAt` у Application с активным или финальным статусом;
- при отсутствии Application или Vacancy возвращается `404 Not Found`.

### PATCH /api/applications/{id}/status

- принимает `ApplicationStatusUpdateRequest`;
- изменяет только `status`;
- успешный статус: `200 OK`;
- проверяет существование Application;
- повтор текущего статуса считается идемпотентным;
- проверяет таблицу разрешённых переходов;
- проверяет наличие `appliedAt` для нового статуса;
- при бизнес-ошибке возвращается `400 Bad Request`.

### DELETE /api/applications/{id}

- удаляет Application;
- успешный статус: `204 No Content`;
- при отсутствии Application возвращается `404 Not Found`.

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
- Изменение статуса выделено в отдельную бизнес-операцию.
- Проверки переходов статусов и дат находятся в Service.
- Repository не проверяется из Controller-тестов напрямую.
- При ошибочных сценариях тестируется отсутствие лишних взаимодействий.

---

## Что уже понимается уверенно

### Git

- working tree и staging area;
- `git status`, `git add`, `git commit`, `git push`;
- отличие `git diff` от `git diff --cached`;
- добавление в staging только нужных файлов;
- проверка будущего коммита до `git commit`;
- маленькие осмысленные коммиты;
- отдельные code- и documentation-коммиты;
- работа с untracked-файлами;
- проверка отсутствия секретов.

### Java, Spring и архитектура

- назначение Controller, Service, Repository, Entity, DTO, Mapper;
- constructor injection;
- `@RestController`, `@Service`, `@Component`;
- `@Valid` и Bean Validation;
- единая обработка `400 Bad Request` и `404 Not Found`;
- различие HTTP-валидации и бизнес-валидации;
- почему бизнес-логика находится в Service;
- почему Entity не возвращаются напрямую;
- различие request DTO и response DTO;
- отдельный DTO и endpoint для изменения статуса;
- переиспользование Service-методов без дублирования поиска;
- идемпотентность повторной установки статуса;
- сравнение enum через `==`;
- `switch expression` для явных правил переходов.

### JPA

- `@Entity`, `@Table`, `@Id`, `@GeneratedValue`;
- `@Column`;
- `@ManyToOne`, `@JoinColumn`, `FetchType.LAZY`;
- `EnumType.STRING`;
- `@PrePersist`, `@PreUpdate`;
- `findById()`, `findAll()`, `save()`, `delete()`;
- `Optional` и `orElseThrow()`.

### Тестирование

- Arrange, Act, Assert;
- mock и поведение Mockito по умолчанию;
- `when(...).thenReturn(...)`, `thenThrow(...)`, `doThrow(...)`;
- `assertSame`, `assertEquals`, `assertThrows`;
- `verify(...)`, `never()`, `verifyNoInteractions(...)`;
- Service unit-тесты;
- mapper-тесты;
- `@WebMvcTest` и `MockMvc`;
- HTTP status, Content-Type, JSON body;
- единый формат ошибок;
- проверка отсутствия `save()`, Mapper и других лишних вызовов;
- необходимость обновлять старые mock-данные после добавления новых бизнес-правил.

---

## Что понимается частично и требует повторения

- различие unit-, controller-, repository- и будущих интеграционных тестов;
- поведение LAZY-связей вне активной JPA-сессии;
- транзакции и назначение `@Transactional`;
- проектирование пагинации и формата paged response;
- `Page`, `Pageable`, `PageRequest`, `Sort`;
- фильтрация через derived query, JPQL или `Specification`;
- управление схемой базы через Flyway вместо Hibernate DDL;
- границы между проверками DTO, Service и ограничениями базы данных.

---

## Технический долг и ограничения текущей версии

- Нет пагинации, сортировки и фильтрации.
- Схема пока не переведена на Flyway.
- Нет Swagger/OpenAPI.
- Нет Dockerfile и `compose.yaml`.
- Нет Testcontainers.
- Нет `Interview`.
- Нет User, Spring Security и JWT.
- Нет GitHub Actions.
- Нет финального README с архитектурой и примерами API.
- Нет истории изменения статусов.
- Дублирующиеся части CRUD пока не обобщаются намеренно.
- `One-to-Many` коллекции не добавляются без практической необходимости.
- MapStruct не добавляется, пока ручной маппинг остаётся понятным и небольшим.

---

## Следующее задание

Начать этап фильтрации, сортировки и пагинации с пагинации списка `Application`:

```text
GET /api/applications?page=0&size=10
```

Предварительный план:

1. Разобраться с `Page`, `Pageable` и `PageRequest`.
2. Изменить Repository-вызов `findAll()` на пагинируемый вариант.
3. Решить, возвращать ли Spring `Page` напрямую или создать собственный response DTO.
4. Сначала реализовать Service и unit-тесты.
5. Затем обновить Controller и `MockMvc`-тесты.
6. Добавить ограничения для некорректных `page` и `size`.
7. После пагинации добавить сортировку.
8. После сортировки перейти к фильтрам.

Пока не добавлять одновременно:

- сложные динамические фильтры;
- `Specification` до понимания базовой пагинации;
- Swagger;
- Docker;
- Spring Security;
- frontend.

---

## Вопросы для повторения перед продолжением

1. Почему `ApplicationUpdateRequest` не содержит `status`?
2. Чем `PUT /api/applications/{id}` отличается от `PATCH /api/applications/{id}/status`?
3. Почему повтор того же статуса не вызывает `save()`?
4. Где находятся правила допустимых переходов и почему?
5. Почему `PLANNED` может существовать без `appliedAt`?
6. Почему при обновлении данных статус берётся из Entity, а не из request?
7. Что произойдёт при добавлении нового enum-статуса без обновления исчерпывающего `switch expression`?
8. Зачем API нужна пагинация, даже если данных пока мало?

---

## Рекомендуемый документационный коммит

После замены файла и проверки diff:

```text
Update project status after Application business rules
```

Проверки перед коммитом:

```powershell
git status
git --no-pager diff -- PROJECT_STATUS.md
git add PROJECT_STATUS.md
git --no-pager diff --cached
git commit -m "Update project status after Application business rules"
git push
git status
```
