package com.capgemini.mrchecker.selenium.core.base.properties;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PropertiesSelenium {

    private String webDrivers = "./lib/webdrivers";                                    // default
    // value
    private String seleniumChrome = webDrivers + "/chrome/chromedriver.exe";                // default
    // value
    private String seleniumOpera = webDrivers + "/opera/operadriver.exe";                // default
    // value
    private String seleniumEdge = webDrivers + "/edge/msedgedriver.exe";                // default
    // value
    private String seleniumFirefox = webDrivers + "/firefox/geckodriver.exe";                // default
    // value
    private String seleniumIE = webDrivers + "/internetexplorer/IEDriverServer.exe";    // default
    // value
    private String proxy = "";                                                    // default
    // value
    private boolean driverAutoUpdateFlag = true;                                                    // default
    // value
    private String chromeDriverVersion = "";
    // private String chromeHeadlessDriverVersion = "";
    private String internetExplorerDriverVersion = "";
    private String geckoDriverVersion = "";
    private String edgeDriverVersion = "";
    private boolean edgeDriverFeatureOnDemandFlag = true;
    private String operaDriverVersion = "";

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
    private void setSeleniumOpera(@Named("selenium.opera") String path) {
        seleniumOpera = path;

    }

    public String getSeleniumOpera() {
        return seleniumOpera;
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
    private void setWebDrivers(@Named("selenium.webdrivers") String path) {
        webDrivers = path;

    }

    public String getWebDrivers() {
        return webDrivers;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setProxy(@Named("selenium.proxy") String path) {
        proxy = path;

    }

    public String getProxy() {
        return proxy;
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
        chromeDriverVersion = version;
    }

    public String getChromeDriverVersion() {
        return chromeDriverVersion;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setInternetExplorerDriverVersion(@Named("wdm.internetExplorerDriverVersion") String version) {
        internetExplorerDriverVersion = version;
    }

    public String getInternetExplorerDriverVersion() {
        return internetExplorerDriverVersion;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setGeckoDriverVersion(@Named("wdm.geckoDriverVersion") String version) {
        geckoDriverVersion = version;
    }

    public String getGeckoDriverVersion() {
        return geckoDriverVersion;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setEdgeDriverVersion(@Named("wdm.edgeVersion") String version) {
        edgeDriverVersion = version;
    }

    public String getEdgeDriverVersion() {
        return edgeDriverVersion;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setEdgeDriverFeatureOnDemandFlag(@Named("wdm.edgeFeatureOnDemand") boolean flag) {
        edgeDriverFeatureOnDemandFlag = flag;
    }

    public boolean getEdgeDriverFeatureOnDemandFlag() {
        return edgeDriverFeatureOnDemandFlag;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setOperaDriverVersion(@Named("wdm.operaDriverVersion") String version) {
        operaDriverVersion = version;
    }

    public String getOperaDriverVersion() {
        return operaDriverVersion;
    }
}
