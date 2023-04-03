package com.capgemini.mrchecker.playwright.core;

import com.capgemini.mrchecker.playwright.core.base.properties.PropertiesPlaywright;
import com.capgemini.mrchecker.playwright.core.base.runtime.RuntimeParametersPlaywright;
import com.capgemini.mrchecker.playwright.core.newDrivers.DriverManager;
import com.capgemini.mrchecker.playwright.core.newDrivers.INewBrowserContext;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.driver.DriverCloseLevel;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Guice;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page.WaitForLoadStateOptions;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Attachment;

import java.time.Duration;
import java.util.Objects;

abstract public class BasePage extends Page implements IBasePage {
    public static final Duration EXPLICIT_SHORT_WAIT_TIME = Duration.ofSeconds(1);
    public static final Duration PROGRESS_BAR_WAIT_TIMER = Duration.ofSeconds(60);
    public static final int MAX_COMPONENT_RELOAD_COUNT = 3;
    private static DriverManager driverManager = null;
    private BasePage parent;
    private static IEnvironmentService environmentService;
    private final static IAnalytics ANALYTICS;
    public final static String ANALYTICS_CATEGORY_NAME = "Playwright-NewDrivers";
    private final static PropertiesPlaywright PROPERTIES_PLAYWRIGHT;
    private final static LoadState DEFAULT_LOAD_STATE = LoadState.LOAD;

    static {
        // Get analytics instance created in BaseTest
        ANALYTICS = BaseTest.getAnalytics();

        // Get and then set properties information from playwright.settings file
        PROPERTIES_PLAYWRIGHT = setPropertiesSettings();

        // Read System or maven parameters
        setRuntimeParametersPlaywright();

        // Read Environment variables either from environments.csv or any other input data.
        setEnvironmentInstance();
    }

    public static IAnalytics getAnalytics() {
        return ANALYTICS;
    }

    public BasePage() {
        this(null, null);
    }

    public BasePage(BasePage parent) {
        this(null, parent);
    }

    public BasePage(com.microsoft.playwright.Page page) {
        this(page, null);
    }

    public BasePage(com.microsoft.playwright.Page page, BasePage parent) {
        verifyStaticObject(PROPERTIES_PLAYWRIGHT.getAllowStaticPage(), "Page");
        setParent(parent);
        setPage(page);

        // If the page is not yet loaded, then load it
        // TODO: check that
        if (!isLoaded()) { // In this scenario check if
            load();
        }
    }

    private void handleBeforeTestFails() {
        makeScreenshotOnSetupFail();
        makeSourcePageOnSetupFail();
    }

    private void handleAfterTestFails() {
        makeScreenshotOnTeardownFail();
        makeSourcePageOnTeardownFail();
    }

    private void handleTestFails() {
        makeScreenshotOnTestFail();
        makeSourcePageOnTestFail();
    }

    @Override
    public void onTestExecutionException() {
        super.onTestExecutionException();
        handleTestFails();
    }

    @Override
    public void onTestFinish() {
        super.onTestFinish();
        if (PROPERTIES_PLAYWRIGHT.getDriverCloseLevel().equals(DriverCloseLevel.TEST)) {
            DriverManager.closeDriver();
        }
    }


