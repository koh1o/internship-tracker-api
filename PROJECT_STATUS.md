# Internship Tracker API — текущее состояние

## Текущий этап

Этап 1 — завершение `Company CRUD` и покрытие Controller тестами.

Для `Company` реализованы все основные слои:

- `Company` Entity;
- `CompanyRepository`;
- `CompanyService`;
- `CompanyController`;
- Repository-, Service- и Controller-тесты.

В `CompanyController` реализованы endpoint:

- `GET /api/companies`;
- `POST /api/companies`;
- `GET /api/companies/{id}`;
- `PUT /api/companies/{id}`;
- `DELETE /api/companies/{id}`.

Через `MockMvc` проверены:

- получение списка компаний;
- получение существующей компании по `id`;
- ответ `404 Not Found` для отсутствующей компании;
- создание компании;
- удаление компании.

Всего в проекте 15 тестов. Последний полный запуск завершился с `BUILD SUCCESS`.

Следующий шаг — добавить controller-тесты для `PUT /api/companies/{id}`, затем перейти к DTO, validation и единой обработке ошибок.

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
- [x] Код отправлен на GitHub.
- [x] В Git не добавлены секреты, `.idea/` и `target/`.

### Spring Boot и первый endpoint

- [x] Создан Spring Boot 4.1.0 проект на Java 21 и Maven.
- [x] Добавлена зависимость Spring Web.
- [x] Создан пакет `controller`.
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
- [x] Для `Company` добавлены поля `id`, `name`, `website`, `description`, `createdAt`, `updatedAt`.
- [x] Настроены JPA-аннотации и ограничения столбцов.
- [x] Добавлено автоматическое заполнение `createdAt` и `updatedAt` через `@PrePersist` и `@PreUpdate`.
- [x] Hibernate создаёт таблицу `companies`.

### Repository

- [x] Создан `CompanyRepository`.
- [x] `CompanyRepository` наследуется от `JpaRepository<Company, Long>`.
- [x] Добавлена зависимость `spring-boot-starter-data-jpa-test`.
- [x] Создан `CompanyRepositoryTest`.
- [x] Проверено внедрение `CompanyRepository`.
- [x] Проверены сохранение и чтение `Company` через PostgreSQL.
- [x] В Repository-тестах используется `@DataJpaTest`.
- [x] PostgreSQL не заменяется встроенной базой благодаря `@AutoConfigureTestDatabase(replace = NONE)`.

### Service

- [x] Создан `CompanyService`.
- [x] `CompanyRepository` внедряется через конструктор.
- [x] Реализован `createCompany()`.
- [x] Реализован `getAllCompanies()`.
- [x] Реализован `getCompanyById()`.
- [x] Реализован `updateCompany()`.
- [x] Реализован `deleteCompany()`.
- [x] Создан `CompanyServiceTest`.
- [x] Repository заменяется mock-объектом через Mockito.
- [x] Проверены создание, получение списка, поиск, обновление и удаление.
- [x] Проверены найденная и отсутствующая компания через `Optional`.
- [x] Проверено, что при обновлении отсутствующей компании `save()` не вызывается.

### Controller и HTTP

- [x] Создан `CompanyController`.
- [x] Реализован `GET /api/companies`.
- [x] Реализован `POST /api/companies` с ответом `201 Created`.
- [x] Реализован `GET /api/companies/{id}`.
- [x] Реализован `PUT /api/companies/{id}`.
- [x] Реализован `DELETE /api/companies/{id}` с ответом `204 No Content`.
- [x] Вручную проверены получение списка и создание компании.
- [x] Вручную проверены ответы `200 OK`, `201 Created`, `204 No Content` и `404 Not Found`.
- [x] Вручную проверено обновление компании через `PUT`.
- [x] Проверено, что при обновлении сохраняется `id` и `createdAt`, а `updatedAt` изменяется.

### Controller-тесты

