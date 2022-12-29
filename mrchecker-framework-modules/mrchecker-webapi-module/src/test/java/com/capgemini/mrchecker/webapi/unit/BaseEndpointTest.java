package com.capgemini.mrchecker.webapi.unit;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.utils.PageFactory;
import com.capgemini.mrchecker.webapi.core.BaseEndpoint;
import com.capgemini.mrchecker.webapi.tags.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

@UnitTest
@ResourceLock("PropertiesFileSettings.class")
public class BaseEndpointTest extends BaseTest {
    @Override
    public void setUp() {
    }

    @Override
    public void tearDown() {
    }

    @Test
    public void test() {
        MyPage myPage = PageFactory.getPageInstance(MyPage.class);
        myPage.myMethod();
    }

    public static class MyPage extends BaseEndpoint {
        public String myMethod() {
            return "Welcome";
        }

        @Override
        public String getEndpoint() {
            return null;
        }
    }
}