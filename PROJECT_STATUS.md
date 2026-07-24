# Internship Tracker API — текущее состояние

## Актуальность файла

Последнее обновление: **2026-07-24**.

Это актуальная стабильная точка проекта после завершения пагинации, сортировки и фильтрации списка `Application` по `status` и `vacancyId`.

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
- необязательная фильтрация по `ApplicationStatus`;
- необязательная фильтрация по `vacancyId`;
- комбинация фильтров `status + vacancyId`;
- собственный DTO для paged response;
- проверка параметров `page` и `size`;
- проверка разрешённых полей и направлений сортировки;
- преобразование строкового query-параметра в enum;
- единый формат ошибок для method validation, сортировки и неверных enum-значений;
- Service- и Controller-тесты всех четырёх комбинаций фильтров.

Текущий крупный этап:

```text
Фильтрация, сортировка и пагинация
```

Завершены:

```text
Пагинация Application
Сортировка Application
Фильтрация Application по status
Фильтрация Application по vacancyId
Комбинация status + vacancyId
```

Следующая часть этапа:

```text
Подготовка масштабируемой фильтрации Application
```

Следующий небольшой шаг — сравнить текущие derived query с `Specification` и подготовить переход к динамическому построению условий до добавления фильтров по компании и датам.

---

## Текущая стабильная точка

Последний рабочий code-коммит:

```text
2e92a60 Add Application vacancy filtering
```

Последний documentation-коммит до обновления этого файла:

```text
5003a51 Update project status after Application status filtering
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
2e92a60 Add Application vacancy filtering
```

Состояние Git после отправки последнего code-коммита:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

Всего в проекте **112 тестов**.

Последний полный запуск:

```text
Tests run: 112
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

Пароль PostgreSQL уже передаётся через переменную окружения `DB_PASSWORD` в рабочем PowerShell-сеансе. Повторно задавать переменную перед каждым запуском тестов не требуется. Она понадобится снова после открытия нового терминала или при ошибке отсутствующей переменной окружения.

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

Для `Application` завершены CRUD, основные бизнес-правила, пагинация, сортировка и два комбинируемых фильтра:

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
- [x] Перегрузки Service делегируют основной реализации в одном направлении.
- [x] Необязательный query-параметр `status`.
- [x] `status` в Controller имеет тип `ApplicationStatus`.
- [x] Необязательный query-параметр `vacancyId`.
- [x] `vacancyId` в Controller имеет тип `Long`.
- [x] Без фильтров используется `findAll(pageable)`.
- [x] Только с `status` используется `findAllByStatus(status, pageable)`.
- [x] Только с `vacancyId` используется `findAllByVacancy_Id(vacancyId, pageable)`.
- [x] С `status` и `vacancyId` используется `findAllByStatusAndVacancy_Id(status, vacancyId, pageable)`.
- [x] Все фильтры сохраняют пагинацию и сортировку.
- [x] Неизвестное enum-значение возвращает управляемый `400 Bad Request`.
- [x] `MethodArgumentTypeMismatchException` обрабатывается централизованно.
- [x] При ошибке преобразования enum Service и Mapper не вызываются.
- [x] Service-тесты покрывают фильтр только по `vacancyId` и комбинацию `status + vacancyId`.
- [x] Controller-тесты покрывают фильтр только по `vacancyId` и комбинацию `status + vacancyId`.

Endpoint:

```text
POST   /api/applications
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&status=INTERVIEW
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&vacancyId=20
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&status=INTERVIEW&vacancyId=20
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

## Пагинация, сортировка и фильтрация Application

### HTTP-контракт

Базовый запрос:

```text
GET /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC
```

Доступные query-параметры:

```text
page       — номер страницы, начиная с 0
size       — максимальное количество элементов на странице
sortBy     — разрешённое поле сортировки
direction  — ASC или DESC
status     — необязательный ApplicationStatus
vacancyId  — необязательный идентификатор Vacancy
```

Значения по умолчанию:

```text
page=0
size=10
sortBy=createdAt
direction=DESC
```

Ограничения:

```text
page >= 0
1 <= size <= 100
```

Разрешённые поля сортировки:

```text
createdAt
appliedAt
nextContactAt
status
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
→ ApplicationService.getAllApplications(page, size, sortBy, direction, status, vacancyId)
→ проверка sortBy
→ преобразование direction в Sort.Direction
→ Sort.by(sortDirection, sortBy)
→ PageRequest.of(page, size, sort)
→ выбор Repository-метода по наличию фильтров
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
- Один и тот же подготовленный `Pageable` используется во всех ветках фильтрации.

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

### Сортировка

Создание сортировки:

```text
String direction
→ Sort.Direction
→ Sort.by(sortDirection, sortBy)
→ PageRequest.of(page, size, sort)
```

В `ApplicationService` используется allow-list:

```text
ALLOWED_SORT_FIELDS
```

Неизвестное поле вызывает:

```text
InvalidApplicationDataException
Unsupported sort field: unknownField
```

Неверное направление преобразуется в проектное исключение:

```text
InvalidApplicationDataException
Unsupported sort direction: SIDEWAYS
```

Общий обработчик всех `IllegalArgumentException` не добавлялся, чтобы не скрывать программные ошибки.

### Фильтрация по status

Controller принимает:

```java
@RequestParam(required = false)
ApplicationStatus status
```

Spring MVC преобразует строку query-параметра в enum до выполнения метода Controller.

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

Клиент получает единый `400 Bad Request`, внутренний текст Spring не возвращается.

### Фильтрация по vacancyId

Controller принимает:

```java
@RequestParam(required = false)
Long vacancyId
```

`vacancyId` передаётся в Service без выбора Repository-метода в Controller.

Repository обращается к вложенному свойству `Application.vacancy.id` через underscore в имени derived query:

```java
Page<Application> findAllByVacancy_Id(
        Long vacancyId,
        Pageable pageable
);
```

Underscore явно разделяет свойства:

```text
vacancy.id
```

### Четыре ветки фильтрации

```text
status == null, vacancyId == null
→ findAll(pageable)

status != null, vacancyId == null
→ findAllByStatus(status, pageable)

status == null, vacancyId != null
→ findAllByVacancy_Id(vacancyId, pageable)

status != null, vacancyId != null
→ findAllByStatusAndVacancy_Id(status, vacancyId, pageable)
```

Repository-методы:

```java
Page<Application> findAllByStatus(
        ApplicationStatus status,
        Pageable pageable
);

Page<Application> findAllByVacancy_Id(
        Long vacancyId,
        Pageable pageable
);