- [x] Добавлена зависимость `spring-boot-starter-webmvc-test`.
- [x] Создан `CompanyControllerTest`.
- [x] Controller тестируется через `@WebMvcTest(CompanyController.class)`.
- [x] `CompanyService` заменяется mock-объектом через `@MockitoBean`.
- [x] Добавлен тест получения списка компаний.
- [x] Добавлен тест успешного поиска компании по `id`.
- [x] Добавлен тест ответа `404 Not Found` для отсутствующей компании.
- [x] Добавлен тест создания компании через `POST`.
- [x] Добавлен тест удаления компании через `DELETE`.
- [x] Проверяются HTTP-статусы, JSON-ответы и вызовы Service.
- [x] Все 15 тестов проекта завершились с `BUILD SUCCESS`.

## Что я уже понимаю

### Git

- Git хранит историю проекта локально.
- GitHub хранит удалённую копию репозитория.
- `git status` показывает состояние рабочего дерева и staging area.
- `git add` помещает изменения в staging area.
- `git commit` сохраняет подготовленные изменения в локальной истории.
- `git push` отправляет локальные коммиты на GitHub.
- `working tree clean` означает отсутствие незакоммиченных изменений.
- Ветка может быть чистой, но при этом опережать `origin/main`, пока не выполнен `git push`.
- `git diff` показывает незастейдженные изменения.
- `git diff --cached` показывает содержимое будущего коммита.
- `.gitignore` исключает служебные и генерируемые файлы.

### Spring и архитектура

- `@RestController` обозначает класс, который принимает HTTP-запросы и возвращает данные.
- `@RequestMapping` задаёт общий путь для endpoint контроллера.
- `@GetMapping`, `@PostMapping`, `@PutMapping` и `@DeleteMapping` связывают HTTP-методы с Java-методами.
- `@RequestBody` преобразует JSON из тела запроса в Java-объект.
- `@PathVariable` получает значение из URL.
- Controller принимает HTTP-запрос и вызывает Service.
- Service содержит бизнес-логику и вызывает Repository.
- Controller не должен обращаться к Repository напрямую.
- Constructor injection передаёт зависимости через конструктор.
- Spring создаёт управляемые объекты для классов с `@Service`, `@Repository` и `@RestController`.
- `ResponseEntity` позволяет управлять HTTP-статусом и телом ответа.

### JPA и PostgreSQL

- `@Entity` обозначает класс, связанный с таблицей базы данных.
- `@Table` явно задаёт имя таблицы.
- `@Id` обозначает первичный ключ.
- `@GeneratedValue` настраивает автоматическое создание идентификатора.
- `@Column` задаёт свойства столбца.
- `@PrePersist` вызывается перед первым сохранением Entity.
- `@PreUpdate` вызывается перед обновлением Entity.
- `JpaRepository<Company, Long>` работает с Entity `Company` и идентификатором типа `Long`.
- Spring Data создаёт реализацию Repository автоматически.
- `save()` сохраняет Entity и возвращает сохранённый объект.
- `findAll()` возвращает список Entity.
- `findById()` возвращает `Optional`, потому что запись может отсутствовать.
- Пароль базы данных должен передаваться через `DB_PASSWORD`, а не храниться в Git.

### Тестирование

- Unit-тест Service проверяет один класс без настоящей базы данных.
- Mock заменяет настоящую зависимость в unit-тесте.
- `when(...).thenReturn(...)` задаёт поведение mock.
- `verify(...)` проверяет вызов метода mock-объекта.
- `Optional.of(company)` обозначает найденную компанию.
- `Optional.empty()` обозначает отсутствие компании.
- `@DataJpaTest` запускает JPA-часть Spring-контекста.
- `@WebMvcTest` запускает MVC-часть приложения для тестирования Controller.
- `MockMvc` выполняет HTTP-подобные запросы без запуска настоящего сервера.
- `@MockitoBean` помещает mock в Spring test context.
- `MediaType.APPLICATION_JSON` обозначает JSON в теле HTTP-запроса.
- `jsonPath("$.name")` проверяет поле JSON-объекта.
- `jsonPath("$[0].name")` проверяет поле первого элемента JSON-массива.
- `any(Company.class)` подходит, когда Spring создаёт новый объект из JSON.
- Для `void`-метода mock обычно не требует настройки через `when(...)`.

