package ru.vsu.polichnoy.lab5;

/**
 * Исключение, возникающее при ошибке внедрения зависимости.
 */
public class InjectionException extends RuntimeException {

    /**
     * Создаёт исключение с сообщением.
     *
     * @param message описание ошибки
     */
    public InjectionException(String message) {
        super(message);
    }

    /**
     * Создаёт исключение с сообщением и исходной причиной.
     *
     * @param message описание ошибки
     * @param cause исходное исключение
     */
    public InjectionException(String message, Throwable cause) {
        super(message, cause);
    }
}