package com.capgemini.mrchecker.playwright.core.utils;

import com.capgemini.mrchecker.playwright.core.BasePage;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.microsoft.playwright.Locator;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

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
    public static void makeScreenShot(Locator locator, String elementName) {
        BasePage.makeScreenShot(elementName + " Screenshot", locator);
    }
}