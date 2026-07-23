# Internship Tracker API — текущее состояние

## Актуальность файла

Последнее обновление: **2026-07-23**.

Это актуальная стабильная точка проекта после завершения пагинации и сортировки списка `Application`.

В источниках проекта должен находиться только один файл с точным названием:

```text
PROJECT_STATUS.md
```

Название файла нельзя изменять.

---

## Текущий этап

Для `Company`, `Vacancy` и `Application` завершён основной CRUD.

Для `Application` дополнительно реализованы:

- отдельная операция изменения статуса;
- проверки согласованности дат;
- явные правила переходов статусов;
- идемпотентная повторная установка текущего статуса;
- пагинация списка;
- сортировка списка;
- собственный DTO для paged response;
- проверка параметров `page` и `size`;
- проверка разрешённых полей и направлений сортировки;
- единый формат ошибок для method validation и бизнес-ошибок сортировки.

Текущий крупный этап:

```text
Фильтрация, сортировка и пагинация
```

Завершены:

```text
Пагинация Application
Сортировка Application
```

Следующая часть этапа:

```text
Фильтрация списка Application
```

Первым небольшим шагом нужно добавить необязательный фильтр по `ApplicationStatus`.

---

## Текущая стабильная точка

Последний рабочий code-коммит:

```text
9c77228 Add Application sorting
```

Последний documentation-коммит до обновления этого файла:

```text
f6e224e Update project status after Application pagination
```

Последние завершённые code-коммиты:

```text
985bdf3 Add Application status update endpoint
aa61b8f Add Application delete endpoint
9edfa59 Add Application date validation
733e469 Add Application status transition validation
7c83dfc Handle unchanged Application status
a76960f Add Application status transition rules
6a882b5 Validate applied date for Application status
4bbbd31 Add Application pagination
9c77228 Add Application sorting
```

Состояние Git после отправки последнего code-коммита:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

Всего в проекте **105 тестов**.

Последний полный запуск:

