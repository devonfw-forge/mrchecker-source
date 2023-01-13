package com.capgemini.mrchecker.playwright.core;

import com.capgemini.mrchecker.playwright.core.base.properties.PropertiesPlaywright;
import com.capgemini.mrchecker.playwright.core.base.runtime.RuntimeParametersPlaywright;
import com.capgemini.mrchecker.playwright.core.newDrivers.DriverManager;
import com.capgemini.mrchecker.playwright.core.newDrivers.INewBrowserContext;
import com.capgemini.mrchecker.test.core.BaseTest;
import com.capgemini.mrchecker.test.core.ModuleType;
import com.capgemini.mrchecker.test.core.Page;
import com.capgemini.mrchecker.test.core.analytics.IAnalytics;
import com.capgemini.mrchecker.test.core.base.environment.IEnvironmentService;
import com.capgemini.mrchecker.test.core.base.properties.PropertiesSettingsModule;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Guice;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
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
    private com.microsoft.playwright.Page page;
    private static IEnvironmentService environmentService;
    private final static IAnalytics ANALYTICS;
    public final static String ANALYTICS_CATEGORY_NAME = "Playwright-NewDrivers";
    private final static PropertiesPlaywright PROPERTIES_PLAYWRIGHT;

    static {
        // Get analytics instance created in BaseTets
        ANALYTICS = BaseTest.getAnalytics();

        // Get and then set properties information from selenium.settings file
        PROPERTIES_PLAYWRIGHT = setPropertiesSettings();

        // Read System or maven parameters
        setRuntimeParametersPlaywright();

        // Read Environment variables either from environmnets.csv or any other input data.
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
        setParent(parent);
        setPage(page);

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
        return ModuleType.PLAYWRIGHT;
    }

    @Attachment("Screenshot on failure")
    @SuppressWarnings("UnusedReturnValue")
    public byte[] makeScreenshotOnFailure() {
        byte[] screenshot = null;
        try {
            screenshot = getPage().screenshot();
        } catch (Exception e) {
            BFLogger.logDebug("[makeScreenshotOnFailure] Unable to take screenshot.");
        }
        return screenshot;
    }

    @Attachment("Source Page on failure")
    @SuppressWarnings("UnusedReturnValue")
    public String makeSourcePageOnFailure() {
        return getPage().content();
    }

    public String getActualPageTitle() {
        return getPage().title();
    }

    public void refreshPage() {
        getPage().reload();
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
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
        this.page = page;
    }

    public com.microsoft.playwright.Page getPage() {
        if (Objects.isNull(page)) {
            page = getDriver().currentPage();
        }
        return page;
    }

    public abstract String pageTitle();

    public void loadPage(String url) {
        BFLogger.logDebug(getClass().getName() + ": Opening  page: " + url);
        getPage().navigate(url);
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
    }

    public boolean isUrlAndPageTitleAsCurrentPage(String url) {
        getPage().waitForLoadState(LoadState.NETWORKIDLE);
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
        // Get and then set properties information from selenium.settings file
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
         * Environment variables either from environmnets.csv or any other input data. For now there is no properties
         * settings file for Selenium module. In future, please have a look on Core Module IEnvironmentService
         * environmetInstance = Guice.createInjector(new EnvironmentModule()) .getInstance(IEnvironmentService.class);
         */
    }
}