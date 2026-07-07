# Internship Tracker API — текущее состояние

## Текущий этап

Этап 1 — реализация Company.

Сущность `Company` и таблица `companies` созданы. Следующий шаг — `CompanyRepository` и проверка сохранения данных.

## Уже выполнено

- [x] Установлен и настроен JDK 21.
- [x] Настроен `JAVA_HOME`.
- [x] Проверен Maven Wrapper 3.9.16.
- [x] Установлен и настроен Git.
- [x] Проверен PostgreSQL 18.4.
- [x] Создан публичный GitHub-репозиторий `internship-tracker-api`.
- [x] Создан Spring Boot 4.1.0 проект.
- [x] Добавлена зависимость Spring Web.
- [x] Инициализирован локальный Git-репозиторий.
- [x] Проверен `.gitignore`.
- [x] Создан пакет `controller`.
- [x] Создан `HelloController`.
- [x] Реализован `GET /api/hello`.
- [x] Endpoint возвращает `Hello, Internship Tracker!`.
- [x] Выполнена команда `.\mvnw.cmd test`.
- [x] Тесты завершились с `BUILD SUCCESS`.
- [x] Создан первый коммит.
- [x] Локальная ветка `main` связана с `origin/main`.
- [x] Код отправлен на GitHub.
- [x] Добавлены Spring Data JPA и PostgreSQL JDBC Driver.
- [x] Создана база данных `internship_tracker`.
- [x] Создана роль `internship_tracker_app`.
- [x] Настроено безопасное получение пароля через `DB_PASSWORD`.
- [x] Создана JPA-сущность `Company`.
- [x] Создана таблица `companies`.
- [x] Проверены типы, длины и nullable-ограничения столбцов.
- [x] Добавлено автоматическое заполнение `createdAt` и `updatedAt`.
- [x] Создан `CompanyRepository`.
- [x] `CompanyRepository` наследуется от `JpaRepository<Company, Long>`.
- [x] Добавлена зависимость `spring-boot-starter-data-jpa-test`.
- [x] Добавлен `CompanyRepositoryTest`.
- [x] Проверено внедрение `CompanyRepository`.
- [x] Проверены сохранение и чтение `Company` через PostgreSQL.
- [x] Все 3 теста завершились с `BUILD SUCCESS`.
- [x] Создан `CompanyService`.
- [x] `CompanyRepository` внедряется через конструктор.
- [x] Реализован метод `createCompany()`.
- [x] Добавлен unit-тест `CompanyServiceTest`.
- [x] Проверены вызов `save()` и возвращаемый результат.
- [x] Все 4 теста завершились с `BUILD SUCCESS`.

## Что я уже понимаю

- Git хранит историю проекта локально.
- GitHub хранит удалённую копию репозитория.
- `git add` помещает изменения в staging area.
- `git commit` сохраняет подготовленные изменения в локальной истории.
- `git push` отправляет локальные коммиты на GitHub.
- `.gitignore` исключает служебные и генерируемые файлы.
- `@RestController` обозначает класс HTTP-контроллера.
- `@GetMapping("/api/hello")` связывает GET-запрос с методом `hello()`.
- HTTP 404 означает, что обработчик для запрошенного пути не найден.
- `BUILD SUCCESS` означает успешное завершение сборки и тестов.
- `@Entity` обозначает класс, который JPA связывает с таблицей базы данных.
- `@Table` явно задаёт имя таблицы.
- `@Id` обозначает первичный ключ.
- `@GeneratedValue` настраивает автоматическое создание идентификатора.
- `@Column` задаёт свойства столбца: обязательность, длину и имя.
- `@PrePersist` вызывается перед первым сохранением Entity.
- `@PreUpdate` вызывается перед обновлением Entity.
- `DataSource` содержит настройки подключения приложения к базе данных.
- Пароль базы данных передаётся через переменную окружения `DB_PASSWORD`.
- `JpaRepository<Company, Long>` работает с Entity `Company` и идентификатором типа `Long`.
- Spring Data создаёт реализацию Repository автоматически.
- `save()` сохраняет Entity и возвращает сохранённый объект.
- `findById()` возвращает `Optional`, потому что запись может отсутствовать.
- `@DataJpaTest` запускает JPA-часть Spring-контекста.
- `@AutoConfigureTestDatabase(replace = NONE)` оставляет подключение к PostgreSQL вместо встроенной тестовой базы.
- Service содержит бизнес-логику и использует Repository для работы с данными.
- Constructor injection передаёт зависимость через конструктор.
- `@Service` позволяет Spring создать и управлять объектом Service.
- Mock заменяет настоящую зависимость в unit-тесте.
- `when(...).thenReturn(...)` задаёт поведение mock.
- `verify(...)` проверяет взаимодействие Service с Repository.

## Что я пока понимаю частично

- Как Spring находит и создаёт компоненты.
- Что такое dependency injection.
- Полную структуру HTTP-запроса и HTTP-ответа.
- Принципы REST API.
- Разницу между Controller, Service и Repository.
- Устройство `pom.xml`.
- Работу с ветками Git и команду `merge`.

## Известные ошибки

Критических ошибок нет.

## Технический долг

- Для `GET /api/hello` пока нет отдельного теста через MockMvc.
- Тест endpoint будет добавлен позже при изучении controller-тестов.

## Последний рабочий коммит

