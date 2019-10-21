package com.capgemini.mrchecker.mobile.core.base.driver;

import com.capgemini.mrchecker.selenium.core.base.properties.PropertiesSelenium;
import com.capgemini.mrchecker.selenium.core.base.runtime.RuntimeParameters;
import com.capgemini.mrchecker.selenium.core.enums.ResolutionEnum;
import com.capgemini.mrchecker.selenium.core.exceptions.BFSeleniumGridNotConnectedException;
import com.capgemini.mrchecker.selenium.core.utils.OperationsOnFiles;
import com.capgemini.mrchecker.selenium.core.utils.ResolutionUtils;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.WebDriverManagerException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DriverManager {

	private static ThreadLocal<INewMobileDriver> drivers = new ThreadLocal<INewMobileDriver>();

	// Setup default variables
	private static final ResolutionEnum     DEFAULT_RESOLUTION     = ResolutionEnum.w1200;
	private static final int                IMPLICITYWAITTIMER     = 2;                                    // in seconds
	private static final String             DOWNLOAD_DIR           = System.getProperty("java.io.tmpdir");
	private static       boolean            driverDownloadedChrome = false;
	private static       boolean            driverDownloadedGecko  = false;
	private static       boolean            driverDownloadedIE     = false;
	private static       boolean            driverDownloadedEdge   = false;
	private static       boolean            driverDownloadedOpera  = false;
	private static       PropertiesSelenium propertiesSelenium;

	@Inject
	public DriverManager(@Named("properties") PropertiesSelenium propertiesSelenium) {

		if (null == DriverManager.propertiesSelenium) {
			DriverManager.propertiesSelenium = propertiesSelenium;
		}

		this.start();
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
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		try {
			closeDriver();
			BFLogger.logDebug("Closing Driver in finalize()");
		} catch (Exception e) {
		}

	}

	public static INewMobileDriver getDriver() {
		INewMobileDriver driver = drivers.get();
		if (driver == null) {
			driver = createDriver();
			drivers.set(driver);
			BFLogger.logDebug("driver:" + driver.toString());
		}
		return driver;
	}

	public static void closeDriver() {
		INewMobileDriver driver = drivers.get();
		if (driver == null) {
			BFLogger.logDebug("closeDriver() was called but there was no driver for this thread.");
		} else {
			try {
				BFLogger.logDebug("Closing WebDriver for this thread. " + RuntimeParameters.BROWSER.getValue());
				driver.quit();
			} catch (WebDriverException e) {
				BFLogger.logDebug("Ooops! Something went wrong while closing the driver: ");
				e.printStackTrace();
			} finally {
				driver = null;
				drivers.remove();
			}
		}
	}

	/**
	 * Method sets desired 'driver' depends on chosen parameters
	 */
	private static INewMobileDriver createDriver() {
		BFLogger.logDebug("Creating new " + RuntimeParameters.BROWSER.toString() + " WebDriver.");
		INewMobileDriver driver;
		String seleniumGridParameter = RuntimeParameters.SELENIUM_GRID.getValue();
		if (isEmpty(seleniumGridParameter)) {
			driver = setupBrowser();
		} else {
			driver = setupGrid();
		}
		driver.manage()
				.timeouts()
				.implicitlyWait(DriverManager.IMPLICITYWAITTIMER, TimeUnit.SECONDS);

		ResolutionUtils.setResolution(driver, DriverManager.DEFAULT_RESOLUTION);
		NewRemoteWebElement.setClickTimer();
		return driver;
	}

	private static boolean isEmpty(String seleniumGridParameter) {
		return seleniumGridParameter == null || seleniumGridParameter.trim()
				.isEmpty();
	}

	/**
	 * Method sets Selenium Grid
	 */
	private static INewMobileDriver setupGrid() {
		try {
			return Driver.SELENIUMGRID.getDriver();
		} catch (WebDriverException e) {
			throw new BFSeleniumGridNotConnectedException(e);
		}
	}

	/**
	 * Method sets desired 'driver' depends on chosen parameters
	 */
	private static INewMobileDriver setupBrowser() {
		String browser = RuntimeParameters.BROWSER.getValue();
		switch (browser) {
			case "chrome":
				return Driver.CHROME.getDriver();
			case "opera":
				return Driver.OPERA.getDriver();
			case "edge":
				return Driver.EDGE.getDriver();
			case "firefox":
				return Driver.FIREFOX.getDriver();
			case "internet explorer":
				return Driver.IE.getDriver();
			case "chromeheadless":
				return Driver.CHROME_HEADLESS.getDriver();
			default:
				throw new RuntimeException("Unable to setup [" + browser + "] browser. Browser not recognized.");
		}
	}

	private enum Driver {

		ANDROID {
			@Override
			public INewMobileDriver getDriver() {
				String browserPath = DriverManager.propertiesSelenium.getSeleniumChrome();
				boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
				synchronized (this) {
					if (isDriverAutoUpdateActivated && !driverDownloadedChrome) {
						if (!DriverManager.propertiesSelenium.getChromeDriverVersion().equals("")) {
							System.setProperty("wdm.chromeDriverVersion", DriverManager.propertiesSelenium.getChromeDriverVersion());
						}
						downloadNewestOrGivenVersionOfWebDriver(ChromeDriver.class);
						OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(ChromeDriver.class)
								.getBinaryPath(), browserPath);
					}
					driverDownloadedChrome = true;
				}

				System.setProperty("webdriver.chrome.driver", browserPath);

				//  https://github.com/appium/appium/tree/master/sample-code/java/src

//			AndroidCreateSessionTest https://github.com/appium/appium/blob/master/sample-code/java/src/AndroidCreateSessionTest.java
//				File classpathRoot = new File(System.getProperty("user.dir"));
//				File appDir = new File(classpathRoot, "../apps");
//				File app = new File(appDir.getCanonicalPath(), "ApiDemos-debug.apk");
//				DesiredCapabilities capabilities = new DesiredCapabilities();
//				capabilities.setCapability("deviceName", "Android Emulator");
//				capabilities.setCapability("app", app.getAbsolutePath());
//				capabilities.setCapability("appPackage", "io.appium.android.apis");
//				capabilities.setCapability("appActivity", ".ApiDemos");
//				driver = new AndroidDriver<WebElement>(getServiceUrl(), capabilities);

//			AndroidCreateWebSessionTest  https://github.com/appium/appium/blob/master/sample-code/java/src/AndroidCreateWebSessionTest.java
//				DesiredCapabilities capabilities = new DesiredCapabilities();
//				capabilities.setCapability("deviceName", "Android Emulator");
//				capabilities.setCapability("browserName", "Chrome");
//				driver = new AndroidDriver<WebElement>(getServiceUrl(), capabilities);

				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("download.default_directory", DOWNLOAD_DIR);
				chromePrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("--test-type");

				// Set users browser options
				RuntimeParameters.BROWSER_OPTIONS.getValues()
						.forEach((key, value) -> {
							BFLogger.logInfo("Browser option: " + key + " " + value);
							String item = (value.toString()
									.isEmpty()) ? key : key + "=" + value;
							options.addArguments(item);
						});

				// DesiredCapabilities cap = DesiredCapabilities.chrome();
				// cap.setCapability(ChromeOptions.CAPABILITY, options);

				INewMobileDriver driver = new NewChromeDriver(options);
				return driver;
			}

		},
		IOS {
			@Override
			public INewMobileDriver getDriver() {
				String browserPath = DriverManager.propertiesSelenium.getSeleniumChrome();
				boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
				synchronized (this) {
					if (isDriverAutoUpdateActivated && !driverDownloadedChrome) {
						if (!DriverManager.propertiesSelenium.getChromeDriverVersion().equals("")) {
							System.setProperty("wdm.chromeDriverVersion", DriverManager.propertiesSelenium.getChromeDriverVersion());
						}
						downloadNewestOrGivenVersionOfWebDriver(ChromeDriver.class);
						OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(ChromeDriver.class)
								.getBinaryPath(), browserPath);
					}
					driverDownloadedChrome = true;
				}

				System.setProperty("webdriver.chrome.driver", browserPath);

				//  https://github.com/appium/appium/tree/master/sample-code/java/src

				// 			IOSCreateSessionTest https://github.com/appium/appium/blob/master/sample-code/java/src/IOSCreateSessionTest.java
				//  	File classpathRoot = new File(System.getProperty("user.dir"));
				//        File appDir = new File(classpathRoot, "../apps");
				//        File app = new File(appDir.getCanonicalPath(), "TestApp.app.zip");
				//        String deviceName = System.getenv("IOS_DEVICE_NAME");
				//        String platformVersion = System.getenv("IOS_PLATFORM_VERSION");
				//        DesiredCapabilities capabilities = new DesiredCapabilities();
				//        capabilities.setCapability("deviceName", deviceName == null ? "iPhone 6s" : deviceName);
				//        capabilities.setCapability("platformVersion", platformVersion == null ? "11.1" : platformVersion);
				//        capabilities.setCapability("app", app.getAbsolutePath());
				//        capabilities.setCapability("automationName", "XCUITest");
				//        driver = new IOSDriver<WebElement>(getServiceUrl(), capabilities);

				//			IOSCreateWebSessionTest   https://github.com/appium/appium/blob/master/sample-code/java/src/IOSCreateWebSessionTest.java
				//				 String deviceName = System.getenv("IOS_DEVICE_NAME");
				//        String platformVersion = System.getenv("IOS_PLATFORM_VERSION");
				//        DesiredCapabilities capabilities = new DesiredCapabilities();
				//        capabilities.setCapability("deviceName", deviceName == null ? "iPhone 6s" : deviceName);
				//        capabilities.setCapability("platformVersion", platformVersion == null ? "11.1" : platformVersion);
				//        capabilities.setCapability("browserName", "Safari");
				//        capabilities.setCapability("automationName", "XCUITest");
				//        driver = new IOSDriver<WebElement>(getServiceUrl(), capabilities);

				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("download.default_directory", DOWNLOAD_DIR);
				chromePrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("--test-type");

				// Set users browser options
				RuntimeParameters.BROWSER_OPTIONS.getValues()
						.forEach((key, value) -> {
							BFLogger.logInfo("Browser option: " + key + " " + value);
							String item = (value.toString()
									.isEmpty()) ? key : key + "=" + value;
							options.addArguments(item);
						});

				// DesiredCapabilities cap = DesiredCapabilities.chrome();
				// cap.setCapability(ChromeOptions.CAPABILITY, options);

				INewMobileDriver driver = new NewChromeDriver(options);
				return driver;
			}

		},
		WINDOWS {
			@Override
			public INewMobileDriver getDriver() {
				String browserPath = DriverManager.propertiesSelenium.getSeleniumChrome();
				boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
				synchronized (this) {
					if (isDriverAutoUpdateActivated && !driverDownloadedChrome) {
						if (!DriverManager.propertiesSelenium.getChromeDriverVersion().equals("")) {
							System.setProperty("wdm.chromeDriverVersion", DriverManager.propertiesSelenium.getChromeDriverVersion());
						}
						downloadNewestOrGivenVersionOfWebDriver(ChromeDriver.class);
						OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(ChromeDriver.class)
								.getBinaryPath(), browserPath);
					}
					driverDownloadedChrome = true;
				}

				System.setProperty("webdriver.chrome.driver", browserPath);

				//  https://github.com/appium/appium/blob/master/sample-code/java/src/WindowsDesktopAppTest.java

				// 			WindowsDesktopAppTest
				//  	DesiredCapabilities caps = new DesiredCapabilities();
				//        caps.setCapability("platformVersion", "10");
				//        caps.setCapability("platformName", "Windows");
				//        caps.setCapability("deviceName", "WindowsPC");
				//        caps.setCapability("app", "Microsoft.WindowsCalculator_8wekyb3d8bbwe!App");
				//        caps.setCapability("newCommandTimeout", 2000);
				//        driver = new WindowsDriver<>(getServiceUrl(), caps);

				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("download.default_directory", DOWNLOAD_DIR);
				chromePrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("--test-type");

				// Set users browser options
				RuntimeParameters.BROWSER_OPTIONS.getValues()
						.forEach((key, value) -> {
							BFLogger.logInfo("Browser option: " + key + " " + value);
							String item = (value.toString()
									.isEmpty()) ? key : key + "=" + value;
							options.addArguments(item);
						});

				// DesiredCapabilities cap = DesiredCapabilities.chrome();
				// cap.setCapability(ChromeOptions.CAPABILITY, options);

				INewMobileDriver driver = new NewChromeDriver(options);
				return driver;
			}

		},
		SELENIUMGRID {
			@Override
			public INewMobileDriver getDriver() {

//				AndroidBrowserSaucelabsTest  https://github.com/appium/appium/blob/master/sample-code/java/src/AndroidBrowserSaucelabsTest.java
				// 				public static final String USERNAME = "YOUR_USERNAME";
//				public static final String ACCESS_KEY = "YOUR_ACESS_KEY";
//				public static final String URL = "https://"+USERNAME+":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub";
//				public static AndroidDriver<?> mobiledriver;
//
//				@BeforeTest
//				public void beforeTest( ) throws MalformedURLException {
//					DesiredCapabilities capabilities = new DesiredCapabilities();
//					capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "4.4");
//					capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,"Android");
//					capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,"UiAutomator2");
//					capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Samsung Galaxy S4 Emulator");
//					capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Browser");
//					capabilities.setCapability("newCommandTimeout", 2000);
//					mobiledriver = new AndroidDriver<>(new URL(URL), capabilities);


				final String SELENIUM_GRID_URL = RuntimeParameters.SELENIUM_GRID.getValue();
				BFLogger.logDebug("Connecting to the selenium grid: " + SELENIUM_GRID_URL);
				DesiredCapabilities capabilities = new DesiredCapabilities();
				String operatingSystem = RuntimeParameters.OS.getValue();

				// TODO add others os's
				switch (operatingSystem) {
					case "windows":
						capabilities.setPlatform(Platform.WINDOWS);
						break;
					case "vista":
						capabilities.setPlatform(Platform.VISTA);
						break;
					case "mac":
						capabilities.setPlatform(Platform.MAC);
						break;
				}

				capabilities.setVersion(RuntimeParameters.BROWSER_VERSION.getValue());
				capabilities.setBrowserName(RuntimeParameters.BROWSER.getValue());

				// Set users browser options
				RuntimeParameters.BROWSER_OPTIONS.getValues()
						.forEach((key, value) -> {
							BFLogger.logInfo("Browser option: " + key + " " + value);
							capabilities.setCapability(key, value);
						});

				NewRemoteWebDriver newRemoteWebDriver = null;
				try {
					newRemoteWebDriver = new NewRemoteWebDriver(new URL(SELENIUM_GRID_URL), capabilities);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					System.out.println("Unable to find selenium grid URL: " + SELENIUM_GRID_URL);
				}
				return newRemoteWebDriver;
			}
		};

		private static <T extends RemoteWebDriver> void downloadNewestOrGivenVersionOfWebDriver(Class<T> webDriverType) {

			String proxy = DriverManager.propertiesSelenium.getProxy();
			String webDriversPath = DriverManager.propertiesSelenium.getWebDrivers();
			try {
				System.setProperty("wdm.targetPath", webDriversPath);
				System.setProperty("wdm.useBetaVersions", "false");

				WebDriverManager.getInstance(webDriverType)
						.proxy(proxy)
						.setup();
				BFLogger.logDebug("Downloaded version of driver=" + WebDriverManager.getInstance(webDriverType).getDownloadedVersion());

			} catch (WebDriverManagerException e) {
				BFLogger.logInfo("Unable to download driver automatically. "
						+ "Please try to set up the proxy in properties file. "
						+ "If you want to download them manually, go to the "
						+ "http://www.seleniumhq.org/projects/webdriver/ site.");
			}

		}

		public INewMobileDriver getDriver() {
			return null;
		}

	}

}
