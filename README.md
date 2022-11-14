# Как установить клиент

### Скачать к себе на машину:
1) Java 11+ (https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
2) Intellij Idea (https://www.jetbrains.com/idea/download/#section=windows)
3) JavaFX SDK (https://download2.gluonhq.com/openjfx/19/openjfx-19_windows-x64_bin-sdk.zip)

##Установка
1) Склонируйте репозиторий
   ![Скриншот](readme/1.PNG "Скриншот")
   ![Скриншот](readme/2.PNG "Скриншот")

2) Настройте параметры запуска
   ![Скриншот](readme/3.PNG "Скриншот")
   ![Скриншот](readme/4.PNG "Скриншот")
   ![Скриншот](readme/5.PNG "Скриншот")
   ![Скриншот](readme/6.PNG "Скриншот")
    Скопируйте параметры:
   --module-path
   "C:\Program Files\Java\javafx-sdk-17.0.2\lib"
   --add-modules
   javafx.controls,javafx.fxml
    ####ВАЖНО!!!
    Вместо C:\Program Files\Java\javafx-sdk-17.0.2 должен быть путь к JavaFX SDK на ВАШЕМ компьютере
   ![Скриншот](readme/7.PNG "Скриншот")

### Сборка
```
mvn clean package
```

### Запуск
```
Стрелочку нажми, или Shift+F10
```


