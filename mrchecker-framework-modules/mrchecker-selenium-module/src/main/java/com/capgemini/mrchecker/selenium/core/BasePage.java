package com.capgemini.mrchecker.selenium.core;

import com.capgemini.mrchecker.selenium.core.base.properties.PropertiesSelenium;
import com.capgemini.mrchecker.selenium.core.base.runtime.RuntimeParametersSelenium;
import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import com.capgemini.mrchecker.selenium.core.newDrivers.DriverManager;
import com.capgemini.mrchecker.selenium.core.newDrivers.INewWebDriver;
import com.capgemini.mrchecker.selenium.core.utils.StepLogger;
import com.capgemini.mrchecker.selenium.core.utils.WindowUtils;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.driver.DriverCloseLevel;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Guice;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

abstract public class BasePage extends Page implements IBasePage {
    public static final Duration EXPLICIT_SHORT_WAIT_TIME = Duration.ofSeconds(1);
    public static final Duration PROGRESS_BAR_WAIT_TIMER = Duration.ofSeconds(60);
    public static final int MAX_COMPONENT_RELOAD_COUNT = 3;
    private static DriverManager driverManager = null;
    private BasePage parent;
    private static IEnvironmentService environmentService;
    private final static IAnalytics ANALYTICS;
    public final static String ANALYTICS_CATEGORY_NAME = "Selenium-NewDrivers";
    private final static PropertiesSelenium PROPERTIES_SELENIUM;

    static {
        // Get analytics instance created in BaseTest
        ANALYTICS = BaseTest.getAnalytics();

        // Get and then set properties information from selenium.settings file
        PROPERTIES_SELENIUM = setPropertiesSettings();

        // Read System or maven parameters
        setRuntimeParametersSelenium();

        // Read Environment variables either from environments.csv or any other input data.
        setEnvironmentInstance();
    }

    public static IAnalytics getAnalytics() {
        return ANALYTICS;
    }

    public BasePage() {
        this(null);
    }

    public BasePage(BasePage parent) {
        verifyStaticObject(PROPERTIES_SELENIUM.getAllowStaticPage(), "Page");
        setParent(parent);

        // If the page is not yet loaded, then load it
        // TODO: check that
        if (!isLoaded()) { // In this scenario check if
            load();
        }
    }

    private void handleBeforeTestFails() {
        if (!DriverManager.hasDriverCrushed()) {
            makeScreenshotOnSetupFail();
            makeSourcePageOnSetupFail();
        }
    }

    private void handleAfterTestFails() {
        if (!DriverManager.hasDriverCrushed()) {
            makeScreenshotOnTeardownFail();
            makeSourcePageOnTeardownFail();
        }
    }

    private void handleTestFails() {
        if (!DriverManager.hasDriverCrushed()) {
            makeScreenshotOnTestFail();
            makeSourcePageOnTestFail();
        }
    }

    @Override
    public void onTestExecutionException() {
        super.onTestExecutionException();
        handleTestFails();
    }

    @Override
    public void onTestFinish() {
        super.onTestFinish();
        if (PROPERTIES_SELENIUM.getDriverCloseLevel().equals(DriverCloseLevel.TEST)) {
            DriverManager.closeDriver();
        }
    }


    @Override
    public void onTestClassFinish() {
        super.onTestClassFinish();
        if (PROPERTIES_SELENIUM.getDriverCloseLevel().equals(DriverCloseLevel.CLASS)) {
            DriverManager.closeDriver();
        }
    }

    @Override
    public void onSetupFailure() {
        super.onSetupFailure();
        handleBeforeTestFails();
    }

    @Override
    public void onTeardownFailure() {
        super.onTeardownFailure();
        handleAfterTestFails();
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.SELENIUM;
    }

    public String getActualPageTitle() {
        return getDriver().getTitle();
    }

    public void refreshPage() {
        getDriver().navigate()
                .refresh();
    }

    public static INewWebDriver getDriver() {
        if (Objects.isNull(driverManager)) {
            driverManager = new DriverManager(PROPERTIES_SELENIUM);
        }
        return driverManager.getDriver();
    }

    public static INewWebDriver getDriver(MutableCapabilities options) {
        if (Objects.isNull(driverManager)) {
            driverManager = new DriverManager(PROPERTIES_SELENIUM);
        }
        return driverManager.getDriver(options);
    }

    /**
     * Navigates to previous site (works like pressing browsers 'Back' button)
     */
    public static void navigateBack() {
        getDriver().navigate()
                .back();
        getDriver().waitForPageLoaded();
    }

    public static Actions getAction() {
        return new Actions(getDriver());
    }

    public void setParent(BasePage parent) {
        this.parent = parent;
    }

