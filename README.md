Название Postgresql бд (версия 17) – testdb. Username – postgres, password – admin.  
Выполнить при создании бд с нуля  
INSERT INTO roles(name) VALUES('ROLE_USER');  
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');  
INSERT INTO roles(name) VALUES('ROLE_ADMIN');  
Дамп бд расположен по пути ...\api\init.sql, используется в докере как стартовая бд.  

Запуск бэкенда – api через пуск в IDE.   
Документация api доступна по адресу http://localhost:8080/swagger-ui/index.html при запущенном бэкенде.  

До запуска фронтенда api-front выполнить npm install для загрузки пакетов Angular CLI.   
Запуск фронтенда командой ng serve --host 0.0.0.0 (ангуляр по умолчанию слушает ipv6) при переходе в каталог ...\api-front.  
При запущенном фронтенде приложение доступно по адресу http://localhost:4200/  
Логины для админа/модератора - admin/moderator. Пароли – 123456  
Докеризация:   
(НЕ ОБЯЗАТЕЛЬНО - если изменять стартовую бд, то docker compose down -v)   
Билд - docker compose up --build  при переходе в \api\ (где лежит compose.yaml) (при запущенном Docker Desktop)  

Видеообзор:  
https://www.youtube.com/watch?v=Qxf9VeMJMhw  