- Hash: `5d5de86`
- Message: `Add Company service creation`
- Тесты перед коммитом: 4 теста прошли успешно.
- Коммит отправлен на GitHub: да.

## Следующее задание

1. Добавить получение списка компаний в `CompanyService`.
2. Добавить unit-тест для получения списка.
3. После этого перейти к поиску компании по `id`.

## Критерии завершения текущего этапа

- `Company` отмечена как JPA Entity.
- Таблица `companies` создана в PostgreSQL.
- Столбцы и ограничения соответствуют модели.
- `createdAt` и `updatedAt` заполняются автоматически.
- Тесты завершаются с `BUILD SUCCESS`.
- В Git не попали секреты, `.idea/` и `target/`.
- Коммит `Add Company entity` создан и отправлен на GitHub.

## Вопросы для повторения

1. Чем `git add` отличается от `git commit`?
2. Чем `git commit` отличается от `git push`?
3. Зачем нужен `.gitignore`?
4. Что делает `@RestController`?
5. Что делает `@GetMapping`?
6. Почему запрос к неизвестному пути возвращает HTTP 404?
7. Что означает `BUILD SUCCESS`?

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
- Коммит `bf02b16 Add Company entity` отправлен на GitHub.

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

#### Возникшие проблемы

- IntelliJ не сразу распознала пакет `jakarta.persistence`.
- Был забыт пароль пользователя `postgres`.
- При работе Git появлялись предупреждения `LF → CRLF`.

#### Исправления

- Maven-проект перезагружен через `Reload All Maven Projects`.
- Пароль `postgres` безопасно сброшен через временное локальное правило.
- Безопасная аутентификация PostgreSQL восстановлена.
- Предупреждения `LF/CRLF` проверены: они не являются ошибками.
- Исправлены форматирование и тип поля `id` с `long` на `Long`.

#### Последний коммит

- `bf02b16 Add Company entity`

#### Следующее действие

- Создать `CompanyRepository`.
- Разобрать наследование от `JpaRepository`.
- Проверить сохранение и чтение `Company`.

### 2026-07-07

#### Выполнено

- Создан пакет `repository`.
- Создан `CompanyRepository`.
- Добавлено наследование от `JpaRepository<Company, Long>`.
- Добавлена зависимость `spring-boot-starter-data-jpa-test`.
- Создан `CompanyRepositoryTest`.
- Проверено внедрение `CompanyRepository`.
- Проверены сохранение и чтение `Company` через PostgreSQL.
- Создан пакет `service`.
- Создан `CompanyService`.
- `CompanyRepository` внедрён в `CompanyService` через конструктор.
- Реализован метод `createCompany()`.
- Создан unit-тест `CompanyServiceTest`.
- Repository заменён mock-объектом через Mockito.
- Проверены результат `createCompany()` и вызов `companyRepository.save()`.
- Все 4 теста завершились с `BUILD SUCCESS`.

#### Изучено

- Назначение слоя Repository.
- Значение параметров `Company` и `Long` в `JpaRepository`.
- Работа методов `save()` и `findById()`.
- Назначение `Optional`.
- Назначение `@DataJpaTest`.
- Работа `@AutoConfigureTestDatabase(replace = NONE)`.
- Разница между слоями Service и Repository.
- Назначение `@Service`.
- Constructor injection.
- Назначение mock-объектов.
- Работа `@Mock` и `@InjectMocks`.
- Настройка mock через `when(...).thenReturn(...)`.
- Проверка вызова метода через `verify(...)`.

#### Возникшие проблемы

- `@DataJpaTest` пытался заменить PostgreSQL на отсутствующую встроенную базу данных.
- Пустой конструктор `Company` имел уровень доступа `protected`, поэтому тест из другого пакета не мог вызвать `new Company()`.
- После форматирования часть изменений не сразу попала в staging area.

#### Исправления

- Добавлено `@AutoConfigureTestDatabase(replace = NONE)`.
- В Repository-тесте использован публичный конструктор `Company(String, String, String)`.
- После форматирования изменённые файлы повторно добавлены через `git add`.
- Перед коммитами проверены staged diff, тесты и `git status`.

#### Коммиты

- `f310b65 Add Company repository and JPA test`
- `b8f6ab3 Update project status after Company repository`
- `5d5de86 Add Company service creation`

Все коммиты отправлены на GitHub.

#### Следующее действие

- Добавить получение списка компаний в `CompanyService`.
- Добавить unit-тест для получения списка.
- После этого перейти к поиску компании по `id`.

## Точка остановки

### 2026-07-07

- Создан `CompanyRepository`.
- Repository наследуется от `JpaRepository<Company, Long>`.
- Проверены сохранение и чтение `Company` через PostgreSQL.
- Создан `CompanyService`.
- `CompanyRepository` внедряется в Service через конструктор.
- Реализован метод `createCompany()`.
- Добавлен unit-тест `CompanyServiceTest` с Mockito.
- Все 4 теста завершаются с `BUILD SUCCESS`.
- Последний коммит с кодом: `5d5de86 Add Company service creation`.
- Коммит отправлен на GitHub.
- Рабочее дерево после отправки коммита было чистым.
- Следующий шаг: добавить получение списка компаний в `CompanyService`.
- Перед запуском всех тестов в новом терминале нужно установить `DB_PASSWORD`.
