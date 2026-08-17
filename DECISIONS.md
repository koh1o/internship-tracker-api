# Internship Tracker API — журнал решений

В этом файле фиксируются важные технические и архитектурные решения проекта.

Каждая запись содержит:

- дату;
- принятое решение;
- причину;
- рассмотренные альтернативы;
- последствия.

---

## 2026-07-03 — Использование Java 21

### Решение

Использовать Java 21 как основную версию языка.

### Причина

Java 21 — современная LTS-версия, подходящая для Spring Boot и портфолийного backend-проекта.

### Альтернативы

- Java 17;
- более новая версия Java.

### Последствия

Проект, IntelliJ IDEA и Maven должны использовать JDK 21.

---

## 2026-07-03 — Использование Spring Boot

### Решение

Использовать Spring Boot для создания REST API.

### Причина

Spring Boot широко применяется в Java backend-разработке и позволяет создавать приложения с понятной структурой.

### Альтернативы

- Jakarta EE;
- Quarkus;
- Micronaut;
- обычный Java HTTP-сервер.

### Последствия

Необходимо изучить Spring Web, dependency injection, Spring Data JPA, конфигурацию и тестирование.

---

## 2026-07-03 — Использование PostgreSQL

### Решение

Использовать PostgreSQL как основную базу данных.

### Причина

Реляционная база данных подходит для сущностей Company, Vacancy и Application и связей между ними.

### Альтернативы

- MySQL;
- H2;
- MongoDB.

### Последствия

Потребуется изучить таблицы, связи, внешние ключи, ограничения, индексы и миграции.

---

## 2026-07-03 — Использование Maven Wrapper

### Решение

Запускать Maven-команды через Maven Wrapper.

### Причина

Maven Wrapper фиксирует версию Maven и позволяет одинаково собирать проект на разных компьютерах и в будущем CI.

### Альтернативы

- использовать Maven, установленный в операционной системе;
- перейти на Gradle Wrapper.

### Последствия

- Файлы `.mvn`, `mvnw` и `mvnw.cmd` должны храниться в Git.
- На Windows используется команда `./mvnw.cmd` или `.\mvnw.cmd` в PowerShell.
- Проверенная версия Maven Wrapper — 3.9.16.

---

## 2026-07-03 — Модульный монолит

### Решение

Разрабатывать приложение как один модульный монолит.

### Причина

Для первого backend-проекта микросервисы добавят лишнюю сложность.

### Альтернативы

- микросервисная архитектура;
- несколько отдельных приложений.

### Последствия

Company, Vacancy, Application и будущий User будут находиться в одном Spring Boot-приложении.

---

## 2026-07-03 — Слоистая архитектура

### Решение

Использовать слои:

- Controller;
- Service;
- Repository;
- Entity;
- DTO;
- Mapper.

### Причина

Слои разделяют ответственность классов и делают код понятнее.

### Альтернативы

- размещение всей логики в Controller;
- размещение всей логики в одном классе;
- Clean Architecture на первом этапе.

### Последствия

- Controller обрабатывает HTTP-запросы.
- Service содержит бизнес-логику.
- Repository работает с базой данных.
- Entity описывает данные базы.
- DTO описывает входные и выходные данные API.
- Mapper преобразует Entity и DTO.

---

## 2026-07-03 — Без frontend в первой версии

### Решение

Не создавать frontend в первой версии проекта.

### Причина

Главная цель проекта — изучение Java backend-разработки.

### Альтернативы

- JavaFX;
- React;
- Angular;
- обычный HTML и JavaScript.

### Последствия

API будет проверяться через браузер, HTTP-клиент и позднее Swagger.

---

## 2026-07-03 — Авторизация после основного CRUD

### Решение

Добавить Spring Security и JWT только после готовности основного API.

### Причина

Авторизация добавляет много новых понятий и может помешать изучению базовой архитектуры.

### Альтернативы

- реализовать авторизацию в начале проекта.

### Последствия

Сначала реализуются Company, Vacancy и Application без пользователей.

---

## 2026-07-03 — Git с первого дня

### Решение

Использовать Git с начала разработки.

