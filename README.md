# NewsManagement

Java 11, Gradle, Spring boot, Open API 3, Cache, PostgreSQL, Lombok, unit and integration tests.

Система управления новостями.

- Основые API:
  -	CRUD для работы с новостями
  
    get /api/v1/news (с пагинацией)
    
    get /api/v1/news/{idNews}
    
    get /api/v1/news/{idNews}/comments (с пагинацией)
    
    get /api/v1/news/{idNews}/comments/{idComment}

    post /api/v1/news
    
    post /api/v1/news/{idNews}/comments

    put /api/v1/news
    
    put /api/v1/news/{idNews}/comments

    delete /api/v1/news/{idNews}
    
    delete /api/v1/news/{idNews}/comments/{idComment}

  -	CRUD для работы с комментарием
  
    get /api/v1/comments (с пагинацией)
    
    get /api/v1/comments/{idComments}

    delete /api/v1/comments

  -	полнотекстовый поиск по полям новостей: title и text
  
    get /api/v1/news/search?keyword=...

- Написаны юнит и интеграционные тесты

- Запрос и ответы логированы с помощью AOP

- Предусмотрена обработка исключений (@RestControllerAdvice)

- Все настройки приложения находятся в .yml файле

- Методы покрыты @Javadoc

- Open api 3.0; описан в .yaml файлах; доступен по адрессу:

  /swagger-ui/index.html#/ 
  
    или
    
  /index.html
  
- Docker. При запуске команды docker-compose up - создаётся 4 контейнера: само приложение (нужен .jar файл); с dev бд; с test бд и с pgadmin - визуальный интерфейс для управления бд;

  - Доступен по адресу: http://localhost:5050/browser/ 
  
  - Данные для входа: 
    логин: news@Management.com
    пароль: 1111
   
  - Далее нужно добавить сервера для работы с dev, test базами. 
   
  - Настройки описаны в .yml файле

- Реализовано кеширование.

- UI отсутсвует. Работа с json

Для старта нужно сделать build проекта с промощью Gradle;

Создать/запустить контейнеры командой: docker-compose up;

Запустить приложение.
