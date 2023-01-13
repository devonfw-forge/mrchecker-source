package com.capgemini.mrchecker.playwright.core.newDrivers;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Selectors;

public class NewPlaywright implements Playwright {
    private final Playwright playwright;

    public NewPlaywright(Playwright playwright) {
        this.playwright = playwright;
    }

    @Override
    public BrowserType chromium() {
        return new NewBrowserType(playwright.chromium());
    }

    @Override
    public BrowserType firefox() {
        return new NewBrowserType(playwright.firefox());
    }

    @Override
    public APIRequest request() {
        return playwright.request();
    }

    @Override
    public Selectors selectors() {
        return playwright.selectors();
    }

    @Override
    public BrowserType webkit() {
        return new NewBrowserType(playwright.webkit());
    }

    @Override
    public void close() {
        playwright.close();
    }
}