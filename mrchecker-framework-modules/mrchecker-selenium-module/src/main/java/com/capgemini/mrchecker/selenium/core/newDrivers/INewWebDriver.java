package com.capgemini.mrchecker.selenium.core.newDrivers;

import java.math.BigDecimal;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import com.capgemini.mrchecker.selenium.core.newDrivers.elementType.*;

public interface INewWebDriver extends WebDriver {
	
	/**
	 * Find all elements within the current page using the given mechanism. This method is affected by the 'implicit
	 * wait' times in force at the time of execution. When implicitly waiting, this method will return as soon as there
	 * are more than 0 items in the found collection, or will return an empty list if the timeout is reached.
	 * 
	 * @deprecated As of release 1.0.0, replaced by {@link #findElementDynamics(By)()}
	 * @param by
	 *            The locating mechanism to use
	 * @return A list of all {@link WebElement}s, or an empty list if nothing matches
	 * @see org.openqa.selenium.By
	 * @see org.openqa.selenium.WebDriver.Timeouts
	 */
	@Deprecated
	List<WebElement> findElements(By by);
	
	/**
	 * Find the first {@link WebElement} using the given method. This method is affected by the 'implicit wait' times in
	 * force at the time of execution. The findElement(..) invocation will return a matching row, or try again
	 * repeatedly until the configured timeout is reached. findElement should not be used to look for non-present
	 * elements, use {@link #findElements(By)} and assert zero length response instead.
	 * 
	 * @deprecated As of release 1.0.0, replaced by {@link #findElementDynamic(By)()}
	 * @param by
	 *            The locating mechanism
	 * @return The first matching element on the current page
	 * @throws NoSuchElementException
	 *             If no matching elements are found
	 * @see org.openqa.selenium.By
	 * @see org.openqa.selenium.WebDriver.Timeouts
	 */
	@Deprecated
	WebElement findElement(By by);
	
	/**
	 * Try to find element quietly - NoSuchElementException will not be thrown. Use it to check if element exist on page
	 * e.g. PopUp
	 * 
	 * @param elementToSearchIn
	 *            elementToSearchIn
	 * @param searchedBySelector
	 *            searchedBySelector
	 * @return found WebElement or null if couldn't find
	 */
	WebElement findElementQuietly(WebElement elementToSearchIn, By searchedBySelector);
	
	/**
	 * Try to find element quietly - NoSuchElementException will not be thrown. Use it to check if element exist on page
	 * e.g. PopUp
	 * 
	 * @param searchedBySelector
	 *            searchedBySelector
	 * @return found WebElement or null if couldn't find
	 */
	WebElement findElementQuietly(By searchedBySelector);
	
	/**
	 * /** Try to find by dynamic element - will wait given by user an amount of seconds for an element to load on page.
	 * If element will not be found will throw an exception (PiAtElementNotLoadedException)
	 * 
	 * @param by
	 *            selector
	 * @param timeOut
	 *            - maximum time to wait for search WebElement
	 * @return found WebElement object
	 * @throws BFElementNotFoundException
	 *             BFElementNotFoundException BFElementNotFoundException
	 */
	WebElement findElementDynamic(By by, int timeOut) throws BFElementNotFoundException;
	
	/**
	 * Try to find by dynamic element - will wait given by user an amount of seconds for an element to load on page. If
	 * element will not be found will throw an exception (PiAtElementNotLoadedException)
	 * 
	 * @param by
	 *            selector
	 * @return found WebElement object
	 * @throws BFElementNotFoundException
	 *             BFElementNotFoundException BFElementNotFoundException
	 */
	WebElement findElementDynamic(By by) throws BFElementNotFoundException;
	
	/**
	 * Try to find by dynamic List of elements - will wait given by user an amount of seconds for an element to load on
	 * page. If element will not be found will throw an exception (PiAtElementNotLoadedException)
	 * 
	 * @param by
	 *            selector
	 * @param timeOut
	 *            - maximum time to wait for search WebElement
	 * @return found WebElement object
	 * @throws BFElementNotFoundException
	 *             BFElementNotFoundException
	 */
	List<WebElement> findElementDynamics(By by, int timeOut);
	
	/**
	 * Try to find by dynamic List of elements - will wait {@link BasePage#EXPLICIT_WAIT_TIMER} seconds for an element
	 * to
	 * load on page. If element will not be found then throw an exception (PiAtElementNotLoadedException)
	 * 
	 * @param by
	 *            selector
	 * @return found WebElement object
	 * @throws BFElementNotFoundException
	 *             BFElementNotFoundException
	 */
	List<WebElement> findElementDynamics(By by);
	
	/**
	 * Waits {@link {@link BasePage#EXPLICIT_WAIT_TIMER}} seconds until an element will be enabled to action. If element
	 * after
	 * this time will not be enabled then throw an exception (BFElementNotFoundException)
	 * 
	 * @param selector
	 *            selector
	 * @return WebElement object ready to click
	 * @throws BFElementNotFoundException
	 *             BFElementNotFoundException
	 */
	WebElement waitForElement(final By selector);
	
