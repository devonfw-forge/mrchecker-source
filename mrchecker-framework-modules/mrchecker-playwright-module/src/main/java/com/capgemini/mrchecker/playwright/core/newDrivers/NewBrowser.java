package com.capgemini.mrchecker.playwright.core.newDrivers;

import com.microsoft.playwright.*;

import java.util.List;
import java.util.function.Consumer;

public class NewBrowser implements Browser {
    private final Browser browser;

    public NewBrowser(Browser browser) {
        this.browser = browser;
    }

    @Override
    public void onDisconnected(Consumer<Browser> consumer) {
        browser.onDisconnected(consumer);
    }

    @Override
    public void offDisconnected(Consumer<Browser> consumer) {
        browser.offDisconnected(consumer);
    }

    @Override
    public BrowserType browserType() {
        return browser.browserType();
    }

    @Override
    public void close() {
        browser.close();
    }

    @Override
    public List<BrowserContext> contexts() {
        return browser.contexts();
    }

    @Override
    public boolean isConnected() {
        return browser.isConnected();
    }

    @Override
    public CDPSession newBrowserCDPSession() {
        return browser.newBrowserCDPSession();
    }

    @Override
    public BrowserContext newContext() {
        return new NewBrowserContext(browser.newContext());
    }

    @Override
    public BrowserContext newContext(NewContextOptions newContextOptions) {
        return new NewBrowserContext(browser.newContext(newContextOptions));
    }

    @Override
    public Page newPage() {
        return new NewPage(browser.newPage());
    }

    @Override
    public Page newPage(NewPageOptions newPageOptions) {
        return new NewPage(browser.newPage(newPageOptions));
    }

    @Override
    public void startTracing(Page page) {
        browser.startTracing(page);
    }

    @Override
    public void startTracing() {
        browser.startTracing();
    }

    @Override
    public void startTracing(Page page, StartTracingOptions startTracingOptions) {
        browser.startTracing(page, startTracingOptions);
    }

    @Override
    public byte[] stopTracing() {
        return browser.stopTracing();
    }

    @Override
    public String version() {
        return browser.version();
    }
}