### HTTP

- `200 OK` означает успешное получение или обновление данных.
- `201 Created` означает успешное создание ресурса.
- `204 No Content` означает успешное выполнение без тела ответа.
- `404 Not Found` означает, что запрошенный ресурс или обработчик не найден.
- `BUILD SUCCESS` означает успешное завершение сборки и тестов.

## Что я пока понимаю частично

- Как Spring сканирует пакеты и создаёт компоненты.
- Полный механизм dependency injection внутри Spring.
- Полную структуру HTTP-запроса и HTTP-ответа.
- Принципы REST API и выбор HTTP-статусов для разных ошибок.
- Устройство `pom.xml` и управление зависимостями Maven.
- Работу с ветками Git и команду `merge`.
- Разницу между Entity и DTO.
- Bean Validation и работу аннотаций валидации.
- Единую обработку ошибок через `@RestControllerAdvice`.
- Использование `ArgumentCaptor` для проверки аргументов mock.

## Известные ошибки

Критических ошибок нет.

Последний полный запуск тестов:

```text
Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Технический долг

- Для `GET /api/hello` пока нет отдельного теста через `MockMvc`.
- Для `PUT /api/companies/{id}` пока нет controller-тестов.
- Controller временно принимает и возвращает `Company` Entity напрямую.
- Входные данные пока не проверяются через Bean Validation.
- Ошибки пока не имеют единого JSON-формата.
- Обработка отсутствующей компании пока выполняется непосредственно в Controller через `Optional`.
- `DELETE` пока всегда возвращает `204 No Content`; поведение для отсутствующего `id` будет уточнено после добавления единой обработки ошибок.
- Схема базы данных пока управляется Hibernate; Flyway будет добавлен позже.
- Предупреждение `spring.jpa.open-in-view` пока не устранено.
- Предупреждение об отсутствии JTA не является блокирующей ошибкой и пока не требует исправления.

## Последний рабочий коммит

- Hash: `3a8d8e0`
- Message: `Add company deletion controller test`
- Все 15 тестов прошли успешно.
- Коммит отправлен на GitHub.
- Ветка `main` синхронизирована с `origin/main`.
- Рабочее дерево чистое.

## Последние коммиты

- `dce5d1f Add company list controller test`
- `ca82a5f Add company lookup controller tests`
- `514a014 Add company creation controller test`
- `3a8d8e0 Add company deletion controller test`

## Следующее задание

1. Добавить controller-тест успешного обновления компании через `PUT /api/companies/{id}`.
2. Настроить mock `companyService.updateCompany(...)` для найденной компании.
3. Выполнить PUT-запрос с JSON-телом.
4. Проверить `200 OK` и поля JSON-ответа.
5. Проверить вызов Service.
6. Добавить controller-тест обновления отсутствующей компании.
7. Проверить `404 Not Found`.
8. Запустить полный набор тестов.
9. Создать отдельный осмысленный коммит.
10. После завершения перейти к DTO, validation и единой обработке ошибок.

## Критерии завершения текущего подэтапа

- [x] Созданы `CompanyRepository`, `CompanyService` и `CompanyController`.
- [x] В `CompanyService` реализован полный CRUD.
- [x] В `CompanyController` реализован полный CRUD.
- [x] Unit-тесты Service используют Mockito.
- [x] Repository-тесты работают с PostgreSQL.
- [x] Через `MockMvc` проверены список, поиск, создание и удаление.
- [ ] Через `MockMvc` проверено успешное обновление компании.
- [ ] Через `MockMvc` проверено обновление отсутствующей компании.
- [x] Вручную проверены все пять CRUD endpoint.
- [x] В Git не попали секреты, `.idea/` и `target/`.
- [x] Все рабочие коммиты отправлены на GitHub.
- [x] Ветка `main` синхронизирована с `origin/main`.

## Вопросы для повторения

1. Чем `git add` отличается от `git commit`?
2. Чем `git commit` отличается от `git push`?
3. Что показывает `git diff --cached`?
4. Почему `working tree clean` не всегда означает, что все коммиты уже отправлены на GitHub?
5. Почему Controller не должен обращаться к Repository напрямую?
6. Зачем нужен слой Service?
7. Почему `findById()` возвращает `Optional`?
8. Что произойдёт при вызове `get()` у `Optional.empty()`?
9. Чем `@DataJpaTest` отличается от `@WebMvcTest`?
10. Зачем в controller-тесте нужен `@MockitoBean`?
11. Чем проверка HTTP-статуса отличается от `verify(...)`?
12. Почему для POST-запроса указывается `MediaType.APPLICATION_JSON`?
13. Что означает `204 No Content`?
14. Почему Entity позже нужно заменить на DTO в API?

## Журнал прогресса

### 2026-07-06

#### Выполнено

- Инициализирован локальный Git-репозиторий.
- Реализован и проверен `GET /api/hello`.
- Проект связан с GitHub.
- Добавлены Spring Data JPA и PostgreSQL JDBC Driver.
- Создана база данных `internship_tracker`.
- Создан отдельный пользователь PostgreSQL `internship_tracker_app`.
- Пользователь приложения назначен владельцем базы.
- Настроено подключение через переменные окружения.
- Настоящий пароль не добавлен в Git.
- Создана JPA-сущность `Company`.
- Hibernate создал таблицу `companies`.
- Проверены типы и ограничения столбцов.
- Добавлены конструкторы, getters и setters.
- Настроено автоматическое заполнение `createdAt` и `updatedAt`.
- Тесты завершились с `BUILD SUCCESS`.

#### Изучено

- Базовый процесс `status → add → commit → push`.
- Назначение staging area.
- Назначение `@RestController` и `@GetMapping`.
- Назначение JPA Entity.
- Связь Java-полей со столбцами таблицы.
- Назначение `@Id`, `@GeneratedValue` и `@Column`.
- Назначение `@PrePersist` и `@PreUpdate`.
- Разница между пользователем `postgres` и пользователем приложения.
- Безопасное получение пароля через `DB_PASSWORD`.
- Базовая роль `DataSource`.

#### Возникшие проблемы и исправления

- IntelliJ не сразу распознала пакет `jakarta.persistence`; Maven-проект был перезагружен.
- Был забыт пароль пользователя `postgres`; пароль безопасно сброшен и аутентификация восстановлена.
- Появлялись предупреждения `LF → CRLF`; проверено, что они не являются ошибками.
- Тип поля `id` исправлен с `long` на `Long`.

#### Последний коммит дня

- `bf02b16 Add Company entity`

### 2026-07-07

#### Выполнено

- Создан `CompanyRepository`.
- Добавлено наследование от `JpaRepository<Company, Long>`.
- Создан `CompanyRepositoryTest`.
- Проверены сохранение и чтение через PostgreSQL.
- Создан `CompanyService`.
- Repository внедрён через конструктор.
- Реализованы `createCompany()`, `getAllCompanies()` и `getCompanyById()`.
- Создан `CompanyServiceTest`.
- Repository заменён mock-объектом.
- Добавлены тесты найденной и отсутствующей компании.
- Все 7 тестов завершились с `BUILD SUCCESS`.

#### Изучено

- Назначение слоя Repository.
- Работа `JpaRepository`.
- Методы `save()`, `findAll()` и `findById()`.
- Назначение `Optional`.
- Назначение `@DataJpaTest`.
- Работа `@AutoConfigureTestDatabase(replace = NONE)`.
- Разница между Service и Repository.
- Constructor injection.
- Mockito: `when(...)`, `thenReturn(...)` и `verify(...)`.

#### Коммиты

- `f310b65 Add Company repository and JPA test`
- `b8f6ab3 Update project status after Company repository`
- `5d5de86 Add Company service creation`
- `3d7434e Add company list service method`
- `cae42ed Add company lookup by id`
- `eddab8f Add missing company lookup test`

### 2026-07-08

#### Выполнено

- Реализован `deleteCompany()` в `CompanyService`.
- Реализован `updateCompany()` в `CompanyService`.
- Добавлены unit-тесты удаления и обновления.
- Проверено обновление существующей и отсутствующей компании.
- Создан `CompanyController`.
- Реализованы endpoint списка, создания и поиска по `id`.
- Затем добавлены endpoint удаления и обновления.
- Все CRUD endpoint проверены вручную.
- Проверены HTTP-статусы `200`, `201`, `204` и `404`.
- Все 10 тестов завершились с `BUILD SUCCESS`.

#### Коммиты

- `72e2407 Add company deletion service method`
- `ed06656 Add company update service method`
- `833ff27 Add company list endpoint`
- `a07fe4c Add company creation endpoint`
- `6c337e3 Add company lookup endpoint`
- `a07ff7b Update project status after company lookup endpoint`
- `142f88c Add company deletion endpoint`
- `6a3838c Add company update endpoint`

### 2026-07-09

#### Выполнено

- Добавлен `CompanyControllerTest`.
- Добавлен тест `GET /api/companies`.
- Добавлен тест успешного `GET /api/companies/{id}`.
- Добавлен тест `404 Not Found` для отсутствующей компании.
- Добавлен тест `POST /api/companies`.
- Добавлен тест `DELETE /api/companies/{id}`.
- Освоены `@WebMvcTest`, `MockMvc`, `@MockitoBean`, JSONPath и `MediaType.APPLICATION_JSON`.
- Проверены вызовы Service через `verify(...)`.
- Полный набор из 15 тестов завершился с `BUILD SUCCESS`.
- Все созданные коммиты отправлены на GitHub.
- Ветка `main` синхронизирована с `origin/main`.

#### Возникшие проблемы и исправления

- IntelliJ случайно добавила лишний статический импорт `AbstractPersistable_.id`; импорт удалён.
- IntelliJ объединила импорты `MockMvcRequestBuilders` в wildcard; возвращены явные импорты.
- В PowerShell символ `#` без кавычек воспринимался как начало комментария; Maven-параметр для запуска одного теста заключён в кавычки.
- Исправлено форматирование аргументов в вызове `delete(...)`.

#### Коммиты

- `dce5d1f Add company list controller test`
- `ca82a5f Add company lookup controller tests`
- `514a014 Add company creation controller test`
- `3a8d8e0 Add company deletion controller test`

#### Следующее действие

- Добавить controller-тесты обновления компании через `PUT /api/companies/{id}`.
- После этого перейти к DTO, validation и единой обработке ошибок.

## Точка остановки

### 2026-07-09

- Для `Company` реализован полный CRUD на уровнях Service и Controller.
- Реализованы все пять CRUD endpoint.
- Все endpoint проверены вручную.
- В `CompanyControllerTest` находится 5 тестов.
- Всего в проекте 15 тестов.
- Все тесты завершаются с `BUILD SUCCESS`.
- Последний рабочий коммит: `3a8d8e0 Add company deletion controller test`.
- Коммит отправлен на GitHub.
- Ветка `main` синхронизирована с `origin/main`.
- Рабочее дерево чистое.
- Следующий шаг: добавить controller-тесты для `PUT /api/companies/{id}`.
- После завершения controller-тестов перейти к DTO, Bean Validation и единой обработке ошибок.
- Перед запуском приложения и полного набора интеграционных тестов в новом терминале нужно установить `DB_PASSWORD`.
