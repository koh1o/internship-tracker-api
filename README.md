# Internship Tracker API

REST API для учёта компаний, вакансий и откликов на стажировки. Позволяет хранить данные о вакансиях, отслеживать этапы откликов, планировать следующий контакт и получать статистику по статусам.

## Возможности

- Создание, просмотр, изменение и удаление компаний, вакансий и откликов.
- Проверка допустимых переходов между статусами отклика.
- Проверка обязательных полей, длины текста и согласованности дат.
- Пагинация, сортировка и комбинированная фильтрация откликов.
- Статистика по всем семи статусам.
- Регистрация пользователей с BCrypt и проверка учётных данных при входе.
- Swagger UI и спецификация OpenAPI.

**Текущий этап:** подготовлена основа Spring Security; JWT находится в разработке. Вход возвращает `UserResponse`, токен не выдаётся. Остальные запросы пока разрешены через `permitAll()`, разделение данных по пользователям не реализовано. Это текущее ограничение проекта.

## Технологии

| Технология | Назначение |
|---|---|
| Java 21, Spring Boot 4.1.0 | REST API и бизнес-логика |
| Spring Data JPA, Hibernate | Работа с сущностями и запросами к БД |
| PostgreSQL 18, Flyway | Хранение данных и версионирование схемы |
| Jakarta Bean Validation | Валидация входных данных |
| Spring Security, BCrypt | Проверка учётных данных и хранение хешей паролей |
| springdoc 3.1.0 | Swagger UI и OpenAPI |
| JUnit, Mockito, MockMvc, Testcontainers | Модульные и интеграционные тесты |
| Maven Wrapper, Docker Compose | Сборка и локальный запуск |

Приложение использует слоистую архитектуру: Controller → Service → Repository → PostgreSQL. DTO отделяют контракт API от JPA-сущностей, преобразования выполняют мапперы.

## Запуск через Docker Compose

Нужны Docker с поддержкой Compose и доступ к реестрам образов и Maven-зависимостей. Устанавливать Java на хост для этого способа не требуется. Команды выполняются из корня репозитория.

Задайте `DB_PASSWORD` в текущем терминале. Значение ниже — пример, замените его своим локальным паролем.

**PowerShell:**

```powershell
$env:DB_PASSWORD = "replace-with-local-password"
docker compose up --build -d
```

**Bash:**

```bash
export DB_PASSWORD='replace-with-local-password'
docker compose up --build -d
```

Compose запускает приложение и `postgres:18-alpine`. Flyway создаёт схему, Hibernate проверяет её соответствие сущностям. API доступен по адресу `http://localhost:8080`; порт приложения привязан к `127.0.0.1`.

| Адрес | Назначение |
|---|---|
| [Swagger UI](http://localhost:8080/swagger-ui.html) | Просмотр API и отправка запросов |
| [OpenAPI](http://localhost:8080/v3/api-docs) | Спецификация API в JSON |

Просмотр логов и остановка:

```bash
docker compose logs -f app
docker compose down
```

Данные PostgreSQL сохраняются в именованном томе. При повторном запуске с существующим томом используйте прежний пароль БД: изменение переменной не меняет пароль уже созданного пользователя PostgreSQL.

## Запуск из IDE или через Maven

Нужны JDK 21 и работающий PostgreSQL с созданными БД и пользователем. Настройки передаются через переменные окружения:

| Переменная | Значение по умолчанию или требование |
|---|---|
| `DB_URL` | `jdbc:postgresql://127.0.0.1:5432/internship_tracker` |
| `DB_USERNAME` | `internship_tracker_app` |
| `DB_PASSWORD` | Обязательна; пароль пользователя БД |

Из корня проекта:

```bash
./mvnw spring-boot:run
```

В PowerShell используйте `./mvnw.cmd spring-boot:run`. Схему создаёт Flyway; вручную создавать таблицы не нужно.

## Основные endpoints

| Ресурс | Методы и пути |
|---|---|
| Компании | `GET`, `POST /api/companies`; `GET`, `PUT`, `DELETE /api/companies/{id}` |
| Вакансии | `GET`, `POST /api/vacancies`; `GET`, `PUT`, `DELETE /api/vacancies/{id}` |
| Отклики | `GET`, `POST /api/applications`; `GET`, `PUT`, `DELETE /api/applications/{id}` |
| Статус отклика | `PATCH /api/applications/{id}/status` |
| Статистика | `GET /api/applications/statistics` |
| Регистрация и вход | `POST /api/auth/register`, `POST /api/auth/login` |

У списка откликов есть параметры `page`, `size`, `sortBy`, `direction` и фильтры `status`, `vacancyId`, `companyId`, `workFormat`, `appliedAtFrom`, `appliedAtTo`, `nextContactAtFrom`, `nextContactAtTo`. Формат и ограничения описаны в [QA-документации](docs/qa/README.md#контракт-проверяемого-api).

`PUT /api/applications/{id}` обновляет вакансию, даты и заметки; смена статуса выполняется отдельным `PATCH`.

## Тестирование

В проекте есть модульные тесты сервисов и мапперов, тесты контроллеров, тесты репозиториев и интеграционные тесты приложения. Интеграционные проверки используют PostgreSQL `18-alpine` через Testcontainers; HTTP-слой вызывается через MockMvc.

Для полного прогона нужны **JDK 21 и доступный Docker**. Локальный PostgreSQL и запущенный Compose-стенд для этих тестов не требуются: подключение к тестовой БД настраивает `@ServiceConnection`.

**PowerShell:**

```powershell
./mvnw.cmd test
```

**Bash:**

```bash
./mvnw test
```

Текстовые и XML-отчёты Maven появляются в `target/surefire-reports/` после прогона.

Для ручной проверки API подготовлены [чек-лист, тест-кейс и кандидат в баг](docs/qa/README.md). В документах указаны версия исходников, ожидаемые результаты и ссылки на соответствующие задачи Jira. Статусы ручных проверок заполняются по результатам выполнения.

## Документация

| Документ | Содержание |
|---|---|
| [QA-документация](docs/qa/README.md) | Подготовка стенда, контракт API и навигация по проверкам |
| [Чек-лист](docs/qa/checklist.md) | 28 проверок управления откликами и связанного удаления вакансии |
| [Тест-кейс TC-01](docs/qa/test-case-status-transition.md) | Запрет перехода `APPLIED → OFFER` |
| [Кандидат в дефект BUG-01](docs/qa/bug-report-vacancy-deletion.md) | Обработка удаления вакансии со связанным откликом |
| [Состояние проекта](PROJECT_STATUS.md) | Реализованные возможности и следующий этап разработки |
| [Журнал решений](DECISIONS.md) | Технические решения и причины их выбора |
