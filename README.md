# Clever-Bank Application

#### Автор: Харлап Диана
#### email: dianaharlap19@gmail.com
## Описание проекта

Проект представляет собой сервер и клиент.

Клиентами являются консольные приложения, взаимодействующие с сервером по протоколу HTTP. Для обращения к серверу клиент отправляет GET и POST HTTP-запросы используя библиотеку Unirest.

Для обработки HTTP-запросов и управления приложением сервер использует HTTP-сервлеты, что позволяет обрабатывать множество входящих запросов одновременно. Для развёртывания сервера был использован контейнер сервлетов Apache Tomcat (как локально, так и в Docker-контейнере).

Для хранения данных была развёрнута СУБД PostgreSQL (как локально, так и в контейнере). Для обеспечения целостности данных (при начислениях процентов, переводах, пополнениях и снятиях) были использованы транзакции.

## Реализованный функционал

Пункты из тестового задания 1,2,3,4,5,6,7,8,9,10,11(частично),12,13,14,15

Примеры чеков, выписок и отчётов по движениям на счетах лежат в папках "client/check", "client/account-statement", "client/money-statement
## Инструкция по запуску сервера и клиента

#### Запуск клиенсткого приложения

Для запуска клиенсткого приложения использовать команду^
```
java -jar CleverBankClient-1.0-SNAPSHOT.jar
```

#### Запуск сервера и базы данных в Docker-контейнерах

Для быстрого запуска проекта можно использовать Docker. В папке server описан Docker-образ сервера и файл docker-compose.yaml, который описывает два сервиса (наш проект и базу данных) для быстрого развёртывания. Чтобы запустить всю необходимую инфраструктуру, необходимо:

1. Убедиться, что на компьютере установлен Docker
2. Из корня проекта перейти в папку "server"
3. В папке "server" выполнить команду:
   ```
   docker compose build && docker compose up
   ```
4. После выполненной команды контейнер сервера будет доступен по адресу localhost:8080, контейнер базы данных на порту 5433.

#### Локальный запуск сервера

Другим вариантом запуска проекта является локальное развёртывания. Для этого необходимо иметь установленную на компьютер СУБД PostgreSQL и контейнер сервлетов Apache Tomcat.

1. Получить доступ к СУБД, создать пустую базу данных "dbCleverBank"
2. Используя SQL-скрипт generation.sql, который находится в папке "server/src/main/resources", инициализировать структуру базы данных.
3. Подготовить к работе контейнер сервлетов Apache Tomcat.
4. Находясь в папке "server" выполнить команду "./gradlew war".
5. Из папки "server/build/libs" скопировать war-архив.
6. Переименовать архив в ROOT.war
7. Открыть корневую папку Tomcat, перейти в папку "webapps", вставить архив ROOT.war
8. Перейти в папку "bin" корневой папки Tomcat, используя скрипт startup.sh запустить сервер
9. После выполненных действий сервер будет доступен по адресу localhost:8080.

## Пример CRUD-операций

#### Операции над User

1. Получить информацию о пользователе

Пример запроса:
```http request
GET /api/v1/crud/users/{userId}
```

Пример ответа:
```json
{
    "id": 1,
    "firstName": "Kirill",
    "lastName": "Sotnikov",
    "patronymic": "Artemovich",
    "login": "kiril"
}
```

2. Создать запись о пользователе

Пример запроса:
```http request
POST /api/v1/crud/users
```

Пример тела запроса:
```json
{
    "firstName": "Kirill",
    "lastName": "Sotnikov",
    "patronymic": "Artemovich",
    "login": "kiril"
}
```

Пример ответа:
```json
{
    "id": 1,
    "firstName": "Kirill",
    "lastName": "Sotnikov",
    "patronymic": "Artemovich",
    "login": "kiril"
}
```
3. Обновить запись о пользователе

Пример запроса:
```http request
PUT /api/v1/crud/users/{userId}
```

Пример тела запроса:
```json
{
    "firstName": "Kirill",
    "lastName": "Sotnikov",
    "patronymic": "Nikolaevich",
    "login": "kiril"
}
```

Пример ответа:
```json
{
    "id": 1,
    "firstName": "Kirill",
    "lastName": "Sotnikov",
    "patronymic": "Nikolaevich",
    "login": "kiril"
}
```

4. Удалить запись о пользователе

Пример запроса:
```http request
DELETE /api/v1/crud/users/{userId}
```

#### Операции над Bank

1. Получить информацию о банке

Пример запроса:
```http request
GET /api/v1/crud/banks/{bankId}
```

Пример ответа:
```json
{
   "id": 1,
   "name": "Clever-Bank"
}
```

2. Создать запись о банке

Пример запроса:
```http request
POST /api/v1/crud/banks
```

Пример тела запроса:
```json
{
   "name": "Bank"
}
```

Пример ответа:
```json
{
   "id": 4,
   "name": "Bank"
}
```
3. Обновить запись о банке

Пример запроса:
```http request
PUT /api/v1/crud/banks/{bankId}
```

