# Internship Tracker API — текущее состояние

## Актуальность файла

Последнее обновление: **2026-08-12**.

Это актуальная стабильная точка проекта после завершения основного CRUD для `Company`, `Vacancy` и `Application`,
реализации бизнес-правил `Application`, пагинации, сортировки, динамической фильтрации, параметр-объекта
`ApplicationFilter`, простой статистики откликов по статусам и перехода с автоматического Hibernate DDL на Flyway migrations.

В проекте должен находиться только один файл с точным названием:

```text
PROJECT_STATUS.md
```

Название файла изменять нельзя.

---

## Текущий этап

Завершён этап:

```text
Flyway migrations
```

Схема PostgreSQL теперь создаётся и изменяется через версионированные Flyway migrations.
Hibernate больше не обновляет схему автоматически и работает с `spring.jpa.hibernate.ddl-auto=validate`.

Следующий крупный этап:

```text
Углублённое тестирование
```

Следующий небольшой шаг:

```text
Провести аудит текущих тестовых слоёв и определить первый интеграционный тест,
который проверяет реальную совместную работу Spring Data JPA, Specification и PostgreSQL.
```

На этом шаге не добавлять Swagger, Docker, Security или новые бизнес-функции.

---

## Текущая стабильная точка

Последний рабочий code-коммит:

```text
abc0fe5 Add initial Flyway migration
```

Последний documentation-коммит до обновления этого файла:

```text
031c7db Document application filtering and statistics progress
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
933ce1d Add Application work format filtering
58ba3f4 Introduce Application filter object
7f902aa Add Application next contact date filtering
a82d9f4 Add application status statistics endpoint
abc0fe5 Add initial Flyway migration
```

Состояние Git после отправки последнего code-коммита:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

Всего в проекте **145 тестов**.

Последний полный запуск после чистого создания схемы через Flyway:

