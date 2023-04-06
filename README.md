# Дипломный проект

## Устанавливаемое программное обеспечение

### Docker Desktop
Скачать Docker Desktop можно с официального сайта по [ссылке](https://docs.docker.com/desktop/install/windows-install/). Порядок установки описан [здесь](https://github.com/netology-code/aqa-homeworks/blob/master/docker/installation.md).

### IntelliJ IDEA
Скачать IntelliJ IDEA можно по ссылке с официального сайта по [ссылке](https://www.jetbrains.com/ru-ru/idea/download/#section=windows).

## Запуск тестов
### MySQL
1. Запустить Docker Desktop.
2. Открыть тестовый проект в IntelliJ IDEA.
3. Запустить MySQL, PostgreSQL, Node.js через терминал командой:
`docker-compose up`
4. В новой вкладке терминала запустить тестируемый веб-сервис:
`java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar ./artifacts/aqa-shop.jar`
5. Убедиться в запуске тестируемого веб-сервиса по адресу: [http://localhost:8080/](http://localhost:8080/)
6. В новой вкладке терминала запустить тесты:
`./gradlew clean test "-Ddb.url=jdbc:mysql://localhost:3306/app"`
7. Сгенерировать отчет Allure с помощью команды в терминале: `./gradlew allureServe`

### PostgreSQL
1. Запустить Docker Desktop.
2. Открыть тестовый проект в IntelliJ IDEA.
3. Запустить MySQL, PostgreSQL, Node.js через терминал командой:
`docker-compose up`
4. В новой вкладке терминала запустить тестируемый веб-сервис:
`java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar ./artifacts/aqa-shop.jar`
5. Убедиться в запуске тестируемого веб-сервиса по адресу: http://localhost:8080/
6. В новой вкладке терминала запустить тесты:
`gradlew clean test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`
7. Сгенерировать отчет Allure с помощью команды в терминале: `./gradlew allureServe`

## Отчетные документы
1. [План дипломного проекта](https://github.com/anna2908/diplom_qa/blob/main/docs/Plan.md).
2. [Отчет по итогам тестирования](https://github.com/anna2908/diplom_qa/blob/main/docs/Report.md).
3. [Отчет по итогам автоматизации](https://github.com/anna2908/diplom_qa/blob/main/docs/Summary.md).
