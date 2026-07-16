# Internship Tracker API — текущее состояние

## Текущий этап

Этап 3 — начата реализация `Vacancy`: создана модель данных вакансии, enum `WorkFormat`, `VacancyRepository` и repository-тесты.

Базовый `Company CRUD` завершён на уровнях Repository, Service и Controller. Реализованы и вручную проверены пять endpoint:

- `GET /api/companies`;
- `POST /api/companies`;
- `GET /api/companies/{id}`;
- `PUT /api/companies/{id}`;
- `DELETE /api/companies/{id}`.

Текущий внешний контракт `CompanyController`:

- `GET /api/companies` возвращает `List<CompanyResponse>`;
- `GET /api/companies/{id}` возвращает `CompanyResponse` или JSON `ErrorResponse` с `404 Not Found`;
- `POST /api/companies` принимает `CompanyRequest` и возвращает `CompanyResponse`;
- `PUT /api/companies/{id}` принимает `CompanyRequest` и возвращает `CompanyResponse` или JSON `ErrorResponse` с `404 Not Found`;
- `DELETE /api/companies/{id}` возвращает `204 No Content`, если компания существует, или JSON `ErrorResponse` с `404 Not Found`, если компания не найдена.

Текущие validation-правила для `CompanyRequest`:

- `name` обязателен: `@NotBlank`;
- `name` максимум 100 символов;
- `website` максимум 255 символов, поле необязательное;
- `description` максимум 1000 символов, поле необязательное.

Для validation-ошибок и not found ошибок используется единый JSON-ответ через `GlobalExceptionHandler` и `ErrorResponse`.

Пример validation-ошибки:

```json
{
  "status": 400,
  "error": "Validation failed",
  "message": "Invalid request data",
  "path": "/api/companies",
  "fieldErrors": {
    "name": "Company name must be at most 100 characters"
  }
}
```

Пример not found ошибки:

```json
{
  "status": 404,
  "error": "Not found",
  "message": "Company not found with id: 999",
  "path": "/api/companies/999",
  "fieldErrors": {}
}
```

Всего в проекте 27 тестов. Последний полный запуск завершился с `BUILD SUCCESS`.

Следующий шаг — обновить `PROJECT_STATUS.md` отдельным status-коммитом. После этого это хорошая точка для перехода в чат 03: там продолжить с `Vacancy DTO`, mapper, service и controller.

## Уже выполнено

### Среда и Git

- [x] Установлен и настроен JDK 21.
- [x] Настроен `JAVA_HOME`.
- [x] Проверен Maven Wrapper 3.9.16.
- [x] Установлен и настроен Git.
- [x] Проверен PostgreSQL 18.4.
- [x] Создан публичный GitHub-репозиторий `internship-tracker-api`.
- [x] Инициализирован локальный Git-репозиторий.
- [x] Локальная ветка `main` связана с `origin/main`.
- [x] Проверен `.gitignore`.
- [x] В Git не добавлены секреты, `.idea/` и `target/`.
- [x] Последние рабочие code-коммиты отправлены на GitHub.
- [x] На момент остановки рабочее дерево было чистым, ветка `main` синхронизирована с `origin/main`.

### Spring Boot

- [x] Создан Spring Boot 4.1.0 проект на Java 21 и Maven.
- [x] Добавлена зависимость Spring Web.
- [x] Создан `HelloController`.
- [x] Реализован `GET /api/hello`.
- [x] Endpoint возвращает `Hello, Internship Tracker!`.
- [x] Приложение успешно запускается на порту 8080.

### База данных и Entity

- [x] Добавлены Spring Data JPA и PostgreSQL JDBC Driver.
- [x] Создана база данных `internship_tracker`.
- [x] Создана роль PostgreSQL `internship_tracker_app`.
- [x] Настроено получение пароля через переменную окружения `DB_PASSWORD`.
- [x] Создана JPA-сущность `Company`.
- [x] Добавлены поля `id`, `name`, `website`, `description`, `createdAt`, `updatedAt`.
- [x] Настроены JPA-аннотации и ограничения столбцов.
- [x] Добавлено заполнение `createdAt` и `updatedAt` через `@PrePersist` и `@PreUpdate`.
- [x] Hibernate создаёт таблицу `companies`.
- [x] Создан enum `WorkFormat`: `OFFICE`, `REMOTE`, `HYBRID`, `NOT_SPECIFIED`.
- [x] Создана JPA-сущность `Vacancy`.
- [x] Добавлены поля `id`, `company`, `title`, `link`, `city`, `workFormat`, `description`, `createdAt`, `updatedAt`.
- [x] Настроена связь `Many-to-One` от `Vacancy` к `Company` через `company_id`.
- [x] Для `workFormat` используется `@Enumerated(EnumType.STRING)`.
- [x] Если `workFormat` равен `null`, он заменяется на `WorkFormat.NOT_SPECIFIED`.
- [x] Для `Vacancy` добавлено заполнение `createdAt` и `updatedAt` через `@PrePersist` и `@PreUpdate`.

