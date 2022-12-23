package com.capgemini.mrchecker.selenium.core;

import com.capgemini.mrchecker.selenium.core.base.properties.PropertiesSelenium;
import com.capgemini.mrchecker.selenium.core.base.runtime.RuntimeParametersSelenium;
import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import com.capgemini.mrchecker.selenium.core.newDrivers.DriverManager;
import com.capgemini.mrchecker.selenium.core.newDrivers.INewWebDriver;
import com.capgemini.mrchecker.selenium.core.utils.WindowUtils;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Guice;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

abstract public class BasePage extends Page implements IBasePage {
    public static final Duration EXPLICIT_SHORT_WAIT_TIME = Duration.ofSeconds(1);
    public static final Duration PROGRESS_BAR_WAIT_TIMER = Duration.ofSeconds(60);
    public static final Duration EXPLICIT_WAIT_TIMER = Duration.ofSeconds(20);
    public static final int MAX_COMPONENT_RELOAD_COUNT = 3;
    private static final ThreadLocal<WebDriverWait> DRIVER_WAIT = new ThreadLocal<>();
    private static DriverManager driverManager = null;
    private BasePage parent;
    private static IEnvironmentService environmentService;
    private final static IAnalytics ANALYTICS;
    public final static String ANALYTICS_CATEGORY_NAME = "Selenium-NewDrivers";
    private final static PropertiesSelenium PROPERTIES_SELENIUM;

    static {
        // Get analytics instance created in BaseTets
        ANALYTICS = BaseTest.getAnalytics();

        // Get and then set properties information from selenium.settings file
        PROPERTIES_SELENIUM = setPropertiesSettings();

        // Read System or maven parameters
        setRuntimeParametersSelenium();

        // Read Environment variables either from environmnets.csv or any other input data.
        setEnvironmentInstance();
    }

    public static IAnalytics getAnalytics() {
        return ANALYTICS;
    }

    public BasePage() {
        this(null);
    }

    public BasePage(BasePage parent) {
        DRIVER_WAIT.set(new WebDriverWait(getDriver(), BasePage.EXPLICIT_WAIT_TIMER));
        setParent(parent);

        // If the page is not yet loaded, then load it
        // TODO: check that
        if (!isLoaded()) { // In this scenario check if
            load();
        }
    }

    @Override
    public void onTestFailure() {
        super.onTestFailure();
        makeScreenshotOnFailure();
        makeSourcePageOnFailure();
    }

    @Override
    public void onTestClassFinish() {
        super.onTestClassFinish();
        DriverManager.closeDriver();
    }

    @Override
    public ModuleType getModuleType() {
        return ModuleType.SELENIUM;
    }

    @Attachment("Screenshot on failure")
    @SuppressWarnings("UnusedReturnValue")
    public byte[] makeScreenshotOnFailure() {
        byte[] screenshot = null;
        try {
            screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (UnhandledAlertException e) {
            BFLogger.logDebug("[makeScreenshotOnFailure] Unable to take screenshot.");
        }
        return screenshot;
    }

    @Attachment("Source Page on failure")
    @SuppressWarnings("UnusedReturnValue")
    public String makeSourcePageOnFailure() {
        return DriverManager.getDriver()
                .getPageSource();
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
        WebDriverWait webDriverWait = DRIVER_WAIT.get();
        if (Objects.isNull(webDriverWait)) {
            webDriverWait = new WebDriverWait(getDriver(), BasePage.EXPLICIT_WAIT_TIMER);
            DRIVER_WAIT.set(webDriverWait);
        }
        return webDriverWait;
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
         * Environment variables either from environmnets.csv or any other input data. For now there is no properties
         * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
         * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
         */
    }
}
