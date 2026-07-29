# Internship Tracker API — текущее состояние

## Актуальность файла

Последнее обновление: **2026-07-29**.

Это актуальная стабильная точка проекта после расширения динамической фильтрации списка `Application`: добавлены фильтры
по `companyId` и диапазону `appliedAt`, а противоречивый диапазон дат отклоняется с `400 Bad Request`.

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
- необязательная фильтрация по `companyId`;
- необязательная фильтрация по нижней границе `appliedAtFrom`;
- необязательная фильтрация по верхней границе `appliedAtTo`;
- фильтрация по диапазону `appliedAtFrom + appliedAtTo`;
- комбинация фильтров `status + vacancyId + companyId + appliedAtFrom + appliedAtTo`;
- проверка согласованности диапазона дат фильтрации;
- динамическая сборка фильтров через `Specification<Application>`;
- выполнение Specification через `JpaSpecificationExecutor<Application>`;
- собственный DTO для paged response;
- проверка параметров `page` и `size`;
- проверка разрешённых полей и направлений сортировки;
- преобразование строкового query-параметра в enum;
- единый формат ошибок для method validation, сортировки и неверных enum-значений;
- Service-, Controller- и Specification-тесты.

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
Переход от derived query к Specification
Фильтрация Application по companyId
Фильтрация Application по диапазону appliedAt
Проверка противоречивого диапазона appliedAt
```

Следующая часть этапа:

```text
Расширение динамической фильтрации Application
```

Следующий небольшой шаг:

```text
Добавить Specification hasWorkFormat(workFormat)
и unit-тест пути Application.vacancy.workFormat
```

На первом подшаге не менять Controller и Service. Сначала реализовать и проверить только новое условие Specification.

---

## Текущая стабильная точка

Последний рабочий code-коммит:

```text
41f7c8f Add Application applied date filtering
```

Последний documentation-коммит до обновления этого файла:

```text
aef0ce2 Update project status after specification refactor
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
f802fa6 Replace application derived queries with specifications
13084b5 Add Application company filtering
41f7c8f Add Application applied date filtering
```

Состояние Git после отправки последнего code-коммита:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

Всего в проекте **131 тест**.

Последний полный запуск:

```text
Tests run: 131
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

Пароль PostgreSQL уже передаётся через переменную окружения `DB_PASSWORD` в рабочем PowerShell-сеансе. Повторно задавать
переменную перед каждым запуском тестов не требуется. Она понадобится снова после открытия нового терминала или при
ошибке отсутствующей переменной окружения.

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

Для `Application` завершены CRUD, основные бизнес-правила, пагинация, сортировка и динамическая фильтрация по статусу,
вакансии, компании и диапазону даты отклика:

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
- [x] Необязательный query-параметр `companyId`.
- [x] `companyId` в Controller имеет тип `Long`.
- [x] Необязательные query-параметры `appliedAtFrom` и `appliedAtTo`.
- [x] Для дат используется ISO date-time через `@DateTimeFormat`.
- [x] Реализованы `hasCompanyId(...)`, `hasAppliedAtFrom(...)` и `hasAppliedAtTo(...)`.
- [x] Путь фильтра по компании проходит через `Application.vacancy.company.id`.
- [x] Нижняя граница использует `greaterThanOrEqualTo(...)`.
- [x] Верхняя граница использует `lessThanOrEqualTo(...)`.
- [x] Противоречивый диапазон дат отклоняется до обращения к Repository.
- [x] Условия фильтрации создаются независимо.
- [x] Отсутствующие фильтры не добавляют ограничения.
- [x] Переданные фильтры объединяются через `and(...)`.
- [x] Для начального состояния используется `Specification.unrestricted()`.
- [x] Repository расширяет `JpaSpecificationExecutor<Application>`.
- [x] Service вызывает один метод `findAll(specification, pageable)` для всех комбинаций фильтров.
- [x] Старые фильтрующие derived query удалены.
- [x] Все фильтры сохраняют пагинацию и сортировку.
- [x] Неизвестное enum-значение возвращает управляемый `400 Bad Request`.
- [x] `MethodArgumentTypeMismatchException` обрабатывается централизованно.
- [x] При ошибке преобразования enum Service и Mapper не вызываются.
- [x] Specification-тесты покрывают отсутствие фильтров, каждый фильтр отдельно и их комбинацию.
- [x] Service-тесты используют `findAll(Specification, Pageable)`.
- [x] Controller-тесты сохраняют прежний HTTP-контракт.

