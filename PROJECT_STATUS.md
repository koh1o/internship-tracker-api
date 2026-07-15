# Internship Tracker API — текущее состояние

## Текущий этап

Этап 2 — `Company` API переведён с Entity на DTO на уровне Controller.

Базовый `Company CRUD` завершён на уровнях Repository, Service и Controller. Реализованы и вручную проверены все пять endpoint:

- `GET /api/companies`;
- `POST /api/companies`;
- `GET /api/companies/{id}`;
- `PUT /api/companies/{id}`;
- `DELETE /api/companies/{id}`.

Созданы и подключены:

- `CompanyRequest` — входной DTO;
- `CompanyResponse` — выходной DTO;
- ручной `CompanyMapper`;
- unit-тесты преобразований `CompanyRequest → Company` и `Company → CompanyResponse`.

`CompanyMapper` зарегистрирован как Spring-компонент через `@Component` и внедрён в `CompanyController`.

Текущий внешний контракт `CompanyController`:

- `GET /api/companies` возвращает `List<CompanyResponse>`;
- `GET /api/companies/{id}` возвращает `CompanyResponse`;
- `POST /api/companies` принимает `CompanyRequest` и возвращает `CompanyResponse`;
- `PUT /api/companies/{id}` принимает `CompanyRequest` и возвращает `CompanyResponse`;
- `DELETE /api/companies/{id}` возвращает `204 No Content`, тело ответа не требуется.

Всего в проекте 19 тестов. Последний полный запуск завершился с `BUILD SUCCESS`.

Следующий шаг — добавить Bean Validation для `CompanyRequest`, затем перейти к единому формату ошибок через `@RestControllerAdvice`.

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

### Repository

- [x] Создан `CompanyRepository`.
- [x] `CompanyRepository` наследуется от `JpaRepository<Company, Long>`.
- [x] Создан `CompanyRepositoryTest`.
- [x] Проверены сохранение и чтение `Company` через PostgreSQL.
- [x] В Repository-тестах используется `@DataJpaTest`.
- [x] PostgreSQL не заменяется встроенной базой благодаря `@AutoConfigureTestDatabase(replace = NONE)`.

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
- [x] Проверяются HTTP-статусы, JSON-ответы и вызовы Service.
- [x] Все controller-тесты проходят после перехода Controller на DTO.

### DTO и mapper

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
- [x] Все 19 тестов проекта завершились с `BUILD SUCCESS`.

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

### JPA и PostgreSQL

- `@Entity` связывает класс с таблицей базы данных.
- `@Id` обозначает первичный ключ.
- `@GeneratedValue` настраивает генерацию идентификатора.
- `@Column` задаёт свойства столбца.
- `@PrePersist` вызывается перед первым сохранением.
- `@PreUpdate` вызывается перед обновлением.
- `JpaRepository<Company, Long>` работает с `Company` и `Long`.
- `findById()` возвращает `Optional`, потому что запись может отсутствовать.
- Пароль базы данных передаётся через `DB_PASSWORD`, а не хранится в Git.

### DTO и mapper

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

### Тестирование

- Unit-тест проверяет отдельный класс без всей инфраструктуры.
- Mock заменяет настоящую зависимость.
- `when(...).thenReturn(...)` задаёт поведение mock.
- `verify(...)` проверяет вызов метода mock-объекта.
- `@DataJpaTest` запускает JPA-часть Spring-контекста.
- `@WebMvcTest` запускает MVC-часть приложения.
- `MockMvc` выполняет HTTP-подобные запросы без настоящего сервера.
- `@MockitoBean` помещает mock в Spring test context.
- `any(...)` и `eq(...)` — Mockito matchers.
- Если в одном вызове используется matcher, остальные аргументы тоже задаются через matchers.
- Обычный mapper без зависимостей можно тестировать без Spring.
- Для теста `toResponse()` использован mock `Company`, потому что у Entity нет setters для серверных полей.

## Что я пока понимаю частично

- Как Spring сканирует пакеты и создаёт компоненты.
- Полный механизм dependency injection внутри Spring.
- Чем `@Import(CompanyMapper.class)` в тесте отличается от обычного component scanning.
- Полную структуру HTTP-запроса и HTTP-ответа.
- Принципы REST API и выбор HTTP-статусов для ошибок.
- Устройство `pom.xml` и управление зависимостями Maven.
- Работу с ветками Git и `merge`.
- Bean Validation.
- Единую обработку ошибок через `@RestControllerAdvice`.
- Использование `ArgumentCaptor`.

## Известные ошибки

Критических ошибок нет.

Последний полный запуск тестов:

```text
Tests run: 19, Failures: 0, Errors: 0
BUILD SUCCESS
```

## Технический долг