### Repository

- [x] Создан `CompanyRepository`.
- [x] `CompanyRepository` наследуется от `JpaRepository<Company, Long>`.
- [x] Создан `CompanyRepositoryTest`.
- [x] Проверены сохранение и чтение `Company` через PostgreSQL.
- [x] В Repository-тестах используется `@DataJpaTest`.
- [x] PostgreSQL не заменяется встроенной базой благодаря `@AutoConfigureTestDatabase(replace = NONE)`.
- [x] Создан `VacancyRepository`.
- [x] `VacancyRepository` наследуется от `JpaRepository<Vacancy, Long>`.
- [x] Создан `VacancyRepositoryTest`.
- [x] Проверено внедрение `VacancyRepository`.
- [x] Проверено сохранение и чтение `Vacancy` вместе с обязательной связью на `Company`.

### Service

- [x] Создан `CompanyService`.
- [x] `CompanyRepository` внедряется через конструктор.
- [x] Реализованы `createCompany()`, `getAllCompanies()`, `getCompanyById()`, `updateCompany()`, `deleteCompany()`.
- [x] Создан `CompanyServiceTest`.
- [x] Repository заменяется mock-объектом через Mockito.
- [x] Проверены создание, список, поиск, обновление и удаление.
- [x] Проверены найденная и отсутствующая компания через `Optional`.
- [x] Проверено, что при обновлении отсутствующей компании `save()` не вызывается.

### Controller и HTTP

- [x] Создан `CompanyController`.
- [x] Реализован полный CRUD.
- [x] `POST /api/companies` возвращает `201 Created`.
- [x] `DELETE /api/companies/{id}` возвращает `204 No Content`.
- [x] Вручную проверены ответы `200 OK`, `201 Created`, `204 No Content` и `404 Not Found`.
- [x] Проверено, что при обновлении сохраняются `id` и `createdAt`, а `updatedAt` изменяется.
- [x] `CompanyMapper` внедрён в `CompanyController`.
- [x] `GET /api/companies` переведён на возврат `List<CompanyResponse>`.
- [x] `GET /api/companies/{id}` переведён на возврат `CompanyResponse`.
- [x] `POST /api/companies` переведён на `CompanyRequest` и `CompanyResponse`.
- [x] `PUT /api/companies/{id}` переведён на `CompanyRequest` и `CompanyResponse`.
- [x] `POST /api/companies` и `PUT /api/companies/{id}` используют `@Valid`.
- [x] `GET /api/companies/{id}` выбрасывает `ResourceNotFoundException`, если компания не найдена.
- [x] `PUT /api/companies/{id}` выбрасывает `ResourceNotFoundException`, если компания не найдена.
- [x] `DELETE /api/companies/{id}` выбрасывает `ResourceNotFoundException`, если компания не найдена.

### Controller-тесты

- [x] Создан `CompanyControllerTest`.
- [x] Controller тестируется через `@WebMvcTest(CompanyController.class)`.
- [x] `CompanyService` заменяется mock-объектом через `@MockitoBean`.
- [x] `CompanyMapper` подключён в controller-тест через `@Import(CompanyMapper.class)`.
- [x] Проверен список компаний.
- [x] Проверен успешный поиск по `id`.
- [x] Проверен `404 Not Found` для отсутствующей компании.
- [x] Проверено создание через `POST`.
- [x] Проверено удаление через `DELETE`.
- [x] Проверено успешное обновление через `PUT`.
- [x] Проверен `404 Not Found` при обновлении отсутствующей компании.
- [x] Проверен `400 Bad Request` при создании компании с пустым `name`.
- [x] Проверен `400 Bad Request` при обновлении компании с пустым `name`.
- [x] Проверен `400 Bad Request` при создании компании со слишком длинным `name`.
- [x] Проверен `400 Bad Request` при обновлении компании со слишком длинным `description`.
- [x] Проверено, что Service не вызывается при невалидных данных.
- [x] Проверен JSON-ответ `ErrorResponse` для validation-ошибки при `POST`.
- [x] Проверен JSON-ответ `ErrorResponse` для validation-ошибки при `PUT`.
- [x] Проверен JSON-ответ `ErrorResponse` для `GET /api/companies/{id}`, если компания не найдена.
- [x] Проверен JSON-ответ `ErrorResponse` для `PUT /api/companies/{id}`, если компания не найдена.
- [x] Проверен JSON-ответ `ErrorResponse` для `DELETE /api/companies/{id}`, если компания не найдена.
- [x] Проверяются HTTP-статусы, JSON-ответы и вызовы Service.
- [x] Все controller-тесты проходят после перехода Controller на DTO, Bean Validation и обработку ошибок.