Endpoint:

```text
POST   /api/applications
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&status=INTERVIEW
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&vacancyId=20
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&status=INTERVIEW&vacancyId=20
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&companyId=5
GET    /api/applications?page=0&size=10&sortBy=appliedAt&direction=ASC&appliedAtFrom=2026-07-01T00:00:00&appliedAtTo=2026-07-31T23:59:00
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
page           — номер страницы, начиная с 0
size           — максимальное количество элементов на странице
sortBy         — разрешённое поле сортировки
direction      — ASC или DESC
status         — необязательный ApplicationStatus
vacancyId      — необязательный идентификатор Vacancy
companyId      — необязательный идентификатор Company
appliedAtFrom  — необязательная нижняя граница appliedAt в ISO date-time
appliedAtTo    — необязательная верхняя граница appliedAt в ISO date-time
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

### Поток выполнения

```text
ApplicationController
→ ApplicationService.getAllApplications(
    page,
    size,
    sortBy,
    direction,
    status,
    vacancyId,
    companyId,
    appliedAtFrom,
    appliedAtTo
  )
→ проверка sortBy
→ преобразование direction в Sort.Direction
→ Sort.by(sortDirection, sortBy)
→ PageRequest.of(page, size, sort)
→ проверка appliedAtFrom <= appliedAtTo
→ ApplicationSpecifications.withFilters(
    status,
    vacancyId,
    companyId,
    appliedAtFrom,
    appliedAtTo
  )
→ applicationRepository.findAll(specification, pageable)
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
- Один и тот же подготовленный `Pageable` передаётся в Repository вместе со Specification.
- Controller не знает, как строятся условия фильтрации.
- Service не содержит веток по всем комбинациям фильтров.
- Repository не содержит отдельный метод для каждой комбинации фильтров.

### Валидация параметров

На параметрах Controller используются:

```java

@Min(0)
int page

@Min(1)
@Max(100)
int size
```

Нарушение ограничения вызывает `HandlerMethodValidationException`, который преобразуется в единый `ErrorResponse`.

### Сортировка

Создание сортировки:

```text
String direction
→ Sort.Direction
→ Sort.by(sortDirection, sortBy)
→ PageRequest.of(page, size, sort)
```

В `ApplicationService` используется allow-list `ALLOWED_SORT_FIELDS`.

Неизвестное поле вызывает:

```text
InvalidApplicationDataException
Unsupported sort field: unknownField
```

Неверное направление вызывает:

```text
InvalidApplicationDataException
Unsupported sort direction: SIDEWAYS
```

### Динамическая фильтрация через Specification

`ApplicationRepository`:

```java
public interface ApplicationRepository
        extends JpaRepository<Application, Long>,
        JpaSpecificationExecutor<Application> {
}
```

`JpaSpecificationExecutor<Application>` добавляет методы выполнения Specification, включая:

```java
findAll(specification, pageable)
```

`ApplicationSpecifications.hasStatus(status)` описывает условие:

```text
Application.status = status
```

`ApplicationSpecifications.hasVacancyId(vacancyId)` описывает путь и условие:

```text
Application.vacancy.id = vacancyId
```

`ApplicationSpecifications.hasCompanyId(companyId)` описывает путь и условие:

```text
Application.vacancy.company.id = companyId
```