Пример тела запроса:
```json
{
   "name": "BankA"
}
```

Пример ответа:
```json
{
   "id": 1,
   "name": "BankA"
}
```

4. Удалить запись о банке

Пример запроса:
```http request
DELETE /api/v1/crud/banks/{bankId}


```

#### Операции над Account

1. Получить информацию об аккаунте

Пример запроса:
```http request
GET /api/v1/crud/accounts/{accountId}
```

Пример ответа:
```json
{
   "id": 1,
   "userId": 1,
   "bankId": 2,
   "balance": 1051.00,
   "currency": "BYN",
   "number": "1234567890",
   "createdDate": 1692738000000
}
```

2. Создать запись об аккаунте

Пример запроса:
```http request
POST /api/v1/crud/accounts
```

Пример тела запроса:
```json
{
   "userId": 1,
   "bankId": 2,
   "balance": 2.00,
   "currency": "BYN",
   "number": "1234567897",
   "createdDate": 1692738000000
}
```

Пример ответа:
```json
{
   "id": 4,
   "userId": 1,
   "bankId": 2,
   "balance": 2.00,
   "currency": "BYN",
   "number": "1234567897",
   "createdDate": 1692738000000
}
```
3. Обновить запись об аккаунте

Пример запроса:
```http request
PUT /api/v1/crud/accounts/{accountId}
```

Пример тела запроса:
```json
{
   "userId": 1,
   "bankId": 2,
   "balance": 2.00,
   "currency": "BYN",
   "number": "1234567897",
   "createdDate": 1692738000000
}
```

Пример ответа:
```json
{
   "id": 4,
   "userId": 1,
   "bankId": 2,
   "balance": 2000.00,
   "currency": "BYN",
   "number": "1234567897",
   "createdDate": 1692738000000
}
```

4. Удалить запись об аккаунте

Пример запроса:
```http request
DELETE /api/v1/crud/accounts/{accountId}
```


#### Операции над Transaction

1. Получить информацию о транзакции

Пример запроса:
```http request
GET /api/v1/crud/transactions/{transactionId}
```

Пример ответа:
```json
{
   "id": 74,
   "senderAccount": {
      "id": 1,
      "userId": 1,
      "bankId": 2,
      "balance": 1051.00,
      "currency": "BYN",
      "number": "1234567890",
      "createdDate": 1692738000000
   },
   "recipientAccount": null,
   "amount": 20.00,
   "time": "19:12:37",
   "date": 1693688400000,
   "type": "DEPOSIT"
}
```

2. Создать запись о транзакции

Пример запроса:
```http request
POST /api/v1/crud/transactions
```

Пример тела запроса:
```json
{
   "senderAccount": {
      "id": 1,
      "userId": 1,
      "bankId": 2,
      "balance": 1051.00,
      "currency": "BYN",
      "number": "1234567890",
      "createdDate": 1692738000000
   },
   "recipientAccount": {
      "id": 2,
      "userId": 2,
      "bankId": 1,
      "balance": 10.00,
      "currency": "BYN",
      "number": "0123456789",
      "createdDate": 1692738000000
   },
   "amount": 100.00,
   "time": "21:12:37",
   "date": 1693688400000,
   "type": "TRANSFER"
}
```

Пример ответа:
```json
{
   "id": 77,
   "senderAccount": {
      "id": 1,
      "userId": 1,
      "bankId": 2,
      "balance": 1051.00,
      "currency": "BYN",
      "number": "1234567890",
      "createdDate": 1692738000000
   },
   "recipientAccount": {
      "id": 2,
      "userId": 2,
      "bankId": 1,
      "balance": 10.00,
      "currency": "BYN",
      "number": "0123456789",
      "createdDate": 1692738000000
   },
   "amount": 100.00,
   "time": "21:12:37",
   "date": 1693688400000,
   "type": "TRANSFER"
}
```
3. Обновить запись о транзакции

Пример запроса:
```http request
PUT /api/v1/crud/transactions/{transactionId}
```

Пример тела запроса:
```json
{
   "senderAccount": {
      "id": 1,
      "userId": 1,
      "bankId": 2,
      "balance": 1051.00,
      "currency": "BYN",
      "number": "1234567890",
      "createdDate": 1692738000000
   },
   "recipientAccount": null,
   "amount": 100.00,
   "time": "19:12:37",
   "date": 1693688400000,
   "type": "DEPOSIT"
}
```

Пример ответа:
```json
{
   "id": 74,
   "senderAccount": {
      "id": 1,
      "userId": 1,
      "bankId": 2,
      "balance": 1051.00,
      "currency": "BYN",
      "number": "1234567890",
      "createdDate": 1692738000000
   },
   "recipientAccount": null,
   "amount": 100.00,
   "time": "19:12:37",
   "date": 1693688400000,
   "type": "DEPOSIT"
}
```

4. Удалить запись о транзакции

Пример запроса:
```http request
DELETE /api/v1/crud/transactions/{transactionId}
```