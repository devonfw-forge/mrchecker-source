package com.capgemini.mrchecker.playwright.core.newDrivers;

import com.capgemini.mrchecker.playwright.core.BasePage;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class NewPage implements Page {
    private final Page page;

    public NewPage(Page page) {
        this.page = page;
    }

    @Override
    public void onClose(Consumer<Page> consumer) {
        page.onClose(consumer);
    }

    @Override
    public void offClose(Consumer<Page> consumer) {
        page.offClose(consumer);
    }

    @Override
    public void onConsoleMessage(Consumer<ConsoleMessage> consumer) {
        page.onConsoleMessage(consumer);
    }

    @Override
    public void offConsoleMessage(Consumer<ConsoleMessage> consumer) {
        page.offConsoleMessage(consumer);
    }

    @Override
    public void onCrash(Consumer<Page> consumer) {
        page.onCrash(consumer);
    }

    @Override
    public void offCrash(Consumer<Page> consumer) {
        page.offCrash(consumer);
    }

    @Override
    public void onDialog(Consumer<Dialog> consumer) {
        page.onDialog(consumer);
    }

    @Override
    public void offDialog(Consumer<Dialog> consumer) {
        page.offDialog(consumer);
    }

    @Override
    public void onDOMContentLoaded(Consumer<Page> consumer) {
        page.onDOMContentLoaded(consumer);
    }

    @Override
    public void offDOMContentLoaded(Consumer<Page> consumer) {
        page.offDOMContentLoaded(consumer);
    }

    @Override
    public void onDownload(Consumer<Download> consumer) {
        page.onDownload(consumer);
    }

    @Override
    public void offDownload(Consumer<Download> consumer) {
        page.offDownload(consumer);
    }

    @Override
    public void onFileChooser(Consumer<FileChooser> consumer) {
        page.onFileChooser(consumer);
    }

    @Override
    public void offFileChooser(Consumer<FileChooser> consumer) {
        page.offFileChooser(consumer);
    }

    @Override
    public void onFrameAttached(Consumer<Frame> consumer) {
        page.onFrameAttached(consumer);
    }

    @Override
    public void offFrameAttached(Consumer<Frame> consumer) {
        page.offFrameAttached(consumer);
    }

    @Override
    public void onFrameDetached(Consumer<Frame> consumer) {
        page.onFrameDetached(consumer);
    }

    @Override
    public void offFrameDetached(Consumer<Frame> consumer) {
        page.offFrameDetached(consumer);
    }

    @Override
    public void onFrameNavigated(Consumer<Frame> consumer) {
        page.onFrameNavigated(consumer);
    }

    @Override
    public void offFrameNavigated(Consumer<Frame> consumer) {
        page.offFrameNavigated(consumer);
    }

    @Override
    public void onLoad(Consumer<Page> consumer) {
        page.onLoad(consumer);
    }

    @Override
    public void offLoad(Consumer<Page> consumer) {
        page.offLoad(consumer);
    }

    @Override
    public void onPageError(Consumer<String> consumer) {
        page.onPageError(consumer);
    }

    @Override
    public void offPageError(Consumer<String> consumer) {
        page.offPageError(consumer);
    }

    @Override
    public void onPopup(Consumer<Page> consumer) {
        page.onPopup(consumer);
    }

    @Override
    public void offPopup(Consumer<Page> consumer) {
        page.offPopup(consumer);
    }

    @Override
    public void onRequest(Consumer<Request> consumer) {
        page.onRequest(consumer);
    }

    @Override
    public void offRequest(Consumer<Request> consumer) {
        page.offRequest(consumer);
    }

    @Override
    public void onRequestFailed(Consumer<Request> consumer) {
        page.onRequestFailed(consumer);
    }

    @Override
    public void offRequestFailed(Consumer<Request> consumer) {
        page.offRequestFailed(consumer);
    }

    @Override
    public void onRequestFinished(Consumer<Request> consumer) {
        page.onRequestFinished(consumer);
    }

    @Override
    public void offRequestFinished(Consumer<Request> consumer) {
        page.offRequestFinished(consumer);
    }

    @Override
    public void onResponse(Consumer<Response> consumer) {
        page.onResponse(consumer);
    }

    @Override
    public void offResponse(Consumer<Response> consumer) {
        page.offResponse(consumer);
    }

    @Override
    public void onWebSocket(Consumer<WebSocket> consumer) {
        page.onWebSocket(consumer);
    }

    @Override
    public void offWebSocket(Consumer<WebSocket> consumer) {
        page.offWebSocket(consumer);
    }

    @Override
    public void onWorker(Consumer<Worker> consumer) {
        page.onWorker(consumer);
    }

    @Override
    public void offWorker(Consumer<Worker> consumer) {
        page.offWorker(consumer);
    }

    @Override
    public void addInitScript(String s) {
        page.addInitScript(s);
    }

    @Override
    public void addInitScript(Path path) {
        page.addInitScript(path);
    }

    @Override
    public ElementHandle addScriptTag() {
        return page.addScriptTag();
    }

    @Override
    public ElementHandle addScriptTag(AddScriptTagOptions addScriptTagOptions) {
        return page.addScriptTag(addScriptTagOptions);
    }

    @Override
    public ElementHandle addStyleTag() {
        return page.addStyleTag();
    }

    @Override
    public ElementHandle addStyleTag(AddStyleTagOptions addStyleTagOptions) {
        return page.addStyleTag(addStyleTagOptions);
    }

    @Override
    public void bringToFront() {
        page.bringToFront();
    }

    @Override
    public void check(String selector) {
        page.check(selector);
    }

    @Override
    public void check(String s, CheckOptions checkOptions) {
        page.check(s, checkOptions);
    }

    @Override
    public void click(String selector) {
        //ToDo: Made similar to Selenium logging
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        long startTime = System.currentTimeMillis();
        page.click(selector);
        BFLogger.logTime(startTime, "click()", selector);
    }


    @Override
    public void click(String s, ClickOptions clickOptions) {
        page.click(s, clickOptions);
    }

    @Override
    public void close() {
        page.close();
    }

    @Override
    public void close(CloseOptions closeOptions) {
        page.close(closeOptions);
    }

    @Override
    public String content() {
        return page.content();
    }

    @Override
    public BrowserContext context() {
        return page.context();
    }

    @Override
    public void dblclick(String selector) {
        page.dblclick(selector);
    }

    @Override
    public void dblclick(String s, DblclickOptions dblclickOptions) {
        page.dblclick(s, dblclickOptions);
    }

    @Override
    public void dispatchEvent(String selector, String type, Object eventInit) {
        page.dispatchEvent(selector, type, eventInit);
    }

    @Override
    public void dispatchEvent(String selector, String type) {
        page.dispatchEvent(selector, type);
    }

    @Override
    public void dispatchEvent(String s, String s1, Object o, DispatchEventOptions dispatchEventOptions) {
        page.dispatchEvent(s, s1, o, dispatchEventOptions);
    }

    @Override
    public void dragAndDrop(String source, String target) {
        page.dragAndDrop(source, target);
    }

    @Override
    public void dragAndDrop(String s, String s1, DragAndDropOptions dragAndDropOptions) {
        page.dragAndDrop(s, s1, dragAndDropOptions);
    }

    @Override
    public void emulateMedia() {
        page.emulateMedia();
    }

    @Override
    public void emulateMedia(EmulateMediaOptions emulateMediaOptions) {
        page.emulateMedia(emulateMediaOptions);
    }

    @Override
    public Object evalOnSelector(String selector, String expression, Object arg) {
        return page.evalOnSelector(selector, expression, arg);
    }

    @Override
    public Object evalOnSelector(String selector, String expression) {
        return page.evalOnSelector(selector, expression);
    }

    @Override
    public Object evalOnSelector(String s, String s1, Object o, EvalOnSelectorOptions evalOnSelectorOptions) {
        return page.evalOnSelector(s, s1, o, evalOnSelectorOptions);
    }

    @Override
    public Object evalOnSelectorAll(String selector, String expression) {
        return page.evalOnSelectorAll(selector, expression);
    }

    @Override
    public Object evalOnSelectorAll(String s, String s1, Object o) {
        return page.evalOnSelectorAll(s, s1, o);
    }

    @Override
    public Object evaluate(String expression) {
        return page.evaluate(expression);
    }

    @Override
    public Object evaluate(String s, Object o) {
        return page.evaluate(s, o);
    }

    @Override
    public JSHandle evaluateHandle(String expression) {
        return page.evaluateHandle(expression);
    }

    @Override
    public JSHandle evaluateHandle(String s, Object o) {
        return page.evaluateHandle(s, o);
    }

    @Override
    public void exposeBinding(String name, BindingCallback callback) {
        page.exposeBinding(name, callback);
    }

    @Override
    public void exposeBinding(String s, BindingCallback bindingCallback, ExposeBindingOptions exposeBindingOptions) {
        page.exposeBinding(s, bindingCallback, exposeBindingOptions);
    }

    @Override
    public void exposeFunction(String s, FunctionCallback functionCallback) {
        page.exposeFunction(s, functionCallback);
    }

    @Override
    public void fill(String selector, String value) {
        page.fill(selector, value);
    }

    @Override
    public void fill(String s, String s1, FillOptions fillOptions) {
        page.fill(s, s1, fillOptions);
    }

    @Override
    public void focus(String selector) {
        page.focus(selector);
    }

    @Override
    public void focus(String s, FocusOptions focusOptions) {
        page.focus(s, focusOptions);
    }

    @Override
    public Frame frame(String s) {
        return page.frame(s);
    }

    @Override
    public Frame frameByUrl(String s) {
        return page.frameByUrl(s);
    }

    @Override
    public Frame frameByUrl(Pattern pattern) {
        return page.frameByUrl(pattern);
    }

    @Override
    public Frame frameByUrl(Predicate<String> predicate) {
        return page.frameByUrl(predicate);
    }

    @Override
    public FrameLocator frameLocator(String s) {
        return page.frameLocator(s);
    }

    @Override
    public List<Frame> frames() {
        return page.frames();
    }

    @Override
    public String getAttribute(String selector, String name) {
        return page.getAttribute(selector, name);
    }

    @Override
    public String getAttribute(String s, String s1, GetAttributeOptions getAttributeOptions) {
        return page.getAttribute(s, s1, getAttributeOptions);
    }

    @Override
    public Locator getByAltText(String text) {
        return page.getByAltText(text);
    }

    @Override
    public Locator getByAltText(String s, GetByAltTextOptions getByAltTextOptions) {
        return page.getByAltText(s, getByAltTextOptions);
    }

    @Override
    public Locator getByAltText(Pattern text) {
        return page.getByAltText(text);
    }

    @Override
    public Locator getByAltText(Pattern pattern, GetByAltTextOptions getByAltTextOptions) {
        return page.getByAltText(pattern, getByAltTextOptions);
    }

    @Override
    public Locator getByLabel(String text) {
        return page.getByLabel(text);
    }

    @Override
    public Locator getByLabel(String s, GetByLabelOptions getByLabelOptions) {
        return page.getByLabel(s, getByLabelOptions);
    }

    @Override
    public Locator getByLabel(Pattern text) {
        return page.getByLabel(text);
    }

    @Override
    public Locator getByLabel(Pattern pattern, GetByLabelOptions getByLabelOptions) {
        return page.getByLabel(pattern, getByLabelOptions);
    }

    @Override
    public Locator getByPlaceholder(String text) {
        return page.getByPlaceholder(text);
    }

    @Override
    public Locator getByPlaceholder(String s, GetByPlaceholderOptions getByPlaceholderOptions) {
        return page.getByPlaceholder(s, getByPlaceholderOptions);
    }

    @Override
    public Locator getByPlaceholder(Pattern text) {
        return page.getByPlaceholder(text);
    }

    @Override
    public Locator getByPlaceholder(Pattern pattern, GetByPlaceholderOptions getByPlaceholderOptions) {
        return page.getByPlaceholder(pattern, getByPlaceholderOptions);
    }

    @Override
    public Locator getByRole(AriaRole role) {
        return page.getByRole(role);
    }

    @Override
    public Locator getByRole(AriaRole ariaRole, GetByRoleOptions getByRoleOptions) {
        return page.getByRole(ariaRole, getByRoleOptions);
    }

    @Override
    public Locator getByTestId(String s) {
        return page.getByTestId(s);
    }

    @Override
    public Locator getByText(String text) {
        return page.getByText(text);
    }

    @Override
    public Locator getByText(String s, GetByTextOptions getByTextOptions) {
        return page.getByText(s, getByTextOptions);
    }

    @Override
    public Locator getByText(Pattern text) {
        return page.getByText(text);
    }

    @Override
    public Locator getByText(Pattern pattern, GetByTextOptions getByTextOptions) {
        return page.getByText(pattern, getByTextOptions);
    }

    @Override
    public Locator getByTitle(String text) {
        return page.getByTitle(text);
    }

    @Override
    public Locator getByTitle(String s, GetByTitleOptions getByTitleOptions) {
        return page.getByTitle(s, getByTitleOptions);
    }

    @Override
    public Locator getByTitle(Pattern text) {
        return page.getByTitle(text);
    }

    @Override
    public Locator getByTitle(Pattern pattern, GetByTitleOptions getByTitleOptions) {
        return page.getByTitle(pattern, getByTitleOptions);
    }

    @Override
    public Response goBack() {
        return page.goBack();
    }

    @Override
    public Response goBack(GoBackOptions goBackOptions) {
        return page.goBack(goBackOptions);
    }

    @Override
    public Response goForward() {
        return page.goForward();
    }

    @Override
    public Response goForward(GoForwardOptions goForwardOptions) {
        return page.goForward(goForwardOptions);
    }

    @Override
    public Response navigate(String url) {
        return page.navigate(url);
    }

    @Override
    public Response navigate(String s, NavigateOptions navigateOptions) {
        return page.navigate(s, navigateOptions);
    }

    @Override
    public void hover(String selector) {
        page.hover(selector);
    }

    @Override
    public void hover(String s, HoverOptions hoverOptions) {
        page.hover(s, hoverOptions);
    }

    @Override
    public String innerHTML(String selector) {
        return page.innerHTML(selector);
    }

    @Override
    public String innerHTML(String s, InnerHTMLOptions innerHTMLOptions) {
        return page.innerHTML(s, innerHTMLOptions);
    }

    @Override
    public String innerText(String selector) {
        return page.innerText(selector);
    }

    @Override
    public String innerText(String s, InnerTextOptions innerTextOptions) {
        return page.innerText(s, innerTextOptions);
    }

    @Override
    public String inputValue(String selector) {
        return page.inputValue(selector);
    }

    @Override
    public String inputValue(String s, InputValueOptions inputValueOptions) {
        return page.inputValue(s, inputValueOptions);
    }

    @Override
    public boolean isChecked(String selector) {
        return page.isChecked(selector);
    }

    @Override
    public boolean isChecked(String s, IsCheckedOptions isCheckedOptions) {
        return page.isChecked(s, isCheckedOptions);
    }

    @Override
    public boolean isClosed() {
        return page.isClosed();
    }

    @Override
    public boolean isDisabled(String selector) {
        return page.isDisabled(selector);
    }

    @Override
    public boolean isDisabled(String s, IsDisabledOptions isDisabledOptions) {
        return page.isDisabled(s, isDisabledOptions);
    }

    @Override
    public boolean isEditable(String selector) {
        return page.isEditable(selector);
    }

    @Override
    public boolean isEditable(String s, IsEditableOptions isEditableOptions) {
        return page.isEditable(s, isEditableOptions);
    }

    @Override
    public boolean isEnabled(String selector) {
        return page.isEnabled(selector);
    }

    @Override
    public boolean isEnabled(String s, IsEnabledOptions isEnabledOptions) {
        return page.isEnabled(s, isEnabledOptions);
    }

    @Override
    public boolean isHidden(String selector) {
        return page.isHidden(selector);
    }

    @Override
    public boolean isHidden(String s, IsHiddenOptions isHiddenOptions) {
        return page.isHidden(s, isHiddenOptions);
    }

    @Override
    public boolean isVisible(String selector) {
        return page.isVisible(selector);
    }

    @Override
    public boolean isVisible(String s, IsVisibleOptions isVisibleOptions) {
        return page.isVisible(s, isVisibleOptions);
    }

    @Override
    public Keyboard keyboard() {
        return page.keyboard();
    }

    @Override
    public Locator locator(String selector) {
        return page.locator(selector);
    }

    @Override
    public Locator locator(String s, LocatorOptions locatorOptions) {
        return page.locator(s, locatorOptions);
    }

    @Override
    public Frame mainFrame() {
        return page.mainFrame();
    }

    @Override
    public Mouse mouse() {
        return page.mouse();
    }

    @Override
    public Page opener() {
        return page.opener();
    }

    @Override
    public void pause() {
        page.pause();
    }

    @Override
    public byte[] pdf() {
        return page.pdf();
    }

    @Override
    public byte[] pdf(PdfOptions pdfOptions) {
        return page.pdf(pdfOptions);
    }

    @Override
    public void press(String selector, String key) {
        page.press(selector, key);
    }

    @Override
    public void press(String s, String s1, PressOptions pressOptions) {
        page.press(s, s1, pressOptions);
    }

    @Override
    public ElementHandle querySelector(String selector) {
        return page.querySelector(selector);
    }

    @Override
    public ElementHandle querySelector(String s, QuerySelectorOptions querySelectorOptions) {
        return page.querySelector(s, querySelectorOptions);
    }

    @Override
    public List<ElementHandle> querySelectorAll(String s) {
        return page.querySelectorAll(s);
    }

    @Override
    public Response reload() {
        return page.reload();
    }

    @Override
    public Response reload(ReloadOptions reloadOptions) {
        return page.reload(reloadOptions);
    }

    @Override
    public APIRequestContext request() {
        return page.request();
    }

    @Override
    public void route(String url, Consumer<Route> handler) {
        page.route(url, handler);
    }

    @Override
    public void route(String s, Consumer<Route> consumer, RouteOptions routeOptions) {
        page.route(s, consumer, routeOptions);
    }

    @Override
    public void route(Pattern url, Consumer<Route> handler) {
        page.route(url, handler);
    }

    @Override
    public void route(Pattern pattern, Consumer<Route> consumer, RouteOptions routeOptions) {
        page.route(pattern, consumer, routeOptions);
    }

    @Override
    public void route(Predicate<String> url, Consumer<Route> handler) {
        page.route(url, handler);
    }

    @Override
    public void route(Predicate<String> predicate, Consumer<Route> consumer, RouteOptions routeOptions) {
        page.route(predicate, consumer, routeOptions);
    }

    @Override
    public void routeFromHAR(Path har) {
        page.routeFromHAR(har);
    }

    @Override
    public void routeFromHAR(Path path, RouteFromHAROptions routeFromHAROptions) {
        page.routeFromHAR(path, routeFromHAROptions);
    }

    @Override
    public byte[] screenshot() {
        return page.screenshot();
    }

    @Override
    public byte[] screenshot(ScreenshotOptions screenshotOptions) {
        return page.screenshot(screenshotOptions);
    }

    @Override
    public List<String> selectOption(String selector, String values) {
        return page.selectOption(selector, values);
    }

    @Override
    public List<String> selectOption(String s, String s1, SelectOptionOptions selectOptionOptions) {
        return page.selectOption(s, s1, selectOptionOptions);
    }

    @Override
    public List<String> selectOption(String selector, ElementHandle values) {
        return page.selectOption(selector, values);
    }

    @Override
    public List<String> selectOption(String s, ElementHandle elementHandle, SelectOptionOptions selectOptionOptions) {
        return page.selectOption(s, elementHandle, selectOptionOptions);
    }

    @Override
    public List<String> selectOption(String selector, String[] values) {
        return page.selectOption(selector, values);
    }

    @Override
    public List<String> selectOption(String s, String[] strings, SelectOptionOptions selectOptionOptions) {
        return page.selectOption(s, strings, selectOptionOptions);
    }

    @Override
    public List<String> selectOption(String selector, SelectOption values) {
        return page.selectOption(selector, values);
    }

    @Override
    public List<String> selectOption(String s, SelectOption selectOption, SelectOptionOptions selectOptionOptions) {
        return page.selectOption(s, selectOption, selectOptionOptions);
    }

    @Override
    public List<String> selectOption(String selector, ElementHandle[] values) {
        return page.selectOption(selector, values);
    }

    @Override
    public List<String> selectOption(String s, ElementHandle[] elementHandles, SelectOptionOptions selectOptionOptions) {
        return page.selectOption(s, elementHandles, selectOptionOptions);
    }

    @Override
    public List<String> selectOption(String selector, SelectOption[] values) {
        return page.selectOption(selector, values);
    }

    @Override
    public List<String> selectOption(String s, SelectOption[] selectOptions, SelectOptionOptions selectOptionOptions) {
        return page.selectOption(s, selectOptions, selectOptionOptions);
    }

    @Override
    public void setChecked(String selector, boolean checked) {
        page.setChecked(selector, checked);
    }

    @Override
    public void setChecked(String s, boolean b, SetCheckedOptions setCheckedOptions) {
        page.setChecked(s, b, setCheckedOptions);
    }

    @Override
    public void setContent(String html) {
        page.setContent(html);
    }

    @Override
    public void setContent(String s, SetContentOptions setContentOptions) {
        page.setContent(s, setContentOptions);
    }

    @Override
    public void setDefaultNavigationTimeout(double v) {
        page.setDefaultNavigationTimeout(v);
    }

    @Override
    public void setDefaultTimeout(double v) {
        page.setDefaultTimeout(v);
    }

    @Override
    public void setExtraHTTPHeaders(Map<String, String> map) {
        page.setExtraHTTPHeaders(map);
    }

    @Override
    public void setInputFiles(String selector, Path files) {
        page.setInputFiles(selector, files);
    }

    @Override
    public void setInputFiles(String s, Path path, SetInputFilesOptions setInputFilesOptions) {
        page.setInputFiles(s, path, setInputFilesOptions);
    }

    @Override
    public void setInputFiles(String selector, Path[] files) {
        page.setInputFiles(selector, files);
    }

    @Override
    public void setInputFiles(String s, Path[] paths, SetInputFilesOptions setInputFilesOptions) {
        page.setInputFiles(s, paths, setInputFilesOptions);
    }

    @Override
    public void setInputFiles(String selector, FilePayload files) {
        page.setInputFiles(selector, files);
    }

    @Override
    public void setInputFiles(String s, FilePayload filePayload, SetInputFilesOptions setInputFilesOptions) {
        page.setInputFiles(s, filePayload, setInputFilesOptions);
    }

    @Override
    public void setInputFiles(String selector, FilePayload[] files) {
        page.setInputFiles(selector, files);
    }

    @Override
    public void setInputFiles(String s, FilePayload[] filePayloads, SetInputFilesOptions setInputFilesOptions) {
        page.setInputFiles(s, filePayloads, setInputFilesOptions);
    }

    @Override
    public void setViewportSize(int width, int height) {
        page.setViewportSize(width, height);
    }

    @Override
    public void tap(String selector) {
        page.tap(selector);
    }

    @Override
    public void tap(String s, TapOptions tapOptions) {
        page.tap(s, tapOptions);
    }

    @Override
    public String textContent(String selector) {
        return page.textContent(selector);
    }

    @Override
    public String textContent(String s, TextContentOptions textContentOptions) {
        return page.textContent(s, textContentOptions);
    }

    @Override
    public String title() {
        return page.title();
    }

    @Override
    public Touchscreen touchscreen() {
        return page.touchscreen();
    }

    @Override
    public void type(String selector, String text) {
        page.type(selector, text);
    }

    @Override
    public void type(String s, String s1, TypeOptions typeOptions) {
        page.type(s, s1, typeOptions);
    }

    @Override
    public void uncheck(String selector) {
        page.uncheck(selector);
    }

    @Override
    public void uncheck(String s, UncheckOptions uncheckOptions) {
        page.uncheck(s, uncheckOptions);
    }

    @Override
    public void unroute(String url) {
        page.unroute(url);
    }

    @Override
    public void unroute(String s, Consumer<Route> consumer) {
        page.unroute(s, consumer);
    }

    @Override
    public void unroute(Pattern url) {
        page.unroute(url);
    }

    @Override
    public void unroute(Pattern pattern, Consumer<Route> consumer) {
        page.unroute(pattern, consumer);
    }

    @Override
    public void unroute(Predicate<String> url) {
        page.unroute(url);
    }

    @Override
    public void unroute(Predicate<String> predicate, Consumer<Route> consumer) {
        page.unroute(predicate, consumer);
    }

    @Override
    public String url() {
        return page.url();
    }

    @Override
    public Video video() {
        return page.video();
    }

    @Override
    public ViewportSize viewportSize() {
        return page.viewportSize();
    }

    @Override
    public Page waitForClose(Runnable callback) {
        return page.waitForClose(callback);
    }

    @Override
    public Page waitForClose(WaitForCloseOptions waitForCloseOptions, Runnable runnable) {
        return page.waitForClose(waitForCloseOptions, runnable);
    }

    @Override
    public ConsoleMessage waitForConsoleMessage(Runnable callback) {
        return page.waitForConsoleMessage(callback);
    }

    @Override
    public ConsoleMessage waitForConsoleMessage(WaitForConsoleMessageOptions waitForConsoleMessageOptions, Runnable runnable) {
        return page.waitForConsoleMessage(waitForConsoleMessageOptions, runnable);
    }

    @Override
    public Download waitForDownload(Runnable callback) {
        return page.waitForDownload(callback);
    }

    @Override
    public Download waitForDownload(WaitForDownloadOptions waitForDownloadOptions, Runnable runnable) {
        return page.waitForDownload(waitForDownloadOptions, runnable);
    }

    @Override
    public FileChooser waitForFileChooser(Runnable callback) {
        return page.waitForFileChooser(callback);
    }

    @Override
    public FileChooser waitForFileChooser(WaitForFileChooserOptions waitForFileChooserOptions, Runnable runnable) {
        return page.waitForFileChooser(waitForFileChooserOptions, runnable);
    }

    @Override
    public JSHandle waitForFunction(String expression, Object arg) {
        return page.waitForFunction(expression, arg);
    }

    @Override
    public JSHandle waitForFunction(String expression) {
        return page.waitForFunction(expression);
    }

    @Override
    public JSHandle waitForFunction(String s, Object o, WaitForFunctionOptions waitForFunctionOptions) {
        return page.waitForFunction(s, o, waitForFunctionOptions);
    }

    @Override
    public void waitForLoadState(LoadState state) {
        page.waitForLoadState(state);
    }

    @Override
    public void waitForLoadState() {
        page.waitForLoadState();
    }

    @Override
    public void waitForLoadState(LoadState loadState, WaitForLoadStateOptions waitForLoadStateOptions) {
        page.waitForLoadState(loadState, waitForLoadStateOptions);
    }

    @Override
    public Response waitForNavigation(Runnable callback) {
        return page.waitForNavigation(callback);
    }

    @Override
    public Response waitForNavigation(WaitForNavigationOptions waitForNavigationOptions, Runnable runnable) {
        return page.waitForNavigation(waitForNavigationOptions, runnable);
    }

    @Override
    public Page waitForPopup(Runnable callback) {
        return page.waitForPopup(callback);
    }

    @Override
    public Page waitForPopup(WaitForPopupOptions waitForPopupOptions, Runnable runnable) {
        return page.waitForPopup(waitForPopupOptions, runnable);
    }

    @Override
    public Request waitForRequest(String urlOrPredicate, Runnable callback) {
        return page.waitForRequest(urlOrPredicate, callback);
    }

    @Override
    public Request waitForRequest(String s, WaitForRequestOptions waitForRequestOptions, Runnable runnable) {
        return page.waitForRequest(s, waitForRequestOptions, runnable);
    }

    @Override
    public Request waitForRequest(Pattern urlOrPredicate, Runnable callback) {
        return page.waitForRequest(urlOrPredicate, callback);
    }

    @Override
    public Request waitForRequest(Pattern pattern, WaitForRequestOptions waitForRequestOptions, Runnable runnable) {
        return page.waitForRequest(pattern, waitForRequestOptions, runnable);
    }

    @Override
    public Request waitForRequest(Predicate<Request> urlOrPredicate, Runnable callback) {
        return page.waitForRequest(urlOrPredicate, callback);
    }

    @Override
    public Request waitForRequest(Predicate<Request> predicate, WaitForRequestOptions waitForRequestOptions, Runnable runnable) {
        return page.waitForRequest(predicate, waitForRequestOptions, runnable);
    }

    @Override
    public Request waitForRequestFinished(Runnable callback) {
        return page.waitForRequestFinished(callback);
    }

    @Override
    public Request waitForRequestFinished(WaitForRequestFinishedOptions waitForRequestFinishedOptions, Runnable runnable) {
        return page.waitForRequestFinished(waitForRequestFinishedOptions, runnable);
    }

    @Override
    public Response waitForResponse(String urlOrPredicate, Runnable callback) {
        return page.waitForResponse(urlOrPredicate, callback);
    }

    @Override
    public Response waitForResponse(String s, WaitForResponseOptions waitForResponseOptions, Runnable runnable) {
        return page.waitForResponse(s, waitForResponseOptions, runnable);
    }

    @Override
    public Response waitForResponse(Pattern urlOrPredicate, Runnable callback) {
        return page.waitForResponse(urlOrPredicate, callback);
    }

    @Override
    public Response waitForResponse(Pattern pattern, WaitForResponseOptions waitForResponseOptions, Runnable runnable) {
        return page.waitForResponse(pattern, waitForResponseOptions, runnable);
    }

    @Override
    public Response waitForResponse(Predicate<Response> urlOrPredicate, Runnable callback) {
        return page.waitForResponse(urlOrPredicate, callback);
    }

    @Override
    public Response waitForResponse(Predicate<Response> predicate, WaitForResponseOptions waitForResponseOptions, Runnable runnable) {
        return page.waitForResponse(predicate, waitForResponseOptions, runnable);
    }

    @Override
    public ElementHandle waitForSelector(String selector) {
        return page.waitForSelector(selector);
    }

    @Override
    public ElementHandle waitForSelector(String s, WaitForSelectorOptions waitForSelectorOptions) {
        return page.waitForSelector(s, waitForSelectorOptions);
    }

    @Override
    public void waitForTimeout(double v) {
        page.waitForTimeout(v);
    }

    @Override
    public void waitForURL(String url) {
        page.waitForURL(url);
    }

    @Override
    public void waitForURL(String s, WaitForURLOptions waitForURLOptions) {
        page.waitForURL(s, waitForURLOptions);
    }

    @Override
    public void waitForURL(Pattern url) {
        page.waitForURL(url);
    }

    @Override
    public void waitForURL(Pattern pattern, WaitForURLOptions waitForURLOptions) {
        page.waitForURL(pattern, waitForURLOptions);
    }

    @Override
    public void waitForURL(Predicate<String> url) {
        page.waitForURL(url);
    }

    @Override
    public void waitForURL(Predicate<String> predicate, WaitForURLOptions waitForURLOptions) {
        page.waitForURL(predicate, waitForURLOptions);
    }

    @Override
    public WebSocket waitForWebSocket(Runnable callback) {
        return page.waitForWebSocket(callback);
    }

    @Override
    public WebSocket waitForWebSocket(WaitForWebSocketOptions waitForWebSocketOptions, Runnable runnable) {
        return page.waitForWebSocket(waitForWebSocketOptions, runnable);
    }

    @Override
    public Worker waitForWorker(Runnable callback) {
        return page.waitForWorker(callback);
    }

    @Override
    public Worker waitForWorker(WaitForWorkerOptions waitForWorkerOptions, Runnable runnable) {
        return page.waitForWorker(waitForWorkerOptions, runnable);
    }

    @Override
    public List<Worker> workers() {
        return page.workers();
    }

    @Override
    public void onceDialog(Consumer<Dialog> consumer) {
        page.onceDialog(consumer);
    }
}