### DTO, mapper, validation и errors

- [x] Создан пакет `dto.company`.
- [x] Создан `CompanyRequest` как `record`.
- [x] `CompanyRequest` содержит `name`, `website`, `description`.
- [x] Создан `CompanyResponse` как `record`.
- [x] `CompanyResponse` содержит `id`, основные поля, `createdAt`, `updatedAt`.
- [x] Создан ручной `CompanyMapper`.
- [x] Реализован `toEntity(CompanyRequest request)`.
- [x] Реализован `toResponse(Company company)`.
- [x] `CompanyMapper` зарегистрирован как Spring-компонент через `@Component`.
- [x] Создан `CompanyMapperTest`.
- [x] Проверено преобразование `CompanyRequest → Company`.
- [x] Проверено преобразование `Company → CompanyResponse`.
- [x] Mapper unit-тестируется без запуска Spring-контекста.
- [x] Добавлена зависимость `spring-boot-starter-validation`.
- [x] В `CompanyRequest` добавлены `@NotBlank` и `@Size` для `name`.
- [x] В `CompanyRequest` добавлен `@Size(max = 255)` для `website`.
- [x] В `CompanyRequest` добавлен `@Size(max = 1000)` для `description`.
- [x] В `CompanyController` добавлен `@Valid` для `POST` и `PUT`.
- [x] Создан пакет `dto.error`.
- [x] Создан `ErrorResponse` как `record`.
- [x] `ErrorResponse` содержит `status`, `error`, `message`, `path`, `fieldErrors`.
- [x] Создан пакет `exception`.
- [x] Создан `GlobalExceptionHandler`.
- [x] `GlobalExceptionHandler` помечен `@RestControllerAdvice`.
- [x] Обработан `MethodArgumentNotValidException`.
- [x] Создан `ResourceNotFoundException`.
- [x] Обработан `ResourceNotFoundException`.
- [x] Validation-ошибки возвращаются как JSON `ErrorResponse`.
- [x] Not found ошибки для `GET`, `PUT` и `DELETE` возвращаются как JSON `ErrorResponse`.
- [x] Все 27 тестов проекта завершились с `BUILD SUCCESS`.

## Что я уже понимаю

### Git

- `git status` показывает состояние рабочего дерева и staging area.
- `git add` помещает изменения в staging area.
- `git commit` сохраняет подготовленные изменения локально.
- `git push` отправляет коммиты на GitHub.
- `git diff` показывает незастейдженные изменения.
- `git diff --cached` показывает содержимое будущего коммита.
- `working tree clean` означает, что нет незакоммиченных изменений в файлах.
- `Your branch is up to date with 'origin/main'` означает, что локальная ветка синхронизирована с GitHub.
- `.gitignore` исключает служебные и генерируемые файлы.

### Архитектура и Spring

- Поток приложения: `HTTP request → Controller → Service → Repository → PostgreSQL`.
- Controller принимает HTTP-запрос и вызывает Service.
- Service содержит бизнес-логику и вызывает Repository.
- Controller не должен обращаться к Repository напрямую.
- Constructor injection передаёт зависимости через конструктор.
- `ResponseEntity` позволяет управлять HTTP-статусом и телом ответа.
- Spring создаёт управляемые объекты для классов с подходящими аннотациями.
- Bean — объект, который создаёт и хранит Spring.
- `@Component` позволяет зарегистрировать обычный класс как Spring bean.
- `@Import` в test context позволяет явно добавить нужный класс в `@WebMvcTest`.
- `@RestControllerAdvice` позволяет централизованно обрабатывать ошибки из controller-слоя.
- `@ExceptionHandler` связывает конкретный тип исключения с методом обработки.
- `RuntimeException` не требует обязательного `throws` и `try/catch`.
- `orElseThrow(...)` либо возвращает значение из `Optional`, либо выбрасывает exception.

### JPA и PostgreSQL

- `@Entity` связывает класс с таблицей базы данных.
- `@ManyToOne` описывает связь many-to-one: много `Vacancy` могут ссылаться на одну `Company`.
- `FetchType.LAZY` означает, что связанная `Company` загружается не сразу, а при обращении к ней.
- `@JoinColumn(name = "company_id")` задаёт внешний ключ в таблице `vacancies`.
- `@Enumerated(EnumType.STRING)` сохраняет enum в базе как строку, а не как число.
- Repository-тесты с `@DataJpaTest` проверяют реальные JPA-маппинги и работу с БД.
- `@Id` обозначает первичный ключ.
- `@GeneratedValue` настраивает генерацию идентификатора.
- `@Column` задаёт свойства столбца.
- `@PrePersist` вызывается перед первым сохранением.
- `@PreUpdate` вызывается перед обновлением.
- `JpaRepository<Company, Long>` работает с `Company` и `Long`.
- `findById()` возвращает `Optional`, потому что запись может отсутствовать.
- Пароль базы данных передаётся через `DB_PASSWORD`, а не хранится в Git.

