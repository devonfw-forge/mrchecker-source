package com.capgemini.mrchecker.selenium.core.newDrivers;

import com.capgemini.mrchecker.selenium.core.base.properties.PropertiesSelenium;
import com.capgemini.mrchecker.selenium.core.base.runtime.RuntimeParametersSelenium;
import com.capgemini.mrchecker.selenium.core.enums.ResolutionEnum;
import com.capgemini.mrchecker.selenium.core.utils.OperationsOnFiles;
import com.capgemini.mrchecker.selenium.core.utils.ResolutionUtils;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.WebDriverManagerException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

public class DriverManager {
    private static final ThreadLocal<INewWebDriver> DRIVERS = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> DRIVERS_WAIT = new ThreadLocal<>();
    private static final ResolutionEnum DEFAULT_RESOLUTION = ResolutionEnum.w1920;
    private static final Duration IMPLICITLY_WAIT = Duration.ofSeconds(2);
    public static final Duration EXPLICIT_WAIT = Duration.ofSeconds(20);
    private static final String DOWNLOAD_DIR = System.getProperty("java.io.tmpdir");
    private static boolean driverDownloadedChrome = false;
    private static boolean driverDownloadedFirefox = false;
    private static boolean driverDownloadedMicrosoftEdge = false;
    private static boolean driverDownloadedInternetExplorer = false;
    private static PropertiesSelenium propertiesSelenium;

    @Inject
    public DriverManager(@Named("properties") PropertiesSelenium propertiesSelenium) {
        if (Objects.isNull(DriverManager.propertiesSelenium)) {
            DriverManager.propertiesSelenium = propertiesSelenium;
        }
    }

    public void start() {
        DriverManager.getDriver();
    }

    public void stop() {
        try {
            closeDriver();
            BFLogger.logDebug("Closing Driver in stop()");
            BFLogger.logInfo(String.format("All clicks took %.2fs", 1.0 * NewRemoteWebElement.dropClickTimer() / 1000));
        } catch (Exception e) {
            // TODO: handle that
        }
    }

    public static INewWebDriver getDriver() {
        INewWebDriver driver = DRIVERS.get();
        if (Objects.isNull(driver)) {
            String browser = RuntimeParametersSelenium.BROWSER.getValue();
            if (Browser.CHROME.is(browser)) {
                return Driver.CHROME.getDriver();
            }
            if (Browser.EDGE.is(browser)) {
                return Driver.EDGE.getDriver();
            }
            if (Browser.FIREFOX.is(browser)) {
                return Driver.FIREFOX.getDriver();
            }
            if (Browser.IE.is(browser)) {
                return Driver.IE.getDriver();
            }
            throw new IllegalStateException("Unsupported browser: " + browser);
        }
        return driver;
    }

    public static WebDriverWait getDriverWait() {
        WebDriverWait webDriverWait = DRIVERS_WAIT.get();
        if (Objects.isNull(webDriverWait)) {
            webDriverWait = new WebDriverWait(getDriver(), EXPLICIT_WAIT);
            DRIVERS_WAIT.set(webDriverWait);
        }
        return webDriverWait;
    }

    //If driver object is not null but there is no session or window it means that driver crashed
    public static boolean hasDriverCrushed() {
        if (wasDriverCreated()) {
            INewWebDriver driver = DRIVERS.get();
            try {
                if (driver.toString().contains("(null)")) {
                    BFLogger.logError("Driver session is null");
                    return true;
                }
                if (driver.getWindowHandles().isEmpty()) {
                    BFLogger.logError("Driver window handles is empty");
                    return true;
                }
            } catch (Exception e) {
                BFLogger.logError("Driver state check exception: " + e.getMessage());
                e.printStackTrace();
                return true;
            }
        }
        return false;
    }

    public static boolean wasDriverCreated() {
        return Objects.nonNull(DRIVERS.get());
    }

    public static void closeDriver() {
        INewWebDriver driver = DRIVERS.get();
        if (Objects.isNull(driver)) {
            BFLogger.logDebug("closeDriver() was called but there was no driver for this thread.");
        } else {
            try {
                BFLogger.logDebug("Closing WebDriver for this thread. " + RuntimeParametersSelenium.BROWSER.getValue());
                driver.quit();
            } catch (WebDriverException e) {
                BFLogger.logError("Ooops! Something went wrong while closing the driver: ");
                e.printStackTrace();
            } finally {
                DRIVERS.remove();
                DRIVERS_WAIT.remove();
            }
        }
    }

