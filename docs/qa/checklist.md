# Чек-лист управления откликами

[QA-документация](README.md) · [Проект](../../README.md) · [Jira ITQA-2](https://koh1o.atlassian.net/browse/ITQA-2)

**Область:** управление `Application` и удаление связанной вакансии.

**Версия-основание:** `f8b9cd13b5c630f4a03f850197df237980ad56ed`.

**Состояние:** все проверки `Not Run`; ручной прогон не выполнялся.

## Условия выполнения

- Подготовить отдельный стенд, компанию и вакансию по [инструкции](README.md#подготовка-стенда).
- Отправлять запросы относительно `http://localhost:8080`; для JSON задавать `Content-Type: application/json`.
- Подставлять фактические числовые ID из ответов API. `{id}` в таблице — ID отклика; для удаления вакансии отдельно указан `{vacancyId}`.
- Для каждого сценария использовать новый набор данных либо явно возвращать его к указанному исходному состоянию. Независимые проверки не должны зависеть от результата предыдущей строки.
- Во всех негативных запросах остальные поля должны быть корректны, чтобы ошибка возникала по проверяемой причине.
- Значения и ограничения API приведены в [контракте](README.md#контракт-проверяемого-api). Если в строке несколько значений, выполнить отдельный запрос для каждого.

## Проверки

| ID | Проверка и исходные условия | Ожидаемый результат | Статус |
|---|---|---|---|
| CL-01 | `POST /api/applications`: существующий `vacancyId`, `status=PLANNED`, даты отсутствуют | `201`; заполнен `id`, возвращены правильный `vacancyId` и `PLANNED`; даты `null`; последующий `GET` подтверждает сохранение | Not Run |
| CL-02 | `POST /api/applications`: `status=APPLIED`, `appliedAt=2026-10-19T10:00:00` | `201`; статус и дата сохранены; последующий `GET` возвращает те же значения | Not Run |
| CL-03 | `POST /api/applications`: не передавать `vacancyId`; отдельным запросом передать `vacancyId=null` | В обоих случаях `400`; `fieldErrors.vacancyId="Vacancy id is required"`; число откликов не изменилось | Not Run |
| CL-04 | `POST /api/applications`: не передавать `status`; отдельным запросом передать `status=null` | В обоих случаях `400`; `fieldErrors.status="Status is required"`; отклик не создан | Not Run |
| CL-05 | `POST /api/applications`: передать числовой `vacancyId`, отсутствие которого предварительно подтверждено через `GET /api/vacancies/{vacancyId}` с ответом `404` | `404`; `message` сообщает `Vacancy not found with id: <vacancyId>` с фактическим ID; отклик не создан | Not Run |
| CL-06 | `POST /api/applications`: `status=APPLIED`, `appliedAt=null` | `400`; `message="Applied date is required for status APPLIED"`; отклик не создан | Not Run |
| CL-07 | `POST /api/applications`: `status=APPLIED`, `appliedAt=2026-10-19T10:00:00`, `nextContactAt=2026-10-19T09:59:59` | `400`; `message="Next contact date must not be before applied date"`; отклик не создан | Not Run |
| CL-08 | `POST /api/applications`: `status=APPLIED`, обе даты `2026-10-19T10:00:00` | `201`; обе даты сохранены с одинаковым значением | Not Run |
| CL-09 | `POST /api/applications`: корректный `PLANNED`; `notes` из 1999 символов `a`, затем отдельный запрос с 2000 символами `a` | Для каждой длины `201`; последующий `GET` подтверждает точное содержимое и длину `notes` | Not Run |
| CL-10 | `POST /api/applications`: корректный `PLANNED`, `notes` из 2001 символа `a` | `400`; `fieldErrors.notes="Notes must not exceed 2000 characters"`; отклик не создан | Not Run |
| CL-11 | `GET /api/applications/{id}` для существующего отклика | `200`; ID, связанная вакансия, статус, даты и заметки совпадают с сохранёнными; сведения о компании соответствуют вакансии | Not Run |
| CL-12 | `GET /api/applications/{id}` для заведомо отсутствующего отклика | `404`; `error="Not found"`; `message` сообщает об отсутствующем ID; `path` совпадает с путём запроса | Not Run |
| CL-13 | `PUT /api/applications/{id}` для существующего `APPLIED`: передать существующий `vacancyId`, новые `notes`, `appliedAt=2026-10-19T11:00:00`, `nextContactAt=2026-10-20T11:00:00` | `200`; повторный `GET` подтверждает новые значения; статус остаётся `APPLIED` | Not Run |
| CL-14 | `PUT /api/applications/{id}` для `APPLIED`: корректные `vacancyId`, `notes`, `nextContactAt`, но `appliedAt=null` | `400`; `message="Applied date is required for status APPLIED"`; повторный `GET` совпадает с состоянием до запроса | Not Run |
| CL-15 | Подготовить `PLANNED` с заполненным `appliedAt`; `PATCH /api/applications/{id}/status` с `{"status":"APPLIED"}` | `200`; повторный `GET` возвращает `APPLIED`; `vacancyId`, даты и заметки сохранены | Not Run |
| CL-16 | Подготовить `APPLIED`; `PATCH /api/applications/{id}/status` с `{"status":"OFFER"}`. Подробные шаги: [TC-01](test-case-status-transition.md) | `400`; `message="Cannot change status from APPLIED to OFFER"`; повторный `GET` подтверждает прежний статус и сохранность полей | Not Run |
| CL-17 | Подготовить `APPLIED`; повторно отправить `{"status":"APPLIED"}` в `PATCH /api/applications/{id}/status` | `200`; статус и остальные поля ответа совпадают с состоянием до запроса, включая `updatedAt` | Not Run |
| CL-18 | `PATCH /api/applications/{id}/status` для существующего отклика: отправить `{}`, затем отдельным запросом `{"status":null}` | В обоих случаях `400`; `fieldErrors.status="Status is required"`; прежнее состояние сохранено | Not Run |
| CL-19 | На изолированном наборе из 4 откликов — 2 `APPLIED`, 1 `PLANNED`, 1 `INTERVIEW` — выполнить `GET /api/applications?status=APPLIED&page=0&size=10` | `200`; `content` содержит ровно 2 нужных отклика, оба с `APPLIED`; `totalElements=2` | Not Run |
| CL-20 | На отдельном наборе ровно из 4 откликов выполнить `GET /api/applications?page=0&size=1` без фильтров | `200`; в `content` один элемент; `page=0`, `size=1`, `totalElements=4`, `totalPages=4` | Not Run |
| CL-21 | Три независимых запроса списка: `?page=-1&size=10`; `?page=0&size=0`; `?page=0&size=101` | Каждый запрос возвращает `400`; корректные остальные параметры не маскируют проверяемое ограничение | Not Run |
| CL-22 | Подготовить минимум 3 отклика с различающимися `createdAt`; `GET /api/applications?sortBy=createdAt&direction=ASC&page=0&size=100` | `200`; все подготовленные записи присутствуют на странице, значения `createdAt` идут по возрастанию | Not Run |
| CL-23 | Отдельные запросы списка: `?sortBy=unknown&direction=ASC` и `?sortBy=createdAt&direction=SIDEWAYS` | Каждый возвращает `400`; `message` соответственно `Unsupported sort field: unknown` и `Unsupported sort direction: SIDEWAYS` | Not Run |
| CL-24 | `GET /api/applications` с `appliedAtFrom=2026-10-20T10:00:00` и `appliedAtTo=2026-10-19T10:00:00` | `400`; `message="Applied at from must not be after applied at to"` | Not Run |
| CL-25 | На изолированном наборе 2 `APPLIED`, 1 `PLANNED`, 1 `INTERVIEW` выполнить `GET /api/applications/statistics` | `200`; `total=4`; `byStatus`: `APPLIED=2`, `PLANNED=1`, `INTERVIEW=1`, остальные 4 статуса имеют 0; сумма значений равна 4 | Not Run |
| CL-26 | `DELETE /api/applications/{id}` для существующего отклика; сохранить ID его вакансии | `204`, тело пустое; последующий `GET` отклика возвращает `404`; `GET` вакансии — `200`, её данные сохранены | Not Run |
| CL-27 | Создать и удалить отдельный отклик; повторить `DELETE /api/applications/{id}` с тем же ID | `404`; `message` сообщает об отсутствующем отклике; другие подготовленные записи не изменены | Not Run |
| CL-28 | `DELETE /api/vacancies/{vacancyId}` для вакансии с существующим откликом; проверить обе записи. См. [BUG-01](bug-report-vacancy-deletion.md) | Предлагаемое поведение: управляемый конфликт `409` в формате `ErrorResponse`, обе записи сохранены. Контракт требует согласования; до этого фиксировать наблюдения без оценки `Pass/Fail` | Not Run |

## Контроль данных и результатов

Для CL-19, CL-20 и CL-25 набор должен быть единственным набором откликов на стенде. При создании `INTERVIEW` передайте `appliedAt`: API позволяет создать отклик сразу с этим статусом. Для CL-22 число откликов на стенде не должно превышать выбранный `size`; предварительно убедитесь, что их `createdAt` различаются.

Перед негативным `POST` и после него сравнивайте `GET /api/applications/statistics` при отсутствии параллельных изменений. Для негативных `PUT` и `PATCH` сравнивайте ответ `GET` по ID до и после запроса. Проверяйте не только HTTP-код, но и нужную причину отказа, поля ошибки и сохранность данных.

Для дат сравнивайте значение времени, а не только строку: сериализация может добавлять доли секунды. Внутри ответа `fieldErrors` и `byStatus` порядок JSON-ключей несущественен.

После выполнения укажите в Jira результат каждого запроса, фактическую версию и окружение. Для `Fail` приложите запрос, ответ и ссылку на дефект; для `Blocked` укажите причину. Правила статусов и очистка приведены в [QA README](README.md).

## Основания проверок

- [ApplicationController](../../src/main/java/io/github/koh1o/internshiptrackerapi/controller/ApplicationController.java) — маршруты, коды и ограничения параметров списка.
- [ApplicationService](../../src/main/java/io/github/koh1o/internshiptrackerapi/service/ApplicationService.java) — даты, переходы, сортировка, диапазоны и статистика.
- [ApplicationRequest](../../src/main/java/io/github/koh1o/internshiptrackerapi/dto/application/ApplicationRequest.java), [ApplicationUpdateRequest](../../src/main/java/io/github/koh1o/internshiptrackerapi/dto/application/ApplicationUpdateRequest.java), [ApplicationStatusUpdateRequest](../../src/main/java/io/github/koh1o/internshiptrackerapi/dto/application/ApplicationStatusUpdateRequest.java) — валидация тела.
- [GlobalExceptionHandler](../../src/main/java/io/github/koh1o/internshiptrackerapi/exception/GlobalExceptionHandler.java) — формат предусмотренных ошибок.