```text
Tests run: 145
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

Пароль PostgreSQL передаётся через переменную окружения `DB_PASSWORD`.
Если открыт новый PowerShell-сеанс, переменную нужно задать заново локально перед запуском приложения или тестов.
Реальный пароль не хранится в Git и не должен попадать в документацию.

При первом запуске тестов после перехода на Flyway была получена ошибка PostgreSQL `SQL State 28P01`.
Причиной оказалась отсутствующая `DB_PASSWORD` в текущем PowerShell-сеансе, а не ошибка migration.
После восстановления переменной все 145 тестов прошли успешно.

Изменения документации должны коммититься отдельно от Java-кода.

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
- [x] Для обычного diff используется `git --no-pager diff`.
- [x] Для staged diff используется `git --no-pager diff --cached`.
- [x] Code-коммиты небольшие и осмысленные.
- [x] Documentation-коммиты не смешиваются с Java-кодом.
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
- [x] Repository-тесты выполняются на PostgreSQL.

### Flyway и управление схемой

- [x] Подключён `spring-boot-starter-flyway`.
- [x] Для PostgreSQL подключён `org.flywaydb:flyway-database-postgresql`.
- [x] Создан каталог `src/main/resources/db/migration`.
- [x] Создана первая versioned migration `V1__create_initial_schema.sql`.
- [x] `V1` создаёт таблицы `companies`, `vacancies` и `applications`.
- [x] Порядок создания учитывает foreign key: `companies → vacancies → applications`.
- [x] В migration зафиксированы `PRIMARY KEY`, `FOREIGN KEY`, `NOT NULL`, длины `VARCHAR` и timestamp-типы.
- [x] Для `WorkFormat` и `ApplicationStatus` сохранены database `CHECK` constraints.
- [x] Hibernate DDL-режим изменён с `update` на `validate`.
- [x] Hibernate больше не должен автоматически изменять схему.
- [x] Старая dev-схема была удалена после проверки, что в ней нет важных данных.
- [x] Пустая PostgreSQL-схема успешно воспроизведена через Flyway.
- [x] Flyway создал служебную таблицу `flyway_schema_history`.
- [x] В `flyway_schema_history` зарегистрирована успешная migration версии `1`.
- [x] После чистого применения `V1` все 145 тестов проходят.

Текущая цепочка управления схемой:

```text
V1 / будущие V2, V3...
→ Flyway
→ PostgreSQL schema
→ Hibernate validate
→ Entity
```

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

Для `Application` завершены CRUD, бизнес-правила, пагинация, сортировка, динамическая фильтрация и простая статистика.

#### Модель и CRUD

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

#### Бизнес-правила

- [x] `nextContactAt` не может быть раньше `appliedAt`.
- [x] Для статусов, отличных от `PLANNED`, поле `appliedAt` обязательно.
- [x] Проверяются допустимые переходы статусов.
- [x] Повторная установка текущего статуса идемпотентна.
- [x] При повторной установке текущего статуса не вызывается лишний `save()`.
- [x] Бизнес-ошибки возвращаются в едином формате `400 Bad Request`.

#### Пагинация и сортировка

- [x] Пагинация через `Page`, `Pageable` и `PageRequest`.
- [x] Собственный generic record `PagedResponse<T>`.
- [x] Entity не возвращаются в paged response.
- [x] Элементы страницы преобразуются в `ApplicationResponse`.
- [x] Значения пагинации по умолчанию.
- [x] Проверка границ `page` и `size`.
- [x] Пустая страница за пределами данных возвращается с `200 OK`.
- [x] Сортировка объединена с пагинацией.
- [x] Поддерживаются направления `ASC` и `DESC`.
- [x] Используется allow-list разрешённых полей сортировки.
- [x] Неверное поле сортировки возвращает управляемый `400 Bad Request`.
- [x] Неверное направление сортировки возвращает управляемый `400 Bad Request`.

#### Динамическая фильтрация

Поддерживаются необязательные фильтры:

- [x] `status`.
- [x] `vacancyId`.
- [x] `companyId`.
- [x] `appliedAtFrom`.
- [x] `appliedAtTo`.
- [x] `workFormat`.
- [x] `nextContactAtFrom`.
- [x] `nextContactAtTo`.

Реализация:

- [x] Фильтры собираются через `Specification<Application>`.
- [x] Repository расширяет `JpaSpecificationExecutor<Application>`.
- [x] Условия добавляются независимо и объединяются через `and(...)`.
- [x] Для отсутствующих фильтров ограничения не добавляются.
- [x] Для начального состояния используется `Specification.unrestricted()`.
- [x] Service выполняет один `findAll(specification, pageable)` для любой комбинации фильтров.
- [x] Старые derived query для комбинаций фильтров удалены.
- [x] Фильтр по компании использует путь `Application.vacancy.company.id`.
- [x] Фильтр по формату работы использует путь `Application.vacancy.workFormat`.
- [x] Нижние границы дат используют `greaterThanOrEqualTo(...)`.
- [x] Верхние границы дат используют `lessThanOrEqualTo(...)`.
- [x] Диапазоны `appliedAt` и `nextContactAt` включающие.
- [x] Противоречивые диапазоны отклоняются до обращения к Repository.
- [x] Для дат используется ISO date-time через `@DateTimeFormat`.
- [x] Неизвестные enum-значения возвращают управляемый `400 Bad Request`.
- [x] `MethodArgumentTypeMismatchException` обрабатывается централизованно.
- [x] При ошибке HTTP-преобразования Service и Mapper не вызываются.
- [x] Параметры фильтрации объединены в immutable record `ApplicationFilter`.
- [x] Controller создаёт `ApplicationFilter` и передаёт его в Service.
- [x] Service и `ApplicationSpecifications` используют один параметр-объект вместо длинной основной сигнатуры.
- [x] Старые перегрузки Service временно сохранены для обратной совместимости существующих вызовов и тестов.
- [x] Specification-, Service- и Controller-тесты покрывают отдельные фильтры и комбинации.

#### Статистика

- [x] Создан `ApplicationStatisticsResponse`.
- [x] Реализован `GET /api/applications/statistics`.
- [x] Ответ содержит общее количество откликов.
- [x] Ответ содержит количество для каждого `ApplicationStatus`.
- [x] Для распределения по статусам используется `EnumMap<ApplicationStatus, Long>`.
- [x] Статусы с количеством `0` также присутствуют в ответе.
- [x] Repository использует derived query `countByStatus(...)`.
- [x] Service-тест проверяет все статусы и обращения к Repository.
- [x] Controller-тест проверяет HTTP status, Content-Type и полный JSON.
- [x] Mapper при построении статистики не используется.

Endpoint:

```text
POST   /api/applications
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&status=INTERVIEW
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&vacancyId=20
GET    /api/applications?page=0&size=10&sortBy=createdAt&direction=DESC&companyId=5
GET    /api/applications?page=0&size=10&sortBy=appliedAt&direction=ASC&appliedAtFrom=2026-07-01T00:00:00&appliedAtTo=2026-07-31T23:59:00
GET    /api/applications?page=0&size=10&workFormat=REMOTE
GET    /api/applications?page=0&size=10&nextContactAtFrom=2026-08-01T00:00:00&nextContactAtTo=2026-08-31T23:59:00
GET    /api/applications/statistics
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
page                — номер страницы, начиная с 0
size                — максимальное количество элементов на странице
sortBy              — разрешённое поле сортировки
direction           — ASC или DESC
status              — необязательный ApplicationStatus
vacancyId           — необязательный идентификатор Vacancy
companyId           — необязательный идентификатор Company
appliedAtFrom       — необязательная нижняя граница appliedAt в ISO date-time
appliedAtTo         — необязательная верхняя граница appliedAt в ISO date-time
workFormat          — необязательный WorkFormat вакансии
nextContactAtFrom   — необязательная нижняя граница nextContactAt в ISO date-time
nextContactAtTo     — необязательная верхняя граница nextContactAt в ISO date-time
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