### Причина

Понятная история коммитов является частью портфолио и показывает развитие проекта.

### Альтернативы

- загрузить готовый проект одним коммитом;
- подключить Git после завершения разработки.

### Последствия

Каждая небольшая законченная функция должна сопровождаться осмысленным коммитом.

---

## 2026-07-03 — Английские названия в коде

### Решение

Использовать английские названия для классов, методов, переменных, пакетов, таблиц, endpoint и коммитов.

### Причина

Это общепринятый формат профессиональной разработки.

### Альтернативы

- русские названия в коде.

### Последствия

Объяснения ведутся на русском языке, но программные названия пишутся на английском.

---

## 2026-07-29 — Динамическая фильтрация через Specification

### Решение

Использовать `Specification<Application>` и `JpaSpecificationExecutor<Application>` для динамической фильтрации списка откликов.

### Причина

Количество необязательных фильтров растёт. Отдельный Repository-метод для каждой комбинации привёл бы к большому числу
методов и ветвлений в Service.

### Альтернативы

- отдельные derived query methods для каждой комбинации;
- ручная сборка JPQL;
- QueryDSL;
- native SQL.

### Последствия

- каждое условие оформляется отдельной Specification;
- условия объединяются через `and(...)`;
- отсутствующий фильтр не добавляет ограничение;
- Service выполняет один `findAll(specification, pageable)`;
- новые фильтры можно добавлять без создания всех комбинаций Repository-методов.

---

## 2026-07-29 — Параметр-объект ApplicationFilter

### Решение

Передавать фильтры `Application` через immutable record `ApplicationFilter`.

### Причина

Основной Service-метод получил слишком длинный список параметров. Добавление каждого нового фильтра требовало менять
сигнатуры Controller, Service, Specification и большое количество тестов.

### Альтернативы

- оставить все фильтры отдельными параметрами;
- использовать mutable class;
- привязать HTTP query-параметры напрямую к отдельному request DTO;
- использовать `Map<String, Object>`.

### Последствия

- Controller создаёт один `ApplicationFilter`;
- Service и `ApplicationSpecifications` принимают один параметр-объект;
- record автоматически предоставляет constructor, accessors, `equals`, `hashCode` и `toString`;
- добавление новых фильтров становится проще;
- старые перегрузки Service временно сохранены для обратной совместимости существующих вызовов и тестов.

---

## 2026-07-29 — Первая версия статистики через countByStatus

### Решение

Реализовать первую версию статистики откликов через общий `count()` и derived query `countByStatus(...)` для каждого
значения `ApplicationStatus`.

### Причина

Такой вариант прост для понимания, использует уже знакомые возможности Spring Data JPA и позволяет сначала завершить
HTTP-контракт и тесты статистики.

### Альтернативы

- один JPQL-запрос с `GROUP BY status`;
- native SQL;
- projection;
- вычисление статистики после загрузки всех `Application` в память.

### Последствия

- endpoint выполняет восемь запросов к базе: один общий и семь по статусам;
- в ответе присутствуют все статусы, включая значения с количеством `0`;
- для результата используется `EnumMap<ApplicationStatus, Long>`;
- Mapper не участвует в статистике;
- позднее реализацию следует оптимизировать одним агрегирующим запросом с `GROUP BY status`.


---

## 2026-08-12 — Flyway как источник истины для схемы

### Решение

Использовать Flyway для создания и изменения схемы PostgreSQL, а Hibernate перевести в режим проверки схемы:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Первая versioned migration:

```text
V1__create_initial_schema.sql
```

### Причина

Автоматическое Hibernate DDL удобно на раннем этапе, но не даёт контролируемой истории изменений схемы. Для портфолийного backend-проекта схема должна воспроизводиться из versioned migrations, которые хранятся в Git.

### Альтернативы

- продолжить использовать Hibernate `create` / `update`;
- создавать таблицы вручную через psql;
- использовать Liquibase.

### Последствия

- Flyway создаёт таблицы `companies`, `vacancies`, `applications`;
- Hibernate только валидирует соответствие Entity схеме;
- новая база может быть собрана из migrations;
- будущие изменения схемы оформляются как `V2`, `V3` и далее;
- versioned migration после применения не должна редактироваться без специальной причины.

