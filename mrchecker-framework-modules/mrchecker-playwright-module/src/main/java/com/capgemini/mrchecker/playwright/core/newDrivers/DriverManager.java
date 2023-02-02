package com.capgemini.mrchecker.playwright.core.newDrivers;

import com.capgemini.mrchecker.playwright.core.base.properties.PropertiesPlaywright;
import com.capgemini.mrchecker.playwright.core.base.runtime.RuntimeParametersPlaywright;
import com.capgemini.mrchecker.playwright.core.enums.ResolutionEnum;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.Proxy;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

public class DriverManager {
    private static final ThreadLocal<NewPlaywright> PLAYWRIGHT_THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<INewBrowserContext> BROWSER_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();
    private static final ResolutionEnum DEFAULT_RESOLUTION = ResolutionEnum.w1920;
    private static final Duration IMPLICITLY_WAIT = Duration.ofSeconds(2);
    public static final Duration EXPLICIT_WAIT = Duration.ofSeconds(20);
    private static final String DOWNLOAD_DIR = System.getProperty("java.io.tmpdir");
    private static boolean driverDownloadedChrome = false;
    private static boolean driverDownloadedFirefox = false;
    private static boolean driverDownloadedMicrosoftEdge = false;
    private static boolean driverDownloadedInternetExplorer = false;
    private static PropertiesPlaywright propertiesPlaywright;

    @Inject
    public DriverManager(@Named("properties") PropertiesPlaywright propertiesPlaywright) {
        if (Objects.isNull(DriverManager.propertiesPlaywright)) {
            DriverManager.propertiesPlaywright = propertiesPlaywright;
        }
    }

    public void start() {
        DriverManager.getDriver();
    }

    public void stop() {
        try {
            closeDriver();
            BFLogger.logDebug("Closing Driver in stop()");
        } catch (Exception e) {
            // TODO: handle that
        }
    }

    public static INewBrowserContext getDriver() {
        return getDriver(getLaunchOptions(), getContextOptions());
    }

    public static INewBrowserContext getDriver(BrowserType.LaunchOptions launchOptions, Browser.NewContextOptions contextOptions) {
        if (Objects.isNull(BROWSER_CONTEXT_THREAD_LOCAL.get())) {
            PLAYWRIGHT_THREAD_LOCAL.set(new NewPlaywright(Playwright.create(getCreateOptions())));
            String browserParam = RuntimeParametersPlaywright.BROWSER.getValue();
            NewBrowserType browserType;
            switch (browserParam.toLowerCase()) {
                case "chrome":
                case "edge":
                case "msedge":
                    browserType = (NewBrowserType) PLAYWRIGHT_THREAD_LOCAL.get().chromium();
                    break;
                case "firefox":
                    browserType = (NewBrowserType) PLAYWRIGHT_THREAD_LOCAL.get().firefox();
                    break;
                case "webkit":
                    browserType = (NewBrowserType) PLAYWRIGHT_THREAD_LOCAL.get().webkit();
                    break;
                default:
                    throw new IllegalStateException("Unsupported browser: " + browserParam);
            }
            NewBrowser browser = (NewBrowser) browserType.launch(launchOptions);
            BROWSER_CONTEXT_THREAD_LOCAL.set((INewBrowserContext) browser.newContext(contextOptions));
        }
        return BROWSER_CONTEXT_THREAD_LOCAL.get();
    }

    public static void closeDriver() {
        if (Objects.isNull(PLAYWRIGHT_THREAD_LOCAL.get())) {
            BFLogger.logDebug("closeDriver() was called but there was no driver for this thread.");
        } else {
            try {
                BFLogger.logDebug("Closing WebDriver for this thread. " + RuntimeParametersPlaywright.BROWSER.getValue());
                PLAYWRIGHT_THREAD_LOCAL.get().close();
            } catch (Exception e) {
                BFLogger.logError("Ooops! Something went wrong while closing the driver: ");
                e.printStackTrace();
            } finally {
                PLAYWRIGHT_THREAD_LOCAL.remove();
                BROWSER_CONTEXT_THREAD_LOCAL.remove();
            }
        }
    }

    public static boolean wasDriverCreated() {
        return Objects.nonNull(BROWSER_CONTEXT_THREAD_LOCAL.get());
    }

    private static Playwright.CreateOptions getCreateOptions() {
        Playwright.CreateOptions options = new Playwright.CreateOptions();
        Map<String, String> env = new HashMap<>();
        env.put("PLAYWRIGHT_BROWSERS_PATH", propertiesPlaywright.getBrowsersPath());
        env.put("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", propertiesPlaywright.getSkipBrowserDownload() + "");

        //Support Selenium Grid 4
        String seleniumGridParameter = RuntimeParametersPlaywright.SELENIUM_GRID.getValue();
        if (!isEmpty(seleniumGridParameter)) {
            BFLogger.logDebug("Connecting to the selenium grid: " + seleniumGridParameter);
            env.put("SELENIUM_REMOTE_URL", seleniumGridParameter);
            //ToDo: Verify connection to S4 and check if additional capabilities are needed
            env.put("SELENIUM_REMOTE_CAPABILITIES", "");
        }

        options.setEnv(env);
        return options;
    }

    private static boolean isEmpty(String seleniumGridParameter) {
        return seleniumGridParameter == null || seleniumGridParameter.trim().isEmpty();
    }

    public static BrowserType.LaunchOptions getLaunchOptions() {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions();
        options.setHeadless(propertiesPlaywright.getHeadless());
        Proxy proxy = propertiesPlaywright.getProxy();
        if (Objects.nonNull(proxy)) {
            options.setProxy(proxy);
        }

        List<String> arguments = new ArrayList<>();
        RuntimeParametersPlaywright.BROWSER_OPTIONS.getValues().forEach((key, value) -> {
            BFLogger.logDebug("Add launch argument: " + key + " = " + value.toString());
            arguments.add(value.toString().isEmpty() ? key : key + "=" + value);
        });
        options.setArgs(arguments);

        String channel = propertiesPlaywright.getChannel();
        if (Objects.nonNull(channel) && !channel.isEmpty()) {
            options.setChannel(channel);
        }
        options.setDownloadsPath(Paths.get(DOWNLOAD_DIR));
        return options;
    }

    public static Browser.NewContextOptions getContextOptions() {
        Browser.NewContextOptions options = new Browser.NewContextOptions();
        options.setScreenSize(DEFAULT_RESOLUTION.getWidth(), DEFAULT_RESOLUTION.getHeight());
        return options;
    }

    @SuppressWarnings("removal")
    @Override
    // TODO: handle that finalize should never been called - it's unreliable. Intoduce Autoclose()
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