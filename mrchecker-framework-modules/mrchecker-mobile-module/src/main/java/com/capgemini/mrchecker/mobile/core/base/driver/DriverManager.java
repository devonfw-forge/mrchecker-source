package com.capgemini.mrchecker.mobile.core.base.driver;


import com.capgemini.mrchecker.mobile.core.base.properties.PropertiesFileSettings;
import com.capgemini.mrchecker.mobile.core.base.runtime.RuntimeParameters;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.capgemini.mrchecker.mobile.core.base.exceptions.BFAppiumServerNotConnectedException;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverManager {

	private static ThreadLocal<INewMobileDriver> drivers = new ThreadLocal<>();

	// Setup default variables
	private static       PropertiesFileSettings propertiesFileSettings;

	@Inject
	public DriverManager(@Named("properties") PropertiesFileSettings propertiesFileSettings) {

		if (null == DriverManager.propertiesFileSettings) {
			DriverManager.propertiesFileSettings = propertiesFileSettings;
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
			BFLogger.logDebug("Creating new " + RuntimeParameters.PLATFORM_NAME.toString() + " WebDriver.");
			driver = setupAppiumServer();
			drivers.set(driver);
			BFLogger.logDebug("driver:" + driver.toString());
		}
		return driver;
	}

	public static AndroidDriver getDriverAndroid() {
		//TODO:  Validate cast !!!!!
		NewAndroidDriver driver = (NewAndroidDriver) drivers.get();
		if (driver == null) {
			BFLogger.logDebug("Creating new Android " + RuntimeParameters.PLATFORM_NAME.toString() + " WebDriver.");
			driver = setupAndroidSession();
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
				BFLogger.logError(e.getMessage());
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
		INewMobileDriver driver = setupAppiumServer();
		return driver;
	}

	/**
	 * Method sets Appium session
	 */
	private static INewMobileDriver setupAppiumServer() {
		try {
			return new DriverManagerForAppium().getDriver();
		} catch (WebDriverException e) {
			throw new BFAppiumServerNotConnectedException(e);
		}
	}

	/**
	 * Method sets Android session
	 */
	private static NewAndroidDriver setupAndroidSession() {
		try {
			return new DriverManagerForAndroid().getDriver();
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
//		APPIUM_SERVER {
//			@Override
//			public INewMobileDriver getDriver() {
//
//				final String APPIUM_SERVER_URL = RuntimeParameters.DEVICE_URL.getValue() + "/wd/hub";
//				BFLogger.logDebug("Connecting to the Appium Server: " + APPIUM_SERVER_URL);
//
//				DesiredCapabilities capabilities = new DesiredCapabilities();
//				capabilities.setCapability(RuntimeParameters.AUTOMATION_NAME.getKey(), RuntimeParameters.AUTOMATION_NAME.getValue());
//				capabilities.setCapability(RuntimeParameters.PLATFORM_NAME.getKey(), RuntimeParameters.PLATFORM_NAME.getValue());
//				capabilities.setCapability(RuntimeParameters.PLATFORM_VERSION.getKey(), RuntimeParameters.PLATFORM_VERSION.getValue());
//				capabilities.setCapability(RuntimeParameters.DEVICE_NAME.getKey(), RuntimeParameters.DEVICE_NAME.getValue());
//				capabilities.setCapability(RuntimeParameters.APP.getKey(), RuntimeParameters.APP.getValue());
//				capabilities.setCapability(RuntimeParameters.BROWSER_NAME.getKey(), RuntimeParameters.BROWSER_NAME.getValue());
//
//				// Set users device options
//				RuntimeParameters.DEVICE_OPTIONS.getValues()
//						.forEach((key, value) -> {
//							BFLogger.logInfo("Device option: " + key + " " + value);
//							capabilities.setCapability(key, value);
//						});
//
//				NewAppiumDriver newRemoteWebDriver = null;
//				try {
//					URL url = new URL(APPIUM_SERVER_URL);
//					//url = new URL("http://target_ip:used_port/wd/hub");
//					//if it needs to use locally started server
//					//then the target_ip is 127.0.0.1 or 0.0.0.0
//					//the default port is 4723
//					BFLogger.logDebug("Capabilities: " + capabilities.toJson());
//					newRemoteWebDriver = new NewAppiumDriver(url, capabilities);
//				} catch (MalformedURLException e) {
//					BFLogger.logError("Unable connect to Appium Server URL: " + APPIUM_SERVER_URL);
//				}
//				return newRemoteWebDriver;
//			}
//		};
//
//
//
//		public INewMobileDriver getDriver() {
//			return null;
//		}

	}


	private interface IDriverManager{

		String getUrl();
		<T extends NewAppiumDriver> INewMobileDriver getDriver();
		DesiredCapabilities getCapabilities();
	}

	private static class DriverManagerForAppium implements IDriverManager{

		public String getUrl(){
			return RuntimeParameters.DEVICE_URL.getValue() + "/wd/hub";
		}


		public DesiredCapabilities getCapabilities(){
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(RuntimeParameters.AUTOMATION_NAME.getKey(), RuntimeParameters.AUTOMATION_NAME.getValue());
			capabilities.setCapability(RuntimeParameters.PLATFORM_NAME.getKey(), RuntimeParameters.PLATFORM_NAME.getValue());
			capabilities.setCapability(RuntimeParameters.PLATFORM_VERSION.getKey(), RuntimeParameters.PLATFORM_VERSION.getValue());
			capabilities.setCapability(RuntimeParameters.DEVICE_NAME.getKey(), RuntimeParameters.DEVICE_NAME.getValue());
			capabilities.setCapability(RuntimeParameters.APP.getKey(), RuntimeParameters.APP.getValue());
			capabilities.setCapability(RuntimeParameters.BROWSER_NAME.getKey(), RuntimeParameters.BROWSER_NAME.getValue());

			// Set users device options
			RuntimeParameters.DEVICE_OPTIONS.getValues()
					.forEach((key, value) -> {
						BFLogger.logInfo("Device option: " + key + " " + value);
						capabilities.setCapability(key, value);
					});


			BFLogger.logDebug("Capabilities: " + capabilities.toJson());
			return capabilities;
		}



		public INewMobileDriver getDriver(){

			BFLogger.logDebug("Connecting to the Appium Server: " + getUrl());

			INewMobileDriver newRemoteWebDriver = null;
			try {
				URL url = new URL(getUrl());
				//url = new URL("http://target_ip:used_port/wd/hub");
				//if it needs to use locally started server
				//then the target_ip is 127.0.0.1 or 0.0.0.0
				//the default port is 4723

				newRemoteWebDriver = new NewAppiumDriver(url, getCapabilities());
			} catch (MalformedURLException e) {
				BFLogger.logError("Unable connect to Appium Server URL: " + getUrl());
			}
			return newRemoteWebDriver;
		};


	}

	private static class DriverManagerForAndroid extends DriverManagerForAppium implements IDriverManager{

		//TODO:  Is it possible to connect to Android without Appium Server  /wd/hub  sub-url ?
		public String getUrl(){
			return RuntimeParameters.DEVICE_URL.getValue() + "/wd/hub";
		}


		@Override
		public DesiredCapabilities getCapabilities(){
			DesiredCapabilities capabilities = super.getCapabilities();
			String key = "";
			String value = "";
			capabilities.setCapability(key, value);
			return capabilities;
		};


		@Override public NewAndroidDriver getDriver(){return null;};

	}

}
