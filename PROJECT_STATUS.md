# Internship Tracker API — текущее состояние

## Актуальность файла

Последнее обновление: **2026-07-23**.

Это актуальная стабильная точка проекта после завершения пагинации, сортировки и первого фильтра списка `Application` по статусу.

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
- необязательная фильтрация списка по `ApplicationStatus`;
- собственный DTO для paged response;
- проверка параметров `page` и `size`;
- проверка разрешённых полей и направлений сортировки;
- преобразование строкового query-параметра в enum;
- единый формат ошибок для method validation, сортировки и неверных enum-значений.

Текущий крупный этап:

```text
Фильтрация, сортировка и пагинация
```

Завершены:

```text
Пагинация Application
Сортировка Application
Фильтрация Application по status
```

Следующая часть этапа:

```text
Расширение фильтрации списка Application
```

Следующий небольшой шаг — добавить необязательный фильтр по `vacancyId` и корректно обработать его комбинацию с уже существующим `status`.

---

## Текущая стабильная точка

Последний рабочий code-коммит:

```text
3c75b4e Add Application status filtering
```

Последний documentation-коммит до обновления этого файла:

```text
3dc440f Update project status after Application sorting
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
3c75b4e Add Application status filtering
```

Состояние Git после отправки последнего code-коммита:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

Всего в проекте **108 тестов**.

Последний полный запуск:

```text
Tests run: 108
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

Для `Application` завершены CRUD, основные бизнес-правила, пагинация, сортировка и первый фильтр:

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
- [x] Необязательный query-параметр `status`.
- [x] `status` в Controller имеет тип `ApplicationStatus`.
- [x] Без `status` используется `findAll(pageable)`.
- [x] С `status` используется derived query `findAllByStatus(status, pageable)`.
- [x] Фильтр сохраняет пагинацию и сортировку.
- [x] Неизвестное enum-значение возвращает управляемый `400 Bad Request`.
- [x] `MethodArgumentTypeMismatchException` обрабатывается централизованно.
- [x] При ошибке преобразования enum Service и Mapper не вызываются.

Endpoint:

```text
POST   /api/applications
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&status=INTERVIEW
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
→ ApplicationService.getAllApplications(page, size, sortBy, direction, status)
→ Sort
→ PageRequest.of(page, size, sort)
→ status отсутствует: ApplicationRepository.findAll(pageable)
→ status передан: ApplicationRepository.findAllByStatus(status, pageable)
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
        String direction,
        ApplicationStatus status
)
```

Четырёхпараметровый метод делегирует ему со значением `status = null`.

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

делегирует четырёхпараметровому методу со значениями:

```text
sortBy=createdAt
direction=DESC
```

Далее четырёхпараметровый метод вызывает пятиаргументный со значением:

```text
status=null
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


## Фильтрация Application по status

### HTTP-контракт

```text
GET /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&status=INTERVIEW
```

Параметр:

```text
status — необязательный статус Application
```

Поддерживаются значения enum:

```text
PLANNED
APPLIED
TEST_TASK
INTERVIEW
OFFER
REJECTED
WITHDRAWN
```

Если `status` отсутствует:

```text
status = null
→ applicationRepository.findAll(pageable)
```

Если `status` передан:

```text
status = ApplicationStatus.INTERVIEW
→ applicationRepository.findAllByStatus(status, pageable)
```

Один и тот же `Pageable` передаёт в Repository:

- номер страницы;
- размер страницы;
- сортировку.

Поэтому фильтрация не отменяет существующие пагинацию и сортировку.

### Repository

Используется derived query:

```java
Page<Application> findAllByStatus(
        ApplicationStatus status,
        Pageable pageable
);
```

Spring Data строит запрос из имени метода.

Derived query выбран потому, что текущий фильтр содержит только одно простое условие. `Specification` пока добавила бы лишнюю инфраструктуру и усложнила бы код без практической пользы.

### Преобразование query-параметра в enum

Controller принимает:

```java
@RequestParam(required = false)
ApplicationStatus status
```

Spring MVC преобразует строку из HTTP-запроса в `ApplicationStatus` до выполнения метода Controller.

Корректный пример:

```text
status=INTERVIEW
→ ApplicationStatus.INTERVIEW
```

Некорректный пример:

```text
status=UNKNOWN
→ MethodArgumentTypeMismatchException
```

Это не Bean Validation. Ошибка возникает на этапе преобразования типа аргумента Controller.

### Обработка неизвестного значения

`GlobalExceptionHandler` обрабатывает:

```text
MethodArgumentTypeMismatchException
```

Клиент получает:

