package com.capgemini.mrchecker.selenium.core.base.properties;

import com.capgemini.mrchecker.test.core.base.driver.DriverCloseLevel;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PropertiesSelenium {
    private final String webDrivers = "./lib/webdrivers";
    private String seleniumChrome = webDrivers + "/chrome/chromedriver.exe";
    private String driverVersionChrome = "";
    private String seleniumEdge = webDrivers + "/edge/msedgedriver.exe";
    private String driverVersionEdge = "";
    private String seleniumFirefox = webDrivers + "/firefox/geckodriver.exe";
    private String driverVersionFirefox = "";
    private String seleniumIE = webDrivers + "/internetexplorer/IEDriverServer.exe";
    private String driverVersionIE = "";
    private boolean driverAutoUpdateFlag = true;
    private boolean allowStaticPage = false;
    private DriverCloseLevel driverCloseLevel = DriverCloseLevel.CLASS;

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setSeleniumChrome(@Named("selenium.chrome") String path) {
        seleniumChrome = path;
    }

    public String getSeleniumChrome() {
        return seleniumChrome;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setSeleniumEdge(@Named("selenium.edge") String path) {
        seleniumEdge = path;
    }

    public String getSeleniumEdge() {
        return seleniumEdge;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setSeleniumFirefox(@Named("selenium.firefox") String path) {
        seleniumFirefox = path;
    }

    public String getSeleniumFirefox() {
        return seleniumFirefox;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setSeleniumIE(@Named("selenium.ie") String path) {
        seleniumIE = path;
    }

    public String getSeleniumIE() {
        return seleniumIE;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setDriverAutoUpdateFlag(@Named("selenium.driverAutoUpdate") boolean flag) {
        driverAutoUpdateFlag = flag;
    }

    public boolean getDriverAutoUpdateFlag() {
        return driverAutoUpdateFlag;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setChromeDriverVersion(@Named("wdm.chromeDriverVersion") String version) {
        driverVersionChrome = version;
    }

    public String getChromeDriverVersion() {
        return driverVersionChrome;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setInternetExplorerDriverVersion(@Named("wdm.internetExplorerDriverVersion") String version) {
        driverVersionIE = version;
    }

    public String getInternetExplorerDriverVersion() {
        return driverVersionIE;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setGeckoDriverVersion(@Named("wdm.geckoDriverVersion") String version) {
        driverVersionFirefox = version;
    }

    public String getGeckoDriverVersion() {
        return driverVersionFirefox;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setEdgeDriverVersion(@Named("wdm.edgeVersion") String version) {
        driverVersionEdge = version;
    }

    public String getEdgeDriverVersion() {
        return driverVersionEdge;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setAllowStaticPage(@Named("selenium.allowStaticPage") boolean value) {
        this.allowStaticPage = value;
    }

    public boolean getAllowStaticPage() {
        return allowStaticPage;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setDriverCloseLevel(@Named("selenium.driverCloseLevel") String level) {
        driverCloseLevel = DriverCloseLevel.fromText(level);
    }

    public DriverCloseLevel getDriverCloseLevel() {
        return driverCloseLevel;
    }
}