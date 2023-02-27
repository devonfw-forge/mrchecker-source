package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import com.capgemini.mrchecker.selenium.core.exceptions.BFComponentStateException;
import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class CheckBox extends BasicElement {
    private By inputSelector;

    /**
     * @param selector - selector of CheckBox element's set
     **/
    public CheckBox(By selector) {
        this(selector, By.cssSelector("input"));
    }

    /**
     * @param selector      - selector of CheckBox element's set
     * @param inputSelector - selector of relative path from CheckBox element's set to basic input element
     **/
    public CheckBox(By selector, By inputSelector) {
        super(ElementType.CHECKBOX, selector);
        setInputSelector(inputSelector);
    }

    public void setCheckBoxByIndex(int index) {
        setCheckBoxByIndexTo(index, true);
    }

    public void setCheckBoxByValue(String value) {
        setCheckBoxByValueTo(value, true);
    }

    public void setCheckBoxByText(String text) {
        setCheckBoxByTextTo(text, true);
    }

    public void unsetCheckBoxByIndex(int index) {
        setCheckBoxByIndexTo(index, false);
    }

    public void unsetCheckBoxByValue(String value) {
        setCheckBoxByValueTo(value, false);
    }

    public void unsetCheckBoxByText(String text) {
        setCheckBoxByTextTo(text, false);
    }

    public void setAllCheckBoxes() {
        setAllCheckBoxesTo(true);
    }

    public void unsetAllCheckBoxes() {
        setAllCheckBoxesTo(false);
    }

    public boolean isCheckBoxSetByIndex(int index) {
        return getCheckBoxesList()
                .get(index)
                .isSelected();
    }

    public boolean isCheckBoxSetByValue(String value) {
        return getCheckBoxesList()
                .get(getCheckBoxIndexByValue(value))
                .isSelected();
    }

    public boolean isCheckBoxSetByText(String text) {
        return getCheckBoxesList()
                .get(getCheckBoxIndexByText(text))
                .isSelected();
    }

    public boolean isAllCheckboxesSet() {
        return isAllCheckBoxesSetTo(true);
    }

    public List<String> getTextList() {
        return Arrays.asList(getText().split("\n"));
    }

    private List<WebElement> getCheckBoxesList() {
        return getWebElement().findElements(getInputSelector());
    }

    private void setCheckBoxByIndexTo(int index, boolean destination) {
        if (getCheckBoxesList()
                .get(index)
                .isSelected() != destination) {
            getCheckBoxesList()
                    .get(index)
                    .click();
        }

        boolean currentState = getCheckBoxesList()
                .get(index)
                .isSelected();
        if (currentState != destination) {
            throw new BFComponentStateException(ElementType.CHECKBOX, "set/unset",
                    String.valueOf(currentState));
        }
    }

    private void setCheckBoxByValueTo(String value, boolean destination) {
        setCheckBoxToByAttribute(value, destination);
    }

    private void setCheckBoxByTextTo(String text, boolean destination) {
        setCheckBoxByIndexTo(getCheckBoxIndexByText(text), destination);
    }

    private int getCheckBoxIndexByText(String text) {
        List<String> textsList = getTextList();
        for (int i = 0; i < textsList.size(); i++) {
            if (textsList.get(i)
                    .equals(text.trim())) {
                return i;
            }
        }
        throw new BFElementNotFoundException("Checkbox with text " + text + " wasn't found.");
    }

    private int getCheckBoxIndexByValue(String value) {
        List<WebElement> checkBoxesList = getCheckBoxesList();
        for (int i = 0; i < checkBoxesList.size(); i++) {
            if (checkBoxesList.get(i)
                    .getAttribute("value")
                    .equals(value.trim())) {
                return i;
            }
        }
        throw new BFElementNotFoundException("Checkbox with value " + value + " wasn't found.");
    }

    private void setCheckBoxToByAttribute(String value, boolean destination) {
        List<WebElement> checkboxesList = getCheckBoxesList();
        WebElement currentElement;
        for (WebElement webElement : checkboxesList) {
            currentElement = webElement;
            if (currentElement.getAttribute("value")
                    .equals(value) && currentElement.isSelected() != destination) {
                currentElement.click();
            }
        }
    }

    private void setAllCheckBoxesTo(boolean destination) {
        List<WebElement> checkboxesList = getCheckBoxesList();
        for (WebElement webElement : checkboxesList) {
            if (webElement
                    .isSelected() != destination) {
                webElement
                        .click();
            }
        }

        if (!isAllCheckBoxesSetTo(destination)) {
            throw new BFComponentStateException(ElementType.CHECKBOX, "set/unset",
                    "setting to " + destination);
        }
    }

    private boolean isAllCheckBoxesSetTo(boolean destination) {
        List<WebElement> checkboxesList = getCheckBoxesList();
        for (WebElement webElement : checkboxesList) {
            if (webElement
                    .isSelected() != destination) {
                return false;
            }
        }
        return true;
    }

    private void setInputSelector(By inputSelector) {
        this.inputSelector = inputSelector;
    }

    public By getInputSelector() {
        return this.inputSelector;
    }
}