    private static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private enum Driver {
        CHROME {
            @Override
            public INewWebDriver getDriver() {
                return DriverManager.getDriver(getChromeOptions());
            }
        },
        EDGE {
            @Override
            public INewWebDriver getDriver() {
                return DriverManager.getDriver(getEdgeOptions());
            }
        },
        FIREFOX {
            @Override
            public INewWebDriver getDriver() {
                return DriverManager.getDriver(getFirefoxOptions(getFirefoxProfile()));
            }
        },
        IE {
            @Override
            public INewWebDriver getDriver() {
                return DriverManager.getDriver(getInternetExplorerOptions());
            }
        };

        protected abstract INewWebDriver getDriver();
    }

    private static <T extends RemoteWebDriver> void downloadNewestOrGivenVersionOfWebDriver(Class<T> webDriverType) {
        String proxy = DriverManager.propertiesSelenium.getProxy();
        String webDriversPath = DriverManager.propertiesSelenium.getWebDrivers();
        try {
            System.setProperty("wdm.targetPath", webDriversPath);
            System.setProperty("wdm.useBetaVersions", "false");

            WebDriverManager.getInstance(webDriverType)
                    .proxy(proxy)
                    .setup();
            BFLogger.logDebug("Downloaded version of driver=" + WebDriverManager.getInstance(webDriverType).getDownloadedDriverVersion());

        } catch (WebDriverManagerException e) {
            BFLogger.logError("Unable to download driver automatically. "
                    + "Please try to set up the proxy in properties file. "
                    + "If you want to download them manually, go to the "
                    + "http://www.seleniumhq.org/projects/webdriver/ site.");
        }
    }

