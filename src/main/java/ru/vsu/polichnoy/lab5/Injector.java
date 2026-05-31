package ru.vsu.polichnoy.lab5;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Выполняет внедрение зависимостей в поля, помеченные аннотацией {@link AutoInjectable}.
 * <p>
 * Тип поля используется как ключ в properties-файле. Значение по этому ключу
 * должно содержать полное имя класса-реализации.
 */
public class Injector {

    private static final String DEFAULT_PROPERTIES_FILE = "injector.properties";

    private final Properties properties;

    /**
     * Создаёт Injector, используя стандартный файл настроек injector.properties.
     */
    public Injector() {
        this(DEFAULT_PROPERTIES_FILE);
    }

    /**
     * Создаёт Injector, используя указанный properties-файл из resources.
     *
     * @param propertiesFilePath путь к properties-файлу внутри resources
     */
    public Injector(String propertiesFilePath) {
        this.properties = loadProperties(propertiesFilePath);
    }

    /**
     * Внедряет зависимости в объект.
     * <p>
     * Метод ищет все поля объекта, помеченные {@link AutoInjectable}. Для каждого
     * такого поля определяется тип, затем по этому типу ищется реализация в
     * properties-файле. После этого создаётся объект реализации и записывается
     * в поле через reflection.
     *
     * @param object объект, в который нужно внедрить зависимости
     * @param <T> тип объекта
     * @return тот же объект после внедрения зависимостей
     * @throws InjectionException если зависимость не удалось внедрить
     */
    public <T> T inject(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Объект для внедрения зависимостей не должен быть null");
        }

        Class<?> currentClass = object.getClass();

        while (currentClass != null) {
            injectFieldsFromClass(object, currentClass);
            currentClass = currentClass.getSuperclass();
        }

        return object;
    }

    private void injectFieldsFromClass(Object object, Class<?> currentClass) {
        Field[] fields = currentClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                injectField(object, field);
            }
        }
    }

    private void injectField(Object object, Field field) {
        Class<?> fieldType = field.getType();
        String interfaceName = fieldType.getName();

        String implementationClassName = properties.getProperty(interfaceName);

        if (implementationClassName == null || implementationClassName.isBlank()) {
            throw new InjectionException("Не найдена реализация для типа: " + interfaceName);
        }

        Object dependency = createDependency(fieldType, implementationClassName.trim());

        boolean accessible = field.canAccess(object);

        try {
            field.setAccessible(true);
            field.set(object, dependency);
        } catch (IllegalAccessException exception) {
            throw new InjectionException("Не удалось записать зависимость в поле: " + field.getName(), exception);
        } finally {
            field.setAccessible(accessible);
        }
    }

    private Object createDependency(Class<?> fieldType, String implementationClassName) {
        try {
            Class<?> implementationClass = Class.forName(implementationClassName);

            if (!fieldType.isAssignableFrom(implementationClass)) {
                throw new InjectionException(
                        "Класс " + implementationClassName +
                                " не является реализацией типа " + fieldType.getName()
                );
            }

            Constructor<?> constructor = implementationClass.getDeclaredConstructor();
            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (ClassNotFoundException exception) {
            throw new InjectionException("Класс реализации не найден: " + implementationClassName, exception);
        } catch (NoSuchMethodException exception) {
            throw new InjectionException(
                    "У класса реализации нет конструктора без параметров: " + implementationClassName,
                    exception
            );
        } catch (ReflectiveOperationException exception) {
            throw new InjectionException("Не удалось создать объект класса: " + implementationClassName, exception);
        }
    }

    private Properties loadProperties(String propertiesFilePath) {
        if (propertiesFilePath == null || propertiesFilePath.isBlank()) {
            throw new IllegalArgumentException("Путь к properties-файлу не должен быть пустым");
        }

        Properties loadedProperties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFilePath)) {
            if (inputStream == null) {
                throw new InjectionException("Файл настроек не найден: " + propertiesFilePath);
            }

            loadedProperties.load(inputStream);
            return loadedProperties;
        } catch (IOException exception) {
            throw new InjectionException("Не удалось прочитать файл настроек: " + propertiesFilePath, exception);
        }
    }
}