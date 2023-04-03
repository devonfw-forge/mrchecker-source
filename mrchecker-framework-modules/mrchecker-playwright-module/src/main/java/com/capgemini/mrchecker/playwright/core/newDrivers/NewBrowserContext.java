package com.capgemini.mrchecker.playwright.core.newDrivers;


import com.microsoft.playwright.*;
import com.microsoft.playwright.options.BindingCallback;
import com.microsoft.playwright.options.Cookie;
import com.microsoft.playwright.options.FunctionCallback;
import com.microsoft.playwright.options.Geolocation;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class NewBrowserContext implements INewBrowserContext {
    private final BrowserContext browserContext;

    private Page currentPage;

    public NewBrowserContext(BrowserContext browserContext) {
        this.browserContext = browserContext;
    }

    @Override
    public void onClose(Consumer<BrowserContext> consumer) {
        browserContext.onClose(consumer);
    }

    @Override
    public void offClose(Consumer<BrowserContext> consumer) {
        browserContext.offClose(consumer);
    }

    @Override
    public void onPage(Consumer<Page> consumer) {
        browserContext.onPage(consumer);
    }

    @Override
    public void offPage(Consumer<Page> consumer) {
        browserContext.offPage(consumer);
    }

    @Override
    public void onRequest(Consumer<Request> consumer) {
        browserContext.onRequest(consumer);
    }

    @Override
    public void offRequest(Consumer<Request> consumer) {
        browserContext.offRequest(consumer);
    }

    @Override
    public void onRequestFailed(Consumer<Request> consumer) {
        browserContext.onRequestFailed(consumer);
    }

    @Override
    public void offRequestFailed(Consumer<Request> consumer) {
        browserContext.offRequestFailed(consumer);
    }

    @Override
    public void onRequestFinished(Consumer<Request> consumer) {
        browserContext.onRequestFinished(consumer);
    }

    @Override
    public void offRequestFinished(Consumer<Request> consumer) {
        browserContext.offRequestFinished(consumer);
    }

    @Override
    public void onResponse(Consumer<Response> consumer) {
        browserContext.onResponse(consumer);
    }

    @Override
    public void offResponse(Consumer<Response> consumer) {
        browserContext.offResponse(consumer);
    }

    @Override
    public void addCookies(List<Cookie> list) {
        browserContext.addCookies(list);
    }

    @Override
    public void addInitScript(String s) {
        browserContext.addInitScript(s);
    }

    @Override
    public void addInitScript(Path path) {
        browserContext.addInitScript(path);
    }

    @Override
    public Browser browser() {
        return browserContext.browser();
    }

    @Override
    public void clearCookies() {
        browserContext.clearCookies();
    }

    @Override
    public void clearPermissions() {
        browserContext.clearPermissions();
    }

    @Override
    public void close() {
        browserContext.close();
    }

    @Override
    public List<Cookie> cookies() {
        return browserContext.cookies();
    }

    @Override
    public List<Cookie> cookies(String s) {
        return browserContext.cookies(s);
    }

    @Override
    public List<Cookie> cookies(List<String> list) {
        return browserContext.cookies(list);
    }

    @Override
    public void exposeBinding(String name, BindingCallback callback) {
        browserContext.exposeBinding(name, callback);
    }

    @Override
    public void exposeBinding(String s, BindingCallback bindingCallback, ExposeBindingOptions exposeBindingOptions) {
        browserContext.exposeBinding(s, bindingCallback, exposeBindingOptions);
    }

    @Override
    public void exposeFunction(String s, FunctionCallback functionCallback) {
        browserContext.exposeFunction(s, functionCallback);
    }

    @Override
    public void grantPermissions(List<String> permissions) {
        browserContext.grantPermissions(permissions);
    }

    @Override
    public void grantPermissions(List<String> list, GrantPermissionsOptions grantPermissionsOptions) {
        browserContext.grantPermissions(list, grantPermissionsOptions);
    }

    @Override
    public Page newPage() {
        setCurrentPage(new NewPage(browserContext.newPage()));
        return currentPage();
    }

    @Override
    public Page currentPage() {
        if (Objects.isNull(currentPage)) {
            return newPage();
        }
        return currentPage;
    }

    @Override
    public void setCurrentPage(Page page) {
        this.currentPage = page;
    }

    @Override
    public List<Page> pages() {
        return browserContext.pages();
    }

    @Override
    public APIRequestContext request() {
        return browserContext.request();
    }

    @Override
    public void route(String url, Consumer<Route> handler) {
        browserContext.route(url, handler);
    }

    @Override
    public void route(String s, Consumer<Route> consumer, RouteOptions routeOptions) {
        browserContext.route(s, consumer, routeOptions);
    }

    @Override
    public void route(Pattern url, Consumer<Route> handler) {
        browserContext.route(url, handler);
    }

    @Override
    public void route(Pattern pattern, Consumer<Route> consumer, RouteOptions routeOptions) {
        browserContext.route(pattern, consumer, routeOptions);
    }

    @Override
    public void route(Predicate<String> url, Consumer<Route> handler) {
        browserContext.route(url, handler);
    }

    @Override
    public void route(Predicate<String> predicate, Consumer<Route> consumer, RouteOptions routeOptions) {
        browserContext.route(predicate, consumer, routeOptions);
    }

    @Override
    public void routeFromHAR(Path har) {
        browserContext.routeFromHAR(har);
    }

    @Override
    public void routeFromHAR(Path path, RouteFromHAROptions routeFromHAROptions) {
//        BaseTest.getAnalytics()
//                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        browserContext.routeFromHAR(path, routeFromHAROptions);
    }

    @Override
    public void setDefaultNavigationTimeout(double v) {
        browserContext.setDefaultNavigationTimeout(v);
    }

    @Override
    public void setDefaultTimeout(double v) {
        browserContext.setDefaultTimeout(v);
    }

    @Override
    public void setExtraHTTPHeaders(Map<String, String> map) {
        browserContext.setExtraHTTPHeaders(map);
    }

    @Override
    public void setGeolocation(Geolocation geolocation) {
        browserContext.setGeolocation(geolocation);
    }

    @Override
    public void setOffline(boolean b) {
        browserContext.setOffline(b);
    }

    @Override
    public String storageState() {
        return browserContext.storageState();
    }

    @Override
    public String storageState(StorageStateOptions storageStateOptions) {
        return browserContext.storageState(storageStateOptions);
    }

    @Override
    public Tracing tracing() {
        return browserContext.tracing();
    }

    @Override
    public void unroute(String url) {
        browserContext.unroute(url);
    }

    @Override
    public void unroute(String s, Consumer<Route> consumer) {
        browserContext.unroute(s, consumer);
    }

    @Override
    public void unroute(Pattern url) {
        browserContext.unroute(url);
    }

    @Override
    public void unroute(Pattern pattern, Consumer<Route> consumer) {
        browserContext.unroute(pattern, consumer);
    }

    @Override
    public void unroute(Predicate<String> url) {
        browserContext.unroute(url);
    }

    @Override
    public void unroute(Predicate<String> predicate, Consumer<Route> consumer) {
        browserContext.unroute(predicate, consumer);
    }

    @Override
    public void waitForCondition(BooleanSupplier booleanSupplier, WaitForConditionOptions waitForConditionOptions) {
        browserContext.waitForCondition(booleanSupplier, waitForConditionOptions);
    }

    @Override
    public Page waitForPage(Runnable callback) {
        return browserContext.waitForPage(callback);
    }

    @Override
    public Page waitForPage(WaitForPageOptions waitForPageOptions, Runnable runnable) {
        return browserContext.waitForPage(waitForPageOptions, runnable);
    }
}