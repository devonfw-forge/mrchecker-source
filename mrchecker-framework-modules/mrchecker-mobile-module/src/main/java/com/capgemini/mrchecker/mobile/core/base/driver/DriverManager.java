package com.capgemini.mrchecker.mobile.core.base.driver;


import com.capgemini.mrchecker.mobile.core.base.properties.PropertiesFileSettings;
import com.capgemini.mrchecker.mobile.core.base.runtime.RuntimeParameters;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.mobile.core.base.exceptions.BFAppiumServerNotConnectedException;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DriverManager {

	private static ThreadLocal<INewMobileDriver> drivers = new ThreadLocal<INewMobileDriver>();

	// Setup default variables
	private static final int                IMPLICITYWAITTIMER     = 2;                                    // in seconds
	private static       PropertiesFileSettings propertiesFileSettings;

	@Inject
	public DriverManager(@Named("properties") PropertiesFileSettings propertiesSelenium) {

		if (null == DriverManager.propertiesFileSettings) {
			DriverManager.propertiesFileSettings = propertiesSelenium;
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
				BFLogger.logDebug("Closing WebDriver for this thread. " + RuntimeParameters.PLATFORM_NAME.getValue());
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
		BFLogger.logDebug("Creating new " + RuntimeParameters.PLATFORM_NAME.toString() + " WebDriver.");
		INewMobileDriver driver;
		String appiumServerParameter = RuntimeParameters.APPIUM_SERVER_URL.getValue();
		if (isEmpty(appiumServerParameter)) {
//			driver = setupDevice();
			driver = null;
		} else {
			driver = setupAppiumServer();
		}
//		driver.manage()
//				.timeouts()
//				.implicitlyWait(DriverManager.IMPLICITYWAITTIMER, TimeUnit.SECONDS);

//		NewRemoteWebElement.setClickTimer();
		return driver;
	}

	private static boolean isEmpty(String appiumServerParameter) {
		return appiumServerParameter == null || appiumServerParameter.trim()
				.isEmpty();
	}

	/**
	 * Method sets Selenium Grid
	 */
	private static INewMobileDriver setupAppiumServer() {
		try {
			return Driver.APPIUM_SERVER.getDriver();
		} catch (WebDriverException e) {
			throw new BFAppiumServerNotConnectedException(e);
		}
	}

	/**
	 * Method sets desired 'driver' depends on chosen parameters
	 */
//	private static INewMobileDriver setupDevice() {
//		String device = RuntimeParameters.DEVICE_NAME.getValue();
//		switch (device) {
//			case "android":
//				return Driver.ANDROID.getDriver();
//			case "ios":
//				return Driver.IOS.getDriver();
//			case "windows":
//				return Driver.WINDOWS.getDriver();
//			default:
//				throw new RuntimeException("Unable to setup [" + device + "] device. Name not recognized. Possible values: android, ios, windows");
//		}
//	}

	private enum Driver {

//		ANDROID {
//			@Override
//			public INewMobileDriver getDriver() {
//				String browserPath = DriverManager.propertiesFileSettings.getSeleniumChrome();
//				boolean isDriverAutoUpdateActivated = DriverManager.propertiesFileSettings.getDriverAutoUpdateFlag();
//				synchronized (this) {
//					if (isDriverAutoUpdateActivated && !driverDownloadedChrome) {
//						if (!DriverManager.propertiesFileSettings.getChromeDriverVersion().equals("")) {
//							System.setProperty("wdm.chromeDriverVersion", DriverManager.propertiesFileSettings.getChromeDriverVersion());
//						}
//						downloadNewestOrGivenVersionOfWebDriver(ChromeDriver.class);
//						OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(ChromeDriver.class)
//								.getBinaryPath(), browserPath);
//					}
//					driverDownloadedChrome = true;
//				}
//
//				System.setProperty("webdriver.chrome.driver", browserPath);
//
//				//  https://github.com/appium/appium/tree/master/sample-code/java/src
//
////			AndroidCreateSessionTest https://github.com/appium/appium/blob/master/sample-code/java/src/AndroidCreateSessionTest.java
////				File classpathRoot = new File(System.getProperty("user.dir"));
////				File appDir = new File(classpathRoot, "../apps");
////				File app = new File(appDir.getCanonicalPath(), "ApiDemos-debug.apk");
////				DesiredCapabilities capabilities = new DesiredCapabilities();
////				capabilities.setCapability("deviceName", "Android Emulator");
////				capabilities.setCapability("app", app.getAbsolutePath());
////				capabilities.setCapability("appPackage", "io.appium.android.apis");
////				capabilities.setCapability("appActivity", ".ApiDemos");
////				driver = new AndroidDriver<WebElement>(getServiceUrl(), capabilities);
//
////			AndroidCreateWebSessionTest  https://github.com/appium/appium/blob/master/sample-code/java/src/AndroidCreateWebSessionTest.java
////				DesiredCapabilities capabilities = new DesiredCapabilities();
////				capabilities.setCapability("deviceName", "Android Emulator");
////				capabilities.setCapability("browserName", "Chrome");
////				driver = new AndroidDriver<WebElement>(getServiceUrl(), capabilities);
//
//				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
//				chromePrefs.put("download.default_directory", DOWNLOAD_DIR);
//				chromePrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
//				ChromeOptions options = new ChromeOptions();
//				options.setExperimentalOption("prefs", chromePrefs);
//				options.addArguments("--test-type");
//
//				// Set users browser options
//				RuntimeParameters.DEVICE_OPTIONS.getValues()
//						.forEach((key, value) -> {
//							BFLogger.logInfo("Device option: " + key + " " + value);
//							String item = (value.toString()
//									.isEmpty()) ? key : key + "=" + value;
//							options.addArguments(item);
//						});
//
//				// DesiredCapabilities cap = DesiredCapabilities.chrome();
//				// cap.setCapability(ChromeOptions.CAPABILITY, options);
//
//				//TODO Add capabilities added by user
//
////				AndroidCreateSessionTest https://github.com/appium/appium/blob/master/sample-code/java/src/AndroidCreateSessionTest.java
//								File classpathRoot = new File(System.getProperty("user.dir"));
//								File appDir = new File(classpathRoot, "../apps");
//				File app = null;
//				try {
//					app = new File(appDir.getCanonicalPath(), "ApiDemos-debug.apk");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				DesiredCapabilities capabilities = new DesiredCapabilities();
//				capabilities.setCapability("deviceName", "Android Emulator");
//				capabilities.setCapability("app", app.getAbsolutePath());
//				capabilities.setCapability("appPackage", "io.appium.android.apis");
//				capabilities.setCapability("appActivity", ".ApiDemos");
//
//				INewMobileDriver driver = new NewAndroidDriver(capabilities);
//				return driver;
//			}
//
//		},
//		IOS {
//			@Override
//			public INewMobileDriver getDriver() {
//				String browserPath = DriverManager.propertiesFileSettings.getSeleniumChrome();
//				boolean isDriverAutoUpdateActivated = DriverManager.propertiesFileSettings.getDriverAutoUpdateFlag();
//				synchronized (this) {
//					if (isDriverAutoUpdateActivated && !driverDownloadedChrome) {
//						if (!DriverManager.propertiesFileSettings.getChromeDriverVersion().equals("")) {
//							System.setProperty("wdm.chromeDriverVersion", DriverManager.propertiesFileSettings.getChromeDriverVersion());
//						}
//						downloadNewestOrGivenVersionOfWebDriver(ChromeDriver.class);
//						OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(ChromeDriver.class)
//								.getBinaryPath(), browserPath);
//					}
//					driverDownloadedChrome = true;
//				}
//
//				System.setProperty("webdriver.chrome.driver", browserPath);
//
//				//  https://github.com/appium/appium/tree/master/sample-code/java/src
//
//				// 			IOSCreateSessionTest https://github.com/appium/appium/blob/master/sample-code/java/src/IOSCreateSessionTest.java
//				//  	File classpathRoot = new File(System.getProperty("user.dir"));
//				//        File appDir = new File(classpathRoot, "../apps");
//				//        File app = new File(appDir.getCanonicalPath(), "TestApp.app.zip");
//				//        String deviceName = System.getenv("IOS_DEVICE_NAME");
//				//        String platformVersion = System.getenv("IOS_PLATFORM_VERSION");
//				//        DesiredCapabilities capabilities = new DesiredCapabilities();
//				//        capabilities.setCapability("deviceName", deviceName == null ? "iPhone 6s" : deviceName);
//				//        capabilities.setCapability("platformVersion", platformVersion == null ? "11.1" : platformVersion);
//				//        capabilities.setCapability("app", app.getAbsolutePath());
//				//        capabilities.setCapability("automationName", "XCUITest");
//				//        driver = new IOSDriver<WebElement>(getServiceUrl(), capabilities);
//
//				//			IOSCreateWebSessionTest   https://github.com/appium/appium/blob/master/sample-code/java/src/IOSCreateWebSessionTest.java
//				//				 String deviceName = System.getenv("IOS_DEVICE_NAME");
//				//        String platformVersion = System.getenv("IOS_PLATFORM_VERSION");
//				//        DesiredCapabilities capabilities = new DesiredCapabilities();
//				//        capabilities.setCapability("deviceName", deviceName == null ? "iPhone 6s" : deviceName);
//				//        capabilities.setCapability("platformVersion", platformVersion == null ? "11.1" : platformVersion);
//				//        capabilities.setCapability("browserName", "Safari");
//				//        capabilities.setCapability("automationName", "XCUITest");
//				//        driver = new IOSDriver<WebElement>(getServiceUrl(), capabilities);
//
//				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
//				chromePrefs.put("download.default_directory", DOWNLOAD_DIR);
//				chromePrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
//				ChromeOptions options = new ChromeOptions();
//				options.setExperimentalOption("prefs", chromePrefs);
//				options.addArguments("--test-type");
//
//				// Set users browser options
//				RuntimeParameters.BROWSER_OPTIONS.getValues()
//						.forEach((key, value) -> {
//							BFLogger.logInfo("Browser option: " + key + " " + value);
//							String item = (value.toString()
//									.isEmpty()) ? key : key + "=" + value;
//							options.addArguments(item);
//						});
//
//				// DesiredCapabilities cap = DesiredCapabilities.chrome();
//				// cap.setCapability(ChromeOptions.CAPABILITY, options);
//
//				INewMobileDriver driver = new NewChromeDriver(options);
//				return driver;
//			}
//
//		},
//		WINDOWS {
//			@Override
//			public INewMobileDriver getDriver() {
//				String browserPath = DriverManager.propertiesFileSettings.getSeleniumChrome();
//				boolean isDriverAutoUpdateActivated = DriverManager.propertiesFileSettings.getDriverAutoUpdateFlag();
//				synchronized (this) {
//					if (isDriverAutoUpdateActivated && !driverDownloadedChrome) {
//						if (!DriverManager.propertiesFileSettings.getChromeDriverVersion().equals("")) {
//							System.setProperty("wdm.chromeDriverVersion", DriverManager.propertiesFileSettings.getChromeDriverVersion());
//						}
//						downloadNewestOrGivenVersionOfWebDriver(ChromeDriver.class);
//						OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(ChromeDriver.class)
//								.getBinaryPath(), browserPath);
//					}
//					driverDownloadedChrome = true;
//				}
//
//				System.setProperty("webdriver.chrome.driver", browserPath);
//
//				//  https://github.com/appium/appium/blob/master/sample-code/java/src/WindowsDesktopAppTest.java
//
//				// 			WindowsDesktopAppTest
//				//  	DesiredCapabilities caps = new DesiredCapabilities();
//				//        caps.setCapability("platformVersion", "10");
//				//        caps.setCapability("platformName", "Windows");
//				//        caps.setCapability("deviceName", "WindowsPC");
//				//        caps.setCapability("app", "Microsoft.WindowsCalculator_8wekyb3d8bbwe!App");
//				//        caps.setCapability("newCommandTimeout", 2000);
//				//        driver = new WindowsDriver<>(getServiceUrl(), caps);
//
//				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
//				chromePrefs.put("download.default_directory", DOWNLOAD_DIR);
//				chromePrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
//				ChromeOptions options = new ChromeOptions();
//				options.setExperimentalOption("prefs", chromePrefs);
//				options.addArguments("--test-type");
//
//				// Set users browser options
//				RuntimeParameters.BROWSER_OPTIONS.getValues()
//						.forEach((key, value) -> {
//							BFLogger.logInfo("Browser option: " + key + " " + value);
//							String item = (value.toString()
//									.isEmpty()) ? key : key + "=" + value;
//							options.addArguments(item);
//						});
//
//				// DesiredCapabilities cap = DesiredCapabilities.chrome();
//				// cap.setCapability(ChromeOptions.CAPABILITY, options);
//
//				INewMobileDriver driver = new NewChromeDriver(options);
//				return driver;
//			}
//
//		},
		APPIUM_SERVER {
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


				final String APPIUM_SERVER_URL = RuntimeParameters.APPIUM_SERVER_URL.getValue() + "/wd/hub";
				BFLogger.logDebug("Connecting to the Appium Server: " + APPIUM_SERVER_URL);
				DesiredCapabilities capabilities = new DesiredCapabilities();

				capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, RuntimeParameters.AUTOMATION_NAME);
				capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, RuntimeParameters.PLATFORM_NAME);
				capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, RuntimeParameters.PLATFORM_VERSION);
				capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, RuntimeParameters.DEVICE_NAME);
				capabilities.setCapability(MobileCapabilityType.APP, RuntimeParameters.APPLICATION_PATH);
				capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, RuntimeParameters.BROWSER_NAME);

				// Set users device options
				RuntimeParameters.DEVICE_OPTIONS.getValues()
						.forEach((key, value) -> {
							BFLogger.logInfo("Device option: " + key + " " + value);
							capabilities.setCapability(key, value);
						});

				NewAppiumDriver newRemoteWebDriver = null;
				try {
					URL url = new URL(APPIUM_SERVER_URL);
					//url = new URL("http://target_ip:used_port/wd/hub");
					//if it needs to use locally started server
					//then the target_ip is 127.0.0.1 or 0.0.0.0
					//the default port is 4723

					newRemoteWebDriver = new NewAppiumDriver(url, capabilities);
				} catch (MalformedURLException e) {
					BFLogger.logError("Unable connect to Appium Server URL: " + APPIUM_SERVER_URL);
				}
				return newRemoteWebDriver;
			}
		};



		public INewMobileDriver getDriver() {
			return null;
		}

	}

}