### Текущий поток выполнения

```text
ApplicationController
→ создаёт ApplicationFilter
→ ApplicationService.getAllApplications(page, size, sortBy, direction, filter)
→ проверяет sortBy и direction
→ создаёт Sort и PageRequest
→ проверяет диапазоны appliedAt и nextContactAt
→ ApplicationSpecifications.withFilters(filter)
→ applicationRepository.findAll(specification, pageable)
→ Page<Application>
→ ApplicationMapper.toResponse(...)
→ PagedResponse<ApplicationResponse>
```

Правила:

- Spring `Page<Application>` не возвращается клиенту напрямую.
- `Application` Entity не возвращается клиенту напрямую.
- Mapper преобразует один `Application`.
- Service организует преобразование элементов страницы.
- Controller получает готовый `PagedResponse<ApplicationResponse>`.
- Controller не строит Specification.
- Service не содержит веток для всех комбинаций фильтров.
- Repository не содержит отдельный метод для каждой комбинации фильтров.
- Один и тот же `Pageable` передаётся в Repository вместе со Specification.
- Страница за пределами данных возвращает пустой `content`, а не `404`.

### ApplicationFilter

Текущий параметр-объект:

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

Назначение:

- уменьшить длинный список параметров основного Service-метода;
- упростить добавление новых фильтров;
- передавать связанные параметры как один неизменяемый объект;
- использовать автоматически созданные `equals`, `hashCode` и `toString` в тестах и отладке.

Старый шестипараметровый конструктор record временно сохранён для совместимости существующих вызовов.

### Валидация диапазонов

Сообщения ошибок:

```text
Applied at from must not be after applied at to
Next contact at from must not be after next contact at to
```

Равные границы разрешены. Ошибка возникает только при `from.isAfter(to)`.

### Динамическая фильтрация через Specification

Поддерживаемые условия:

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

`ApplicationSpecifications.withFilters(filter)` начинает с `Specification.unrestricted()` и добавляет через `and(...)`
только условия для ненулевых полей `ApplicationFilter`.

`Specification<Application>` описывает условие, но не выполняет запрос.
Запрос выполняется при вызове Repository.

### Статистика Application

HTTP-запрос:

```text
GET /api/applications/statistics
```

Формат ответа:

```json
{
  "total": 12,
  "byStatus": {
    "PLANNED": 2,
    "APPLIED": 4,
    "TEST_TASK": 1,
    "INTERVIEW": 2,
    "OFFER": 1,
    "REJECTED": 2,
    "WITHDRAWN": 0
  }
}
```

Текущая реализация выполняет:

```text
1 запрос applicationRepository.count()
7 запросов applicationRepository.countByStatus(status)
```

Итого: **8 запросов к базе данных**.