`ApplicationSpecifications.hasAppliedAtFrom(appliedAtFrom)` описывает условие:

```text
Application.appliedAt >= appliedAtFrom
```

`ApplicationSpecifications.hasAppliedAtTo(appliedAtTo)` описывает условие:

```text
Application.appliedAt <= appliedAtTo
```

`ApplicationSpecifications.withFilters(...)` начинает с `Specification.unrestricted()` и через `and(...)` добавляет
только те условия, параметры которых не равны `null`.

Примеры:

```text
все фильтры == null
→ Specification.unrestricted()

status != null
→ hasStatus(status)

vacancyId != null
→ hasVacancyId(vacancyId)

companyId != null
→ hasCompanyId(companyId)

appliedAtFrom != null
→ hasAppliedAtFrom(appliedAtFrom)

appliedAtTo != null
→ hasAppliedAtTo(appliedAtTo)

appliedAtFrom != null, appliedAtTo != null
→ hasAppliedAtFrom(appliedAtFrom).and(hasAppliedAtTo(appliedAtTo))
```

`Specification.unrestricted()` — существующая Specification без ограничивающего `Predicate`. Она используется как
безопасная начальная точка, а не как проверка существования.

Методы `hasStatus(...)`, `hasVacancyId(...)`, `hasCompanyId(...)`, `hasAppliedAtFrom(...)` и `hasAppliedAtTo(...)`
возвращают описание условий. Запрос к базе выполняется позже при вызове Repository.

Параметры lambda Specification:

```text
root            — путь от Entity к её полям и связям
query           — объект строящегося Criteria-запроса
criteriaBuilder — создаёт Predicate, например equal или and
```

### Перегрузки Service

Текущий поток вызовов:

```text
getAllApplications(page, size)
→ getAllApplications(page, size, sortBy, direction)
→ getAllApplications(page, size, sortBy, direction, status)
→ getAllApplications(page, size, sortBy, direction, status, vacancyId)
→ getAllApplications(page, size, sortBy, direction, status, vacancyId, companyId)
→ getAllApplications(
    page,
    size,
    sortBy,
    direction,
    status,
    vacancyId,
    companyId,
    appliedAtFrom,
    appliedAtTo
  )
→ Specification
→ Repository
```

Основная логика находится только в девятипараметровом методе.

Перегрузки делегируют только в сторону более полной сигнатуры. Обратных вызовов быть не должно, иначе возникает
бесконечная рекурсия и `StackOverflowError`.

### Тестирование

`ApplicationSpecificationsTest` проверяет содержание условий:

- путь `Application.status`;
- путь `Application.vacancy.id`;
- путь `Application.vacancy.company.id`;
- путь `Application.appliedAt`;
- вызовы `equal(...)`, `greaterThanOrEqualTo(...)` и `lessThanOrEqualTo(...)`;
- объединение нескольких `Predicate` через `criteriaBuilder.and(...)`;
- `Specification.unrestricted()` при отсутствии фильтров;
- каждый фильтр отдельно;
- комбинацию фильтров;
- диапазон `appliedAtFrom + appliedAtTo`.

`ApplicationServiceTest` проверяет обязанности Service:

- создание правильного `Pageable`;
- пагинацию и сортировку;
- вызов `findAll(Specification, Pageable)`;
- преобразование Entity в `ApplicationResponse`;
- содержимое и метаданные `PagedResponse`;
- отсутствие Repository и Mapper при ошибочной сортировке;
- отсутствие Repository и Mapper при противоречивом диапазоне дат.

`ApplicationControllerTest` проверяет HTTP-контракт:

