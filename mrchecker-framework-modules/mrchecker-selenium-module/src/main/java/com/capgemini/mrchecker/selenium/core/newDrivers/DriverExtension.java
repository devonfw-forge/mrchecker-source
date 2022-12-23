package com.capgemini.mrchecker.selenium.core.newDrivers;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import com.capgemini.mrchecker.selenium.core.newDrivers.elementType.*;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DriverExtension {

    private INewWebDriver driver;

    public DriverExtension(INewWebDriver driver) {
        setDriver(driver);
    }

    public INewWebDriver getDriver() {
        return driver;
    }

    public WebElement findElementQuietly(By by) {
        return findElementQuietly(null, by);
    }

    @SuppressWarnings("deprecation")
    public WebElement findElementQuietly(WebElement elementToSearchIn, By by) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);

        WebElement element = null;
        try {
            element = Objects.isNull(elementToSearchIn) ? getDriver().findElement(by) : new NewRemoteWebElement(elementToSearchIn).findElement(by);
        } catch (NoSuchElementException e) {
            BFLogger.logError("Element [" + by.toString() + "] was not found in given element");
        }
        return element;
    }

    public WebElement findElementDynamic(By by) throws BFElementNotFoundException {
        long startTime = System.currentTimeMillis();
        return findElementDynamicBasic(by, startTime);
    }

    public WebElement findElementDynamic(final By by, Duration timeOut) throws BFElementNotFoundException {
        long startTime = System.currentTimeMillis();
        return findElementDynamicBasic(by, startTime, timeOut);
    }

    public WebElement findElementDynamic(WebElement elementToSearchIn, By by) throws BFElementNotFoundException {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        long startTime = System.currentTimeMillis();
        return Objects.isNull(elementToSearchIn) ? findElementDynamic(by) : findElementDynamicBasic(by, startTime);
    }

    private WebElement findElementDynamicBasic(By by, long startTime) throws BFElementNotFoundException {
        return this.findElementDynamicBasic(by, startTime, DriverManager.EXPLICIT_WAIT);
    }

    private WebElement findElementDynamicBasic(By by, long startTime, Duration timeOut) throws BFElementNotFoundException {
        WebElement element;
        WebDriverWait wait = webDriverWait(timeOut);
        try {
            element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (TimeoutException | NoSuchElementException e) {
            boolean isTimeout = true;
            throw new BFElementNotFoundException(by, isTimeout, timeOut);
        }
        BFLogger.logTime(startTime, "findElementDynamic()", by.toString());
        return element;
    }

    public List<WebElement> findElementsDynamic(By by, Duration timeOut) throws BFElementNotFoundException {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        long startTime = System.currentTimeMillis();
        WebDriverWait wait = webDriverWait(timeOut);
        List<WebElement> elements;
        try {
            elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        } catch (BFElementNotFoundException | TimeoutException e) {
            throw new BFElementNotFoundException(by, true, timeOut);
        }
        if (elements.isEmpty()) {
            BFLogger.logError("Not found element : " + by.toString() + ".");
        }
        BFLogger.logTime(startTime, "findElementDynamics()", by.toString());
        return elements;
    }

    public List<WebElement> findElementsDynamic(By by) throws BFElementNotFoundException {
        return findElementsDynamic(by, DriverManager.EXPLICIT_WAIT);
    }

    public WebElement waitForElement(final By by) throws BFElementNotFoundException {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        long startTime = System.currentTimeMillis();
        WebElement element;

        FluentWait<WebDriver> wait = new FluentWait<>(driver);
        wait.pollingEvery(Duration.ofMillis(250));
        wait.withTimeout(Duration.ofMinutes(2));
        try {
            element = webDriverWait().until(driver -> driver.findElement(by));
        } catch (TimeoutException | NoSuchElementException e) {
            boolean isTimeout = true;
            throw new BFElementNotFoundException(by, isTimeout, DriverManager.EXPLICIT_WAIT);
        }
        BFLogger.logTime(startTime, "waitForElement()", by.toString());
        return element;
    }

    public WebElement waitUntilElementIsClickable(final By by) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        long startTime = System.currentTimeMillis();
        WebElement element;
        try {
            element = webDriverWait().until(ExpectedConditions.elementToBeClickable(by));
        } catch (TimeoutException | NoSuchElementException e) {
            boolean isTimeout = true;
            throw new BFElementNotFoundException(by, isTimeout, DriverManager.EXPLICIT_WAIT);
        }
        BFLogger.logTime(startTime, "waitUntilElementIsClickable()", by.toString());
        return element;
    }

    public WebElement waitForElementVisible(final By by) throws BFElementNotFoundException {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        long startTime = System.currentTimeMillis();

        WebElement element;
        try {
            element = webDriverWait().until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (TimeoutException | NoSuchElementException e) {
            boolean isTimeout = true;
            throw new BFElementNotFoundException(by, isTimeout, DriverManager.EXPLICIT_WAIT);
        }
        BFLogger.logTime(startTime, "waitForElementVisible()", by.toString());
        return element;
    }

    public void waitForPageLoaded() throws BFElementNotFoundException {
        long startTime = System.currentTimeMillis();
        final String jsVariable = "return document.readyState";
        ExpectedCondition<Boolean> expectation = driver -> ((JavascriptExecutor) driver).executeScript(jsVariable)
                .equals("complete");
        Duration progressBarWaitTimer = BasePage.PROGRESS_BAR_WAIT_TIMER;
        WebDriverWait wait = webDriverWait(progressBarWaitTimer);
        try {
            wait.until(expectation);
        } catch (TimeoutException | NoSuchElementException e) {
            boolean isTimeout = true;
            throw new BFElementNotFoundException(By.cssSelector(jsVariable), isTimeout, progressBarWaitTimer);
        }
        BFLogger.logTime(startTime, "waitForPageLoaded");
    }

    private void setDriver(INewWebDriver driver) {
        this.driver = driver;
    }

    private WebDriverWait webDriverWait() {
        return webDriverWait(DriverManager.EXPLICIT_WAIT);
    }

    private WebDriverWait webDriverWait(Duration timeOut) {
        return new WebDriverWait(getDriver(), timeOut);
    }

    static List<WebElement> convertWebElementList(List<WebElement> elementList) {
        List<WebElement> elementsList = new ArrayList<>();
        for (WebElement element : elementList) {
            elementsList.add(new NewRemoteWebElement(element));
        }
        return elementsList;
    }

    public Button elementButton(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new Button(selector);
    }

    public RadioButtonElement elementRadioButton(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new RadioButtonElement(selector);
    }

    public RadioButtonElement elementRadioButton(By selector, By childs) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new RadioButtonElement(selector, childs);
    }

    public RadioButtonElement elementRadioButton(By selector, By childs, List<String> selectedAttributes) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new RadioButtonElement(selector, childs, selectedAttributes);
    }

    public InputTextElement elementInputText(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new InputTextElement(selector);
    }

    public DropdownListElement elementDropdownList(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new DropdownListElement(selector);
    }

    public ListElements elementList(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new ListElements(selector);
    }

    public CheckBox elementCheckbox(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new CheckBox(selector);
    }

    public CheckBox elementCheckbox(By selector, By childs) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new CheckBox(selector, childs);
    }

    public LabelElement elementLabel(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new LabelElement(selector);
    }

    public TabElement elementTab(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new TabElement(selector);
    }

    public TabElement elementTab(By selector, By childs) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new TabElement(selector, childs);
    }

    public TabElement elementTab(By selector, By childs, List<String> selectedAttributes) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new TabElement(selector, childs, selectedAttributes);
    }

    public NavigationBarElement elementNavigationBar(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new NavigationBarElement(selector);
    }

    public NavigationBarElement elementNavigationBar(By selector, By inputChildsSelector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new NavigationBarElement(selector, inputChildsSelector);
    }

    public TooltipElement elementTooltip(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new TooltipElement(selector);
    }

    public MenuElement elementMenu(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new MenuElement(selector);
    }

    public MenuElement elementMenu(By selector, By childsSelector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new MenuElement(selector, childsSelector);
    }

    public MenuElement elementMenu(By selector, By childsSelector, By subMenuSelector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new MenuElement(selector, childsSelector, subMenuSelector);
    }

    public MenuElement elementMenu(By selector, By childsSelector, By subMenuSelector, By childsSubMenuSelector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new MenuElement(selector, childsSelector, subMenuSelector, childsSubMenuSelector);
    }

    public HorizontalSliderElement elementHorizontalSlider(final By sliderContainerSelector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new HorizontalSliderElement(sliderContainerSelector);
    }

    public HorizontalSliderElement elementHorizontalSlider(final By sliderContainerSelector, final By sliderSelector, final By valueSelector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new HorizontalSliderElement(sliderContainerSelector, sliderSelector, valueSelector);
    }

    public HorizontalSliderElement elementHorizontalSlider(final By sliderContainerSelector,
                                                           final By sliderSelector,
                                                           final By valueSelector,
                                                           final BigDecimal minRange,
                                                           final BigDecimal maxRange,
                                                           final BigDecimal step) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new HorizontalSliderElement(sliderContainerSelector, sliderSelector, valueSelector, minRange, maxRange, step);
    }

    public IFrame elementIFrame(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new IFrame(selector);
    }

    public ImageElement elementImage(By selector) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return new ImageElement(selector);
    }

    public void mouseRightClick(By selector) {
        WebElement element = findElementDynamic(selector);
        Actions action = new Actions(getDriver()).contextClick(element);
        action.build()
                .perform();
    }

    public void mouseLeftClick(By selector) {
        WebElement element = getDriver().findElementQuietly(selector);
        if (element != null) {
            mouseLeftClick(element);
        } else {
            BFLogger.logDebug("Unable to perform left mouse click due to null WebElement");
        }
    }

    public void mouseLeftClick(WebElement element) {
        new Actions(getDriver()).click(element)
                .build()
                .perform();
    }

}
