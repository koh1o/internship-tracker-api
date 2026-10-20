# Тестирование Internship Tracker API

[К описанию проекта](../../README.md)

В этом разделе собрана документация ручного тестирования управления откликами: чек-лист, подробный негативный тест-кейс и кандидат в дефект обработки удаления связанной вакансии. Раздел дополняет существующие автотесты проекта.

| Параметр | Значение |
|---|---|
| Версия исходников | [`f8b9cd13b5c630f4a03f850197df237980ad56ed`](https://github.com/koh1o/internship-tracker-api/commit/f8b9cd13b5c630f4a03f850197df237980ad56ed) |
| Первичная подготовка | 19.10.2026 |
| Адаптация для репозитория | 20.10.2026 |
| Область | `Application` и удаление связанной `Vacancy` |
| Состояние | Ручные проверки не выполнены; кандидат в дефект требует воспроизведения |

Указанный коммит фиксирует основание для ожидаемых результатов. Перед прогоном на другой версии проверьте, не изменились ли DTO, бизнес-правила и обработка ошибок.

## Артефакты

| Артефакт | Документ | Jira | Состояние |
|---|---|---|---|
| Чек-лист | [28 проверок управления откликами](checklist.md) | [ITQA-2](https://koh1o.atlassian.net/browse/ITQA-2) | Не выполнено |
| TC-01 | [Запрет перехода APPLIED → OFFER](test-case-status-transition.md) | [ITQA-1](https://koh1o.atlassian.net/browse/ITQA-1) | Not Run |
| BUG-01 | [Удаление вакансии со связанным откликом](bug-report-vacancy-deletion.md) | [ITQA-3](https://koh1o.atlassian.net/browse/ITQA-3) | Требует воспроизведения |

Jira может требовать входа и разрешения на просмотр проекта. Полное содержание артефактов доступно в этих Markdown-файлах. `ITQA-*` — ключи задач Jira; `CL-*`, `TC-01` и `BUG-01` — идентификаторы проверок и документов.

## Границы проверки

Чек-лист охватывает создание и изменение откликов, обязательные поля, границы длины `notes`, даты, несколько сценариев смены статуса, чтение, фильтрацию по статусу, пагинацию, сортировку, статистику и удаление. Это начальный набор проверок, а не полная регрессия всего API.

Регистрация и вход, CRUD компаний и вакансий в полном объёме, все комбинации фильтров, все переходы статусов, производительность и безопасность не входят в текущий ручной набор. Компания и вакансия создаются как тестовые данные.

## Подготовка стенда

1. Запустите Docker и задайте `DB_PASSWORD` по инструкции в [главном README](../../README.md#запуск-через-docker-compose).
2. Из корня репозитория запустите отдельный Compose-проект:

   ```bash
   docker compose -p internship-tracker-qa up --build -d
   ```

   Имя проекта отделяет контейнеры и именованный том QA-стенда от обычного Compose-запуска. Порт `8080` должен быть свободен; обычный стенд и QA-стенд не запускаются одновременно на этом порту. Повторный запуск с тем же именем использует прежние данные.

3. Откройте [Swagger UI](http://localhost:8080/swagger-ui.html). Базовый URL для запросов — `http://localhost:8080`.
4. Используйте Swagger UI или Postman; для запросов с JSON-телом задавайте `Content-Type: application/json`.
5. Зафиксируйте фактический коммит, ОС, версию PostgreSQL, способ запуска и дату прогона в Jira.

Для остановки QA-стенда используйте `docker compose -p internship-tracker-qa down`. Данные сохраняются. Удалять весь том для выполнения этих проверок не требуется.

На версии-основании Bearer-токен для этих endpoints не нужен: [SecurityConfiguration](../../src/main/java/io/github/koh1o/internshiptrackerapi/configuration/SecurityConfiguration.java) пока разрешает все запросы. После внедрения JWT предусловия нужно обновить.

## Подготовка тестовых данных

Каждый сценарий начинает работу с собственными данными. Для проверок, меняющих или удаляющих записи, не используйте набор другого теста. Значение `1` в JSON ниже — только пример числового ID: перед отправкой заменяйте его на ID из фактического ответа API.

### Компания

`POST /api/companies`

```json
{
  "name": "QA Portfolio Company"
}
```

Ожидается `201 Created`. Сохраните поле `id` как `companyId`.

### Вакансия

`POST /api/vacancies`

Подставьте полученный `companyId` вместо `1`:

```json
{
  "companyId": 1,
  "title": "QA Intern",
  "workFormat": "REMOTE"
}
```

Ожидается `201 Created`. Сохраните поле `id` как `vacancyId`.

### Отклик

`POST /api/applications`

Подставьте полученный `vacancyId` вместо `1`:

```json
{
  "vacancyId": 1,
  "status": "APPLIED",
  "appliedAt": "2026-10-19T10:00:00",
  "nextContactAt": "2026-10-20T10:00:00",
  "notes": "Portfolio status transition test"
}
```

Ожидается `201 Created`. Сохраните поле `id` как `applicationId`. Для сценария с `PLANNED` меняйте статус и даты по условиям проверки. Эти даты — фиксированные тестовые значения; ограничение относительно текущего времени в проверяемой версии не задано.

В путях вида `/api/applications/{applicationId}` заменяйте маркер целиком фактическим числовым ID, без фигурных скобок.

### Очистка

Удаляйте только созданные сценарием записи, начиная с зависимых:

1. `DELETE /api/applications/{applicationId}` — для каждого созданного отклика.
2. `DELETE /api/vacancies/{vacancyId}` — после удаления всех его откликов.
3. `DELETE /api/companies/{companyId}` — после удаления всех её вакансий.

Успешное удаление возвращает `204 No Content`. После воспроизведения BUG-01 сначала сохраните ответы и логи, затем выполните очистку.

## Контракт проверяемого API

| Часть контракта | Правило текущей версии |
|---|---|
| Создание отклика | `vacancyId` и `status` обязательны; `notes` — не более 2000 символов |
| Даты | Для любого статуса кроме `PLANNED` требуется `appliedAt`; если обе даты заданы, `nextContactAt >= appliedAt` |
| Формат даты | ISO local date-time, например `2026-10-19T10:00:00`; поля представлены `LocalDateTime` без смещения часового пояса |
| Изменение данных | `PUT /api/applications/{id}` принимает `vacancyId`, `appliedAt`, `nextContactAt`, `notes`; статус не входит в DTO |
| Изменение статуса | `PATCH /api/applications/{id}/status`, тело `{"status":"..."}` |
| Пагинация | `page >= 0`, `1 <= size <= 100`; по умолчанию `page=0`, `size=10` |
| Сортировка | `createdAt`, `appliedAt`, `nextContactAt`, `status`; направления `ASC`, `DESC`; по умолчанию `createdAt DESC` |
| Фильтры | `status`, `vacancyId`, `companyId`, `workFormat`, диапазоны `appliedAt` и `nextContactAt` |
| Ответ списка | `content`, `page`, `size`, `totalElements`, `totalPages` |
| Статистика | `GET /api/applications/statistics`: `total` и `byStatus`; все семь статусов, включая значения с нулевым количеством |
| Ошибки валидации | `400`, `error="Validation failed"`, `message="Invalid request data"`, ошибки полей в `fieldErrors` |
| Нарушение бизнес-правил | `400`, `error="Bad request"`, причина в `message`, пустой `fieldErrors` |
| Отсутствующая запись | `404`, `error="Not found"`, путь запроса в `path` |

### Переходы статусов

| Исходный статус | Допустимые новые статусы |
|---|---|
| `PLANNED` | `APPLIED`, `WITHDRAWN` |
| `APPLIED` | `TEST_TASK`, `INTERVIEW`, `REJECTED`, `WITHDRAWN` |
| `TEST_TASK` | `INTERVIEW`, `REJECTED`, `WITHDRAWN` |
| `INTERVIEW` | `OFFER`, `REJECTED`, `WITHDRAWN` |
| `OFFER`, `REJECTED`, `WITHDRAWN` | Переход в другой статус запрещён |

Повторная установка текущего статуса возвращает `200` без изменения данных. При переходе из `PLANNED` в любой другой допустимый статус `appliedAt` должен быть уже заполнен; `PATCH` меняет только статус.

## Порядок прогона и фиксация результатов

1. Выполните [TC-01](test-case-status-transition.md) и сохраните запросы и ответы для каждого шага.
2. Пройдите [чек-лист](checklist.md), создавая дополнительные данные под условия строк.
3. Отдельно проверьте [BUG-01](bug-report-vacancy-deletion.md). Ожидаемый контракт удаления зависимой вакансии нужно согласовать до оценки `Pass/Fail` для CL-28.
4. В Jira укажите результат, коммит, дату, окружение и доказательства. При изменении документации обновите соответствующий Markdown-файл; если результаты ведутся в Jira, добавьте сюда дату прогона и ссылку на запись с результатами.

| Статус проверки | Значение |
|---|---|
| Not Run | Проверка не выполнялась |
| Pass | Все ожидаемые результаты подтверждены, включая сохранённое состояние |
| Fail | Есть подтверждённое расхождение с согласованным ожиданием |
| Blocked | Проверку нельзя завершить из-за стенда, данных или неуточнённого требования; причина записана |

Статус задачи Jira «Готово» сам по себе не является результатом тестирования. Для строки с несколькими значениями фиксируйте отдельный результат каждого запроса; строка получает `Pass` только после всех её проверок.

## Связь с автотестами

| Ручной сценарий | Существующий автотест |
|---|---|
| Недопустимый переход `APPLIED → OFFER`, CL-16 / TC-01 | `ApplicationIntegrationTest.shouldRejectInvalidStatusTransition` |
| `APPLIED` без даты, CL-06 | `ApplicationIntegrationTest.shouldRejectAppliedApplicationWithoutAppliedAt` |
| Дата контакта раньше даты отклика, CL-07 | `ApplicationIntegrationTest.shouldRejectApplicationWhenNextContactAtIsBeforeAppliedAt` |
| Повторная установка статуса, CL-17 | `ApplicationIntegrationTest.shouldAllowSettingSameApplicationStatus` |
| Удаление вакансии без откликов, контроль для BUG-01 | `VacancyIntegrationTest.shouldDeleteVacancy` |

Исходники: [ApplicationIntegrationTest](../../src/test/java/io/github/koh1o/internshiptrackerapi/integration/ApplicationIntegrationTest.java), [VacancyIntegrationTest](../../src/test/java/io/github/koh1o/internshiptrackerapi/integration/VacancyIntegrationTest.java). Наличие метода в коде не подтверждает успешный прогон на конкретном окружении.

Основания контракта: [ApplicationController](../../src/main/java/io/github/koh1o/internshiptrackerapi/controller/ApplicationController.java), [ApplicationService](../../src/main/java/io/github/koh1o/internshiptrackerapi/service/ApplicationService.java), [DTO отклика](../../src/main/java/io/github/koh1o/internshiptrackerapi/dto/application/), [GlobalExceptionHandler](../../src/main/java/io/github/koh1o/internshiptrackerapi/exception/GlobalExceptionHandler.java), [состояние проекта](../../PROJECT_STATUS.md).
