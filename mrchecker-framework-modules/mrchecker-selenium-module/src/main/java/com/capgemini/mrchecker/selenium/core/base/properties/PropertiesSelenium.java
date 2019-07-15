package com.capgemini.mrchecker.selenium.core.base.properties;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PropertiesSelenium {

	private String  webDrivers           = "./lib/webdrivers";                                    // default value
	private String  seleniumChrome       = webDrivers + "/chrome/chromedriver.exe";               // default value
	private String  seleniumOpera        = webDrivers + "/opera/operadriver.exe";                 // default value
	private String  seleniumEdge         = webDrivers + "/edge/msedgedriver.exe";                 // default value
	private String  seleniumFirefox      = webDrivers + "/firefox/geckodriver.exe";               // default value
	private String  seleniumIE           = webDrivers + "/internetexplorer/IEDriverServer.exe";   // default value
	private String  proxy                = "";                                                    // default value
	private boolean driverAutoUpdateFlag = true;                                                  // default value
	private String  chromeDriverVersion           = "";
	private String  chromeHeadlessDriverVersion   = "";
	private String  internetExplorerDriverVersion = "";
	private String  geckoDriverVersion            = "";

	@Inject(optional = true)
	private void setSeleniumChrome(@Named("selenium.chrome") String path) {
		this.seleniumChrome = path;

	}

	public String getSeleniumChrome() {
		return this.seleniumChrome;
	}

	@Inject(optional = true)
	private void setSeleniumOpera(@Named("selenium.opera") String path) {
		this.seleniumOpera = path;

	}

	public String getSeleniumOpera() {
		return this.seleniumOpera;
	}

	@Inject(optional = true)
	private void setSeleniumEdge(@Named("selenium.edge") String path) {
		this.seleniumEdge = path;

	}

	public String getSeleniumEdge() {
		return this.seleniumEdge;
	}

	@Inject(optional = true)
	private void setSeleniumFirefox(@Named("selenium.firefox") String path) {
		this.seleniumFirefox = path;

	}

	public String getSeleniumFirefox() {
		return this.seleniumFirefox;
	}

	@Inject(optional = true)
	private void setSeleniumIE(@Named("selenium.ie") String path) {
		this.seleniumIE = path;

	}

	public String getSeleniumIE() {
		return this.seleniumIE;
	}

	@Inject(optional = true)
	private void setWebDrivers(@Named("selenium.webdrivers") String path) {
		this.webDrivers = path;

	}

	public String getWebDrivers() {
		return this.webDrivers;
	}

	@Inject(optional = true)
	private void setProxy(@Named("selenium.proxy") String path) {
		this.proxy = path;

	}

	public String getProxy() {
		return this.proxy;
	}

	@Inject(optional = true)
	private void setDriverAutoUpdateFlag(@Named("selenium.driverAutoUpdate") boolean flag) {
		this.driverAutoUpdateFlag = flag;
	}

	public boolean getDriverAutoUpdateFlag() {
		return this.driverAutoUpdateFlag;
	}

	@Inject(optional = true)
	private void setChromeDriverVersion(@Named("wdm.chromeDriverVersion") String version) {
		this.chromeDriverVersion = version;
	}

	public String getChromeDriverVersion() {
		return this.chromeDriverVersion;
	}

	@Inject(optional = true)
	private void setInternetExplorerDriverVersion(@Named("wdm.internetExplorerDriverVersion") String version) {
		this.geckoDriverVersion = version;
	}

	public String getInternetExplorerDriverVersion() {
		return this.internetExplorerDriverVersion;
	}

	@Inject(optional = true)
	private void setGeckoDriverVersion(@Named("wdm.geckoDriverVersion") String version) {
		this.geckoDriverVersion = version;
	}

	public String getGeckoDriverVersion() {
		return this.geckoDriverVersion;
	}

}
