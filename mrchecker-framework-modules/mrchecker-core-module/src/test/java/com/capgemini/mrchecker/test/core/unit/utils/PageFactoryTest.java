package com.capgemini.mrchecker.test.core.unit.utils;

import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;
import com.capgemini.mrchecker.test.core.tags.UnitTest;
import com.capgemini.mrchecker.test.core.utils.PageFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
@ResourceLock(value = "SingleThread")
public class PageFactoryTest {

    @Test
    public void shouldCreateChildPage() {
        Page page = PageFactory.getPageInstance(ChildPage.class);

        assertThat(page, is(instanceOf(ChildPage.class)));
    }

    @Test
    public void shouldCreateChildPageThrowException() {
        assertThrows(BFInputDataException.class, () -> PageFactory.getPageInstance(ChildPageThrowingException.class));
    }

    @Test
    public void shouldCreateChildPageThrowExceptionWhenPrivateConstructor() {
        assertThrows(BFInputDataException.class, () -> PageFactory.getPageInstance(ChildPageWithPrivateConstructor.class));
    }

    @Test
    public void shouldCreateChildPageWithArgs() {
        Page page = PageFactory.getPageInstance(ChildPageWithArgs.class, new Object(), 1);

        assertThat(page, is(instanceOf(ChildPageWithArgs.class)));
    }

    @Test
    public void shouldCreateChildWithArgsPageThrowException() {
        assertThrows(BFInputDataException.class, () -> PageFactory.getPageInstance(ChildPageWithArgsThrowingException.class, new Object(), 1));
    }

    @Test
    public void shouldCreateChildWithArgsPageThrowExceptionWhenPrivateConstructor() {
        assertThrows(BFInputDataException.class, () -> PageFactory.getPageInstance(ChildPageWithArgsWithPrivateConstructor.class, new Object(), 1));
    }

    public static class ChildPage extends Page {

        public ChildPage() {
        }

        @Override
        public ModuleType getModuleType() {
            return ModuleType.CORE;
        }
    }

    public static class ChildPageThrowingException extends Page {

        public ChildPageThrowingException() throws InstantiationException {
            throw new InstantiationException();
        }

        @Override
        public ModuleType getModuleType() {
            return ModuleType.CORE;
        }
    }

    public static class ChildPageWithPrivateConstructor extends Page {

        private ChildPageWithPrivateConstructor() {
        }

        @Override
        public ModuleType getModuleType() {
            return ModuleType.CORE;
        }
    }

    public static class ChildPageWithArgs extends Page {

        public ChildPageWithArgs(Object o, Integer i) {
        }

        @Override
        public ModuleType getModuleType() {
            return ModuleType.CORE;
        }
    }

    public static class ChildPageWithArgsThrowingException extends Page {

        public ChildPageWithArgsThrowingException(Object o, Integer i) throws InstantiationException {
            throw new InstantiationException();
        }

        @Override
        public ModuleType getModuleType() {
            return ModuleType.CORE;
        }
    }

    public static class ChildPageWithArgsWithPrivateConstructor extends Page {

        private ChildPageWithArgsWithPrivateConstructor(Object o, Integer i) {
        }

        @Override
        public ModuleType getModuleType() {
            return ModuleType.CORE;
        }
    }

    @Test
    public void shouldCreateChildPageByClassName() {
        Page page = PageFactory.getPageInstance("com.capgemini.mrchecker.test.core.unit.utils.PageFactoryTest$ChildPage");

        assertThat(page, is(instanceOf(ChildPage.class)));
    }

    @Test
    public void shouldCreateChildPageByClassNameThrowException() {
        assertThrows(BFInputDataException.class, () -> PageFactory.getPageInstance("com.capgemini.mrchecker.test.core.unit.utils.PageFactoryTest$NoSuchPage"));
    }

    @Test
    public void shouldCreateChildPageWithArgsByClassName() {
        Page page = PageFactory.getPageInstance("com.capgemini.mrchecker.test.core.unit.utils.PageFactoryTest$ChildPageWithArgs", new Object(), 1);

        assertThat(page, is(instanceOf(ChildPageWithArgs.class)));
    }

    @Test
    public void shouldCreateChildPageWithArgsByClassNameThrowException() {
        assertThrows(BFInputDataException.class, () -> PageFactory.getPageInstance("com.capgemini.mrchecker.test.core.unit.utils.PageFactoryTest$NoSuchPage", new Object(), 1));
    }
}