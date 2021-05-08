# Описание 
Сервер для работы приложениия [Travel-With-Me](https://github.com/MariaChizhova/Travel-With-Me).

# Запуск
Убедитесь, что у вас установлен `maven` и `docker`. 

Для начала надо собрать `jar`-файл:
```
mvn -Dmaven.test.skip=true install
```

Теперь можно запускать сервер:
```
sudo ./run.sh
```

Сервер будет доступен по адресу `http://localhost:9090/`.

# Тестирование
Добавим пользователя:
```
curl -X POST http://localhost:9090/add_user?email=elena.golovach@gmail.com
```

Проверим, что он действительно существует:
```
curl -X GET http://localhost:9090/get_user?email=elena.golovach@gmail.com
```

Должно вывестись это: 
```
{"id":1,"firstName":null,"lastName":null,"email":"elena.golovach@gmail.com","avatar":null,"numberFollowers":0,"numberFollowing":0}
```

# Работа 
