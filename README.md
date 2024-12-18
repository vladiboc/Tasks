# Проект Tasks<br>

<font size='4em'>
    (Веб-приложение с асинхронным взаимодействием как с пользователем, таки и с БД Mongo)
</font>

## Описание проекта
Проект состоит из двух компонент:  
- **Mongo** - NoSQL СУБД, запускаемая докер-композицией
- **Tasks** - асинхронное приложение Spring Boot, CRUD сервис управления пользователями и их задачами 

## Стек технологий
- Java 17
- SpringFramework Boot 3.3.1
- Gradle 8.8
- Mongo 6.0.8
- Docker 24.0.7
- Docker Compose 2.20.2

## Инструкция по локальному запуску приложения

**I Клонирование репозитория**  
Скачайте (склонируйте) проект, перейдите в директорию проекта:  
```$ git clone https://github.com/vladiboc/Tasks.git; cd Tasks```

**II Сборка Jar-файла проекта**  
В директории проекта запустите команду:  
```$ ./gradlew bootJar```  
Это соберет (построит) Jar-файл сервиса Tasks в поддиректории `build/libs`.  

**III Запуск Mongo**  
Перейдите в поддиректорию `docker-mongo`, запустите в ней докер-композицию Mongo:  
```$ cd docker-mongo; docker compose up```  

**IV Запуск сервиса Tasks**  
Откройте еще одно окно терминала. В директории проекта выполните команду:  
```$ java -jar build/libs/tasks-0.0.1.jar```
OpenApi сервиса будет доступно по URL:  
http://localhost:8080/swagger

## Работа приложения
Работа с CRUD-операциями сервиса Tasks описана в API сервиса Tasks по адресу:  
http://localhost:8080/swagger  
