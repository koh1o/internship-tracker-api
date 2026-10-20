# TC-01 Запрет перехода отклика из APPLIED в OFFER

[QA-документация](README.md) · [Чек-лист](checklist.md) · [Jira ITQA-1](https://koh1o.atlassian.net/browse/ITQA-1)

## Назначение

Проверить, что API отклоняет прямой переход `APPLIED → OFFER` с предусмотренной бизнес-ошибкой, а состояние отклика в БД не меняется.

| Поле | Значение |
|---|---|
| Идентификатор | TC-01 |
| Тип | Функциональный, негативный, API |
| Приоритет тест-кейса | Высокий: проверка бизнес-правила жизненного цикла отклика |
| Endpoint | `PATCH /api/applications/{applicationId}/status` |
| Версия-основание | `f8b9cd13b5c630f4a03f850197df237980ad56ed` |
| Проверка чек-листа | CL-16 |
| Результат прогона | Not Run |

## Основание

В [ApplicationService](../../src/main/java/io/github/koh1o/internshiptrackerapi/service/ApplicationService.java) из `APPLIED` разрешены `TEST_TASK`, `INTERVIEW`, `REJECTED`, `WITHDRAWN`. Переход в `OFFER` допустим из `INTERVIEW`. Недопустимый переход вызывает `InvalidApplicationDataException`, который [GlobalExceptionHandler](../../src/main/java/io/github/koh1o/internshiptrackerapi/exception/GlobalExceptionHandler.java) преобразует в `400 Bad Request`.

## Предусловия

1. На отдельном стенде запущено приложение проверяемой версии. Базовый URL — `http://localhost:8080`.
2. По [инструкции подготовки данных](README.md#подготовка-тестовых-данных) создана компания `QA Portfolio Company` и её вакансия `QA Intern` с форматом `REMOTE`. Оба `POST` вернули `201`.
3. Сохранены фактические числовые `companyId` и `vacancyId`. В JSON ниже `1` заменить на полученный `vacancyId`; реальные ID не обязаны равняться `1`.
4. Для JSON-запросов установлен `Content-Type: application/json`. На версии-основании авторизация для этого API не требуется.
5. Во время сценария другие запросы не меняют созданные записи.

## Тестовые данные

Тело создания отклика для шага 1:

```json
{
  "vacancyId": 1,
  "status": "APPLIED",
  "appliedAt": "2026-10-19T10:00:00",
  "nextContactAt": "2026-10-20T10:00:00",
  "notes": "Portfolio status transition test"
}
```

Тело изменения статуса для шага 3:

```json
{
  "status": "OFFER"
}
```

## Шаги

| № | Действие | Ожидаемый результат |
|---|---|---|
| 1 | Отправить `POST /api/applications` с телом создания, подставив фактический `vacancyId`. Сохранить `id` из ответа как `applicationId` | `201 Created`; `id` заполнен; `status=APPLIED`; `vacancyId`, даты и `notes` соответствуют запросу; `companyId`, `companyName`, `vacancyTitle` соответствуют подготовленным данным |
| 2 | Выполнить `GET /api/applications/{applicationId}`. Сохранить HTTP-код и полное тело ответа как исходное состояние | `200 OK`; возвращён созданный отклик с `APPLIED`; поля совпадают с результатом шага 1 |
| 3 | Отправить `PATCH /api/applications/{applicationId}/status` с `{"status":"OFFER"}`. Сохранить ответ | `400 Bad Request`; тело соответствует ожидаемой ошибке ниже; причина отказа — именно недопустимый переход |
| 4 | Повторить `GET /api/applications/{applicationId}` и сравнить с ответом шага 2 | `200 OK`; `status=APPLIED`; `id`, `vacancyId`, `vacancyTitle`, `companyId`, `companyName`, `appliedAt`, `nextContactAt`, `notes`, `createdAt`, `updatedAt` не изменились |

В каждом URL маркер `{applicationId}` заменить целиком фактическим числовым ID, без фигурных скобок.

### Ожидаемая ошибка на шаге 3

Пример для `applicationId=1`. Значение `path` должно содержать фактический ID из шага 1:

```json
{
  "status": 400,
  "error": "Bad request",
  "message": "Cannot change status from APPLIED to OFFER",
  "path": "/api/applications/1/status",
  "fieldErrors": {}
}
```

## Критерий прохождения

Все четыре шага дали ожидаемые результаты. API вернул предусмотренную причину отказа, а повторное чтение подтвердило неизменность сохранённых данных. Одного ответа `400` недостаточно: ошибка валидации тела или отсутствующий ID не подтверждают нужное бизнес-правило.

Даты сравниваются по значению времени; порядок JSON-ключей не учитывается. Если шаг создания или предусловие не выполнены, фиксируется причина блокировки сценария, а не результат проверки перехода.

## Постусловия

После сохранения доказательств удалить созданный отклик, затем вакансию, затем компанию:

1. `DELETE /api/applications/{applicationId}` → `204`.
2. `DELETE /api/vacancies/{vacancyId}` → `204`.
3. `DELETE /api/companies/{companyId}` → `204`.

Использовать только ID данных этого сценария. Существующие данные других проверок не удалять.

## Связанный автотест

В [ApplicationIntegrationTest](../../src/test/java/io/github/koh1o/internshiptrackerapi/integration/ApplicationIntegrationTest.java) есть метод `shouldRejectInvalidStatusTransition`: он отправляет `APPLIED → OFFER`, ожидает `400` и проверяет прежний статус через репозиторий. Используются MockMvc и PostgreSQL Testcontainers.

Ручной тест-кейс дополнительно задаёт проверку полного тела ошибки и сравнение полей ответа до и после отказа. Наличие автотеста не означает, что ручной сценарий уже выполнен.

Для отдельного запуска автотеста из корня проекта с JDK 21 и доступным Docker:

**PowerShell:**

```powershell
./mvnw.cmd "-Dtest=ApplicationIntegrationTest#shouldRejectInvalidStatusTransition" test
```

**Bash:**

```bash
./mvnw '-Dtest=ApplicationIntegrationTest#shouldRejectInvalidStatusTransition' test
```

## Результат ручного прогона

| Поле | Значение |
|---|---|
| Результат | Not Run |
| Фактический результат | Не заполнен: ручной прогон не выполнялся |
| Дата и исполнитель | Заполнить после выполнения |
| Коммит и окружение стенда | Зафиксировать при прогоне |
| Доказательства | Добавить запросы и ответы шагов 1–4 в Jira |
| Связанный дефект | Добавить при обнаружении расхождения |
