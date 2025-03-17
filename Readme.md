
# TaskManagement

## Описание
TaskManagement — это REST API для управления задачами, разработанное с использованием Java и Spring Boot. Система позволяет пользователям создавать, редактировать, удалять и просматривать задачи, а также добавлять к ним комментарии. Каждая задача содержит заголовок, описание, статус (например, "в ожидании", "в процессе", "завершено"), приоритет (например, "высокий", "средний", "низкий"), автора и исполнителя. Реализована аутентификация и авторизация с ролями (USER и ADMIN).

Проект предназначен для разработчиков, которым нужна простая и гибкая система управления задачами с возможностью интеграции в более крупные приложения.

## Основные возможности
- Регистрация и аутентификация пользователей (JWT-токены).
- Создание, редактирование, удаление и фильтрация задач.
- Назначение задач исполнителям и изменение их статуса.
- Добавление и просмотр комментариев к задачам.
- Ролевая модель: USER (базовые права) и ADMIN (полный доступ).

## Требования
- **Java**: 17
- **Maven**: 3.8.1 или новее
- **PostgreSQL**: для хранения данных (или H2 для тестирования)
- **Docker**: для запуска контейнеров (опционально)

## Зависимости
- Spring Boot 3.4.2
- Spring Data JPA (PostgreSQL, H2)
- Spring Security (JWT-аутентификация)
- Springdoc OpenAPI (Swagger UI)
- Lombok 1.18.30
- JUnit 5 и Mockito (для тестов)

Полный список зависимостей указан в `pom.xml`.

## Установка и запуск

### Локальный запуск
1. Склонируйте репозиторий:
   ```bash
   git clone <URL репозитория>
   ```
2. Настройте базу данных PostgreSQL:
    - Создайте базу данных с именем `users`.
    - Убедитесь, что PostgreSQL работает на порту `5432` (или измените настройки в `application.yml`).
3. Настройте файл `src/main/resources/application.yml`:
   ```yaml
   spring:
     application:
       name: User-Service
     datasource:
       driver-class-name: org.postgresql.Driver
       url: jdbc:postgresql://localhost:5432/users
       username: postgres
       password: postgres
     jpa:
       hibernate:
         ddl-auto: update
       properties:
         hibernate:
           dialect: org.hibernate.dialect.PostgreSQLDialect
       show-sql: true
   server:
     port: 9001
   ```
4. Соберите и запустите проект

   в папке где находиться pom.xml напишите:
   ```bash
   mvn clean install
   ```

### Запуск с Docker
1. Убедитесь, что Docker и Docker Compose установлены.
2. Перейдите в корневую директорию проекта, где находится `docker-compose.yml`:
   ```yaml
   services:
     postgres_bd:
       image: 'postgres:latest'
       environment:
         - 'POSTGRES_DB=users'
         - 'POSTGRES_PASSWORD=postgres'
         - 'POSTGRES_USER=postgres'
       ports:
         - '5432:5432'
     user-service:
       environment:
         SPRING_DATASOURCE_URL: jdbc:postgresql://postgres_bd:5432/users
         SPRING_DATASOURCE_USERNAME: postgres
         SPRING_DATASOURCE_PASSWORD: postgres
       build:
         context: ./User-Service
         dockerfile: Dockerfile
       ports:
         - "8001:9001"
       depends_on:
         - postgres_bd
   networks:
     crud-network:
   ```
3. Запустите контейнеры:
   ```bash
   docker-compose up --build
   ```

Приложение будет доступно по адресу `http://localhost:8001`.

## API Эндпоинты
Документация API доступна через Swagger UI по адресу: `http://localhost:8001/swagger-ui.html`.

### Аутентификация
- `POST /api/auth/register` — регистрация пользователя.
    - Тело запроса: `{ "email": "user@example.com", "password": "pass", "role": "USER" }`
    - Ответ: JWT-токен.
- `POST /api/auth/login` — вход в систему.
    - Тело запроса: `{ "email": "user@example.com", "password": "pass" }`
    - Ответ: JWT-токен.

### Задачи
- `POST /api/tasks/create` — создание задачи.
    - Тело запроса: `{ "title": "Task", "description": "Details", "status": "TODO", "priority": "HIGH" }`
- `GET /api/tasks/filter` — фильтрация задач (параметры: `status`, `priority`, `page`, `size`).
- `PATCH /api/tasks/{taskId}/take` — взятие задачи в работу.
- `PATCH /api/tasks/{taskId}/complete` — завершение задачи.
- `PUT /api/tasks/{taskId}/edit` — редактирование задачи.
- Тело запроса:  `{ "title": "New Title", "description": "New Details", "status": "IN_PROGRESS", "priority": "MEDIUM" }`
  Ограничение: только ADMIN или назначенный пользователь.
- `PATCH /api/tasks/{taskId}/assign` — назначение задачи пользователю.
- Параметры: `assigneeEmail=user@example.com`
  Ограничение: только ADMIN.
- `DELETE /api/tasks/{taskId}` — удаление задачи.
  Ограничение: только ADMIN.

### Комментарии
- `POST /api/comments/{taskId}` — добавление комментария.
    - Тело запроса: `{ "text": "Comment text" }`
- `GET /api/comments/{taskId}` — получение комментариев по задаче.

### Пользователи
- `GET /api/users/getByEmail?email=user@example.com` — получение пользователя по email.
- `PATCH /api/users/{id}` — обновление данных пользователя.

## Тестирование
Проект включает юнит-тесты для сервисов (`AuthService`, `TaskService`, `CommentService`, `UserService`, `JwtService`) с использованием JUnit 5 и Mockito. Для запуска тестов:
```bash
mvn test
```

## Примечания
- Для доступа к защищённым эндпоинтам добавьте заголовок `Authorization: Bearer <токен>` в запросы.
- Роли: `USER` — базовый доступ, `ADMIN` — полный контроль.
# TaskManagement