    @Override
    public void onTestClassFinish() {
        super.onTestClassFinish();
        if (PROPERTIES_PLAYWRIGHT.getDriverCloseLevel().equals(DriverCloseLevel.CLASS)) {
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
        return ModuleType.PLAYWRIGHT;
    }


    public String getActualPageTitle() {
        return getPage().title();
    }

    public static INewBrowserContext getDriver() {
        if (Objects.isNull(driverManager)) {
            driverManager = new DriverManager(PROPERTIES_PLAYWRIGHT);
        }
        return driverManager.getDriver();
    }

    public static INewBrowserContext getDriver(BrowserType.LaunchOptions launchOptions, Browser.NewContextOptions newContextOptions) {
        if (Objects.isNull(driverManager)) {
            driverManager = new DriverManager(PROPERTIES_PLAYWRIGHT);
        }
        return driverManager.getDriver(launchOptions, newContextOptions);
    }

    public void setParent(BasePage parent) {
        this.parent = parent;
    }

    public BasePage getParent() {
        return parent;
    }

    public void setPage(com.microsoft.playwright.Page page) {
        if (Objects.nonNull(page)) {
            getDriver().setCurrentPage(page);
        }
    }

    public com.microsoft.playwright.Page getPage() {
        return getDriver().currentPage();
    }

    public abstract String pageTitle();

    public void loadPage(String url) {
        loadPage(url, DEFAULT_LOAD_STATE);
    }

    public void loadPage(String url, LoadState state) {
        loadPage(url, state, null);
    }

    public void loadPage(String url, LoadState state, WaitForLoadStateOptions options) {
        BFLogger.logDebug(getClass().getName() + ": Opening  page: " + url);
        getPage().navigate(url);
        waitForLoadState(state, options);
    }

    public void refreshPage() {
        waitForLoadState(DEFAULT_LOAD_STATE);
    }

    public void refreshPage(LoadState state) {
        waitForLoadState(state, null);
    }

    public void refreshPage(LoadState state, WaitForLoadStateOptions options) {
        getPage().reload();
        waitForLoadState(state, options);
    }

    public void waitForLoadState() {
        waitForLoadState(DEFAULT_LOAD_STATE);
    }

    public void waitForLoadState(LoadState state) {
        waitForLoadState(state, null);
    }

    public void waitForLoadState(LoadState state, WaitForLoadStateOptions options) {
        getPage().waitForLoadState(state, options);
    }

    public boolean isUrlAndPageTitleAsCurrentPage(String url) {
        waitForLoadState();
        String pageTitle = pageTitle();
        String currentUrl = getPage().url();
        String currentPageTitle = getActualPageTitle();
        if (!currentUrl.contains(url) || !pageTitle.equals(currentPageTitle)) {
            BFLogger.logDebug(getClass().getName() + ": Current loaded page (" + url + ") with pageTitle ("
                    + currentPageTitle + "). Page to load: (" + url + ") ,for page title: (" + pageTitle + ")");
            return false;
        }
        return true;
    }

    private static PropertiesPlaywright setPropertiesSettings() {
        // Get and then set properties information from playwright.settings file
        return Guice.createInjector(PropertiesSettingsModule.init())
                .getInstance(PropertiesPlaywright.class);
    }

    private static void setRuntimeParametersPlaywright() {
        // Read System or maven parameters
        BFLogger.logDebug(java.util.Arrays.asList(RuntimeParametersPlaywright.values())
                .toString());
    }

    private static void setEnvironmentInstance() {
        /*
         * Environment variables either from environments.csv or any other input data. For now there is no properties
         * settings file for Playwright module. In future, please have a look on Core Module IEnvironmentService
         * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
         */
    }

    public static void makeScreenShot(String attachmentName) {
        makeScreenShot(attachmentName, null);
    }

    @Attachment(value = "{attachmentName}", type = "image/png")
    public static byte[] makeScreenShot(String attachmentName, Locator locator) {
        BFLogger.logDebug("BasePage.makeScreenShot attachmentName=" + attachmentName);
        byte[] screenshot = new byte[0];
        try {
            if (Objects.isNull(locator)) {
                screenshot = getDriver().currentPage().screenshot();
            } else {
                screenshot = locator.screenshot();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return screenshot;
    }

    private static void makeScreenshotOnTestFail() {
        makeScreenShot("Screenshot on test fail", null);
    }

    private static void makeScreenshotOnSetupFail() {
        makeScreenShot("Screenshot on setup fail", null);
    }

    private static void makeScreenshotOnTeardownFail() {
        makeScreenShot("Screenshot on teardown fail", null);
    }

    @Attachment("{attachmentName}")
    public static String makeSourcePage(String attachmentName) {
        BFLogger.logDebug("BasePage.makeSourcePage attachmentName=" + attachmentName);
        try {
            return getDriver().currentPage().content();
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