```json
{
  "status": 400,
  "error": "Bad request",
  "message": "Unsupported value for parameter status: UNKNOWN",
  "path": "/api/applications",
  "fieldErrors": {}
}
```

Внутренний текст исключения Spring клиенту не возвращается.

Запрос с неизвестным статусом не доходит до Service, поэтому при таком сценарии:

- `ApplicationService` не вызывается;
- `ApplicationMapper` не вызывается;
- Repository не вызывается.

### Перегрузки Service

Текущий поток вызовов:

```text
getAllApplications(page, size)
→ getAllApplications(page, size, sortBy, direction)
→ getAllApplications(page, size, sortBy, direction, status)
→ Repository
```

Обратных вызовов между перегрузками быть не должно, иначе возникает бесконечная рекурсия и `StackOverflowError`.

Во время реализации такая ошибка была обнаружена тестами и исправлена.

### Тестирование фильтра

Service-тест проверяет:

- передачу `ApplicationStatus.INTERVIEW`;
- создание `Pageable` с ожидаемой сортировкой;
- вызов `findAllByStatus(status, pageable)`;
- отсутствие вызова `findAll(pageable)`;
- преобразование Entity в `ApplicationResponse`;
- содержимое и метаданные `PagedResponse`.

Controller-тесты проверяют:

- преобразование `status=INTERVIEW` в enum;
- передачу статуса в Service;
- JSON содержимого страницы;
- отсутствие фильтра как `status = null`;
- неизвестное значение `UNKNOWN`;
- единый формат `ErrorResponse`;
- отсутствие вызовов Service и Mapper при ошибке типа.

---

## Текущий контракт Application API

### POST /api/applications

- принимает `ApplicationRequest`;
- возвращает `ApplicationResponse`;
- успешный статус: `201 Created`;
- при отсутствии Vacancy возвращается `404 Not Found`;
- проверяет даты и соответствие `status`/`appliedAt`.

### GET /api/applications

- принимает `page`, `size`, `sortBy`, `direction`, необязательный `status`;
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
- без `status` возвращаются все Application;
- с `status` возвращаются только Application с указанным статусом;
- неизвестное значение `status` возвращает `400 Bad Request`;
- фильтры по Vacancy, Company и датам пока не добавлены.

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
- Необязательный enum-параметр преобразуется Spring MVC до выполнения Controller.
- Отсутствующий фильтр передаётся в Service как `null`.
- Выбор между `findAll(pageable)` и `findAllByStatus(status, pageable)` находится в Service.
- Derived query используется для одного простого фильтра.
- При ошибочных сценариях проверяется отсутствие лишних взаимодействий.
- Универсальные CRUD-, sorting- и filtering-классы пока не добавляются.

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
- почему внешний API не должен зависеть от внутреннего текста ошибки Spring Data;
- зачем параметр `status` имеет тип `ApplicationStatus`;
- что означает `status = null`;
- почему неизвестный enum не доходит до Service;
- назначение `MethodArgumentTypeMismatchException`;
- почему для одного фильтра выбран derived query.

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
- `Sort.by(direction, property)`;
- derived query `findAllByStatus(status, pageable)`;
- передача `Pageable` в фильтрующий repository-метод.

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
- Controller-тесты для успешной и ошибочной сортировки;
- Service-тест фильтрации по статусу;
- проверка отсутствия обычного `findAll(pageable)` при активном фильтре;
- Controller-тест преобразования строки в enum;
- тест `MethodArgumentTypeMismatchException`;
- проверка отсутствия Service/Mapper при неизвестном enum.

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
- построение Spring Data запроса по имени derived query;
- выбор между несколькими repository-методами;
- рост количества derived query при добавлении фильтров;
- переход к `Specification`;
- комбинирование нескольких необязательных фильтров;
- Flyway вместо Hibernate DDL;
- границы между DTO validation, method validation, Service и базой данных.

---

## Технический долг и ограничения

- Пагинация, сортировка и фильтр по статусу реализованы только для `Application`.
- Для `Company` и `Vacancy` пагинация, сортировка и фильтрация пока не добавлены.
- Для `Application` пока реализован только один фильтр — `status`.
- Фильтры по `vacancyId`, `companyId` и датам пока не реализованы.
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

Добавить второй необязательный фильтр списка `Application` — по `vacancyId`.

Предварительный HTTP-контракт:

```text
GET /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&vacancyId=20
```

Нужно поддержать четыре комбинации:

```text
status отсутствует, vacancyId отсутствует
status передан, vacancyId отсутствует
status отсутствует, vacancyId передан
status передан, vacancyId передан
```