### DTO, mapper, validation и errors

- DTO используется для передачи данных между клиентом и API.
- Entity описывает модель базы данных и не должна без необходимости становиться внешним контрактом API.
- `CompanyRequest` содержит только поля, которые клиент может отправить.
- `CompanyResponse` содержит в том числе серверные поля.
- `record` автоматически создаёт конструктор, методы доступа, `equals()`, `hashCode()` и `toString()`.
- У record методы доступа называются `name()`, `website()` и так далее.
- Mapper преобразует DTO в Entity и Entity в DTO.
- Ручной mapper явно показывает, какие поля копируются.
- `CompanyMapper.toEntity()` создаёт Entity из входного DTO.
- `CompanyMapper.toResponse()` создаёт выходной DTO из Entity.
- В Controller Entity остаётся внутри приложения, а клиент получает DTO.
- Service пока продолжает работать с Entity, а DTO используются на границе API.
- Bean Validation проверяет входные данные до вызова Service.
- `@Valid` включает проверку validation-аннотаций для `@RequestBody`.
- `@NotBlank` запрещает `null`, пустую строку и строку только из пробелов.
- `@Size(max = N)` ограничивает длину строки, но не запрещает `null`.
- `ErrorResponse` задаёт единый формат JSON-ответа для ошибок.
- `MethodArgumentNotValidException` возникает при ошибке Bean Validation для `@Valid @RequestBody`.
- `FieldError` хранит информацию об ошибке конкретного поля.
- `LinkedHashMap` сохраняет порядок добавления field errors.
- `putIfAbsent()` добавляет ошибку поля только если для этого поля ещё нет сообщения.
- `HttpServletRequest#getRequestURI()` позволяет получить путь запроса для ответа об ошибке.
- `ResourceNotFoundException` используется, когда ресурс не найден.
- Для `404` тело ответа есть, но `fieldErrors` пустой, потому что нет ошибок конкретных полей.
- `GlobalExceptionHandler` нужен, чтобы не дублировать одинаковую логику ошибок в каждом Controller.

### Тестирование

- Unit-тест проверяет отдельный класс без всей инфраструктуры.
- Mock заменяет настоящую зависимость.
- `when(...).thenReturn(...)` задаёт поведение mock.
- `verify(...)` проверяет вызов метода mock-объекта.
- `never()` проверяет, что метод не был вызван.
- `@DataJpaTest` запускает JPA-часть Spring-контекста.
- `@WebMvcTest` запускает MVC-часть приложения.
- `MockMvc` выполняет HTTP-подобные запросы без настоящего сервера.
- `@MockitoBean` помещает mock в Spring test context.
- `any(...)` и `eq(...)` — Mockito matchers.
- Если в одном вызове используется matcher, остальные аргументы тоже задаются через matchers.
- Обычный mapper без зависимостей можно тестировать без Spring.
- Для теста `toResponse()` использован mock `Company`, потому что у Entity нет setters для серверных полей.
- Невалидный request должен возвращать `400 Bad Request` и не доходить до Service.
- `content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)` проверяет, что ответ является JSON.
- `jsonPath("$.fieldErrors.name")` проверяет значение поля внутри JSON-ответа.
- `jsonPath("$.fieldErrors").isEmpty()` проверяет пустой объект ошибок полей.

## Что я пока понимаю частично

- Как Spring сканирует пакеты и создаёт компоненты.
- Полный механизм dependency injection внутри Spring.
- Чем `@Import(CompanyMapper.class)` в тесте отличается от обычного component scanning.
- Полную структуру HTTP-запроса и HTTP-ответа.
- Принципы REST API и выбор HTTP-статусов для ошибок.
- Устройство `pom.xml` и управление зависимостями Maven.
- Работу с ветками Git и `merge`.
- Использование `ArgumentCaptor`.
- Нужно ли `DELETE /api/companies/{id}` возвращать `404`, если компания отсутствует.
- Когда лучше переносить `ResourceNotFoundException` из Controller в Service.

## Известные ошибки

Критических ошибок нет.

Последний полный запуск тестов:

```text
Tests run: 27, Failures: 0, Errors: 0
BUILD SUCCESS
```

## Технический долг

- Для `GET /api/hello` пока нет отдельного теста через `MockMvc`.
- Единый формат ошибок реализован для validation-ошибок и not found ошибок в `GET`/`PUT`/`DELETE` Company.
- `DELETE /api/companies/{id}` теперь возвращает `404 Not Found` с JSON `ErrorResponse`, если компания отсутствует.
- Обработка отсутствующей компании выполняется через `ResourceNotFoundException`: для `GET` и `PUT` exception выбрасывается в Controller, для `DELETE` — в Service.
- Схема базы данных пока управляется Hibernate; Flyway будет добавлен позже.
- Предупреждение `spring.jpa.open-in-view` пока не устранено.
- Предупреждение об отсутствии JTA не является блокирующей ошибкой.

