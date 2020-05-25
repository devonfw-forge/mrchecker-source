package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;

/**
 * Created by LKURZAJ on 07.03.2017.
 */
public class NavigationBarElement extends BasicElement {
	
	private final By inputChildsSelector;
	
	/**
	 * @param cssSelector
	 *            - selector of Navigation Bar element's set
	 **/
	public NavigationBarElement(By cssSelector) {
		this(cssSelector, By.cssSelector("li"));
	}
	
	/**
	 * @param cssSelector
	 *            - selector of Navigation Bar element's set
	 **/
	public NavigationBarElement(By cssSelector, By inputChildsSelector) {
		super(ElementType.NAVIGATION_BAR, cssSelector);
		this.inputChildsSelector = inputChildsSelector;
	}
	
	public List<String> getItemsTextList() {
		List<WebElement> listElems = getItems();
		List<String> out = new ArrayList<>();
		
		for (WebElement listElem : listElems) {
			out.add(listElem
					.getText());
		}
		return out;
	}
	
	public String getFirstItemText() {
		return getItemsTextList()
				.get(0);
	}
	
	public String getActiveItemText() {
		List<WebElement> listItems = getItems();
		for (WebElement listItem : listItems) {
			if (listItem
					.getAttribute("class")
					.contains("active")) {
				return listItem
						.getText();
			}
		}
		throw new BFElementNotFoundException("Any active item was found in " + getElement()
				.toString());
	}
	
	public void clickFirstItem() {
		getItems()
				.get(0)
				.click();
	}
	
	public void clickActiveItem() {
		getItems()
				.get(getDepth() - 1)
				.click();
	}
	
	public void clickItemByIndex(int index) {
		if (index > 0 && index >= getItems()
				.size()) {
			throw new BFInputDataException("Index " + index + " larger than list's size: "
					+ getItems()
							.size());
		}
		getItems()
				.get(index)
				.click();
	}
	
	public void clickItemByText(String text) {
		for (int i = 0; i < getItems()
				.size(); i++) {
			if (getItems()
					.get(i)
					.getText()
					.equals(text)) {
				getItems()
						.get(i)
						.click();
				return;
			}
		}
		throw new BFElementNotFoundException("Item with text: " + text + " wasn't found in " + getText());
	}
	
	public int getDepth() {
		return getItems()
				.size();
	}
	
	private List<WebElement> getItems() {
		return getElement()
				.findElements(inputChildsSelector);
	}
}
