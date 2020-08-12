package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;

/**
 * Created by LKURZAJ on 08.03.2017.
 */
public class MenuElement extends BasicElement {
	
	private final By	childsSelector;
	private final By	subMenuSelector;
	private final By	childsSubMenuSelector;
	
	public MenuElement(By cssSelector) {
		this(cssSelector, By.cssSelector("li"));
	}
	
	public MenuElement(By cssSelector, By childsSelector) {
		this(cssSelector, childsSelector, cssSelector, childsSelector);
	}
	
	public MenuElement(By cssSelector, By childsSelector, By subMenuSelector) {
		this(cssSelector, childsSelector, subMenuSelector, By.cssSelector("li"));
	}
	
	public MenuElement(By cssSelector, By childsSelector, By subMenuSelector, By childsSubMenuSelector) {
		super(ElementType.MENU, cssSelector);
		this.childsSelector = childsSelector;
		this.subMenuSelector = subMenuSelector;
		this.childsSubMenuSelector = childsSubMenuSelector;
	}
	
	public void selectItemByIndex(int index) {
		getItemByIndex(index)
				.click();
	}
	
	public void selectItemByText(String text) {
		getItemByText(text)
				.click();
	}
	
	public String getItemLinkByIndex(int index) {
		return getLinkItemFromWebElement(getItemByIndex(index)).getAttribute("href");
	}
	
	public String getItemLinkByText(String text) {
		return getLinkItemFromWebElement(getItemByText(text)).getAttribute("href");
	}
	
	public List<String> getItemsTextList() {
		return Arrays.asList(getText()
				.split("\n"));
	}
	
	public void selectSubMenuItemByText(String itemText, String subItemText) {
		getSubItemByText(itemText, subItemText)
				.click();
	}
	
	public void selectSubMenuItemByIndex(int itemIndex, int subItemIndex) {
		getSubItemByIndex(itemIndex, subItemIndex)
				.click();
	}
	
	public String getSubMenuItemLinkByText(String itemText, String subItemText) {
		return getLinkItemFromWebElement(getSubItemByText(itemText, subItemText)).getAttribute("href");
	}
	
	public String getSubMenuItemLinkByIndex(int itemIndex, int subItemIndex) {
		return getLinkItemFromWebElement(getSubItemByIndex(itemIndex, subItemIndex)).getAttribute("href");
	}
	
	public int getItemsCount() {
		return getItems()
				.size();
	}
	
	private void clickMenuItemAndWaitForSubMenuVisible(WebElement menuItem) {
		menuItem.click();
		BasePage.getAction()
				.moveToElement(menuItem)
				.perform();
		BasePage.getDriver()
				.waitForElementVisible(subMenuSelector);
	}
	
	private WebElement getLinkItemFromWebElement(WebElement webElement) {
		return webElement.findElements(By.cssSelector("a"))
				.get(0);
	}
	
	private WebElement getSubItemByIndex(int itemIndex, int subItemIndex) {
		WebElement elem = getItemByIndex(itemIndex);
		clickMenuItemAndWaitForSubMenuVisible(elem);
		return getElementByIndex(getItemSubItems(elem), subItemIndex);
	}
	
	private WebElement getSubItemByText(String itemText, String subItemText) {
		WebElement webElement = getItemByText(itemText);
		clickMenuItemAndWaitForSubMenuVisible(webElement);
		return getElementByText(getItemSubItems(webElement), subItemText);
	}
	
	private List<WebElement> getItemSubItems(WebElement webElement) {
		return webElement.findElements(childsSubMenuSelector);
	}
	
	private List<WebElement> getItemSubItemsByText(WebElement webElement) {
		clickMenuItemAndWaitForSubMenuVisible(webElement);
		return getItemSubItems(webElement);
	}
	
	private WebElement getItemByText(String text) {
		return getElementByText(getItems(), text);
	}
	
	private WebElement getItemByIndex(int index) {
		return getElementByIndex(getItems(), index);
	}
	
	private List<WebElement> getItems() {
		return getElement()
				.findElements(childsSelector);
	}
	
	private WebElement getElementByIndex(List<WebElement> listElements, int index) {
		if (index < 0 || index >= listElements.size()) {
			throw new BFInputDataException("Index out of range: 0 - " + (listElements.size() - 1));
		}
		return listElements.get(index);
	}
	
	private WebElement getElementByText(List<WebElement> listElements, String text) {
		for (WebElement listElement : listElements) {
			if (listElement
					.getText()
					.equals(text)) {
				return listElement;
			}
		}
		throw new BFElementNotFoundException("Item with text: " + text + " not found in " + listElements.toString());
	}
}