## Последний рабочий code-коммит

- Hash: `c5929d3`
- Message: `Add Vacancy entity`
- Добавлен enum `WorkFormat`.
- Добавлена JPA-сущность `Vacancy` со связью `Many-to-One` к `Company`.
- Добавлен `VacancyRepository`.
- Добавлен `VacancyRepositoryTest` для проверки сохранения и чтения вакансии через PostgreSQL.
- Все 27 тестов прошли успешно.
- Коммит отправлен на GitHub.
- Ветка `main` синхронизирована с `origin/main`.
- Рабочее дерево чистое перед обновлением `PROJECT_STATUS.md`.

## Последние коммиты

- `c2ee641 Update project status after validation error handling`
- `a6a7972 Handle not found errors`
- `773760d Handle update not found errors`
- `72fa767 Update project status after not found handling`
- `8867709 Handle delete not found errors`
- `75348b4 Update project status after delete error handling`
- `c5929d3 Add Vacancy entity`
- `c2cc04f Add error response DTO`
- `1cfe9dc Handle validation errors`
- `11ab7d1 Verify update validation error response`

## Следующее задание

1. Обновить локальный `PROJECT_STATUS.md` этим файлом и сделать отдельный status-коммит.
2. Перейти в чат 03.
3. В чате 03 начать следующий блок: `Vacancy DTO`, `VacancyMapper`, `VacancyService`, `VacancyController`.
4. После DTO и service добавить validation и обработку `404` для `Vacancy`.

## Критерии завершения текущего подэтапа

- [x] Созданы `CompanyRequest` и `CompanyResponse`.
- [x] Создан ручной `CompanyMapper`.
- [x] Реализованы оба направления преобразования.
- [x] Mapper покрыт unit-тестами.
- [x] `CompanyMapper` зарегистрирован как Spring-компонент.
- [x] Mapper внедрён в `CompanyController`.
- [x] `GET /api/companies` возвращает `List<CompanyResponse>`.
- [x] `GET /api/companies/{id}` возвращает `CompanyResponse`.
- [x] `POST /api/companies` принимает `CompanyRequest`.
- [x] `POST /api/companies` возвращает `CompanyResponse`.
- [x] `PUT /api/companies/{id}` принимает `CompanyRequest`.
- [x] `PUT /api/companies/{id}` возвращает `CompanyResponse`.
- [x] Controller больше не возвращает Entity напрямую для endpoint с телом ответа.
- [x] Controller-тесты проходят после перехода на DTO.
- [x] Добавлена зависимость Bean Validation.
- [x] В `CompanyRequest` добавлены правила для `name`, `website` и `description`.
- [x] В Controller добавлен `@Valid` для `POST` и `PUT`.
- [x] Добавлены controller-тесты на невалидные запросы.
- [x] Создан `ErrorResponse`.
- [x] Создан `GlobalExceptionHandler`.
- [x] Создан `ResourceNotFoundException`.
- [x] Validation-ошибки возвращаются в едином JSON-формате.
- [x] JSON validation-ошибок покрыт controller-тестами для `POST` и `PUT`.
- [x] Not found ошибки для `GET /api/companies/{id}` возвращаются в едином JSON-формате.
- [x] Not found ошибки для `PUT /api/companies/{id}` возвращаются в едином JSON-формате.
- [x] Not found ошибки для `DELETE /api/companies/{id}` возвращаются в едином JSON-формате.
- [x] JSON not found ошибок покрыт controller-тестами для `GET`, `PUT` и `DELETE`.
- [x] Поведение `DELETE /api/companies/{id}` для отсутствующего `id` уточнено: возвращается `404 Not Found` с JSON `ErrorResponse`.
- [x] Все 27 тестов завершаются с `BUILD SUCCESS`.
- [x] Code-коммиты отправлены на GitHub.
- [x] Уточнено поведение `DELETE /api/companies/{id}` для отсутствующего `id`.
- [x] `PROJECT_STATUS.md` обновлён и отправлен на GitHub после завершения блока `DELETE` not found.

## Вопросы для повторения