Page<Application> findAllByStatusAndVacancy_Id(
        ApplicationStatus status,
        Long vacancyId,
        Pageable pageable
);
```

### Перегрузки Service

Текущий поток вызовов:

```text
getAllApplications(page, size)
→ getAllApplications(page, size, sortBy, direction)
→ getAllApplications(page, size, sortBy, direction, status)
→ getAllApplications(page, size, sortBy, direction, status, vacancyId)
→ Repository
```

Основная логика находится только в шестипараметровом методе.

Перегрузки делегируют только в сторону более полной сигнатуры. Обратных вызовов быть не должно, иначе возникает бесконечная рекурсия и `StackOverflowError`.

### Тестирование

Service-тесты проверяют:

- пагинацию и сортировку без фильтров;
- фильтр только по `status`;
- фильтр только по `vacancyId`;
- комбинацию `status + vacancyId`;
- вызов правильного Repository-метода;
- отсутствие вызовов неправильных Repository-методов;
- преобразование Entity в `ApplicationResponse`;
- содержимое и метаданные `PagedResponse`;
- отсутствие Repository и Mapper при ошибочной сортировке.

Controller-тесты проверяют:

- передачу `page`, `size`, `sortBy`, `direction` в Service;
- значения по умолчанию;
- передачу отсутствующих фильтров как `null`;
- преобразование `status=INTERVIEW` в enum;
- передачу `vacancyId=20` как `Long`;
- одновременную передачу `status` и `vacancyId`;
- JSON содержимого страницы;
- `400 Bad Request` для неправильной пагинации, сортировки и enum;
- отсутствие вызовов Service и Mapper при ошибке преобразования типа.

---

## Текущий контракт Application API

### POST /api/applications

- принимает `ApplicationRequest`;
- возвращает `ApplicationResponse`;
- успешный статус: `201 Created`;
- при отсутствии Vacancy возвращается `404 Not Found`;
- проверяет даты и соответствие `status`/`appliedAt`.

### GET /api/applications

- принимает `page`, `size`, `sortBy`, `direction`, необязательные `status` и `vacancyId`;
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
- без фильтров возвращаются все Application;
- с `status` возвращаются Application с указанным статусом;
- с `vacancyId` возвращаются Application указанной Vacancy;
- `status` и `vacancyId` можно использовать одновременно;
- неизвестное значение `status` возвращает `400 Bad Request`;
- фильтры по Company и датам пока не добавлены.

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
- Controller принимает query-параметры, но не выбирает Repository-метод.
- Repository получает уже подготовленный `Pageable`.
- Необязательный enum-параметр преобразуется Spring MVC до выполнения Controller.
- Отсутствующие фильтры передаются в Service как `null`.
- Выбор Repository-метода по комбинации фильтров находится в Service.
- Derived query используются для двух простых фильтров и их комбинации.
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
- зачем старые Service-методы делегируют основной реализации;
- почему перегрузки должны делегировать только в одном направлении;
- почему внешний API не должен зависеть от внутреннего текста ошибки Spring;
- зачем параметр `status` имеет тип `ApplicationStatus`;
- что означают `status = null` и `vacancyId = null`;
- почему неизвестный enum не доходит до Service;
- назначение `MethodArgumentTypeMismatchException`;
- почему Controller не выбирает Repository-метод;
- почему для одного и двух простых фильтров можно использовать derived query.

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
- derived query по вложенному свойству `findAllByVacancy_Id(...)`;
- комбинированный derived query `findAllByStatusAndVacancy_Id(...)`;
- передача одного `Pageable` во все фильтрующие repository-методы.

### Тестирование

- Arrange, Act, Assert;
- Mockito: `when`, `verify`, `never`, `verifyNoInteractions`;
- различие `assertSame` и `assertEquals`;
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
- Service-тесты всех веток фильтрации;
- проверка отсутствия неправильных Repository-вызовов;
- Controller-тест преобразования строки в enum;
- тест `MethodArgumentTypeMismatchException`;
- проверка отсутствия Service/Mapper при неизвестном enum;
- необходимость обновлять `when(...)` и `verify(...)` после изменения сигнатуры вызываемого метода.

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
- разбор вложенных свойств в имени derived query;
- рост количества derived query при добавлении независимых фильтров;
- динамическое комбинирование фильтров;
- интерфейс `JpaSpecificationExecutor`;
- интерфейс `Specification<T>`;
- Criteria API, используемый внутри Specification;
- выбор между derived query, JPQL и Specification;
- Flyway вместо Hibernate DDL;
- границы между DTO validation, method validation, Service и базой данных.

---

## Технический долг и ограничения

- Пагинация, сортировка и фильтрация реализованы только для `Application`.
- Для `Company` и `Vacancy` пагинация, сортировка и фильтрация пока не добавлены.
- Для `Application` реализованы только фильтры `status` и `vacancyId`.
- Фильтры по `companyId` и датам пока не реализованы.
- При добавлении каждого нового независимого фильтра количество derived query будет быстро расти.
- Текущий выбор Repository-метода содержит четыре явные ветки и пока остаётся читаемым.
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

Разобрать переход от нескольких derived query к Spring Data JPA `Specification`.

Цель следующего шага — не сразу добавлять новые HTTP-фильтры, а понять, как избежать отдельного Repository-метода для каждой комбинации условий.

Текущая проблема:

```text
2 независимых фильтра
→ 4 комбинации
→ 3 фильтрующих derived query + findAll
```

При добавлении `companyId` станет до восьми комбинаций, а при добавлении фильтров по датам количество сочетаний продолжит расти.

Предварительный план:

1. Объяснить, что такое `Specification<Application>`.
2. Объяснить, зачем нужен `JpaSpecificationExecutor<Application>`.
3. Разобрать параметры `root`, `query` и `criteriaBuilder` без углубления во всё внутреннее устройство Criteria API.
4. Написать маленькую Specification только для `status`.
5. Добавить Specification только для `vacancyId`.
6. Научиться соединять условия через `and()`.
7. Сохранить текущий HTTP-контракт без изменений.
8. Сначала покрыть новую сборку фильтра unit-тестами.
9. Затем заменить четыре ветки Repository одним вызовом `findAll(specification, pageable)`.
10. Убедиться, что все 112 существующих тестов продолжают проходить или корректно обновлены под новую архитектуру.
11. После рефакторинга решить, добавлять ли `companyId` и фильтры по датам.
12. Зафиксировать архитектурное решение в `DECISIONS.md`.

На первом подшаге пока не добавлять:

- `companyId`;
- фильтры по датам;
- несколько статусов;
- пользовательский поиск по тексту;
- QueryDSL;
- ручной Criteria API в Service;
- Swagger;
- Docker;
- Spring Security;
- frontend.

### Критерии готовности следующего шага

Переход к Specification будет завершён, когда:

- `ApplicationRepository` поддерживает `JpaSpecificationExecutor<Application>`;
- условия по `status` и `vacancyId` создаются независимо;
- отсутствующий фильтр не добавляет условие в запрос;
- два фильтра объединяются через логическое `AND`;
- пагинация и сортировка продолжают передаваться через `Pageable`;
- Service больше не выбирает один из четырёх derived query;
- HTTP-контракт Controller не меняется;
- успешные и ошибочные Controller-тесты продолжают проходить;
- новая логика покрыта тестами;
- полный набор тестов проходит;
- решение объяснимо на собеседовании;
- изменение закоммичено отдельно;
- `DECISIONS.md` обновлён отдельным documentation-коммитом при необходимости.

---

## Вопросы для повторения

1. Какие значения получают `status` и `vacancyId`, если query-параметры отсутствуют?
2. Почему `status` в Controller имеет тип `ApplicationStatus`, а не `String`?
3. Почему `status=UNKNOWN` не доходит до Service?
4. Чем преобразование enum отличается от Bean Validation?
5. Почему `vacancyId` передаётся в Service, а Controller не выбирает Repository-метод?
6. Что означает underscore в `findAllByVacancy_Id`?
7. Как имя `findAllByStatusAndVacancy_Id` описывает условие запроса?
8. Почему все repository-методы принимают `Pageable`?
9. Что хранится в `Pageable` кроме номера и размера страницы?
10. Какие четыре комбинации существуют у двух необязательных фильтров?
11. Почему основная логика находится в шестипараметровом Service-методе?
12. Почему перегрузки должны делегировать только в одном направлении?
13. Из-за чего может возникнуть `StackOverflowError` между перегрузками?
14. Почему в тесте списков использовался `assertEquals`, а не `assertSame`?
15. Почему после изменения сигнатуры Controller-вызова пришлось обновлять старые `when(...)` и `verify(...)`?
16. Как тест доказывает, что Service выбрал правильную ветку Repository?
17. Почему количество derived query растёт при добавлении независимых фильтров?
18. Как `Specification` может уменьшить количество repository-методов?
19. Что должно остаться неизменным для клиента после внутреннего рефакторинга на Specification?
20. Почему сначала полезно рефакторить существующие фильтры, а уже потом добавлять новые?

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
git commit -m "Update project status after Application vacancy filtering"
git push
git status
```

Рекомендуемое сообщение коммита:

```text
Update project status after Application vacancy filtering
```
