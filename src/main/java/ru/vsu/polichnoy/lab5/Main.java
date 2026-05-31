package ru.vsu.polichnoy.lab5;

/**
 * Консольная программа для демонстрации внедрения зависимостей через reflection.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Лабораторная работа №5");
        System.out.println("Внедрение зависимостей с помощью reflection");
        System.out.println();

        SomeBean bean = new Injector().inject(new SomeBean());

        System.out.println("Результат вызова SomeBean.foo():");
        bean.foo();

        System.out.println();
        System.out.println();
        System.out.println("Если в injector.properties заменить SomeImpl на OtherImpl,");
        System.out.println("то результат изменится с AC на BC без изменения Java-кода.");
    }
}