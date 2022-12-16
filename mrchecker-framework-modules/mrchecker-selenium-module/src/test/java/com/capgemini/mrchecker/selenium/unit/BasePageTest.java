package com.capgemini.mrchecker.selenium.unit;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.tags.UnitTest;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.utils.PageFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import static org.junit.Assert.assertEquals;

@UnitTest
@ResourceLock(value = "SingleThread")
public class BasePageTest extends BaseTest {

    @Test
    public void testGetDriver() {
        TestPage testPage = PageFactory.getPageInstance(TestPage.class);
        assertEquals("", "Google", testPage.pageTitle());

    }

    public static class TestPage extends BasePage {

        @Override
        public boolean isLoaded() {

            return false;
        }

        @Override
        public void load() {
            getDriver().get("http://google.com");

        }

        @Override
        public String pageTitle() {
            return getDriver().getTitle();
        }

    }

}
