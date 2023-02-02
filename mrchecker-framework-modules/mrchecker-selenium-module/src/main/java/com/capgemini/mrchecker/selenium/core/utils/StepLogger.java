package com.capgemini.mrchecker.selenium.core.utils;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;
import java.util.UUID;

public class StepLogger extends com.capgemini.mrchecker.test.core.utils.StepLogger {
    public static void error(String error, boolean makeScreenShot) {
        String message = "[ERROR] " + error;
        String uuid = UUID.randomUUID().toString();
        try {
            AllureLifecycle allureLifeCycle = Allure.getLifecycle();
            allureLifeCycle.startStep(uuid, new StepResult().setStatus(Status.FAILED).setName(message));
            BFLogger.logError(message);
            if (makeScreenShot) {
                makeScreenShot();
            }
            allureLifeCycle.stopStep(uuid);
        } catch (NoSuchElementException e) {
            step(message);
        }
    }

    @Step("--Screenshot--")
    public static void makeScreenShot() {
        BasePage.makeScreenShot("Screenshot");
    }

    @Step("-- {elementName} Screenshot--")
    public static void makeScreenShot(WebElement element, String elementName) {
        BasePage.makeScreenShot(elementName + " Screenshot", element);
    }

    @Step("-- {elementName} Screenshot--")
    public static void makeScreenShot(By selector, String elementName) {
        BasePage.makeScreenShot(elementName + " Screenshot", selector);
    }
}