- query-параметры и значения по умолчанию;
- преобразование `status` в enum;
- передачу `vacancyId` и `companyId` как `Long`;
- преобразование `appliedAtFrom` и `appliedAtTo` из ISO date-time;
- JSON paged response;
- управляемые `400 Bad Request`;
- отсутствие вызова Service при ошибке HTTP-преобразования или method validation.

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
- Specification описывает динамические условия выборки.
- Controller не обращается к Repository напрямую.
- Mapper не обращается к Repository.
- Entity не возвращаются клиенту напрямую.
- Spring `Page` не возвращается клиенту напрямую.
- Используется constructor injection.
- Ошибки обрабатываются централизованно.
- Маппинг пишется вручную.
- Правила дат и переходов статусов находятся в Service.
- Допустимость полей сортировки проверяется в Service.
- Controller принимает query-параметры, но не строит Specification.
- Service получает готовую Specification из `ApplicationSpecifications`.
- Repository выполняет Specification вместе с `Pageable`.
- Отсутствующие фильтры передаются в Service как `null`.
- Динамическая сборка фильтров находится в `ApplicationSpecifications.withFilters(...)`.
- Derived query для комбинаций `status` и `vacancyId` удалены.
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

- назначение Controller, Service, Repository, Entity, DTO, Mapper и Specification;
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
- зачем параметр `status` имеет тип `ApplicationStatus`;
- что означает `null` для любого необязательного фильтра;
- почему путь к компании идёт через `vacancy.company.id`;
- как работают включающие границы `>=` и `<=`;
- почему противоречивый диапазон дат проверяется в Service;
- почему неизвестный enum не доходит до Service;
- назначение `MethodArgumentTypeMismatchException`;
- почему Controller не выбирает Repository-метод;
- почему Service больше не содержит ветки для всех комбинаций фильтров;
- почему новый фильтр можно добавить отдельной Specification.

### JPA и Spring Data

- основные JPA-аннотации;
- `Many-to-One`, `FetchType.LAZY`;
- `EnumType.STRING`;
- `findById()`, `findAll()`, `save()`, `delete()`;
- `Optional` и `orElseThrow()`;
- базовое назначение `Page`;
- базовое назначение `Pageable`;
- `PageRequest.of(page, size, sort)`;
- назначение `Sort`;
- `Sort.Direction.fromString(...)`;
- `JpaSpecificationExecutor<Application>` добавляет методы работы со Specification;
- `Specification<Application>` описывает условие, а не загружает данные;
- `Specification.unrestricted()` означает отсутствие ограничений;
- `root.get(...)` создаёт путь к полю или связи;
- `criteriaBuilder.equal(...)` создаёт условие равенства;
- `greaterThanOrEqualTo(...)` и `lessThanOrEqualTo(...)` создают включающие границы диапазона;
- `and(...)` объединяет условия;
- Specification выполняется при вызове Repository.

### Тестирование

- Arrange, Act, Assert;
- Mockito: `when`, `verify`, `never`, `verifyNoInteractions`;
- различие `assertSame` и `assertEquals`;
- Service unit-тесты;
- mapper-тесты;
- `@WebMvcTest` и `MockMvc`;
- проверка HTTP status, Content-Type и JSON;
- тестирование непустой и пустой страницы;
- проверка `Pageable` с ожидаемым `Sort`;
- необходимость обновлять `when(...)` и `verify(...)` после изменения вызываемого Repository-метода;
- `ApplicationSpecificationsTest` проверяет построение условий;
- `ApplicationServiceTest` проверяет использование Specification, пагинацию, маппинг и ответ;
- Controller-тесты проверяют внешний HTTP-контракт.

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
- Criteria API глубже минимально используемых `Path`, `Predicate`, `equal` и `and`;
- назначение параметра `CriteriaQuery<?> query` в сложных Specification;
- выбор между derived query, JPQL, Specification и QueryDSL;
- интеграционное тестирование Specification на реальной базе;
- Flyway вместо Hibernate DDL;
- границы между DTO validation, method validation, Service и базой данных.

---

## Технический долг и ограничения

