package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RadioButtonElement extends BasicElement implements IBasicElement {
    private By inputSelector;
    private List<WebElement> listElements;
    private List<String> listSelectedAttributes;

    /**
     * @param selector - selector of Radio Button element's set
     **/
    public RadioButtonElement(By selector) {
        this(selector, By.cssSelector("input"), Collections.singletonList("selected"));
    }

    /**
     * @param selector      - selector of Radio Button element's set
     * @param inputSelector - selector of relative path from Radio Button element's set to basic input element
     **/
    public RadioButtonElement(By selector, By inputSelector) {
        this(ElementType.INPUT_TEXT, selector, inputSelector, Collections.singletonList("selected"));
    }

    /**
     * @param selector               - selector of Radio Button element's set
     * @param inputSelector          - selector of relative path from Radio Button element's set to basic input element
     * @param listSelectedAttributes - list of class name describing selected item
     **/
    public RadioButtonElement(By selector, By inputSelector, List<String> listSelectedAttributes) {
        this(ElementType.INPUT_TEXT, selector, inputSelector, listSelectedAttributes);
    }

    /**
     * @param selector               - selector of Radio Button element's set
     * @param inputSelector          - selector of relative path from Radio Button element's set to basic input element
     * @param listSelectedAttributes - list of class name describing selected item
     **/
    protected RadioButtonElement(ElementType elemType, By selector, By inputSelector,
                                 List<String> listSelectedAttributes) {
        super(elemType, selector);
        setInputSelector(inputSelector);
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
            System.out.println(getElementTypeName() + ": " + getSelector() + " isn't clicked.");
        }
    }

    private String[] getTextArray() {
        return getWebElement()
                .getText()
                .trim()
                .split("\n");
    }

    private void setInputSelector(By inputSelector) {
        this.inputSelector = inputSelector;
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
        setListItems(getWebElement()
                .findElements(inputSelector));
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