- Для `GET /api/hello` пока нет отдельного теста через `MockMvc`.
- Входные данные пока не проверяются через Bean Validation.
- Ошибки пока не имеют единого JSON-формата.
- Обработка отсутствующей компании пока выполняется непосредственно в Controller через `Optional`.
- `DELETE` пока всегда возвращает `204 No Content`.
- Схема базы данных пока управляется Hibernate; Flyway будет добавлен позже.
- Предупреждение `spring.jpa.open-in-view` пока не устранено.
- Предупреждение об отсутствии JTA не является блокирующей ошибкой.

## Последний рабочий коммит

- Hash: `fb51605`
- Message: `Use Company DTOs in update endpoint`
- `PUT /api/companies/{id}` переведён на `CompanyRequest` и `CompanyResponse`.
- Все основные endpoint `CompanyController` теперь используют DTO там, где есть тело запроса или ответа.
- Все 19 тестов прошли успешно.
- Коммит отправлен на GitHub.
- Ветка `main` синхронизирована с `origin/main`.
- Рабочее дерево было чистым до обновления `PROJECT_STATUS.md`.

## Последние коммиты

- `a1811e5 Use Company mapper in lookup endpoint`
- `7c86add Update project status after mapper lookup endpoint`
- `ee036c5 Use Company response DTO in list endpoint`
- `e644e66 Use Company DTOs in creation endpoint`
- `fb51605 Use Company DTOs in update endpoint`

## Следующее задание

1. Обновить `PROJECT_STATUS.md` отдельным коммитом.
2. Добавить Bean Validation в `CompanyRequest`.
3. Добавить `@Valid` в `CompanyController` для `POST` и `PUT`.
4. Добавить controller-тесты на невалидные запросы.
5. Проверить HTTP-статусы и формат ошибок, который Spring возвращает по умолчанию.
6. Затем добавить единый формат ошибок через `@RestControllerAdvice`.

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
- [x] Все 19 тестов завершаются с `BUILD SUCCESS`.
- [x] Code-коммиты отправлены на GitHub.
- [ ] `PROJECT_STATUS.md` обновлён, закоммичен и отправлен на GitHub.

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
- Запускались точечные controller-тесты после каждого изменения.
- Запускался полный `CompanyControllerTest`.
- Запускался полный набор тестов проекта.
- Все 19 тестов завершились с `BUILD SUCCESS`.
- Code-коммиты отправлены на GitHub.

#### Изучено

- Зачем mapper должен быть Spring bean, если Controller получает его через constructor injection.
- Почему `@WebMvcTest` не всегда загружает обычные `@Component`.
- Как явно добавить mapper в controller-тест через `@Import`.
- Как постепенно переводить Controller на DTO, не ломая весь CRUD сразу.
- Почему Service пока может продолжать работать с Entity, а Controller уже может принимать и возвращать DTO.
- Как использовать `stream().map(...).toList()` для преобразования списка Entity в список DTO.

#### Коммиты

- `a1811e5 Use Company mapper in lookup endpoint`
- `7c86add Update project status after mapper lookup endpoint`
- `ee036c5 Use Company response DTO in list endpoint`
- `e644e66 Use Company DTOs in creation endpoint`
- `fb51605 Use Company DTOs in update endpoint`

#### Следующее действие

- Обновить `PROJECT_STATUS.md` отдельным коммитом.
- Затем добавить Bean Validation в `CompanyRequest`.

## Точка остановки

### 2026-07-15

- Для `Company` реализован полный CRUD.
- Все пять CRUD endpoint проверены вручную.
- В `CompanyControllerTest` находится 7 тестов.
- Созданы `CompanyRequest` и `CompanyResponse`.
- Создан и протестирован ручной `CompanyMapper`.
- `CompanyMapper` зарегистрирован через `@Component`.
- `CompanyMapper` внедрён в `CompanyController`.
- Все основные endpoint `CompanyController` переведены на DTO там, где DTO нужен.
- `GET /api/companies` возвращает `List<CompanyResponse>`.
- `GET /api/companies/{id}` возвращает `CompanyResponse`.
- `POST /api/companies` принимает `CompanyRequest` и возвращает `CompanyResponse`.
- `PUT /api/companies/{id}` принимает `CompanyRequest` и возвращает `CompanyResponse`.
- `DELETE /api/companies/{id}` возвращает `204 No Content`, DTO не нужен.
- В `CompanyMapperTest` находится 2 unit-теста.
- Всего в проекте 19 тестов.
- Все тесты завершаются с `BUILD SUCCESS`.
- Последний рабочий code-коммит: `fb51605 Use Company DTOs in update endpoint`.
- Коммит отправлен на GitHub.
- Ветка `main` синхронизирована с `origin/main`.
- Рабочее дерево было чистым до обновления `PROJECT_STATUS.md`.
- Следующий шаг: закоммитить обновлённый `PROJECT_STATUS.md`, затем добавить Bean Validation для `CompanyRequest`.
- Перед запуском приложения и полного набора интеграционных тестов в новом терминале нужно установить `DB_PASSWORD`.
