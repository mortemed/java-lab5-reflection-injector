package ru.vsu.polichnoy.lab5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InjectorTest {

    @Test
    void injectShouldInjectDefaultImplementations() {
        Injector injector = new Injector("injector-test-default.properties");

        SomeBean bean = injector.inject(new SomeBean());

        assertNotNull(bean.getField1());
        assertNotNull(bean.getField2());

        assertInstanceOf(SomeImpl.class, bean.getField1());
        assertInstanceOf(SODoer.class, bean.getField2());
    }

    @Test
    void injectShouldUseAnotherImplementationFromProperties() {
        Injector injector = new Injector("injector-test-other.properties");

        SomeBean bean = injector.inject(new SomeBean());

        assertNotNull(bean.getField1());
        assertNotNull(bean.getField2());

        assertInstanceOf(OtherImpl.class, bean.getField1());
        assertInstanceOf(SODoer.class, bean.getField2());
    }

    @Test
    void injectShouldReturnSameObject() {
        Injector injector = new Injector("injector-test-default.properties");

        SomeBean bean = new SomeBean();
        SomeBean injectedBean = injector.inject(bean);

        assertSame(bean, injectedBean);
    }

    @Test
    void injectShouldRejectNullObject() {
        Injector injector = new Injector("injector-test-default.properties");

        assertThrows(IllegalArgumentException.class, () -> injector.inject(null));
    }

    @Test
    void constructorShouldRejectMissingPropertiesFile() {
        assertThrows(InjectionException.class, () -> new Injector("missing.properties"));
    }

    @Test
    void injectShouldRejectMissingImplementationInProperties() {
        Injector injector = new Injector("injector-test-missing.properties");

        assertThrows(InjectionException.class, () -> injector.inject(new SomeBean()));
    }
}