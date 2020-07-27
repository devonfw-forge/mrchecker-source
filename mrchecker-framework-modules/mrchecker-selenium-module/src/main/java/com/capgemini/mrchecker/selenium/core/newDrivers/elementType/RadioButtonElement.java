package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;

public class RadioButtonElement extends BasicElement implements IBasicElement {
	
	private By					inputChildsSelector;
	private List<WebElement>	listElements;
	private List<String>		listSelectedAttributes;
	
	/**
	 * @param cssSelector
	 *            - selector of Radio Button element's set
	 **/
	public RadioButtonElement(By cssSelector) {
		this(cssSelector, By.cssSelector("input"), Collections.singletonList("selected"));
	}
	
	/**
	 * @param cssSelector
	 *            - selector of Radio Button element's set
	 * @param inputChildsSelector
	 *            - selector of relative path from Radio Button element's set to basic input element
	 **/
	public RadioButtonElement(By cssSelector, By inputChildsSelector) {
		this(ElementType.INPUT_TEXT, cssSelector, inputChildsSelector, Collections.singletonList("selected"));
	}
	
	/**
	 * @param cssSelector
	 *            - selector of Radio Button element's set
	 * @param inputChildsSelector
	 *            - selector of relative path from Radio Button element's set to basic input element
	 * @param listSelectedAttributes
	 *            - list of class name describing selected item
	 **/
	public RadioButtonElement(By cssSelector, By inputChildsSelector, List<String> listSelectedAttributes) {
		this(ElementType.INPUT_TEXT, cssSelector, inputChildsSelector, listSelectedAttributes);
	}
	
	/**
	 * @param cssSelector
	 *            - selector of Radio Button element's set
	 * @param inputChildsSelector
	 *            - selector of relative path from Radio Button element's set to basic input element
	 * @param listSelectedAttributes
	 *            - list of class name describing selected item
	 **/
	protected RadioButtonElement(ElementType elemType, By cssSelector, By inputChildsSelector,
			List<String> listSelectedAttributes) {
		super(elemType, cssSelector);
		setInputChildsSelector(inputChildsSelector);
		setSelectedAttributes(listSelectedAttributes);
	}
	
	public int getSelectedItemIndex() {
		setItems();
		return listElements.indexOf(getSelectedItem());
	}
	
	public String getSelectedItemText() {
		return getSelectedItem().getText();
	}
	
	public String getSelectedItemValue() {
		return getSelectedItem().getAttribute("value");
	}
	
	public List<String> getTextList() {
		return Arrays.asList(getTextArray());
	}
	
	public int getItemsCount() {
		setItems();
		return listElements.size();
	}
	
	public boolean isItemSelectedByText(String elementText) {
		return getSelectedItemText()
				.equals(elementText);
	}
	
	public boolean isItemSelectedByIndex(int elementIndex) {
		return getSelectedItemIndex() == elementIndex;
	}
	
	public boolean isItemSelectedByValue(String elementValue) {
		return getSelectedItemValue()
				.equals(elementValue);
	}
	
	public void selectItemByText(String elementText) {
		WebElement elementToClick = getItemByText(elementText);
		elementToClick.click();
		checkIsItemClicked(elementToClick);
	}
	
	public void selectItemByIndex(int elementIndex) {
		WebElement elementToClick = getItemByIndex(elementIndex);
		elementToClick.click();
		checkIsItemClicked(elementToClick);
	}
	
	public void selectItemByValue(String elementValue) {
		WebElement elementToClick = getItemByValue(elementValue);
		elementToClick.click();
		checkIsItemClicked(elementToClick);
	}
	
	private void checkIsItemClicked(WebElement element) {
		if (isItemSelected(element)) {
			System.out.println(getElementTypeName() + ": " + toString() + " isn't clicked.");
		}
	}
	
	private String[] getTextArray() {
		return getElement()
				.getText()
				.trim()
				.split("\n");
	}
	
	private void setInputChildsSelector(By selector) {
		inputChildsSelector = selector;
	}
	
	private void setSelectedAttributes(List<String> listSelectedAttributes) {
		this.listSelectedAttributes = listSelectedAttributes;
	}
	
	private void setListItems(List<WebElement> listElements) {
		this.listElements = listElements;
	}
	
	private boolean isItemSelected(WebElement el) {
		return el.isSelected();
	}
	
	private void setItems() {
		setListItems(getElement()
				.findElements(inputChildsSelector));
	}
	
	private WebElement getItemByIndex(int index) {
		setItems();
		
		if (listElements.isEmpty()) {
			throw new BFElementNotFoundException("Any " + getElementTypeName() + " element was found.");
		}
		
		return listElements.get(index);
	}
	
	private WebElement getItemByText(String visibleText) {
		for (int i = 0; i < getItemsCount(); i++) {
			if (listElements.get(i)
					.getText()
					.equals(visibleText)) {
				return listElements.get(i);
			}
		}
		throw new BFElementNotFoundException(getElementTypeName() + " with text: " + visibleText + " wasn't found in "
				+ Arrays.toString(getTextArray()));
	}
	
	private WebElement getItemByValue(String value) {
		setItems();
		for (int i = 0; i < getItemsCount(); i++) {
			if (listElements.get(i)
					.getAttribute("value")
					.equals(value)) {
				return listElements.get(i);
			}
		}
		throw new BFElementNotFoundException(
				getElementTypeName() + " with value: " + value + " wasn't found in " + Arrays.toString(getTextArray()));
	}
	
	private boolean isClassContainSelectionAttributes(String classAttribute) {
		for (String listSelectedAttribute : listSelectedAttributes) {
			if (classAttribute.contains(listSelectedAttribute)) {
				return true;
			}
		}
		return false;
	}
	
	private WebElement getSelectedItem() {
		setItems();
		for (WebElement listElement : listElements) {
			if (isItemSelected(listElement)
					|| isClassContainSelectionAttributes(listElement
							.getAttribute("class"))) {
				return listElement;
			}
		}
		throw new BFElementNotFoundException("Any element is selected in " + Arrays.toString(getTextArray()));
	}
}