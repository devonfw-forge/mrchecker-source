package com.capgemini.mrchecker.selenium.core.newDrivers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.capgemini.mrchecker.selenium.core.base.properties.PropertiesSelenium;
import com.capgemini.mrchecker.selenium.core.base.runtime.RuntimeParametersSelenium;
import com.capgemini.mrchecker.selenium.core.enums.ResolutionEnum;
import com.capgemini.mrchecker.selenium.core.exceptions.BFSeleniumGridNotConnectedException;
import com.capgemini.mrchecker.selenium.core.utils.OperationsOnFiles;
import com.capgemini.mrchecker.selenium.core.utils.ResolutionUtils;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.WebDriverManagerException;

public class DriverManager {
	
	private static final ThreadLocal<INewWebDriver> drivers = new ThreadLocal<>();
	
	// Setup default variables
	private static final ResolutionEnum	DEFAULT_RESOLUTION		= ResolutionEnum.w1200;
	private static final int			IMPLICITYWAITTIMER		= 2;									// in seconds
	private static final String			DOWNLOAD_DIR			= System.getProperty("java.io.tmpdir");
	private static boolean				driverDownloadedChrome	= false;
	private static boolean				driverDownloadedGecko	= false;
	private static boolean				driverDownloadedIE		= false;
	private static boolean				driverDownloadedEdge	= false;
	private static boolean				driverDownloadedOpera	= false;
	private static PropertiesSelenium	propertiesSelenium;
	
	@Inject
	public DriverManager(@Named("properties") PropertiesSelenium propertiesSelenium) {
		
		if (Objects.isNull(DriverManager.propertiesSelenium)) {
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
			// TODO: handle that
		}
	}
	
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
	
	public static INewWebDriver getDriver() {
		INewWebDriver driver = drivers.get();
		if (Objects.isNull(driver)) {
			driver = createDriver();
			drivers.set(driver);
			BFLogger.logDebug("Driver:" + driver);
		}
		
		return driver;
	}
	
	public static void closeDriver() {
		INewWebDriver driver = drivers.get();
		if (Objects.isNull(driver)) {
			BFLogger.logDebug("closeDriver() was called but there was no driver for this thread.");
		} else {
			try {
				BFLogger.logDebug("Closing WebDriver for this thread. " + RuntimeParametersSelenium.BROWSER.getValue());
				driver.quit();
			} catch (WebDriverException e) {
				BFLogger.logDebug("Ooops! Something went wrong while closing the driver: ");
				e.printStackTrace();
			} finally {
				drivers.remove();
			}
		}
	}
	