Это допустимая простая первая версия. Позднее статистику следует оптимизировать одним агрегирующим запросом
с `GROUP BY status`. После такого запроса Service всё равно должен добавить в `EnumMap` отсутствующие статусы со значением `0`.

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
- Controller принимает query-параметры и создаёт `ApplicationFilter`, но не строит Specification.
- Service получает Specification из `ApplicationSpecifications`.
- Repository выполняет Specification вместе с `Pageable`.
- Отсутствующие фильтры представлены значениями `null` внутри `ApplicationFilter`.
- Derived query используются только для простых понятных операций, например `countByStatus(...)`.
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
- зачем нужен allow-list полей сортировки;
- различие inheritance, overloading и delegation;
- почему перегрузки делегируют только в одном направлении;
- что означает `null` для необязательного фильтра;
- почему путь к компании идёт через `vacancy.company.id`;
- почему путь к формату работы идёт через `vacancy.workFormat`;
- как работают включающие границы `>=` и `<=`;
- почему противоречивые диапазоны проверяются в Service;
- почему неизвестный enum не доходит до Service;
- назначение `MethodArgumentTypeMismatchException`;
- зачем использовать parameter object `ApplicationFilter`;
- что record создаёт constructor, accessors, `equals`, `hashCode` и `toString`;
- почему Controller обращается к Service, а Service — к Repository;
- как Spring Data разбирает имя `countByStatus`;
- почему для enum-ключей подходит `EnumMap`;
- почему текущая статистика выполняет восемь запросов;
- как `GROUP BY status` уменьшит количество запросов;
- что database migration переводит БД из одного известного состояния в следующее;
- чем Flyway migration отличается от `spring.jpa.hibernate.ddl-auto=update`;
- что `ddl-auto=validate` проверяет схему, но не создаёт и не изменяет таблицы;
- зачем migrations хранятся в Git;
- зачем Flyway нужна таблица `flyway_schema_history`;
- почему порядок `companies → vacancies → applications` важен из-за foreign key;
- почему Flyway и Hibernate `validate` выполняют разные роли.

### JPA и Spring Data

- основные JPA-аннотации;
- `Many-to-One`, `FetchType.LAZY`;
- `EnumType.STRING`;
- `findById()`, `findAll()`, `save()`, `delete()`;
- `Optional` и `orElseThrow()`;
- базовое назначение `Page`, `Pageable`, `PageRequest` и `Sort`;
- `JpaSpecificationExecutor<Application>`;
- `Specification<Application>` описывает условие, а не загружает данные;
- `Specification.unrestricted()` означает отсутствие ограничений;
- `root.get(...)` создаёт путь к полю или связи;
- `criteriaBuilder.equal(...)` создаёт условие равенства;
- `greaterThanOrEqualTo(...)` и `lessThanOrEqualTo(...)` создают включающие границы;
- `and(...)` объединяет условия;
- derived query method разбирается Spring Data по имени метода.

### Тестирование

- Arrange, Act, Assert;
- Mockito: `when`, `verify`, `never`, `verifyNoInteractions`;
- правильная форма `verify(mock).method()`;
- различие `assertSame` и `assertEquals`;
- Service unit-тесты;
- mapper-тесты;
- `@WebMvcTest` и `MockMvc`;
- проверка HTTP status, Content-Type и JSON;
- тестирование непустой и пустой страницы;
- проверка `Pageable` с ожидаемым `Sort`;
- `ApplicationSpecificationsTest` проверяет построение условий;
- `ApplicationServiceTest` проверяет бизнес-логику и взаимодействия;
- `ApplicationControllerTest` проверяет внешний HTTP-контракт;
- Mapper не должен вызываться при построении статистики.

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
- Criteria API глубже используемых `Path`, `Predicate`, `equal`, сравнений и `and`;
- назначение `CriteriaQuery<?> query` в сложных Specification;
- выбор между derived query, JPQL, Specification и QueryDSL;
- projection для агрегирующих запросов;
- JPQL-запрос с `GROUP BY`;
- интеграционное тестирование Specification на изолированной базе;
- правила дальнейшего развития Flyway migrations: новые `V2`, `V3` и неизменяемость уже применённых versioned migrations;
- checksum, `validate`, `repair`, baseline и другие более продвинутые возможности Flyway;
- границы между DTO validation, method validation, Service и базой данных.

---

## Технический долг и ограничения