```text
Tests run: 105
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

После локальной замены этого файла должен быть изменён только `PROJECT_STATUS.md`.

Изменение `PROJECT_STATUS.md` нужно закоммитить отдельно от Java-кода.

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
- [x] Для diff используется `git --no-pager diff`.
- [x] Для staged diff используется `git --no-pager diff --cached`.
- [x] Code-коммиты небольшие и осмысленные.
- [x] Documentation-коммиты не смешиваются с Java-кодом.
- [x] Последний code-коммит отправлен на GitHub.
- [x] После `git push` проверяется чистый working tree.

### Spring Boot и PostgreSQL

- [x] Создан Spring Boot 4.1.0 проект на Java 21 и Maven.
- [x] Добавлен Spring Web.
- [x] Реализован `GET /api/hello`.
- [x] Приложение запускается на порту 8080.
- [x] Добавлены Spring Data JPA и PostgreSQL JDBC Driver.
- [x] Создана база данных `internship_tracker`.
- [x] Создана роль `internship_tracker_app`.
- [x] Секреты не хранятся в Git.
- [x] Пароль базы данных передаётся через переменную окружения.

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

- [x] Enum `WorkFormat`.
- [x] Значения `OFFICE`, `REMOTE`, `HYBRID`, `NOT_SPECIFIED`.
- [x] Entity `Vacancy`.
- [x] Связь `Many-to-One` с `Company` через `company_id`.
- [x] Используется `FetchType.LAZY`.
- [x] Enum хранится через `EnumType.STRING`.
- [x] Реализовано правило `null workFormat → NOT_SPECIFIED`.
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

Для `Application` завершены CRUD, основные бизнес-правила, пагинация и сортировка:

- [x] Enum `ApplicationStatus`.
- [x] Статусы `PLANNED`, `APPLIED`, `TEST_TASK`, `INTERVIEW`, `OFFER`, `REJECTED`, `WITHDRAWN`.
- [x] Entity `Application`.
- [x] Связь `Many-to-One` с `Vacancy` через `vacancy_id`.
- [x] Используется `FetchType.LAZY`.
- [x] Статус хранится через `EnumType.STRING`.
- [x] Поля `appliedAt`, `nextContactAt`, `notes`.
- [x] Ограничение `notes` до 2000 символов.
- [x] Repository и repository-тест.
- [x] `ApplicationRequest`.
- [x] `ApplicationUpdateRequest`.
- [x] `ApplicationStatusUpdateRequest`.
- [x] `ApplicationResponse`.
- [x] Ручной `ApplicationMapper` и mapper-тесты.
- [x] Service и unit-тесты.
- [x] Controller и `MockMvc`-тесты.
- [x] Создание, получение, обновление и удаление.
- [x] Отдельное изменение статуса через `PATCH`.
- [x] Статус не изменяется через обычный `PUT`.
- [x] Идемпотентная повторная установка того же статуса без лишнего `save()`.
- [x] Проверки дат и переходов статусов.
- [x] Единый `400 Bad Request` для бизнес-ошибок.
- [x] Пагинация через `Page`, `Pageable` и `PageRequest`.
- [x] Собственный generic record `PagedResponse<T>`.
- [x] Entity не возвращаются в paged response.
- [x] Элементы страницы преобразуются в `ApplicationResponse`.
- [x] Значения пагинации по умолчанию.
- [x] Проверка границ `page` и `size`.
- [x] Единый `ErrorResponse` для method validation.
- [x] Пустая страница за пределами данных возвращается с `200 OK`.
- [x] Сортировка объединена с пагинацией.
- [x] Поддерживаются направления `ASC` и `DESC`.
- [x] Используется безопасный список разрешённых полей сортировки.
- [x] Неверное поле сортировки возвращает управляемый `400 Bad Request`.
- [x] Неверное направление сортировки возвращает управляемый `400 Bad Request`.
- [x] Двухпараметровый Service-метод делегирует основной реализации с сортировкой по умолчанию.

Endpoint:

```text
POST   /api/applications
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC
GET    /api/applications/{id}
PUT    /api/applications/{id}
PATCH  /api/applications/{id}/status
DELETE /api/applications/{id}
```

---

## Бизнес-правила Application

### Согласованность дат

- `nextContactAt` не может быть раньше `appliedAt`.
- Одинаковые значения дат разрешены.
- Если одна из дат отсутствует, порядок дат не нарушается.
- Для любого статуса, кроме `PLANNED`, поле `appliedAt` обязательно.
- `PLANNED` может существовать без `appliedAt`.
- Через `PUT` нельзя удалить `appliedAt`, если текущий статус не `PLANNED`.
- Нельзя перейти из `PLANNED` в активный статус без `appliedAt`.

Сообщения ошибок:

```text
Next contact date must not be before applied date
Applied date is required for status APPLIED
```

Второе сообщение формируется динамически для конкретного статуса.

### Переходы статусов

```text
PLANNED   → APPLIED, WITHDRAWN
APPLIED   → TEST_TASK, INTERVIEW, REJECTED, WITHDRAWN
TEST_TASK → INTERVIEW, REJECTED, WITHDRAWN
INTERVIEW → OFFER, REJECTED, WITHDRAWN
OFFER     → нет переходов
REJECTED  → нет переходов
WITHDRAWN → нет переходов
```

Повторная установка текущего статуса разрешена.

При повторной установке Service:

- возвращает текущий `Application`;
- не вызывает `setStatus()`;
- не вызывает `save()`.

Запрещённый переход возвращает сообщение вида:

```text
Cannot change status from PLANNED to INTERVIEW
```

Правила переходов находятся в `ApplicationService` и реализованы через исчерпывающий `switch expression`.

---

## Пагинация Application

### HTTP-контракт

```text
GET /api/applications?page=0&size=10
```

Query-параметры:

```text
page — номер страницы, начиная с 0
size — максимальное количество элементов на странице
```

Значения по умолчанию:

```text
page=0
size=10
```

Ограничения:

```text
page >= 0
1 <= size <= 100
```

### Формат ответа

Controller возвращает:

```java
PagedResponse<ApplicationResponse>
```

Поля:

```text
content
page
size
totalElements
totalPages
```

Пример:

```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 0,
  "totalPages": 0
}
```

### Поток выполнения

```text
ApplicationController
→ ApplicationService.getAllApplications(page, size, sortBy, direction)
→ Sort
→ PageRequest.of(page, size, sort)
→ ApplicationRepository.findAll(pageable)
→ Page<Application>
→ ApplicationMapper.toResponse(...)
→ PagedResponse<ApplicationResponse>
```

Правила:

- Spring `Page<Application>` не возвращается клиенту напрямую.
- `Application` Entity не возвращается клиенту.
- Mapper преобразует один `Application`.
- Service организует преобразование элементов всей страницы.
- Controller получает готовый `PagedResponse<ApplicationResponse>`.
- Controller не выполняет маппинг страницы.
- Страница за пределами данных возвращает пустой `content`, а не `404`.

### Валидация параметров

На параметрах Controller используются:

```java
@Min(0)
int page

