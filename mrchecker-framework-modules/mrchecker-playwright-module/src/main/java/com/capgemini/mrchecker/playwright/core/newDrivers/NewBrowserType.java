package com.capgemini.mrchecker.playwright.core.newDrivers;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;

import java.nio.file.Path;

public class NewBrowserType implements BrowserType {
    private final BrowserType browserType;

    public NewBrowserType(BrowserType browserType) {
        this.browserType = browserType;
    }

    @Override
    public Browser connect(String wsEndpoint) {
        return new NewBrowser(browserType.connect(wsEndpoint));
    }

    @Override
    public Browser connect(String s, ConnectOptions connectOptions) {
        return new NewBrowser(browserType.connect(s, connectOptions));
    }

    @Override
    public Browser connectOverCDP(String endpointURL) {
        return new NewBrowser(browserType.connectOverCDP(endpointURL));
    }

    @Override
    public Browser connectOverCDP(String s, ConnectOverCDPOptions connectOverCDPOptions) {
        return new NewBrowser(browserType.connectOverCDP(s, connectOverCDPOptions));
    }

    @Override
    public String executablePath() {
        return browserType.executablePath();
    }

    @Override
    public Browser launch() {
        return new NewBrowser(browserType.launch());
    }

    @Override
    public Browser launch(LaunchOptions launchOptions) {
        return new NewBrowser(browserType.launch(launchOptions));
    }

    @Override
    public BrowserContext launchPersistentContext(Path userDataDir) {
        return new NewBrowserContext(browserType.launchPersistentContext(userDataDir));
    }

    @Override
    public BrowserContext launchPersistentContext(Path path, LaunchPersistentContextOptions launchPersistentContextOptions) {
        return new NewBrowserContext(browserType.launchPersistentContext(path, launchPersistentContextOptions));
    }

    @Override
    public String name() {
        return browserType.name();
    }
}