---

## 2026-08-12 — Не использовать Flyway repair для учебной checksum-ошибки на пустой development DB

### Решение

При checksum mismatch первой migration на локальной development DB без важных данных не использовать `flyway repair`, а удалить учебную схему и `flyway_schema_history`, затем применить текущую V1 заново.

### Причина

Checksum mismatch возник после форматирования уже применённой V1. База не содержала важных данных, поэтому безопаснее было пересоздать её из текущей versioned migration и сохранить понятную историю, чем обучаться обходу ошибки через `repair` без необходимости.

### Альтернативы

- выполнить `flyway repair`;
- вернуть файл V1 байт-в-байт к старому состоянию;
- вручную менять запись checksum в `flyway_schema_history`.

### Последствия

- подтверждено понимание назначения checksum;
- после стабилизации V1 её больше не редактировать;
- реальные изменения схемы делать новой migration;
- `repair` не использовать как обычный способ скрыть несоответствие migration history.

---

## 2026-08-14 — Specification unit tests дополняются реальными PostgreSQL integration tests

### Решение

Сохранить Mockito-тесты `ApplicationSpecificationsTest`, но дополнить их repository integration tests через:

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
```

### Причина

Mocked Criteria API проверяет построение `Predicate`, но не доказывает, что итоговая цепочка Spring Data JPA → Hibernate → SQL → PostgreSQL действительно возвращает правильные записи.

### Альтернативы

- оставить только Mockito unit tests;
- удалить unit tests и оставить только PostgreSQL tests;
- проверять фильтры только через Controller tests.

### Последствия

- unit и integration tests выполняют разные задачи и сохраняются вместе;
- реальные tests проверяют отдельные filters, их комбинации и отсутствие filters;
- для каждого важного filter scenario используются matching и non-matching rows;
- Repository tests пока зависят от локального PostgreSQL;
- вопрос перехода на Testcontainers вынесен в отдельное решение.

---

## 2026-08-14 — Включающие границы date filters проверяются отдельными integration tests

### Решение

Отдельно проверять, что диапазоны `appliedAt` и `nextContactAt` включают значения, равные `from` и `to`.

### Причина

Обычный сценарий `before / inside / after` подтверждает работу диапазона в целом, но не доказывает семантику операторов `>=` и `<=` на самих границах.

### Альтернативы

- считать unit tests Specification достаточными;
- проверять только одно значение внутри диапазона;
- проверять boundaries только через Service tests.

### Последствия

Integration data строятся как:

```text
before → не входит
from   → входит
inside → входит
to     → входит
after  → не входит
```

Тесты не зависят от порядка результата, если `Sort` явно не задан.

---

## 2026-08-14 — Проверять Specification, Sort и Pageable совместно

### Решение

Добавить repository integration tests, которые проверяют совместную работу:

```text
Specification + Sort + Pageable + PostgreSQL
```

### Причина

Фильтрация, сортировка и pagination используются клиентом одновременно. Раздельные tests каждого механизма не полностью доказывают правильность их комбинации в реальном SQL-запросе.

### Альтернативы

- оставить только отдельные unit/service tests;
- проверять комбинацию только через MockMvc;
- сразу перейти к end-to-end tests.

### Последствия

Проверяются:

- filtering + sorting;
- filtering + pagination;
- filtering + sorting + pagination;
- `Page` metadata (`number`, `size`, `totalElements`, `totalPages`);
- порядок content только при явно заданном `Sort`;
- JPA Entity в assertions сравниваются по `id`, а не через object identity.

---

## 2026-08-14 — Testcontainers отложен до отдельного выбора стратегии integration testing

### Решение

Пока оставить repository integration tests на локальном PostgreSQL и не подключать Testcontainers автоматически.

### Причина

Сначала нужно было научиться отличать unit test от integration test и проверить сами Specification scenarios на реальной PostgreSQL. Подключение Testcontainers добавляет отдельные понятия: lifecycle контейнера, datasource wiring, startup cost и изоляцию среды.

### Альтернативы

- подключить Testcontainers сразу;
- продолжить использовать локальный PostgreSQL до конца проекта;
- использовать embedded database.

### Последствия

- текущие integration tests реальны, но требуют доступного локального PostgreSQL;
- следующий этап начинается с осознанного выбора: Testcontainers или более высокоуровневые integration scenarios;
- если будет выбран Testcontainers, Flyway должен применяться и к test container database.

---

## 2026-08-16 — Перевести PostgreSQL integration tests на Testcontainers

### Решение

Использовать PostgreSQL Testcontainer для repository и full application integration tests вместо зависимости от вручную настроенной локальной test database.

Это решение заменяет временное решение от 2026-08-14 оставить integration tests на локальном PostgreSQL.

### Причина

Тесты должны быть воспроизводимыми и не зависеть от:

- локально запущенного PostgreSQL;
- локальных данных;
- фиксированного порта;
- `DB_PASSWORD` в окружении разработчика.

Testcontainers позволяет запускать для tests настоящий PostgreSQL в изолированном временном контейнере.

### Альтернативы

- продолжить использовать локальный PostgreSQL;
- использовать H2 или другую embedded database;
- вручную запускать отдельный PostgreSQL container перед tests.

### Последствия

- добавлены test dependencies `spring-boot-testcontainers` и `testcontainers-postgresql`;
- используется PostgreSQL image `postgres:18-alpine`;
- tests требуют работающий Docker;
- test suite больше не зависит от локального PostgreSQL и `DB_PASSWORD`;
- Flyway применяет migrations к test database;
- Hibernate валидирует схему контейнера;
- PostgreSQL container создаётся на время test run и удаляется после него.

---

## 2026-08-16 — Управлять PostgreSQL Testcontainer через Spring bean и @ServiceConnection

### Решение

Создать общую `TestcontainersConfiguration`, объявить `PostgreSQLContainer` как Spring bean и пометить его `@ServiceConnection`.

### Причина

Spring Boot умеет автоматически получать connection details контейнера и использовать их для `DataSource` и Flyway. Общая configuration также устраняет дублирование container setup в test classes.

### Альтернативы

- `@Testcontainers` + `@Container` в каждом test class;
- вручную передавать datasource properties через `@DynamicPropertySource`;
- создавать отдельную container configuration для каждого test class.

### Последствия

- `TestcontainersConfiguration` переиспользуется через `@Import(TestcontainersConfiguration.class)`;
- lifecycle контейнера управляется Spring;
- `testcontainers-junit-jupiter` в итоговой конфигурации не требуется;
- repository tests сохраняют `@AutoConfigureTestDatabase(replace = NONE)`, чтобы Spring не заменял PostgreSQL embedded database.

---

## 2026-08-16 — Добавить full application integration tests через MockMvc

### Решение

Добавить отдельный уровень integration testing для `Company`, `Vacancy` и `Application` на основе:

```text
@SpringBootTest
+ @AutoConfigureMockMvc
+ PostgreSQL Testcontainer
```

### Причина

`@WebMvcTest` проверяет web layer изолированно, а repository integration tests проверяют persistence layer. Нужны тесты, которые доказывают совместную работу всей основной цепочки приложения.

### Альтернативы

- ограничиться unit, controller и repository tests;
- запускать реальный HTTP server для каждого integration test;
- сразу писать внешние end-to-end tests.

### Последствия

Full application integration tests проверяют цепочку:

```text
MockMvc
→ Controller
→ Service
→ Repository
→ Hibernate
→ PostgreSQL
```

MockMvc не запускает внешний HTTP server, но используется полный Spring context.

Созданы отдельные integration test classes для:

- `Company`;
- `Vacancy`;
- `Application`.

---

## 2026-08-16 — Проверять состояние PostgreSQL после изменяющих HTTP-операций

### Решение

В full application integration tests после `POST`, `PUT`, `PATCH` и `DELETE` дополнительно проверять конечное состояние PostgreSQL через Repository.

### Причина

Корректный HTTP status и JSON response сами по себе не доказывают, что данные действительно сохранены, обновлены или удалены. Для ошибочных операций также важно доказать, что состояние БД не было испорчено.

### Альтернативы

- проверять только HTTP status и JSON;
- проверять внутренние вызовы Repository через Mockito.

### Последствия

- после `POST` проверяется появление записи;
- после `PUT` и успешного `PATCH` проверяется сохранённое новое состояние;
- после invalid status transition проверяется сохранение старого status;
- после `DELETE` проверяется удаление конкретной Entity;
- для зависимых Entity дополнительно проверяется отсутствие нежелательного cascade delete;
- Mockito interactions остаются задачей unit tests Service-слоя.

---

## 2026-08-16 — Не скрывать LAZY-проблемы через @Transactional в full integration tests

### Решение

Не добавлять `@Transactional` к full integration test только ради возможности читать lazy-поля detached Entity.

### Причина

При `FetchType.LAZY` Hibernate может вернуть proxy связанной Entity. После завершения session попытка прочитать неинициализированное поле может привести к `LazyInitializationException`.

Добавление `@Transactional` только ради такой assertion удерживало бы session открытой и скрывало бы реальное поведение lazy loading.

### Альтернативы

- добавить `@Transactional` к integration test;
- заменить `LAZY` на `EAGER`;
- специально инициализировать связь перед завершением session.

### Последствия

- foreign key в detached Entity проверяется через identifier связанной Entity, например `company.id` или `vacancy.id`;
- данные связанных Entity проверяются через HTTP response DTO;
- `FetchType.LAZY` остаётся в production-модели;
- поведение lazy proxy и `LazyInitializationException` разобрано на практике.

---

## 2026-08-17 — Не дублировать всю матрицу business rules в full integration tests

### Решение

Для `ApplicationIntegrationTest` использовать несколько representative scenarios вместо повторения всех Service unit tests через `@SpringBootTest`.

### Причина

Full integration tests тяжелее и медленнее unit tests. Их задача — доказать совместную работу слоёв и ключевые application flows, а не повторять каждую ветку бизнес-логики.

### Альтернативы

- продублировать все допустимые и недопустимые переходы `ApplicationStatus` через full integration tests;
- оставить только happy-path CRUD scenarios.

### Последствия

`ApplicationIntegrationTest` покрывает ключевые сценарии:

- valid create;
- get existing;
- get missing;
- create with missing `Vacancy`;
- `APPLIED` without `appliedAt`;
- `nextContactAt < appliedAt`;
- valid transition `APPLIED → INTERVIEW`;
- invalid transition `APPLIED → OFFER` с проверкой unchanged DB state;
- idempotent `APPLIED → APPLIED`;
- `PUT` с изменением Vacancy/dates/notes без изменения status;
- `DELETE` с сохранением `Vacancy` и `Company`.

Полная матрица status transitions остаётся на уровне unit tests `ApplicationService`.

---

## 2026-08-17 — Считать текущий testing block завершённым

### Решение

После repository integration tests, Testcontainers и full application integration tests для трёх основных сущностей завершить текущий этап тестирования и перейти к Swagger/OpenAPI.

### Причина

В проекте уже есть несколько взаимодополняющих уровней tests:

```text
unit tests
controller tests
repository integration tests
full application integration tests
```

Продолжение массового расширения test suite сейчас даст меньше учебной и практической пользы, чем переход к следующему этапу портфолио-проекта.

### Альтернативы

- продолжить расширять full integration tests;
- сначала полностью рефакторить test setup;
- сразу перейти к Spring Security/JWT.

### Последствия

- следующий этап — Swagger/OpenAPI;
- Security/JWT остаётся отложенным;
- механический refactor повторяющегося integration test setup можно сделать позднее отдельным cleanup/refactor commit;
- текущий testing block считается завершённым на стабильной рабочей версии.

---

## Шаблон новой записи

## YYYY-MM-DD — Название решения

### Решение

Описание принятого решения.

### Причина

Почему принято это решение.

### Альтернативы

- вариант 1;
- вариант 2.

### Последствия

Как решение повлияет на проект.