@Min(1)
@Max(100)
int size
```

Нарушение ограничения вызывает:

```text
HandlerMethodValidationException
```

`GlobalExceptionHandler` преобразует его в единый `ErrorResponse`.

Проверены сценарии:

- обычная непустая страница;
- пустая страница;
- параметры по умолчанию;
- `page=-1`;
- `size=0`;
- `size=101`.

---

## Сортировка Application

### HTTP-контракт

```text
GET /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC
```

Query-параметры:

```text
sortBy — поле Entity, по которому выполняется сортировка
direction — направление сортировки
```

Значения по умолчанию:

```text
sortBy=createdAt
direction=DESC
```

Разрешённые поля:

```text
createdAt
appliedAt
nextContactAt
status
```

Разрешённые направления:

```text
ASC
DESC
```

`Sort.Direction.fromString(...)` принимает направление без учёта регистра, но внешний контракт и тесты используют `ASC` и `DESC`.

### Архитектурное поведение

Основной Service-метод:

```java
getAllApplications(
        int page,
        int size,
        String sortBy,
        String direction
)
```

Создание сортировки:

```text
String direction
→ Sort.Direction
→ Sort.by(sortDirection, sortBy)
→ PageRequest.of(page, size, sort)
```

Двухпараметровый метод:

```java
getAllApplications(int page, int size)
```

делегирует основному методу со значениями:

```text
sortBy=createdAt
direction=DESC
```

Это:

- сохраняет старую сигнатуру;
- не дублирует получение и маппинг страницы;
- обеспечивает стабильный порядок по умолчанию;
- оставляет основную реализацию в одном месте.

### Проверка поля сортировки

В `ApplicationService` используется allow-list:

```text
ALLOWED_SORT_FIELDS
```

Клиентское значение нельзя без проверки передавать в `Sort.by(...)`, потому что неизвестное имя свойства может привести к runtime-ошибке Spring Data/JPA.

Неизвестное поле вызывает:

```text
InvalidApplicationDataException
```

Сообщение:

```text
Unsupported sort field: unknownField
```

### Проверка направления

Вызов `Sort.Direction.fromString(...)` находится в `try/catch`.

Внутренний `IllegalArgumentException` Spring Data преобразуется в проектное исключение:

```text
InvalidApplicationDataException
```

Сообщение API:

```text
Unsupported sort direction: SIDEWAYS
```

Новый общий обработчик для всех `IllegalArgumentException` не добавлялся, чтобы случайно не скрывать программные ошибки.

### Тестирование сортировки

Service-тесты проверяют:

- `createdAt DESC`;
- `appliedAt ASC`;
- передачу ожидаемого `Sort` внутри `Pageable`;
- неправильное поле;
- неправильное направление;
- отсутствие вызовов Repository и Mapper при ошибке;
- сортировку по умолчанию в старых тестах пагинации.

Controller-тесты проверяют:

- передачу `sortBy` и `direction` в Service;
- значения по умолчанию `createdAt DESC`;
- `400 Bad Request` для неправильного поля;
- `400 Bad Request` для неправильного направления;
- полный формат `ErrorResponse`;
- отсутствие вызовов Mapper.

Параметры сортировки не дублируются в `PagedResponse`.

---

## Текущий контракт Application API

### POST /api/applications

- принимает `ApplicationRequest`;
- возвращает `ApplicationResponse`;
- успешный статус: `201 Created`;
- при отсутствии Vacancy возвращается `404 Not Found`;
- проверяет даты и соответствие `status`/`appliedAt`.

### GET /api/applications

- принимает `page`, `size`, `sortBy`, `direction`;
- возвращает `PagedResponse<ApplicationResponse>`;
- успешный статус: `200 OK`;
- значения по умолчанию: `page=0`, `size=10`, `sortBy=createdAt`, `direction=DESC`;
- `page >= 0`;
- `1 <= size <= 100`;
- разрешённые поля: `createdAt`, `appliedAt`, `nextContactAt`, `status`;
- направления: `ASC`, `DESC`;
- пустая база возвращает пустой `content`;
- страница за пределами данных возвращает пустой `content`;
- неправильное поле возвращает `400 Bad Request`;
- неправильное направление возвращает `400 Bad Request`;
- фильтрация пока не добавлена.

### GET /api/applications/{id}

- возвращает `ApplicationResponse`;
- успешный статус: `200 OK`;
- отсутствующий Application возвращает `404 Not Found`.

### PUT /api/applications/{id}

- принимает `ApplicationUpdateRequest`;
- обновляет Vacancy, даты и заметки;
- не изменяет `status`;
- возвращает `ApplicationResponse`;
- успешный статус: `200 OK`;
- проверяет бизнес-правила дат;
- отсутствующий Application или Vacancy возвращает `404 Not Found`.

### PATCH /api/applications/{id}/status

- принимает `ApplicationStatusUpdateRequest`;
- изменяет только `status`;
- успешный статус: `200 OK`;
- повтор текущего статуса идемпотентен;
- проверяет разрешённые переходы;
- проверяет наличие `appliedAt`;
- бизнес-ошибка возвращает `400 Bad Request`.

### DELETE /api/applications/{id}

- удаляет Application;
- успешный статус: `204 No Content`;
- отсутствующий Application возвращает `404 Not Found`.

---

## Архитектурные правила

```text
HTTP request → Controller → Service → Repository → PostgreSQL
```

- Controller отвечает за HTTP-запросы и ответы.
- Service содержит бизнес-логику и организует операции.
- Repository отвечает за доступ к данным.
- Entity описывает модель хранения.
- DTO описывает внешний API-контракт.
- Mapper преобразует Entity и DTO.
- Controller не обращается к Repository напрямую.
- Mapper не обращается к Repository.
- Entity не возвращаются клиенту напрямую.
- Spring `Page` не возвращается клиенту напрямую.
- Используется constructor injection.
- Ошибки обрабатываются централизованно.
- Маппинг пишется вручную.
- Правила дат и переходов статусов находятся в Service.
- Допустимость полей сортировки проверяется в Service.
- Controller принимает query-параметры, но не решает, какие поля разрешены.
- Repository получает уже подготовленный `Pageable`.
- При ошибочных сценариях проверяется отсутствие лишних взаимодействий.
- Универсальные CRUD- и sorting-классы пока не добавляются.

---

## Что уже понимается уверенно

### Git

- working tree и staging area;
- `git status`, `git add`, `git commit`, `git push`;
- обычный и staged diff;
- маленькие code-коммиты;
- отдельные documentation-коммиты;
- работа с untracked-файлами;
- проверка отсутствия секретов;
- проверка чистого working tree после push.

### Java, Spring и архитектура

- назначение Controller, Service, Repository, Entity, DTO и Mapper;
- constructor injection;
- Bean Validation;
- единая обработка `400` и `404`;
- различие HTTP- и бизнес-валидации;
- почему Entity не возвращаются напрямую;
- request DTO и response DTO;
- отдельная операция изменения статуса;
- идемпотентность;
- enum и `switch expression`;
- назначение `PagedResponse<T>`;
- почему Controller не возвращает `Page<Application>`;
- почему размер страницы ограничен;
- почему пустая страница не является `404`;
- почему допустимые поля сортировки проверяются в Service;
- зачем нужен allow-list полей сортировки;
- зачем старый Service-метод делегирует основной реализации;
- почему внешний API не должен зависеть от внутреннего текста ошибки Spring Data.

### JPA и Spring Data

- основные JPA-аннотации;
- `Many-to-One`, `FetchType.LAZY`;
- `EnumType.STRING`;
- `findById()`, `findAll()`, `save()`, `delete()`;
- `Optional` и `orElseThrow()`;
- базовое назначение `Page`;
- базовое назначение `Pageable`;
- `PageRequest.of(page, size)`;
- `PageRequest.of(page, size, sort)`;
- `getNumber()`, `getSize()`, `getTotalElements()`, `getTotalPages()`;
- назначение `Sort`;
- `Sort.Direction.ASC` и `Sort.Direction.DESC`;
- `Sort.Direction.fromString(...)`;
- `Sort.by(direction, property)`.

### Тестирование

- Arrange, Act, Assert;
- Mockito: `when`, `verify`, `never`, `verifyNoInteractions`;
- Service unit-тесты;
- mapper-тесты;
- `@WebMvcTest` и `MockMvc`;
- проверка HTTP status, Content-Type и JSON;
- единый формат ошибок;
- тестирование непустой и пустой страницы;
- параметры по умолчанию;
- границы `page` и `size`;
- отсутствие вызова Service при ошибочной HTTP-валидации;
- проверка `Pageable` с ожидаемым `Sort`;
- проверка отсутствия Repository/Mapper при неверной сортировке;
- Controller-тесты для успешной и ошибочной сортировки.

---

## Что понимается частично и требует повторения

- различие unit-, controller-, repository- и интеграционных тестов;
- LAZY-связи вне активной JPA-сессии;
- транзакции и `@Transactional`;
- различие `MethodArgumentNotValidException` и `HandlerMethodValidationException`;
- внутреннее устройство `Page`, `Pageable`, `PageRequest` и `Sort`;
- поведение сортировки по nullable-полям;
- порядок `NULL` при `ASC` и `DESC` в PostgreSQL;
- стабильность сортировки при одинаковых значениях поля;
- фильтрация через derived query;
- фильтрация с `Pageable`;
- выбор между несколькими repository-методами и `Specification`;
- комбинирование нескольких необязательных фильтров;
- Flyway вместо Hibernate DDL;
- границы между DTO validation, method validation, Service и базой данных.

---

## Технический долг и ограничения

- Пагинация и сортировка реализованы только для `Application`.
- Для `Company` и `Vacancy` пагинация и сортировка пока не добавлены.
- Фильтрация не реализована.
- Пока нет дополнительной сортировки по `id` для полностью стабильного порядка при одинаковых значениях основного поля.
- `PagedResponse<T>` пока не содержит `first`, `last` или `hasNext`.
- Method validation возвращает первое найденное сообщение.
- `fieldErrors` для query-параметров пока пустой.
- Схема не переведена на Flyway.
- Нет Swagger/OpenAPI.
- Нет Dockerfile и `compose.yaml`.
- Нет Testcontainers.
- Нет `Interview`.
- Нет User, Spring Security и JWT.
- Нет GitHub Actions.
- Нет финального README.
- Нет истории изменения статусов.
- CRUD-код намеренно не обобщается раньше времени.
- `One-to-Many` коллекции не добавляются без необходимости.
- MapStruct пока не используется.

---

## Следующее задание

Добавить первый простой фильтр списка `Application` — по статусу.

Предварительный HTTP-контракт:

```text
GET /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&status=INTERVIEW
```

Параметр `status` должен быть необязательным:

- если `status` отсутствует, возвращаются все Application;
- если `status` передан, возвращаются только Application с этим статусом;
- пагинация и сортировка должны продолжить работать;
- неизвестное enum-значение должно возвращать управляемый `400 Bad Request`.

Рекомендуемая последовательность:

1. Повторить, как Spring преобразует строковый query-параметр в enum.
2. Определить тип параметра Controller: `ApplicationStatus`.
3. Добавить repository-метод с `ApplicationStatus` и `Pageable`.
4. Изменить Service так, чтобы он выбирал repository-вызов в зависимости от наличия `status`.
5. Сначала добавить Service unit-тесты.
6. Проверить отсутствие фильтра.
7. Проверить фильтр по одному статусу.
8. Затем обновить Controller и `MockMvc`-тесты.
9. Проверить корректное enum-значение.
10. Проверить неизвестное enum-значение.
11. Сохранить существующие пагинацию и сортировку.
12. Сделать отдельный небольшой code-коммит.

На этом шаге пока не добавлять:

- фильтр по компании;
- фильтр по Vacancy;
- фильтры по датам;
- несколько статусов одновременно;
- `Specification`;
- Criteria API;
- JPQL;
- Swagger;
- Docker;
- Spring Security;
- frontend;
- универсальный фильтр для всех сущностей.

---

## Предварительный вариант Repository

Первый простой вариант может использовать derived query:

```java
Page<Application> findAllByStatus(
        ApplicationStatus status,
        Pageable pageable
);
```

Логика Service:

```text
status отсутствует
→ applicationRepository.findAll(pageable)

