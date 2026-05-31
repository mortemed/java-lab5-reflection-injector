package ru.vsu.polichnoy.lab5;

/**
 * Класс с зависимостями, которые должны быть внедрены через Injector.
 */
public class SomeBean {

    @AutoInjectable
    private SomeInterface field1;

    @AutoInjectable
    private SomeOtherInterface field2;

    /**
     * Вызывает методы внедрённых зависимостей.
     */
    public void foo() {
        field1.doSomething();
        field2.doSomeOther();
    }

    public SomeInterface getField1() {
        return field1;
    }

    public SomeOtherInterface getField2() {
        return field2;
    }
}