Ожидаемая логика Repository:

```text
нет фильтров
→ findAll(pageable)

только status
→ findAllByStatus(status, pageable)

только vacancyId
→ findAllByVacancyId(vacancyId, pageable)

status и vacancyId
→ findAllByStatusAndVacancyId(status, vacancyId, pageable)
```

Точные имена derived query нужно сверить с полями Entity. Для связи `Application.vacancy` может понадобиться путь по вложенному свойству `vacancy.id`.

Рекомендуемая последовательность:

1. Повторить, как derived query обращается к вложенному полю связанной Entity.
2. Выбрать точное имя repository-метода для `vacancy.id`.
3. Сначала добавить repository-методы.
4. Добавить Service-тест для фильтра только по `vacancyId`.
5. Добавить Service-тест для комбинации `status + vacancyId`.
6. Расширить основной Service-метод параметром `Long vacancyId`.
7. Реализовать четыре ветки выбора Repository.
8. Обновить делегирующие перегрузки без рекурсии.
9. Добавить необязательный `@RequestParam Long vacancyId` в Controller.
10. Обновить старые Controller-тесты.
11. Добавить успешный Controller-тест для `vacancyId`.
12. Добавить тест комбинации `status + vacancyId`.
13. Запустить полный набор тестов.
14. Сделать отдельный code-коммит.

На этом шаге пока не добавлять:

- фильтр по Company;
- фильтры по датам;
- несколько статусов;
- `Specification`;
- Criteria API;
- JPQL;
- Swagger;
- Docker;
- Spring Security;
- frontend;
- универсальный фильтр для всех сущностей.

Этот шаг нужен не только ради функциональности. Он покажет, как число derived query растёт при комбинировании независимых фильтров. После него нужно отдельно решить, продолжать ли derived query или переходить к `Specification`.

---

## Предварительные варианты Repository

Возможные методы:

```java
Page<Application> findAllByVacancyId(
        Long vacancyId,
        Pageable pageable
);
```

или, если Spring Data требует явный путь по вложенному свойству:

```java
Page<Application> findAllByVacancy_Id(
        Long vacancyId,
        Pageable pageable
);
```

Для комбинации:

```java
Page<Application> findAllByStatusAndVacancyId(
        ApplicationStatus status,
        Long vacancyId,
        Pageable pageable
);
```

Точное имя нужно выбрать после проверки структуры `Application` и правил Spring Data derived query.

Полную реализацию нужно написать самостоятельно после разбора нового шага.

---

## Критерии готовности фильтра по vacancyId

Фильтр завершён, когда:

- `vacancyId` является необязательным query-параметром;
- без фильтров возвращается полный paged список;
- работает фильтр только по `status`;
- работает фильтр только по `vacancyId`;
- работает комбинация `status + vacancyId`;
- пагинация и сортировка сохраняются во всех четырёх ветках;
- Repository получает один и тот же подготовленный `Pageable`;
- Service покрыт unit-тестами для новых веток;
- Controller покрыт `MockMvc`-тестами;
- существующие 108 тестов не сломаны;
- полный набор тестов проходит;
- изменение закоммичено отдельно;
- code-коммит отправлен на GitHub;
- разработчик может объяснить, почему количество derived query растёт при добавлении независимых фильтров.

---

## Вопросы для повторения

1. Что получает Service, если query-параметр `status` отсутствует?
2. Почему `status` в Controller имеет тип `ApplicationStatus`, а не `String`?
3. Почему `status=UNKNOWN` не доходит до Service?
4. Чем преобразование enum отличается от Bean Validation?
5. Зачем обрабатывать `MethodArgumentTypeMismatchException`?
6. Почему внутренний текст исключения Spring не нужно возвращать клиенту?
7. Почему `findAllByStatus` принимает `Pageable`?
8. Что хранится в переданном `Pageable`?
9. Чем `findAll(pageable)` отличается от `findAllByStatus(status, pageable)`?
10. Почему для одного фильтра derived query проще, чем `Specification`?
11. Что произойдёт с количеством repository-методов после добавления `vacancyId`?
12. Какие четыре комбинации возникают у двух необязательных фильтров?
13. Как derived query может обратиться к `vacancy.id`?
14. Почему все перегрузки Service должны делегировать только в одном направлении?
15. Из-за чего во время реализации возник `StackOverflowError`?
16. Какие тесты докажут, что комбинация `status + vacancyId` не сломала пагинацию и сортировку?

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
git commit -m "Update project status after Application status filtering"
git push
git status
```

Рекомендуемое сообщение коммита:

```text
Update project status after Application status filtering
```