1. Чем Entity отличается от DTO?
2. Почему не стоит возвращать JPA Entity клиенту напрямую?
3. Зачем разделять `CompanyRequest` и `CompanyResponse`?
4. Почему в `CompanyRequest` нет `id`, `createdAt` и `updatedAt`?
5. Что Java автоматически создаёт для `record`?
6. Чем `request.name()` отличается от `company.getName()`?
7. Что делает `CompanyMapper.toEntity()`?
8. Что делает `CompanyMapper.toResponse()`?
9. Почему mapper можно тестировать без Spring?
10. Почему для теста `toResponse()` использован mock `Company`?
11. Что нужно сделать, чтобы Spring смог внедрить `CompanyMapper` в Controller?
12. Почему в `CompanyControllerTest` понадобился `@Import(CompanyMapper.class)`?
13. Почему `@WebMvcTest` сам не подгрузил обычный `@Component`?
14. Почему Service пока продолжает работать с Entity?
15. Какие endpoint теперь принимают `CompanyRequest`?
16. Какие endpoint теперь возвращают `CompanyResponse`?
17. Почему для `DELETE /api/companies/{id}` DTO не нужен?
18. Что делает `@Valid`?
19. Чем `@NotBlank` отличается от `@Size`?
20. Почему `@Size(max = 255)` не запрещает `null`?
21. Почему при validation-ошибке Service не должен вызываться?
22. Зачем нужен `ErrorResponse`?
23. Зачем нужен `@RestControllerAdvice`?
24. Что делает `@ExceptionHandler`?
25. Почему `ResourceNotFoundException` наследуется от `RuntimeException`?
26. Что делает `orElseThrow(...)`?
27. Почему после `orElseThrow(...)` можно работать с обычным `Company`, а не с `Optional<Company>`?
28. Почему для `404` используется пустой `fieldErrors`?
29. Почему обработку ошибок лучше держать в `GlobalExceptionHandler`?
30. Какой ответ должен возвращать `DELETE /api/companies/{id}`, если компания не найдена?

## Журнал прогресса

### 2026-07-06

- Настроены JDK, Maven, Git, PostgreSQL и GitHub.
- Создан Spring Boot проект.
- Реализован `GET /api/hello`.
- Создана Entity `Company`.
- Последний коммит дня: `bf02b16 Add Company entity`.

### 2026-07-07

- Созданы `CompanyRepository` и Repository-тесты.
- Создан `CompanyService`.
- Реализованы создание, список и поиск по `id`.
- Созданы Service unit-тесты.
- Все 7 тестов завершились с `BUILD SUCCESS`.

### 2026-07-08

- Реализованы удаление и обновление в Service.
- Создан `CompanyController`.
- Реализованы все пять CRUD endpoint.
- CRUD endpoint проверены вручную.
- Все 10 тестов завершились с `BUILD SUCCESS`.

### 2026-07-09

- Создан `CompanyControllerTest`.
- Добавлены тесты списка, поиска, создания и удаления.
- Освоены `@WebMvcTest`, `MockMvc`, `@MockitoBean`, JSONPath и `MediaType.APPLICATION_JSON`.
- Полный набор из 15 тестов завершился с `BUILD SUCCESS`.

### 2026-07-13

#### Выполнено

- Добавлен controller-тест успешного обновления компании.
- Добавлен controller-тест `404 Not Found` при обновлении отсутствующей компании.
- Полный набор из 17 тестов завершился с `BUILD SUCCESS`.
- Созданы `CompanyRequest` и `CompanyResponse` как `record`.
- Создан ручной `CompanyMapper`.
- Добавлен `CompanyMapperTest`.
- Проверены преобразования `CompanyRequest → Company` и `Company → CompanyResponse`.
- Полный набор из 19 тестов завершился с `BUILD SUCCESS`.
- Все рабочие коммиты отправлены на GitHub.
- Ветка `main` синхронизирована с `origin/main`.

#### Изучено

- Назначение DTO.
- Разница между входным и выходным DTO.
- Назначение Java `record`.
- Назначение mapper.
- Ручное преобразование DTO и Entity.
- Unit-тест mapper без Spring-контекста.
- Использование mock Entity, когда нет setters для серверных полей.

#### Возникшие проблемы и исправления

- Сначала был создан лишний DTO-класс `Company`; он удалён.
- В `CompanyResponse` компоненты record были разделены точками с запятой; исправлено на запятые.
- В тесте `id` ошибочно сравнивался со строкой `"1L"`; исправлено на `1L`.
- В конце DTO отсутствовал перевод строки; форматирование исправлено.
- В `CompanyMapper` и тестах выровнено форматирование.

#### Коммиты

- `559ce0a Add company update controller test`
- `1e3b33a Add missing company update controller test`
- `25fc666 Update project status before Company DTO stage`
- `291b89f Add Company DTOs and mapper`

### 2026-07-15

#### Выполнено

