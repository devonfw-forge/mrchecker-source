package com.capgemini.mrchecker.selenium.core.newDrivers;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import com.capgemini.mrchecker.selenium.core.exceptions.BFInitializationException;
import com.capgemini.mrchecker.selenium.core.newDrivers.elementType.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.*;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public final class NewRemoteWebDriver extends RemoteWebDriver implements INewWebDriver {

    private DriverExtension driverExtension;

    public NewRemoteWebDriver(URL url, DesiredCapabilities capabilities) {
        super(url, capabilities);
        driverExtension = new DriverExtension(this);
    }

    @Override
    public Response execute(String command) {
        return super.execute(command);
    }

    @Override
    public Response execute(String driverCommand, Map<String, ?> parameters) {
        Response response = null;
        try {
            response = super.execute(driverCommand, parameters);
        } catch (SessionNotCreatedException e) {
            throw new BFInitializationException(e, "SessionNotFoundException");
        }
        return response;
    }

    @Override
    protected ExecuteMethod getExecuteMethod() {
        return new RemoteExecuteMethod(this);
    }

    /**
     * @deprecated As of release 1.0.0, replaced by {@link #findElementDynamics(By)()}
     */
    @Deprecated
    @Override
    public List<WebElement> findElements(By by) {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        return DriverExtension.convertWebElementList(super.findElements(by));
    }

    /**
     * @deprecated As of release 1.0.0, replaced by {@link #findElementDynamic(By)()}
     */
    @Deprecated
    @Override
    public WebElement findElement(By by) throws BFElementNotFoundException {
        BasePage.getAnalytics()
                .sendMethodEvent(BasePage.ANALYTICS_CATEGORY_NAME);
        WebElement elementFromDriver = null;
        try {
            elementFromDriver = super.findElement(by);
        } catch (NoSuchElementException e) {
            throw new BFElementNotFoundException(by);
        }
        return new NewRemoteWebElement(elementFromDriver);
    }

    /**
     * Try to find element quietly - NoSuchElementException will not be thrown. Use it to check if element exist on page
     * e.g. PopUp
     *
     * @param elementToSearchIn
     * @param searchedBySelector
     * @return found WebElement or null if couldn't find
     */
    @Override
    public WebElement findElementQuietly(WebElement elementToSearchIn, By searchedBySelector) {
        return driverExtension.findElementQuietly(elementToSearchIn, searchedBySelector);
    }

    /**
     * Try to find element quietly - NoSuchElementException will not be thrown. Use it to check if element exist on page
     * e.g. PopUp
     *
     * @param searchedBySelector
     * @return found WebElement or null if couldn't find
     */
    @Override
    public WebElement findElementQuietly(By searchedBySelector) {
        return driverExtension.findElementQuietly(null, searchedBySelector);
    }

    /**
     * Try to find by dynamic element - will wait given by user an amount of seconds for an element to load on page. If
     * element will not be found will throw an exception (PiAtElementNotLoadedException)
     *
     * @param by      selector
     * @param timeOut - maximum time to wait for search WebElement
     * @return found WebElement object
     * @throws BFElementNotFoundException
     * @author
     */
    public WebElement findElementDynamic(By by, Duration timeOut) throws BFElementNotFoundException {
        return driverExtension.findElementDynamic(by, timeOut);
    }

    /**
     * Try to find by dynamic element - will wait given by user an amount of seconds for an element to load on page. If
     * element will not be found will throw an exception (PiAtElementNotLoadedException)
     *
     * @param by selector
     * @return found WebElement object
     * @throws BFElementNotFoundException
     * @author
     */
    public WebElement findElementDynamic(By by) throws BFElementNotFoundException {
        return driverExtension.findElementDynamic(by);
    }

    /**
     * Try to find by dynamic List of elements - will wait given by user an amount of seconds for an element to load on
     * page. If element will not be found will throw an exception (PiAtElementNotLoadedException)
     *
     * @param by      selector
     * @param timeOut - maximum time to wait for search WebElement
     * @return found WebElement object
     * @author
     */
    public List<WebElement> findElementDynamics(By by, Duration timeOut) {
        return driverExtension.findElementsDynamic(by, timeOut);
    }

    /**
     * Try to find by dynamic List of elements - will wait {@link BasePage#EXPLICIT_WAIT_TIMER} seconds for an element
     * to
     * load on page. If element will not be found will throw an exception (PiAtElementNotLoadedException)
     *
     * @param by selector
     * @return found WebElement object
     * @author
     */
    public List<WebElement> findElementDynamics(By by) {
        return driverExtension.findElementsDynamic(by);
    }

    public WebElement waitForElement(final By selector) {
        return driverExtension.waitForElement(selector);
    }

    public WebElement waitForElementVisible(final By selector) {
        return driverExtension.waitForElementVisible(selector);
    }

    /**
     * Waits {@link BasePage#EXPLICIT_WAIT_TIMER seconds until an element will be clickable. If element will not be
     * clickable then throw an exception (BFElementNotFoundException)
     *
     * @param selector selector
     * @return WebElement object ready to click
     * @throws BFElementNotFoundException
     * @author
     */
    @Override
    public WebElement waitUntilElementIsClickable(final By selector) {
        return driverExtension.waitUntilElementIsClickable(selector);
    }

    @Override
    public void waitForPageLoaded() {
        driverExtension.waitForPageLoaded();

    }

    @Override
    public Button elementButton(By selector) {
        return driverExtension.elementButton(selector);
    }

    @Override
    public RadioButtonElement elementRadioButton(By selector) {
        return driverExtension.elementRadioButton(selector);
    }

    @Override
    public RadioButtonElement elementRadioButton(By selector, By inputChildsSelector) {
        return driverExtension.elementRadioButton(selector, inputChildsSelector);
    }

    @Override
    public RadioButtonElement elementRadioButton(By selector,
                                                 By inputChildsSelector,
                                                 List<String> listSelectedAttributes) {
        return driverExtension.elementRadioButton(selector, inputChildsSelector, listSelectedAttributes);
    }

    @Override
    public InputTextElement elementInputText(By selector) {
        return driverExtension.elementInputText(selector);
    }

    @Override
    public DropdownListElement elementDropdownList(By selector) {
        return driverExtension.elementDropdownList(selector);
    }

    @Override
    public ListElements elementList(By selector) {
        return driverExtension.elementList(selector);
    }

    @Override
    public CheckBox elementCheckbox(By selector) {
        return driverExtension.elementCheckbox(selector);
    }

    @Override
    public LabelElement elementLabel(By selector) {
        return driverExtension.elementLabel(selector);
    }

    @Override
    public ImageElement elementImage(By selector) {
        return driverExtension.elementImage(selector);
    }

    @Override
    public TabElement elementTab(By selector) {
        return driverExtension.elementTab(selector);
    }

    @Override
    public TabElement elementTab(By selector, By inputChildsSelector) {
        return driverExtension.elementTab(selector, inputChildsSelector);
    }

    @Override
    public TabElement elementTab(By selector, By inputChildsSelector, List<String> listSelectedAttributes) {
        return driverExtension.elementTab(selector, inputChildsSelector, listSelectedAttributes);
    }

    @Override
    public NavigationBarElement elementNavigationBar(By selector) {
        return driverExtension.elementNavigationBar(selector);
    }

    @Override
    public NavigationBarElement elementNavigationBar(By selector, By inputChildsSelector) {
        return driverExtension.elementNavigationBar(selector, inputChildsSelector);
    }

    @Override
    public TooltipElement elementTooltip(By cssSelector) {
        return driverExtension.elementTooltip(cssSelector);
    }

    @Override
    public MenuElement elementMenu(By selector) {
        return driverExtension.elementMenu(selector);
    }

    @Override
    public MenuElement elementMenu(By selector, By childsSelector) {
        return driverExtension.elementMenu(selector, childsSelector);
    }

    @Override
    public MenuElement elementMenu(By selector, By childsSelector, By subMenuSelector) {
        return driverExtension.elementMenu(selector, childsSelector, subMenuSelector);
    }

    @Override
    public MenuElement elementMenu(By selector, By childsSelector, By subMenuSelector, By childsSubMenuSelector) {
        return driverExtension.elementMenu(selector, childsSelector, subMenuSelector, childsSubMenuSelector);
    }

    @Override
    public HorizontalSliderElement elementHorizontalSlider(final By sliderContainerSelector) {
        return driverExtension.elementHorizontalSlider(sliderContainerSelector);
    }

    @Override
    public HorizontalSliderElement elementHorizontalSlider(final By sliderContainerSelector, final By sliderSelector, final By valueSelector) {
        return driverExtension.elementHorizontalSlider(sliderContainerSelector, sliderSelector, valueSelector);
    }

    @Override
    public HorizontalSliderElement elementHorizontalSlider(final By sliderContainerSelector,
                                                           final By sliderSelector,
                                                           final By valueSelector,
                                                           final BigDecimal minRange,
                                                           final BigDecimal maxRange,
                                                           final BigDecimal step) {
        return driverExtension.elementHorizontalSlider(sliderContainerSelector, sliderSelector, valueSelector, minRange, maxRange, step);
    }

    @Override
    public IFrame elementIFrame(By selector) {
        return driverExtension.elementIFrame(selector);
    }

    @Override
    public void mouseRightClick(By selector) {
        driverExtension.mouseRightClick(selector);
    }

    @Override
    public void mouseLeftClick(By selector) {
        driverExtension.mouseLeftClick(selector);
    }

    @Override
    public void mouseLeftClick(WebElement element) {
        driverExtension.mouseLeftClick(element);
    }
}