status передан
→ applicationRepository.findAllByStatus(status, pageable)
```

Полную реализацию нужно написать самостоятельно после разбора нового шага.

---

## Критерии готовности фильтра по статусу

Фильтр по статусу завершён, когда:

- `status` является необязательным query-параметром;
- без `status` возвращается полный paged список;
- с `status` возвращаются только подходящие Application;
- пагинация продолжает работать;
- сортировка продолжает работать;
- Repository получает тот же подготовленный `Pageable`;
- неизвестное значение статуса возвращает `400 Bad Request`;
- ошибка имеет единый `ErrorResponse`;
- Service покрыт unit-тестами;
- Controller покрыт `MockMvc`-тестами;
- существующие тесты не сломаны;
- полный набор тестов проходит;
- изменение закоммичено отдельно;
- code-коммит отправлен на GitHub;
- разработчик может объяснить, почему пока выбран derived query, а не `Specification`.

---

## Вопросы для повторения

1. Что содержит `Sort`?
2. Чем `Sort.Direction.ASC` отличается от `Sort.Direction.DESC`?
3. Почему нельзя передавать любое клиентское `sortBy` в `Sort.by(...)`?
4. Почему allow-list находится в Service?
5. Как пагинация и сортировка объединяются в одном `PageRequest`?
6. Зачем двухпараметровый Service-метод делегирует четырёхпараметровому?
7. Почему параметры сортировки не добавлены в `PagedResponse`?
8. Почему внутренний `IllegalArgumentException` Spring Data преобразуется в `InvalidApplicationDataException`?
9. Что произойдёт, если две записи имеют одинаковый `createdAt`?
10. Что должен делать API, если параметр `status` не передан?
11. Какой тип лучше использовать для `status` в Controller: `String` или `ApplicationStatus`, и почему?
12. Чем `findAll(pageable)` будет отличаться от `findAllByStatus(status, pageable)`?
13. Почему первый фильтр лучше реализовать отдельно, а не сразу добавлять `Specification`?
14. Какие тесты нужны, чтобы доказать, что фильтрация не сломала сортировку и пагинацию?

---

## Рекомендуемый documentation-коммит

После замены файла проверь, что изменён только:

```text
PROJECT_STATUS.md
```

Команды:

```powershell
git status
git --no-pager diff -- PROJECT_STATUS.md
git add PROJECT_STATUS.md
git --no-pager diff --cached
git status
git commit -m "Update project status after Application sorting"
git push
git status
```

Рекомендуемое сообщение коммита:

```text
Update project status after Application sorting
```