- Пагинация, сортировка и фильтрация реализованы только для `Application`.
- Для `Company` и `Vacancy` пагинация, сортировка и фильтрация пока не добавлены.
- Для `Application` реализованы фильтры `status`, `vacancyId`, `companyId`, `appliedAtFrom` и `appliedAtTo`.
- Фильтр по `workFormat` пока не реализован.
- Пока нет фильтрации по `nextContactAt`.
- Unit-тесты проверяют структуру Specification через моки Criteria API, но пока нет интеграционного теста реального
  SQL-фильтра.
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

Добавить отдельную Specification для фильтра по формату работы вакансии:

```text
Application
→ vacancy
→ workFormat
```

Ожидаемое условие:

```text
Application.vacancy.workFormat = workFormat
```

### Первый небольшой подшаг

Реализовать только:

```java
Specification<Application> hasWorkFormat(WorkFormat workFormat)
```

и unit-тест:

```text
shouldCreateWorkFormatSpecification
```

В методе ожидается цепочка путей:

```text
root.get("vacancy")
→ vacancyPath.get("workFormat")
→ criteriaBuilder.equal(workFormatPath, workFormat)
```

### Ограничения первого подшага

Пока не нужно:

- добавлять `workFormat` в `withFilters(...)`;
- менять сигнатуры Service;
- менять Controller;
- добавлять query-параметр в HTTP API;
- переписывать существующие тесты Service и Controller;
- делать интеграционный тест;
- коммитить незавершённую функциональность.

### Критерии готовности первого подшага

- создан метод `hasWorkFormat(WorkFormat workFormat)`;
- путь проходит через `vacancy.workFormat`;
- используется `criteriaBuilder.equal(...)`;
- unit-тест проверяет оба вызова `get(...)`;
- unit-тест проверяет `equal(...)`;
- unit-тест проверяет возвращённый `Predicate` через `assertSame`;
- полный набор тестов проходит;
- ожидаемое количество после одного нового теста: **132 теста**.

После этого отдельными шагами:

1. добавить `workFormat` в `withFilters(...)`;
2. покрыть комбинации unit-тестами;
3. передать `workFormat` через Service;
4. добавить query-параметр Controller;
5. обновить Service- и Controller-тесты;
6. сохранить пагинацию, сортировку и остальные фильтры;
7. выполнить ручную проверку API;
8. сделать отдельный code-коммит.

---

## Вопросы для повторения

1. Почему фильтр по компании использует путь `Application.vacancy.company.id`?
2. Чем `hasCompanyId(...)` отличается от `hasVacancyId(...)`?
3. Что означает `appliedAt >= appliedAtFrom`?
4. Что означает `appliedAt <= appliedAtTo`?
5. Почему обе границы диапазона включающие?
6. Что происходит, если передан только `appliedAtFrom`?
7. Что происходит, если передан только `appliedAtTo`?
8. Почему диапазон с `appliedAtFrom > appliedAtTo` проверяется в Service?
9. Почему при неверном диапазоне Repository и Mapper не должны вызываться?
10. Зачем Controller использует `@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)`?
11. Почему Service-тест может использовать `any()` для Specification?
12. Что именно проверяет `ApplicationSpecificationsTest`?
13. Почему старые перегрузки Service сохранены?
14. Где находится единственная основная реализация `getAllApplications(...)`?
15. Сколько необязательных фильтров сейчас поддерживает `GET /api/applications`?
16. Как условия нескольких фильтров объединяются?
17. Что произойдёт, если все фильтры равны `null`?
18. Какой путь нужен для будущего фильтра по `workFormat`?
19. Почему новый фильтр сначала полезно реализовать отдельно от Controller и Service?
20. Почему Specification предотвращает рост количества Repository-методов?

---

## Рекомендуемый documentation-коммит

После замены файла проверить, что изменён только:

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
git commit -m "Update project status after application filtering"
git push
git status
```

Рекомендуемое сообщение коммита:

```text
Update project status after application filtering
```
