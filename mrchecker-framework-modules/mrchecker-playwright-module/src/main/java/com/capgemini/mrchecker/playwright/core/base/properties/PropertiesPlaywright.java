package com.capgemini.mrchecker.playwright.core.base.properties;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.microsoft.playwright.options.Proxy;

public class PropertiesPlaywright {
    private Proxy proxy;
    private boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
    private String channel;
    private String browsersPath = "./lib/playwright";
    private int skipBrowserDownload = 0;

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setProxy(@Named("playwright.browser.proxy") String proxy) {
        this.proxy = new Proxy(proxy);
    }

    public Proxy getProxy() {
        return proxy;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setProxy(@Named("playwright.browser.headless") boolean headless) {
        this.headless = headless;
    }

    public boolean getHeadless() {
        return headless;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setChannel(@Named("playwright.browser.channel") String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setBrowsersPath(@Named("playwright.browser.downloadPath") String browsersPath) {
        this.browsersPath = browsersPath;
    }

    public String getBrowsersPath() {
        return browsersPath;
    }

    @Inject(optional = true)
    @SuppressWarnings("unused")
    private void setSkipBrowserDownload(@Named("playwright.browser.skipDownload") int value) {
        this.skipBrowserDownload = value;
    }

    public int getSkipBrowserDownload() {
        return skipBrowserDownload;
    }
}