    public static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        setCommonChromiumOptions(options);
        return options;
    }

    public static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        setCommonChromiumOptions(options);
        return options;
    }

    public static FirefoxProfile getFirefoxProfile() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(false);
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", DOWNLOAD_DIR);
        profile.setPreference("browser.download.useDownloadDir", true);
        profile.setPreference("security.ssl.enable_ocsp_stapling", false);
        profile.setPreference("dom.disable_beforeunload", true);
        profile.setPreference("dom.webnotifications.enabled", false);
        profile.setPreference("dom.push.enabled", false);
        profile.setPreference("browser.cache.disk.enable", false);
        profile.setPreference("browser.cache.memory.enable", false);
        profile.setPreference("browser.cache.offline.enable", false);
        profile.setPreference("browser.startup.homepage_override.mstone", "ignore");
        profile.setPreference("security.tls.version.min", 1);
        profile.setPreference("intl.accept_languages", "en-us");
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/comma-separated-values, application/vnd.ms-excel, application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("pdfjs.disabled", false);
        profile.setPreference("network.http.use-cache", false);

        RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
            BFLogger.logDebug("Add to Firefox profile: " + key + "=" + value);
            profile.setPreference(key, value);
        });

        return profile;
    }

    public static FirefoxOptions getFirefoxOptions(FirefoxProfile profile) {
        FirefoxOptions options = new FirefoxOptions();
        setCommonBrowserOptions(options);
        setFirefoxHeadless(options);
        options.setProfile(profile);
        options.setCapability("security.ssl.enable_ocsp_stapling", false);
        options.setCapability("handleAlerts", true);
        options.setCapability("network.http.use-cache", false);
        options.setCapability("security.cert_pinning.enforcement_level", 0);
        options.setCapability("security.enterprise_roots.enabled", true);
        return options;
    }

    public static InternetExplorerOptions getInternetExplorerOptions() {
        InternetExplorerOptions options = new InternetExplorerOptions();
        setCommonBrowserOptions(options);
        return options;
    }

    public static INewWebDriver getDriver(MutableCapabilities options) {
        INewWebDriver driver = DRIVERS.get();
        if (Objects.isNull(driver)) {
            BFLogger.logDebug("Creating new " + RuntimeParametersSelenium.BROWSER.getValue() + " WebDriver.");
            String seleniumGridParameter = RuntimeParametersSelenium.SELENIUM_GRID.getValue();
            if (!isEmpty(seleniumGridParameter)) {
                driver = getRemoteDriver(options);
            } else {
                String browser = RuntimeParametersSelenium.BROWSER.getValue();
                if (Browser.CHROME.is(browser)) {
                    driver = getChromeDriver((ChromeOptions) options);
                } else if (Browser.EDGE.is(browser)) {
                    driver = getEdgeDriver((EdgeOptions) options);
                } else if (Browser.FIREFOX.is(browser)) {
                    driver = getFirefoxDriver((FirefoxOptions) options);
                } else if (Browser.IE.is(browser)) {
                    driver = getInternetExplorerDriver((InternetExplorerOptions) options);
                } else {
                    throw new IllegalStateException("Unsupported browser: " + browser);
                }
            }
            DRIVERS.set(driver);
            BFLogger.logDebug("Driver:" + driver);
            driver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT);
            ResolutionUtils.setResolution(driver, DriverManager.DEFAULT_RESOLUTION);
            NewRemoteWebElement.setClickTimer();
        }
        return driver;
    }

    private static INewWebDriver getChromeDriver(ChromeOptions chromeOptions) {
        INewWebDriver driver = DRIVERS.get();
        if (Objects.isNull(driver)) {
            String browserPath = DriverManager.propertiesSelenium.getSeleniumChrome();
            boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
            synchronized (DOWNLOAD_DIR) {
                if (isDriverAutoUpdateActivated && !driverDownloadedChrome) {
                    if (!DriverManager.propertiesSelenium.getChromeDriverVersion().isEmpty()) {
                        System.setProperty("wdm.chromeDriverVersion", DriverManager.propertiesSelenium.getChromeDriverVersion());
                    }
                    downloadNewestOrGivenVersionOfWebDriver(ChromeDriver.class);
                    OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(ChromeDriver.class).getDownloadedDriverPath(), browserPath);
                }
                driverDownloadedChrome = true;
            }
            System.setProperty("webdriver.chrome.driver", browserPath);
            return new NewChromeDriver(chromeOptions);
        }
        return driver;
    }

    private static INewWebDriver getEdgeDriver(EdgeOptions edgeOptions) {
        INewWebDriver driver = DRIVERS.get();
        if (Objects.isNull(driver)) {
            String browserPath = DriverManager.propertiesSelenium.getSeleniumEdge();
            boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
            synchronized (DOWNLOAD_DIR) {
                if (isDriverAutoUpdateActivated && !driverDownloadedMicrosoftEdge) {
                    if (!DriverManager.propertiesSelenium.getEdgeDriverVersion().isEmpty()) {
                        System.setProperty("wdm.edgeVersion", DriverManager.propertiesSelenium.getEdgeDriverVersion());
                    }
                    downloadNewestOrGivenVersionOfWebDriver(EdgeDriver.class);
                    OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(EdgeDriver.class).getDownloadedDriverPath(), browserPath);
                }
                driverDownloadedMicrosoftEdge = true;
            }
            System.setProperty("webdriver.edge.driver", browserPath);
            return new NewEdgeDriver(edgeOptions);
        }
        return driver;
    }

    private static INewWebDriver getFirefoxDriver(FirefoxOptions firefoxOptions) {
        INewWebDriver driver = DRIVERS.get();
        if (Objects.isNull(driver)) {
            String browserPath = DriverManager.propertiesSelenium.getSeleniumFirefox();
            boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
            synchronized (DOWNLOAD_DIR) {
                if (isDriverAutoUpdateActivated && !driverDownloadedFirefox) {
                    if (!DriverManager.propertiesSelenium.getGeckoDriverVersion().isEmpty()) {
                        System.setProperty("wdm.geckoDriverVersion", DriverManager.propertiesSelenium.getGeckoDriverVersion());
                    }
                    downloadNewestOrGivenVersionOfWebDriver(FirefoxDriver.class);
                    OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(FirefoxDriver.class).getDownloadedDriverPath(), browserPath);
                }
                driverDownloadedFirefox = true;
            }
            System.setProperty("webdriver.gecko.driver", browserPath);
            System.setProperty("webdriver.firefox.logfile", "logs\\firefox_logs.txt");
            return new NewFirefoxDriver(firefoxOptions);
        }
        return driver;
    }

    private static INewWebDriver getInternetExplorerDriver(InternetExplorerOptions internetExplorerOptions) {
        INewWebDriver driver = DRIVERS.get();
        if (Objects.isNull(driver)) {
            String browserPath = DriverManager.propertiesSelenium.getSeleniumIE();
            boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
            synchronized (DOWNLOAD_DIR) {
                if (isDriverAutoUpdateActivated && !driverDownloadedInternetExplorer) {
                    if (!DriverManager.propertiesSelenium.getInternetExplorerDriverVersion()
                            .equals("")) {
                        System.setProperty("wdm.internetExplorerDriverVersion", DriverManager.propertiesSelenium.getInternetExplorerDriverVersion());
                    }
                    downloadNewestOrGivenVersionOfWebDriver(InternetExplorerDriver.class);
                    OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(InternetExplorerDriver.class).getDownloadedDriverPath()
                            , browserPath);
                }
                driverDownloadedInternetExplorer = true;
            }
            System.setProperty("webdriver.ie.driver", browserPath);
            return new NewInternetExplorerDriver(internetExplorerOptions);
        }
        return driver;
    }

    private static INewWebDriver getRemoteDriver(MutableCapabilities options) {
        final String SELENIUM_GRID_URL = RuntimeParametersSelenium.SELENIUM_GRID.getValue();
        BFLogger.logDebug("Connecting to the selenium grid: " + SELENIUM_GRID_URL);
        DesiredCapabilities capabilities = new DesiredCapabilities();

        //browserName
        setBrowserName(capabilities, options);

        //browserVersion
        setBrowserVersion(capabilities);

        //platformName
        setPlatformName(capabilities);

        NewRemoteWebDriver newRemoteWebDriver = null;
        try {
            newRemoteWebDriver = new NewRemoteWebDriver(new URL(SELENIUM_GRID_URL), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("Unable to find selenium grid URL: " + SELENIUM_GRID_URL);
        }
        return newRemoteWebDriver;
    }

    private static void setBrowserName(DesiredCapabilities capabilities, MutableCapabilities options) {
        String browser = RuntimeParametersSelenium.BROWSER.getValue();
        if (Browser.CHROME.is(browser)) {
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        } else if (Browser.EDGE.is(browser)) {
            capabilities.setCapability(EdgeOptions.CAPABILITY, options);
        } else if (Browser.FIREFOX.is(browser)) {
            capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
        } else {
            throw new IllegalStateException("Unsupported browser: " + browser);
        }
        capabilities.setBrowserName(browser);
    }

    @SuppressWarnings("deprecation")
    private static void setBrowserVersion(DesiredCapabilities capabilities) {
        String browserVersion = RuntimeParametersSelenium.BROWSER_VERSION.getValue();
        if (!isEmpty(browserVersion)) {
            //Backward compatibility with Selenium 3 grids
            if (Boolean.parseBoolean(System.getProperty("selenium3grid", "false"))) {
                capabilities.setCapability(CapabilityType.VERSION, browserVersion);
            } else {
                capabilities.setVersion(browserVersion);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static void setPlatformName(DesiredCapabilities capabilities) {
        String operatingSystem = RuntimeParametersSelenium.OS.getValue();
        if (!isEmpty(operatingSystem)) {
            Platform platform = Platform.fromString(operatingSystem);
            //Backward compatibility with Selenium 3 grids
            if (Boolean.parseBoolean(System.getProperty("selenium3grid", "false"))) {
                capabilities.setCapability(CapabilityType.PLATFORM, platform);
            } else {
                capabilities.setPlatform(platform);
            }
        }
    }

    private static void setCommonBrowserOptions(AbstractDriverOptions options) {
        options.setImplicitWaitTimeout(IMPLICITLY_WAIT);
        //options.setPageLoadTimeout();
        //options.setScriptTimeout();
        //options.setProxy();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.DISMISS);
        options.setAcceptInsecureCerts(true);
        if (Boolean.parseBoolean(System.getProperty("selenium3grid", "false"))) {
            options.setCapability("acceptSslCerts", true);
        }

        RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
            if ((new AcceptedW3CCapabilityKeys()).test(key)) {
                BFLogger.logDebug("Add to browser capabilities: " + key + "=" + value);
                options.setCapability(key, value);
            }
        });
    }

    private static void setCommonChromiumOptions(ChromiumOptions options) {
        setCommonBrowserOptions(options);
        setChromiumHeadless(options);
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", DOWNLOAD_DIR);
        chromePrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
        chromePrefs.put("profile.default_content_setting_values.notifications", 2);

        RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
            if (!value.toString().isEmpty()) {
                BFLogger.logDebug("Add to Chromium prefs: " + key + "=" + value);
                chromePrefs.put(key, value);
            }
        });

        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--test-type");
        options.addArguments("window-size=1920x1080");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--disable-popup-blocking");

        RuntimeParametersSelenium.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
            String item = (value.toString().isEmpty()) ? key : key + "=" + value;
            BFLogger.logDebug("Add to Chromium arguments: " + item);
            options.addArguments(item);
        });
    }

    private static void setChromiumHeadless(ChromiumOptions options) {
        String headless = RuntimeParametersSelenium.HEADLESS.getValue();
        switch (headless.toLowerCase().trim()) {
            case "false":
                break;
            case "true":
                options.addArguments("--headless");
                break;
            case "chrome":
                options.addArguments("--headless=chrome");
                break;
            case "new":
                options.addArguments("--headless=new");
                break;
            default:
                throw new IllegalStateException("Unsupported Firefox headless state: " + headless);
        }
    }

    private static void setFirefoxHeadless(FirefoxOptions options) {
        String headless = RuntimeParametersSelenium.HEADLESS.getValue();
        switch (headless.toLowerCase().trim()) {
            case "false":
                break;
            case "true":
                options.addArguments("-headless");
                break;
            default:
                throw new IllegalStateException("Unsupported Firefox headless state: " + headless);
        }
    }

    @SuppressWarnings("removal")
    @Override
    // TODO: handle that finalize should never been called - it's unreliable. Introduce Autoclose()
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            closeDriver();
            BFLogger.logDebug("Closing Driver in finalize()");
        } catch (Exception e) {
            // TODO: handle that
        }
    }
}