- `CompanyMapper` зарегистрирован как Spring-компонент через `@Component`.
- `CompanyMapper` внедрён в `CompanyController` через конструктор.
- Endpoint `GET /api/companies/{id}` переведён с `Company` на `CompanyResponse`.
- В `CompanyControllerTest` добавлен `@Import(CompanyMapper.class)`.
- `GET /api/companies` переведён на возврат `List<CompanyResponse>`.
- `POST /api/companies` переведён на `CompanyRequest` и `CompanyResponse`.
- `PUT /api/companies/{id}` переведён на `CompanyRequest` и `CompanyResponse`.
- Добавлена зависимость `spring-boot-starter-validation`.
- В `CompanyRequest` добавлены validation-аннотации для `name`, `website` и `description`.
- В `CompanyController` добавлен `@Valid` для `POST` и `PUT`.
- Добавлены controller-тесты для validation-ошибок.
- Временной диагностикой через `andDo(print())` проверено, что стандартный Spring response для validation error имел `Status = 400`, `Content type = null`, пустой `Body`.
- Создан `ErrorResponse`.
- Создан `GlobalExceptionHandler`.
- `GlobalExceptionHandler` обрабатывает `MethodArgumentNotValidException`.
- Validation-ошибки теперь возвращаются в JSON-формате `ErrorResponse`.
- В `CompanyControllerTest` проверяется JSON validation-ошибки для `POST`.
- В `CompanyControllerTest` проверяется JSON validation-ошибки для `PUT`.
- Запускались точечные controller-тесты после каждого изменения.
- Запускался полный `CompanyControllerTest`.
- Запускался полный набор тестов проекта.
- Все 27 тестов завершились с `BUILD SUCCESS`.
- Code-коммиты отправлены на GitHub.

#### Изучено

- Зачем mapper должен быть Spring bean, если Controller получает его через constructor injection.
- Почему `@WebMvcTest` не всегда загружает обычные `@Component`.
- Как явно добавить mapper в controller-тест через `@Import`.
- Как постепенно переводить Controller на DTO, не ломая весь CRUD сразу.
- Почему Service пока может продолжать работать с Entity, а Controller уже может принимать и возвращать DTO.
- Как использовать `stream().map(...).toList()` для преобразования списка Entity в список DTO.
- Как работает базовая Bean Validation для `@RequestBody`.
- Почему при невалидном request Service не должен вызываться.
- Как стандартный Spring response для validation-ошибки отличается от собственного `ErrorResponse`.
- Как `@RestControllerAdvice` и `@ExceptionHandler` помогают централизовать обработку ошибок.
- Как через `jsonPath` проверить вложенные поля JSON-ответа.

#### Коммиты

- `a1811e5 Use Company mapper in lookup endpoint`
- `7c86add Update project status after mapper lookup endpoint`
- `ee036c5 Use Company response DTO in list endpoint`
- `e644e66 Use Company DTOs in creation endpoint`
- `fb51605 Use Company DTOs in update endpoint`
- `011a0c3 Update project status after Company DTO migration`
- `2931f1a Add Bean Validation dependency`
- `87e18c8 Add Company request name validation`
- `b6b37b4 Add Company request length validation`
- `d544f4c Update project status after Company validation`
- `c2cc04f Add error response DTO`
- `1cfe9dc Handle validation errors`
- `11ab7d1 Verify update validation error response`

### 2026-07-16

#### Выполнено

- Обновлён `PROJECT_STATUS.md` после validation error handling.
- Создан `ResourceNotFoundException`.
- В `GlobalExceptionHandler` добавлен handler для `ResourceNotFoundException`.
- `GET /api/companies/{id}` переведён на `orElseThrow(...)` и `ResourceNotFoundException`.
- Тест `shouldReturnNotFoundWhenCompanyDoesNotExist` обновлён: теперь проверяет JSON `ErrorResponse`.
- Коммит `Handle not found errors` отправлен на GitHub.
- `PUT /api/companies/{id}` переведён на `orElseThrow(...)` и `ResourceNotFoundException`.
- Тест `shouldReturnNotFoundWhenUpdatingMissingCompany` обновлён: теперь проверяет JSON `ErrorResponse`.
- Коммит `Handle update not found errors` отправлен на GitHub.
- Поведение `DELETE /api/companies/{id}` для отсутствующей компании уточнено: возвращается `404 Not Found` с JSON `ErrorResponse`.
- В `CompanyService.deleteCompany(id)` добавлена проверка `existsById(id)`.
- Старый service-тест успешного удаления обновлён: теперь mock возвращает `true` для `existsById(id)`.
- Добавлен service-тест `shouldThrowResourceNotFoundExceptionWhenDeletingMissingCompany`.
- Добавлен controller-тест `shouldReturnNotFoundWhenDeletingMissingCompany`.
- Коммит `Handle delete not found errors` отправлен на GitHub.
- Обновлён `PROJECT_STATUS.md` после delete error handling.
- Создан enum `WorkFormat`.
- Создана JPA-сущность `Vacancy`.
- Настроена связь `Many-to-One` от `Vacancy` к `Company`.
- Создан `VacancyRepository`.
- Создан `VacancyRepositoryTest`.
- Проверено сохранение и чтение `Vacancy` через PostgreSQL.
- Полный набор тестов проекта прошёл: 27 tests, `BUILD SUCCESS`.
- Коммит `Add Vacancy entity` отправлен на GitHub.
- Запускался точечный тест для `GET` not found.
- Запускался точечный тест для `PUT` not found.
- Запускался полный `CompanyControllerTest`.
- Запускался полный набор тестов проекта.
- Все 27 тестов завершились с `BUILD SUCCESS`.