	/**
	 * Method sets desired 'driver' depends on chosen parameters
	 */
	private static INewWebDriver createDriver() {
		BFLogger.logDebug("Creating new " + RuntimeParametersSelenium.BROWSER.toString() + " WebDriver.");
		INewWebDriver driver;
		String seleniumGridParameter = RuntimeParametersSelenium.SELENIUM_GRID.getValue();
		
		driver = isEmpty(seleniumGridParameter) ? setupBrowser() : setupGrid();
		
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
	private static INewWebDriver setupGrid() {
		try {
			return Driver.SELENIUMGRID.getDriver();
		} catch (WebDriverException e) {
			throw new BFSeleniumGridNotConnectedException(e);
		}
	}
	
	/**
	 * Method sets desired 'driver' depends on chosen parameters
	 */
	private static INewWebDriver setupBrowser() {
		String browser = RuntimeParametersSelenium.BROWSER.getValue();
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
		
		CHROME {
			@Override
			public INewWebDriver getDriver() {
				String browserPath = DriverManager.propertiesSelenium.getSeleniumChrome();
				boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
				synchronized (this) {
					if (isDriverAutoUpdateActivated && !driverDownloadedChrome) {
						if (!DriverManager.propertiesSelenium.getChromeDriverVersion()
								.equals("")) {
							System.setProperty("wdm.chromeDriverVersion", DriverManager.propertiesSelenium.getChromeDriverVersion());
						}
						downloadNewestOrGivenVersionOfWebDriver(ChromeDriver.class);
						OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(ChromeDriver.class)
								.getBinaryPath(), browserPath);
					}
					driverDownloadedChrome = true;
				}
				
				System.setProperty("webdriver.chrome.driver", browserPath);
				HashMap<String, Object> chromePrefs = new HashMap<>();
				chromePrefs.put("download.default_directory", DOWNLOAD_DIR);
				chromePrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("--test-type");
				
				// Set users browser options
				RuntimeParametersSelenium.BROWSER_OPTIONS.getValues()
						.forEach((key, value) -> {
							BFLogger.logInfo("Browser option: " + key + " " + value);
							String item = (value.toString()
									.isEmpty()) ? key : key + "=" + value;
							options.addArguments(item);
						});
						
				// DesiredCapabilities cap = DesiredCapabilities.chrome();
				// cap.setCapability(ChromeOptions.CAPABILITY, options);
				
				return new NewChromeDriver(options);
			}
			
		},
		EDGE {
			@Override
			public INewWebDriver getDriver() {
				// Microsoft WebDriver for Microsoft Edge from version 18 is a Windows Feature on Demand.
				// To install run the following in an elevated command prompt:
				// DISM.exe /Online /Add-Capability /CapabilityName:Microsoft.WebDriver~~~~0.0.1.0
				// For builds prior to 18, download the appropriate driver for your installed version of Microsoft Edge
				// Info: https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/#downloads
				boolean featureOnDemand = DriverManager.propertiesSelenium.getEdgeDriverFeatureOnDemandFlag();
				if (!featureOnDemand) {
					String browserPath = DriverManager.propertiesSelenium.getSeleniumEdge();
					boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
					synchronized (this) {
						if (isDriverAutoUpdateActivated && !driverDownloadedEdge) {
							if (!DriverManager.propertiesSelenium.getEdgeDriverVersion()
									.equals("")) {
								System.setProperty("wdm.edgeVersion", DriverManager.propertiesSelenium.getEdgeDriverVersion());
							}
							downloadNewestOrGivenVersionOfWebDriver(EdgeDriver.class);
							OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(EdgeDriver.class)
									.getBinaryPath(), browserPath);
						}
						driverDownloadedEdge = true;
					}
					System.setProperty("webdriver.edge.driver", browserPath);
				}
				
				EdgeOptions options = new EdgeOptions();
				
				// Set users browser options
				RuntimeParametersSelenium.BROWSER_OPTIONS.getValues()
						.forEach((key, value) -> {
							BFLogger.logInfo("Browser option: " + key + " " + value);
							options.setCapability(key, value);
						});
						
				return new NewEdgeDriver(options);
			}
			
		},
		OPERA {
			@Override
			public INewWebDriver getDriver() {
				String browserPath = DriverManager.propertiesSelenium.getSeleniumOpera();
				boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
				synchronized (this) {
					if (isDriverAutoUpdateActivated && !driverDownloadedOpera) {
						if (!DriverManager.propertiesSelenium.getOperaDriverVersion()
								.equals("")) {
							System.setProperty("wdm.operaDriverVersion", DriverManager.propertiesSelenium.getOperaDriverVersion());
						}
						downloadNewestOrGivenVersionOfWebDriver(OperaDriver.class);
						OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(OperaDriver.class)
								.getBinaryPath(), browserPath);
					}
					driverDownloadedOpera = true;
				}
				
				System.setProperty("webdriver.opera.driver", browserPath);
				HashMap<String, Object> operaPrefs = new HashMap<>();
				operaPrefs.put("download.default_directory", DOWNLOAD_DIR);
				operaPrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
				OperaOptions options = new OperaOptions();
				options.setExperimentalOption("prefs", operaPrefs);
				options.addArguments("--test-type");
				
				// Set users browser options
				RuntimeParametersSelenium.BROWSER_OPTIONS.getValues()
						.forEach((key, value) -> {
							BFLogger.logInfo("Browser option: " + key + " " + value);
							options.setCapability(key, value);
						});
						
				return new NewOperaDriver(options);
			}
		},
		CHROME_HEADLESS {
			@Override
			public INewWebDriver getDriver() {
				String browserPath = DriverManager.propertiesSelenium.getSeleniumChrome();
				boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
				synchronized (this) {
					if (isDriverAutoUpdateActivated && !driverDownloadedChrome) {
						if (!DriverManager.propertiesSelenium.getChromeDriverVersion()
								.equals("")) {
							System.setProperty("wdm.chromeDriverVersion", DriverManager.propertiesSelenium.getChromeDriverVersion());
						}
						downloadNewestOrGivenVersionOfWebDriver(ChromeDriver.class);
						OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(ChromeDriver.class)
								.getBinaryPath(), browserPath);
					}
					driverDownloadedChrome = true;
				}
				System.setProperty("webdriver.chrome.driver", browserPath);
				HashMap<String, Object> chromePrefs = new HashMap<>();
				chromePrefs.put("download.default_directory", DOWNLOAD_DIR);
				chromePrefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", chromePrefs);
				options.addArguments("headless");
				options.addArguments("window-size=1200x600");
				
				// Set users browser options
				RuntimeParametersSelenium.BROWSER_OPTIONS.getValues()
						.forEach((key, value) -> {
							BFLogger.logInfo("Browser option: " + key + " " + value);
							String item = (value.toString()
									.isEmpty()) ? key : key + "=" + value;
							options.addArguments(item);
						});
						
				// DesiredCapabilities cap = DesiredCapabilities.chrome();
				// cap.setCapability(ChromeOptions.CAPABILITY, options);
				
				return new NewChromeDriver(options);
			}
			
		},
		FIREFOX {
			@Override
			public INewWebDriver getDriver() {
				String browserPath = DriverManager.propertiesSelenium.getSeleniumFirefox();
				boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
				synchronized (this) {
					if (isDriverAutoUpdateActivated && !driverDownloadedGecko) {
						if (!DriverManager.propertiesSelenium.getGeckoDriverVersion()
								.equals("")) {
							System.setProperty("wdm.geckoDriverVersion", DriverManager.propertiesSelenium.getGeckoDriverVersion());
						}
						downloadNewestOrGivenVersionOfWebDriver(FirefoxDriver.class);
						OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(FirefoxDriver.class)
								.getBinaryPath(), browserPath);
					}
					driverDownloadedGecko = true;
				}
				System.setProperty("webdriver.gecko.driver", browserPath);
				System.setProperty("webdriver.firefox.logfile", "logs\\firefox_logs.txt");
				
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("webdriver.firefox.marionette", true);
				profile.setPreference("browser.download.folderList", 2);
				profile.setPreference("browser.download.dir", DOWNLOAD_DIR);
				profile.setPreference("browser.download.useDownloadDir", true);
				
				profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
						"text/comma-separated-values, application/vnd.ms-excel, application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
				profile.setPreference("browser.download.manager.showWhenStarting", false);
				profile.setPreference("browser.helperApps.alwaysAsk.force", false);
				
				FirefoxOptions options = new FirefoxOptions().setProfile(profile);
				// Set users browser options
				RuntimeParametersSelenium.BROWSER_OPTIONS.getValues()
						.forEach((key, value) -> {
							BFLogger.logInfo("Browser option: " + key + " " + value);
							options.setCapability(key, value);
						});
						
				return new NewFirefoxDriver(options);
			}
		},
		IE {
			@Override
			public INewWebDriver getDriver() {
				String browserPath = DriverManager.propertiesSelenium.getSeleniumIE();
				boolean isDriverAutoUpdateActivated = DriverManager.propertiesSelenium.getDriverAutoUpdateFlag();
				synchronized (this) {
					if (isDriverAutoUpdateActivated && !driverDownloadedIE) {
						if (!DriverManager.propertiesSelenium.getInternetExplorerDriverVersion()
								.equals("")) {
							System.setProperty("wdm.internetExplorerDriverVersion", DriverManager.propertiesSelenium.getInternetExplorerDriverVersion());
						}
						downloadNewestOrGivenVersionOfWebDriver(InternetExplorerDriver.class);
						OperationsOnFiles.moveWithPruneEmptydirectories(WebDriverManager.getInstance(InternetExplorerDriver.class)
								.getBinaryPath(), browserPath);
					}
					driverDownloadedIE = true;
				}
				System.setProperty("webdriver.ie.driver", browserPath);
				DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
				
				// Set users browser options
				RuntimeParametersSelenium.BROWSER_OPTIONS.getValues()
						.forEach((key, value) -> {
							BFLogger.logInfo("Browser option: " + key + " " + value);
							ieCapabilities.setCapability(key, value);
						});
						
				// Due to some issues with IE11 this line must be commented
				// ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
				// true);
				return new NewInternetExplorerDriver(ieCapabilities);
			}
			
		},
		SAFARI {
			@Override
			protected INewWebDriver getDriver() {
				return null;
			}
		},
		SELENIUMGRID {
			@Override
			public INewWebDriver getDriver() {
				final String SELENIUM_GRID_URL = RuntimeParametersSelenium.SELENIUM_GRID.getValue();
				BFLogger.logDebug("Connecting to the selenium grid: " + SELENIUM_GRID_URL);
				DesiredCapabilities capabilities = new DesiredCapabilities();
				String operatingSystem = RuntimeParametersSelenium.OS.getValue();
				
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
				
				capabilities.setVersion(RuntimeParametersSelenium.BROWSER_VERSION.getValue());
				capabilities.setBrowserName(RuntimeParametersSelenium.BROWSER.getValue());
				
				// Set users browser options
				RuntimeParametersSelenium.BROWSER_OPTIONS.getValues()
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
				BFLogger.logDebug("Downloaded version of driver=" + WebDriverManager.getInstance(webDriverType)
						.getDownloadedVersion());
				
			} catch (WebDriverManagerException e) {
				BFLogger.logInfo("Unable to download driver automatically. "
						+ "Please try to set up the proxy in properties file. "
						+ "If you want to download them manually, go to the "
						+ "http://www.seleniumhq.org/projects/webdriver/ site.");
			}
			
		}
		
		protected abstract INewWebDriver getDriver();
	}
}