    public BasePage getParent() {
        return parent;
    }

    public abstract String pageTitle();

    public void loadPage(String url) {
        BFLogger.logDebug(getClass().getName() + ": Opening  page: " + url);
        getDriver().get(url);
        getDriver().waitForPageLoaded();
    }

    public boolean isUrlAndPageTitleAsCurrentPage(String url) {
        getDriver().waitForPageLoaded();
        String pageTitle = this.pageTitle();
        String currentUrl = BasePage.getDriver()
                .getCurrentUrl();
        String currentPageTitle = BasePage.getDriver()
                .getTitle();
        if (!currentUrl.contains(url) || !pageTitle.equals(currentPageTitle)) {
            BFLogger.logDebug(getClass().getName() + ": Current loaded page (" + url + ") with pageTitle ("
                    + currentPageTitle + "). Page to load: (" + url + ") ,for page title: (" + pageTitle + ")");
            return false;
        }
        return true;
    }

    /**
     * Recommended to use. It is method to check is element visible. If element not exists in DOM , method throw
     * BFElementNotFoundException
     *
     * @param cssSelector cssSelector
     * @return false if element have an attribute displayed = none, otherwise return true;
     * @throws BFElementNotFoundException BFElementNotFoundException
     */
    public static boolean isElementDisplayed(By cssSelector) {
        @SuppressWarnings("deprecation")
        List<WebElement> elements = getDriver().findElements(cssSelector);
        if (elements.isEmpty()) {
            throw new BFElementNotFoundException(cssSelector);
        }
        return elements.get(0)
                .isDisplayed();
    }

    /**
     * It is method to check is element visible. It search element inside parent. If element not exists in DOM , method
     * throw BFElementNotFoundException
     *
     * @param cssSelector cssSelector
     * @param parent      parent
     * @return false if element have an attribute displayed = none, otherwise return true;
     * @throws BFElementNotFoundException BFElementNotFoundException
     */
    public static boolean isElementDisplayed(By cssSelector, WebElement parent) {
        List<WebElement> elements = parent.findElements(cssSelector);
        if (elements.isEmpty()) {
            throw new BFElementNotFoundException(cssSelector);
        }
        return elements.get(0)
                .isDisplayed();
    }

    /**
     * Check if given selector is displayed on the page and it contain a specific text
     *
     * @param cssSelector cssSelector
     * @param text        text
     * @return true if a given element is displayed with a specific text
     * @throws BFElementNotFoundException if element is not found in DOM
     */
    @SuppressWarnings("deprecation")
    public static boolean isElementDisplayed(By cssSelector, String text) {
        @SuppressWarnings("deprecation")
        List<WebElement> elements = getDriver().findElements(cssSelector);
        if (elements.isEmpty()) {
            throw new BFElementNotFoundException(cssSelector);
        }
        boolean retValue = elements.get(0)
                .isDisplayed();
        if (retValue && text != null) {
            retValue = elements.get(0)
                    .getText()
                    .equals(text);
        }
        return retValue;
    }

    /**
     * Check if given selector is displayed on the page
     *
     * @param selector selector
     * @param parent   parent
     * @return true if a given element is displayed
     */
    public static boolean isElementDisplayedNoException(By selector, WebElement parent) {
        try {
            return BasePage.isElementDisplayed(selector, parent);
        } catch (BFElementNotFoundException exc) {
            return false;
        }
    }