	/**
	 * Waits {@link {@link BasePage#EXPLICIT_WAIT_TIMER}} seconds until an element will be clickable. If element will
	 * not be
	 * clickable then throw an exception (BFElementNotFoundException)
	 * 
	 * @param selector
	 *            selector
	 * @return WebElement object ready to click
	 * @author
	 * @throws BFElementNotFoundException
	 *             BFElementNotFoundException
	 */
	WebElement waitUntilElementIsClickable(final By selector);
	
	/**
	 * Waits for element located by selector to become visible. Throws exception if element is not visible after
	 * specified time period.
	 * 
	 * @param selector
	 *            selector
	 * @return found WebElement object
	 * @throws BFElementNotFoundException
	 *             BFElementNotFoundException
	 */
	WebElement waitForElementVisible(final By selector);
	
	/**
	 * This function force browser (especially usable for IE ) to wait till page is loaded
	 *
	 * @throws BFElementNotFoundException
	 *             BFElementNotFoundException
	 */
	void waitForPageLoaded();
	
	/**
	 * Operations on Button
	 */
	Button elementButton(final By selector);
	
	/**
	 * Operations on Radio Buttons
	 */
	RadioButtonElement elementRadioButton(final By selector);
	
	RadioButtonElement elementRadioButton(final By selector, final By inputChildsSelector);
	
	RadioButtonElement elementRadioButton(final By selector,
			final By inputChildsSelector,
			final List<String> listSelectedAttributes);
	
	/**
	 * Operations on Input Text field
	 */
	InputTextElement elementInputText(final By selector);
	
	/**
	 * Operations on Dropdown List
	 */
	DropdownListElement elementDropdownList(final By selector);
	
	/**
	 * Operations on Lists
	 */
	ListElements elementList(final By selector);
	
	/**
	 * Operations on Checkbox
	 */
	CheckBox elementCheckbox(final By selector);
	
	/**
	 * Operations on Label
	 */
	LabelElement elementLabel(final By selector);
	
	/**
	 * Operations on image
	 */
	ImageElement elementImage(final By selector);
	
	/**
	 * Operations on Tabs
	 */
	TabElement elementTab(final By selector);
	
	TabElement elementTab(final By selector, final By inputChildsSelector);
	
	TabElement elementTab(final By selector, final By inputChildsSelector, final List<String> listSelectedAttributes);
	
	/**
	 * Operations on Navigation Bar
	 */
	NavigationBarElement elementNavigationBar(final By selector);
	
	NavigationBarElement elementNavigationBar(final By selector, final By inputChildsSelector);
	
	/**
	 * Operations on Tooltip
	 */
	TooltipElement elementTooltip(final By cssSelector);
	
	/**
	 * Operations on Menu
	 */
	MenuElement elementMenu(final By selector);
	
	MenuElement elementMenu(final By selector, final By childsSelector);
	
	MenuElement elementMenu(final By selector, final By childsSelector, final By subMenuSelector);
	
	MenuElement elementMenu(final By selector,
			final By childsSelector,
			final By subMenuSelector,
			final By childsSubMenuSelector);
	
	/**
	 * Returns horizontal slider object for given container selector.
	 * 
	 * @param sliderContainerSelector
	 *            sliderContainerSelector
	 * @return HorizontalSliderElement
	 * @see HorizontalSliderElement
	 */
	HorizontalSliderElement elementHorizontalSlider(final By sliderContainerSelector);
	
	/**
	 * Returns horizontal slider object for given container selector, slider selector and value selector.
	 * 
	 * @param sliderContainerSelector
	 *            sliderContainerSelector
	 * @param sliderSelector
	 *            sliderSelector
	 * @param valueSelector
	 *            valueSelector
	 * @return HorizontalSliderElement
	 * @see HorizontalSliderElement
	 */
	HorizontalSliderElement elementHorizontalSlider(final By sliderContainerSelector, final By sliderSelector, final By valueSelector);
	
	/**
	 * Returns horizontal slider object for given container selector, slider selector and value selector.
	 * Also a range of possible steps and step's size should be defined to perform operations/
	 * 
	 * @param sliderContainerSelector
	 *            sliderContainerSelector
	 * @param sliderSelector
	 *            sliderSelector
	 * @param valueSelector
	 *            valueSelector
	 * @param minRange
	 *            minRange
	 * @param maxRange
	 *            maxRange
	 * @param step
	 *            step
	 * @return HorizontalSliderElement
	 * @see HorizontalSliderElement
	 */
	HorizontalSliderElement elementHorizontalSlider(final By sliderContainerSelector,
			final By sliderSelector,
			final By valueSelector,
			final BigDecimal minRange,
			final BigDecimal maxRange,
			final BigDecimal step);
	
	/**
	 * Operations on iFrame
	 */
	IFrame elementIFrame(By selector);
	
	/**
	 * Mouse right click on specific element defined by @param selector
	 */
	void mouseRightClick(final By selector);
	
	/**
	 * Performs left mouse click on specific element
	 * 
	 * @param selector
	 *            selector
	 */
	void mouseLeftClick(final By selector);
	
	/**
	 * Performs left mouse click on specific element
	 * 
	 * @param element
	 *            element
	 */
	void mouseLeftClick(WebElement element);
}