- Пагинация, сортировка и фильтрация реализованы только для `Application`.
- Для `Company` и `Vacancy` пагинация, сортировка и фильтрация пока не добавлены.
- Сохранено несколько старых перегрузок `getAllApplications(...)`; позднее их можно сократить после стабилизации API.
- Статистика выполняет восемь запросов к базе данных.
- Статистику позднее можно оптимизировать одним агрегирующим запросом с `GROUP BY status`.
- Unit-тесты проверяют структуру Specification через моки Criteria API, но нет интеграционного теста реальной фильтрации SQL.
- Repository-тесты зависят от локально запущенного PostgreSQL; Testcontainers пока не используется.
- Пока нет дополнительной сортировки по `id` для стабильного порядка при одинаковых значениях основного поля.
- `PagedResponse<T>` пока не содержит `first`, `last` или `hasNext`.
- Method validation возвращает первое найденное сообщение.
- `fieldErrors` для query-параметров пока пустой.
- Начальная схема переведена на Flyway; последующие изменения схемы должны оформляться новыми migrations.
- `spring.jpa.open-in-view` пока не отключён явно.
- В тестах есть предупреждение Mockito о динамическом подключении Java agent; сборку это сейчас не ломает.
- В `Company.java` остался wildcard import `jakarta.persistence.*`; исправить отдельным cleanup-коммитом.
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

Начать этап углублённого тестирования.

### Первый небольшой подшаг

Провести аудит существующих тестов и выбрать первый интеграционный сценарий, который сейчас не покрыт реальной работой с БД.

В первую очередь рассмотреть динамическую фильтрацию `Application` через `Specification`, потому что текущий
`ApplicationSpecificationsTest` проверяет Criteria API через mocks, но не проверяет фактический SQL на PostgreSQL.

Перед написанием нового теста нужно определить:

- чем unit-тест Specification отличается от интеграционного теста Repository/Specification;
- какой Spring test slice или контекст нужен;
- какие тестовые данные понадобятся для `Company → Vacancy → Application`;
- какой один фильтр или небольшую комбинацию фильтров проверить первой;
- как не превратить первый интеграционный тест в большой сценарий на десятки условий.

### Ограничения

Пока не нужно:

- добавлять Swagger/OpenAPI;
- добавлять Docker или Docker Compose;
- добавлять Spring Security, User или JWT;
- оптимизировать статистику через `GROUP BY`;
- исправлять `Company.java` wildcard import внутри тестового feature-коммита;
- добавлять Testcontainers до отдельного решения о стратегии интеграционных тестов;
- писать один огромный integration test на весь API.

### Критерии готовности первого подшага

- понятно различие unit-, controller-, repository- и integration tests;
- выбран один небольшой интеграционный сценарий;
- тест проверяет реальную работу JPA/Specification с PostgreSQL, а не только mocks;
- существующие 145 тестов продолжают проходить;
- новый тест понятен и может быть объяснён своими словами;
- изменения прошли review и зафиксированы отдельным code-коммитом.

---

## Вопросы для повторения

1. Что такое database migration и какую проблему она решает?
2. Чем Flyway migration отличается от `spring.jpa.hibernate.ddl-auto=update`?
3. Что делает `spring.jpa.hibernate.ddl-auto=validate` и чего он не делает?
4. Почему migrations должны храниться в Git?
5. Для чего Flyway создаёт `flyway_schema_history`?
6. Почему `companies` создаётся раньше `vacancies`, а `vacancies` раньше `applications`?
7. Почему `work_format` и `status` хранятся как `VARCHAR`, но дополнительно защищены `CHECK` constraint?
8. Почему бизнес-правила переходов `ApplicationStatus` не были перенесены в `V1`?
9. Что нужно будет сделать с БД, если в будущем Entity получит новую сохраняемую колонку?
10. Почему после появления `V2` нельзя просто бездумно переписывать уже применённую `V1`?
11. Чем текущий mock-based `ApplicationSpecificationsTest` отличается от интеграционной проверки фильтрации на PostgreSQL?
12. Почему отсутствие `DB_PASSWORD` привело к падению Flyway до создания `EntityManagerFactory`?

---

## Рекомендуемый documentation-коммит

Перед коммитом проверить:

```powershell
git status --short
git --no-pager diff --check
git --no-pager diff -- PROJECT_STATUS.md DECISIONS.md
```

После проверки:

```powershell
git add PROJECT_STATUS.md DECISIONS.md
git --no-pager diff --cached --check
git --no-pager diff --cached
git commit -m "Document Flyway migration setup"
git push
git status
```

Ожидаемое состояние после push:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```