    /**
     * Check if given selector is displayed on the page
     *
     * @param selector selector
     * @return true if a given element is displayed
     */
    public static boolean isElementDisplayedNoException(By selector) {
        try {
            return BasePage.isElementDisplayed(selector);
        } catch (BFElementNotFoundException exc) {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean isElementPresent(By cssSelector) {
        return !getDriver().findElements(cssSelector)
                .isEmpty();
    }

    public static boolean isLinkClickable(By selector) {
        @SuppressWarnings("deprecation")
        WebElement linkElement = getDriver().findElement(selector);
        return isLinkClickable(linkElement);
    }

    public static boolean isLinkClickable(WebElement element) {
        return !element.getAttribute("href")
                .equals("");
    }

    public static WebDriverWait getWebDriverWait() {
        if (Objects.isNull(driverManager)) {
            driverManager = new DriverManager(PROPERTIES_SELENIUM);
        }
        return driverManager.getDriverWait();
    }

    private static boolean isTitleElementDisplayed(By selector, String title) {
        @SuppressWarnings("deprecation")
        List<WebElement> pageTitle = getDriver().findElements(selector);
        boolean resValue = !pageTitle.isEmpty();
        if (resValue) {
            resValue = pageTitle.get(0)
                    .isDisplayed()
                    && pageTitle.get(0)
                    .getText()
                    .equals(title);
        }
        return resValue;
    }

    /**
     * Open link in new tab
     *
     * @param url url
     */
    public static void openInNewTab(String url) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.open(arguments[0], '_blank');", url);
        WindowUtils.switchWindow(url, true);
    }

    private static PropertiesSelenium setPropertiesSettings() {
        // Get and then set properties information from selenium.settings file
        return Guice.createInjector(PropertiesSettingsModule.init())
                .getInstance(PropertiesSelenium.class);
    }

    private static void setRuntimeParametersSelenium() {
        // Read System or maven parameters
        BFLogger.logDebug(java.util.Arrays.asList(RuntimeParametersSelenium.values())
                .toString());
    }

    private static void setEnvironmentInstance() {
        /*
         * Environment variables either from environments.csv or any other input data. For now there is no properties
         * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
         * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
         */
    }

    public static void makeScreenShot(String attachmentName) {
        makeScreenShot(attachmentName, (WebElement) null);
    }

    public static void makeScreenShot(String attachmentName, By selector) {
        List<WebElement> elements = getDriver().findElementDynamics(selector);
        if (elements.size() == 1) {
            makeScreenShot(attachmentName, elements.get(0));
        } else {
            for (int i = 0; i < elements.size(); i++) {
                makeScreenShot(MessageFormat.format("{0} [{1}]", attachmentName, i), elements.get(i));
            }
        }
    }

    @Attachment(value = "{attachmentName}", type = "image/png")
    public static byte[] makeScreenShot(String attachmentName, WebElement element) {
        BFLogger.logDebug("BasePage.makeScreenShot attachmentName=" + attachmentName);
        byte[] screenshot = new byte[0];
        if (DriverManager.hasDriverCrushed() || !DriverManager.wasDriverCreated()) {
            StepLogger.error("Unable to take element screenshot - No active driver", false);
            return screenshot;
        }
        try {
            INewWebDriver driver = getDriver();
            screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            if (null == element) {
                return screenshot;
            }
            BufferedImage fullImg = ImageIO.read(new ByteArrayInputStream(screenshot));
            int maxX = fullImg.getWidth() - 1;
            int maxY = fullImg.getHeight() - 1;

            Point elementPoint = element.getLocation();
            int elementBeginX = elementPoint.getX();
            int elementBeginY = elementPoint.getY();

            //Element start point outside screen (not visible at all)
            if (elementBeginX >= maxX || elementBeginY >= maxY) {
                return screenshot;
            }

            //Element start point outside screen (partially visible)
            if (elementBeginY < 1) {
                elementBeginY = 1;
            }
            if (elementBeginX < 1) {
                elementBeginX = 1;
            }

            Dimension elementSize = element.getSize();
            int elementEndX = elementPoint.getX() + elementSize.getWidth();
            int elementEndY = elementPoint.getY() + elementSize.getHeight();

            if (maxX < elementEndX) {
                elementEndX = maxX;
            }
            if (maxY < elementEndY) {
                elementEndY = maxY;
            }

            int elementWidth = elementEndX - elementBeginX;
            int elementHeight = elementEndY - elementBeginY;

            //Too small object
            if (elementWidth < 1 || elementHeight < 1) {
                return screenshot;
            }

            RenderedImage elementScreenshot = fullImg.getSubimage(elementBeginX, elementBeginY, elementWidth,
                    elementHeight);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(elementScreenshot, "png", stream);
            screenshot = stream.toByteArray();
            stream.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return screenshot;
    }

    private static void makeScreenshotOnTestFail() {
        makeScreenShot("Screenshot on test fail", (WebElement) null);
    }

    private static void makeScreenshotOnSetupFail() {
        makeScreenShot("Screenshot on setup fail", (WebElement) null);
    }

    private static void makeScreenshotOnTeardownFail() {
        makeScreenShot("Screenshot on teardown fail", (WebElement) null);
    }

    @Attachment("{attachmentName}")
    public static String makeSourcePage(String attachmentName) {
        BFLogger.logDebug("BasePage.makeSourcePage attachmentName=" + attachmentName);
        if (DriverManager.hasDriverCrushed() || !DriverManager.wasDriverCreated()) {
            StepLogger.error("Unable to take page source - No active driver", false);
            return "";
        }
        try {
            return getDriver().getPageSource();
        } catch (Throwable ex) {
            return ex.getMessage();
        }
    }

    private static void makeSourcePageOnTestFail() {
        makeSourcePage("Source Page on test fail");
    }

    private static void makeSourcePageOnSetupFail() {
        makeSourcePage("Source Page on setup fail");
    }

    private static void makeSourcePageOnTeardownFail() {
        makeSourcePage("Source Page on teardown fail");
    }
}