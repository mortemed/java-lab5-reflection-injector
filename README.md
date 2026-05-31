"Вещи и люди нас

окружают. И те,

и эти терзают глаз.

**Лучше жить в темноте.**
<p>

Я сижу на скамье

в парке, глядя вослед

проходящей семье.

**Мне опротивел свет.**"

Иосиф Бродский - "Натюрморт"

# Лабораторная работа №5 — Reflection Injector

## Задание

Создать аннотацию `@AutoInjectable`.

Создать класс `Injector`, который с помощью reflection ищет поля, помеченные этой аннотацией, и инициализирует их объектами классов, указанных в `.properties`-файле.

Метод `inject` должен принимать объект произвольного класса, находить в нём поля с аннотацией `@AutoInjectable`, определять тип поля и внедрять реализацию, указанную в файле настроек.

## Основная идея

В классе `SomeBean` есть поля:

    @AutoInjectable
    private SomeInterface field1;

    @AutoInjectable
    private SomeOtherInterface field2;

Эти поля нигде не создаются вручную.

Они инициализируются классом Injector.

Файл injector.properties содержит соответствия:

    ru.vsu.polichnoy.lab5.SomeInterface=ru.vsu.polichnoy.lab5.SomeImpl
    ru.vsu.polichnoy.lab5.SomeOtherInterface=ru.vsu.polichnoy.lab5.SODoer

Это означает:

* для поля типа SomeInterface нужно создать объект SomeImpl;
* для поля типа SomeOtherInterface нужно создать объект SODoer.


## Пример работы

Код:

    SomeBean bean = new Injector().inject(new SomeBean());
    bean.foo();

Вывод:

    AC

Если в injector.properties заменить:

    ru.vsu.polichnoy.lab5.SomeInterface=ru.vsu.polichnoy.lab5.SomeImpl

на:

    ru.vsu.polichnoy.lab5.SomeInterface=ru.vsu.polichnoy.lab5.OtherImpl

то вывод изменится на:

    BC

При этом Java-код менять не нужно.

## Реализованный функционал

В проекте реализованы:

* аннотация @AutoInjectable;
* класс Injector;
* чтение настроек из .properties;
* поиск аннотированных полей через reflection;
* доступ к приватным полям через setAccessible(true);
* создание объектов реализаций через reflection;
* проверка, что класс реализации подходит к типу поля;
* обработка ошибок через InjectionException;
* демонстрационные интерфейсы и реализации;
* unit-тесты.
 
## Основные классы
AutoInjectable.java - Аннотация для пометки полей, которые должны быть инициализированы автоматически.

Injector.java - Класс, выполняющий внедрение зависимостей.

InjectionException.java - Исключение для ошибок внедрения зависимостей.

SomeInterface.java &
SomeOtherInterface.java - Демонстрационные интерфейсы.

SomeImpl.java &
OtherImpl.java &
SODoer.java - Демонстрационные реализации интерфейсов.

SomeBean.java - Класс с полями, помеченными @AutoInjectable.

Main.java - Консольная демонстрация работы инжектора.

Структура проекта

    src/main/java/ru/vsu/polichnoy/lab5/AutoInjectable.java
    src/main/java/ru/vsu/polichnoy/lab5/Injector.java
    src/main/java/ru/vsu/polichnoy/lab5/InjectionException.java
    src/main/java/ru/vsu/polichnoy/lab5/SomeInterface.java
    src/main/java/ru/vsu/polichnoy/lab5/SomeOtherInterface.java
    src/main/java/ru/vsu/polichnoy/lab5/SomeImpl.java
    src/main/java/ru/vsu/polichnoy/lab5/OtherImpl.java
    src/main/java/ru/vsu/polichnoy/lab5/SODoer.java
    src/main/java/ru/vsu/polichnoy/lab5/SomeBean.java
    src/main/java/ru/vsu/polichnoy/lab5/Main.java
    
    src/main/resources/injector.properties
    
    src/test/java/ru/vsu/polichnoy/lab5/InjectorTest.java
    src/test/resources/injector-test-default.properties
    src/test/resources/injector-test-other.properties
    src/test/resources/injector-test-missing.properties

## Используемые технологии
* Java 17 compatibility;
* JDK 21;
* Maven;
* JUnit 5;
* Reflection API;
* Java Properties;
* Git.

Автор: Даниил Поличной