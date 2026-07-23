# Internship Tracker API — текущее состояние

## Актуальность файла

Последнее обновление: **2026-07-23**.

Это актуальная стабильная точка проекта после завершения пагинации списка `Application`.

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
- собственный DTO для paged response;
- проверка параметров `page` и `size`;
- единый формат ошибок для method validation.

Текущий крупный этап:

```text
Фильтрация, сортировка и пагинация
```

Пагинация `Application` завершена.

Следующая часть этапа:

```text
Сортировка списка Application
```

После сортировки нужно перейти к фильтрации.

---

## Текущая стабильная точка

Последний рабочий code-коммит:

```text
4bbbd31 Add Application pagination
```

Последний documentation-коммит до обновления этого файла:

```text
8cef1e5 Update project status after Application business rules
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
```

Состояние Git после отправки последнего code-коммита:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

Всего в проекте **99 тестов**.

Последний полный запуск:

```text
Tests run: 99
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

Для `Application` завершены CRUD, основные бизнес-правила и пагинация:

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

Endpoint:

```text
POST   /api/applications
GET    /api/applications?page=0&size=10
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
→ ApplicationService.getAllApplications(page, size)
→ PageRequest.of(page, size)
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

## Текущий контракт Application API

### POST /api/applications

- принимает `ApplicationRequest`;
- возвращает `ApplicationResponse`;
- успешный статус: `201 Created`;
- при отсутствии Vacancy возвращается `404 Not Found`;
- проверяет даты и соответствие `status`/`appliedAt`.

### GET /api/applications

- принимает `page` и `size`;
- возвращает `PagedResponse<ApplicationResponse>`;
- успешный статус: `200 OK`;
- значения по умолчанию: `page=0`, `size=10`;
- `page >= 0`;
- `1 <= size <= 100`;
- пустая база возвращает пустой `content`;
- страница за пределами данных возвращает пустой `content`;
- сортировка и фильтрация пока не добавлены.

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
- При ошибочных сценариях проверяется отсутствие лишних взаимодействий.
- Универсальные CRUD-классы пока не добавляются.

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
- почему пустая страница не является `404`.

### JPA и Spring Data

- основные JPA-аннотации;
- `Many-to-One`, `FetchType.LAZY`;
- `EnumType.STRING`;
- `findById()`, `findAll()`, `save()`, `delete()`;
- `Optional` и `orElseThrow()`;
- базовое назначение `Page`;
- базовое назначение `Pageable`;
- `PageRequest.of(page, size)`;
- `getNumber()`, `getSize()`, `getTotalElements()`, `getTotalPages()`.

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
- отсутствие вызова Service при ошибочной HTTP-валидации.

---

## Что понимается частично и требует повторения

- различие unit-, controller-, repository- и интеграционных тестов;
- LAZY-связи вне активной JPA-сессии;
- транзакции и `@Transactional`;
- различие `MethodArgumentNotValidException` и `HandlerMethodValidationException`;
- внутреннее устройство `Page`, `Pageable` и `PageRequest`;
- `Sort` и `Sort.Direction`;
- объединение сортировки с `PageRequest`;
- безопасный список разрешённых полей сортировки;
- обработка неверного поля и направления;
- фильтрация через derived query, JPQL или `Specification`;
- Flyway вместо Hibernate DDL;
- границы между DTO validation, method validation, Service и базой данных.

---

## Технический долг и ограничения

- Пагинация реализована только для `Application`.
- Для `Company` и `Vacancy` пагинация пока не добавлена.
- Сортировка не реализована.
- Фильтрация не реализована.
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

Начать сортировку списка `Application`.

Предварительный HTTP-контракт:

```text
GET /api/applications?page=0&size=10&sortBy=createdAt&direction=desc
```

Рекомендуемая последовательность:

1. Разобраться с `Sort`.
2. Разобраться с `Sort.Direction`.
3. Понять, как сортировка передаётся в `PageRequest`.
4. Выбрать разрешённые поля сортировки.
5. Определить значения `sortBy` и `direction` по умолчанию.
6. Сначала изменить Service и unit-тесты.
7. Затем изменить Controller и `MockMvc`-тесты.
8. Проверить `ASC` и `DESC`.
9. Проверить неизвестное направление.
10. Проверить неизвестное поле.
11. Сделать отдельный code-коммит.
12. После сортировки перейти к фильтрации.

Пока не добавлять:

- фильтрацию;
- `Specification`;
- сложные динамические запросы;
- Swagger;
- Docker;
- Spring Security;
- frontend;
- универсальную сортировку для всех сущностей.

---

## Критерии готовности сортировки

Сортировка завершена, когда:

- клиент может указать разрешённое поле;
- клиент может выбрать `ASC` или `DESC`;
- есть значения по умолчанию;
- неверное поле не приводит к внутренней ошибке базы;
- неверное направление возвращает управляемый `400 Bad Request`;
- сортировка объединена с пагинацией;
- Service покрыт unit-тестами;
- Controller покрыт `MockMvc`-тестами;
- все тесты проходят;
- code-коммит отправлен на GitHub;
- разработчик может объяснить `Sort` и его связь с `PageRequest`.

---

## Вопросы для повторения

1. Чем `Page<Application>` отличается от `List<Application>`?
2. Что описывает `Pageable`?
3. Зачем нужен `PageRequest.of(page, size)`?
4. Почему первая страница имеет номер `0`?
5. Почему страница за пределами данных возвращает пустой `content`?
6. Почему API возвращает `PagedResponse<ApplicationResponse>`?
7. Где вызывается `ApplicationMapper` при пагинации?
8. Чем `MethodArgumentNotValidException` отличается от `HandlerMethodValidationException`?
9. Почему `size` ограничен значением `100`?
10. Что означают `totalElements` и `totalPages`?
11. Почему Controller-тест не проверяет Repository?
12. Почему при неверных `page` или `size` Service не вызывается?
13. Что должна описывать сортировка?
14. Почему нельзя без проверки передавать любое поле в `Sort.by(...)`?

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
git commit -m "Update project status after Application pagination"
git push
git status
```

Рекомендуемое сообщение коммита:

```text
Update project status after Application pagination
```
