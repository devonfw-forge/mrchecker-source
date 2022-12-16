package com.capgemini.mrchecker.security.unit;

import com.capgemini.mrchecker.security.core.BasePage;
import com.capgemini.mrchecker.security.tags.UnitTest;
import com.capgemini.mrchecker.test.core.utils.PageFactory;
import org.junit.jupiter.api.Test;

@UnitTest
public class BasePageTest {

    @Test
    public void test() {
        MyPage myPage = PageFactory.getPageInstance(MyPage.class);
        myPage.myMethod();
    }

    public static class MyPage extends BasePage {

        public String myMethod() {
            return "Welcome";
        }
    }

}