#### Изучено

- Что `RuntimeException` не требует обязательного `throws` и `try/catch`.
- Как `ResourceNotFoundException` превращается в HTTP-ответ через `@ExceptionHandler`.
- Что `Optional.empty()` — это отсутствие значения, а exception — способ прервать обычный поток выполнения.
- Что `orElseThrow(...)` либо возвращает значение из `Optional`, либо выбрасывает exception.
- Почему после `orElseThrow(...)` можно работать с обычным `Company`.
- Почему у `404` есть тело ответа `ErrorResponse`, но `fieldErrors` пустой.
- Почему централизованная обработка ошибок лучше дублирования JSON в каждом Controller.
- Как тестировать `void` метод Mockito через `doThrow(...).when(...).method(...)`.
- Почему после добавления `existsById(id)` нужно обновить старый service-тест удаления.
- Что `DELETE` может возвращать `404`, если API хочет явно сообщать клиенту, что ресурс не найден.
- Что `enum` ограничивает набор допустимых значений.
- Почему в Entity `Vacancy` хранится объект `Company`, а не только `companyId`.
- Почему `WorkFormat` лучше сохранять через `EnumType.STRING`.
- Почему Mockito `verify(...)` не используется для реального repository в `@DataJpaTest`.

#### Коммиты

- `c2ee641 Update project status after validation error handling`
- `a6a7972 Handle not found errors`
- `773760d Handle update not found errors`
- `72fa767 Update project status after not found handling`
- `8867709 Handle delete not found errors`
- `75348b4 Update project status after delete error handling`
- `c5929d3 Add Vacancy entity`

#### Следующее действие

- Обновить `PROJECT_STATUS.md` в репозитории отдельным status-коммитом.
- После status-коммита перейти в чат 03.
- В чате 03 продолжить с `Vacancy DTO`, mapper, service и controller.

## Точка остановки

### 2026-07-16

- Для `Company` реализован полный CRUD.
- Все пять CRUD endpoint проверены вручную.
- В `CompanyControllerTest` находится 11 тестов.
- Созданы `CompanyRequest` и `CompanyResponse`.
- Создан и протестирован ручной `CompanyMapper`.
- `CompanyMapper` зарегистрирован через `@Component`.
- `CompanyMapper` внедрён в `CompanyController`.
- Все основные endpoint `CompanyController` переведены на DTO там, где DTO нужен.
- `GET /api/companies` возвращает `List<CompanyResponse>`.
- `GET /api/companies/{id}` возвращает `CompanyResponse` или JSON `ErrorResponse` с `404 Not Found`.
- `POST /api/companies` принимает `CompanyRequest` и возвращает `CompanyResponse`.
- `PUT /api/companies/{id}` принимает `CompanyRequest` и возвращает `CompanyResponse` или JSON `ErrorResponse` с `404 Not Found`.
- `DELETE /api/companies/{id}` возвращает `204 No Content`, если компания существует, или JSON `ErrorResponse` с `404 Not Found`, если компания не найдена.
- В `CompanyRequest` добавлены validation-правила для `name`, `website` и `description`.
- В `CompanyController` добавлен `@Valid` для `POST` и `PUT`.
- Validation покрыта controller-тестами.
- Создан `ErrorResponse`.
- Создан `GlobalExceptionHandler`.
- Создан `ResourceNotFoundException`.
- Validation-ошибки возвращаются в едином JSON-формате.
- `404 Not Found` для `GET`, `PUT` и `DELETE` возвращается в едином JSON-формате.
- В `CompanyMapperTest` находится 2 unit-теста.
- Созданы `WorkFormat`, `Vacancy`, `VacancyRepository`, `VacancyRepositoryTest`.
- `VacancyRepositoryTest` проверяет сохранение и чтение вакансии с обязательной связью на компанию.
- Всего в проекте 27 тестов.
- Все тесты завершаются с `BUILD SUCCESS`.
- Последний рабочий code-коммит: `c5929d3 Add Vacancy entity`.
- Коммит отправлен на GitHub.
- Ветка `main` синхронизирована с `origin/main`.
- Рабочее дерево чистое перед обновлением `PROJECT_STATUS.md`.
- Следующий шаг: обновить `PROJECT_STATUS.md` отдельным коммитом, затем перейти в чат 03.
- Перед запуском приложения и полного набора интеграционных тестов в новом терминале нужно установить `DB_PASSWORD`.
