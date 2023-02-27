package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import com.capgemini.mrchecker.selenium.core.exceptions.BFComponentStateException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class DropdownListElement extends BasicElement implements IBasicElement {
    public DropdownListElement(By selector) {
        super(ElementType.DROPDOWN, selector);
    }

    public void selectDropdownByIndex(int index) {
        getSelect()
                .selectByIndex(index);
        List<WebElement> list = getSelect()
                .getOptions();

        if (!list.get(index)
                .isSelected()) {
            throw new BFComponentStateException(ElementType.DROPDOWN, "select", "Option with index: "
                    + index + " should be set from in " + getSelect());
        }
    }

    public boolean isDropdownElementSelectedByIndex(int index) {
        return getPossibleOptions()
                .get(index)
                .isSelected();
    }

    public void selectDropdownByValue(String value) {
        value = value.trim();
        getSelect().selectByValue(value);
        if (!isDropdownElementSelectedByValue(value)) {
            throw new BFComponentStateException(ElementType.DROPDOWN, "select", "Option with value: "
                    + value + " should be set from in " + getSelect());
        }
    }

    public void selectDropdownByVisibleText(String value) {
        boolean flag = false;

        getSelect().selectByVisibleText(value);

        List<String> list = getAllSelectedOptionsText();
        for (String s : list) {
            if (s.equals(value)) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            throw new RuntimeException(
                    "Option with text: " + value + " wasn't selected in " + getSelect());
        }
    }

    public List<String> getAllSelectedOptionsText() {
        List<WebElement> list = getSelect()
                .getAllSelectedOptions();
        return getValuesFromWebElements(list);
    }

    public String getFirstSelectedOptionText() {
        return getSelect()
                .getFirstSelectedOption()
                .getText()
                .trim();
    }

    public int getAmountOfPossibleValues() {
        List<WebElement> list = getSelect()
                .getOptions();
        return list.size();
    }

    public boolean isDropdownElementSelectedByValue(String value) {
        int index = getIndexDropdownElementByValue(value);
        return getPossibleOptions()
                .get(index)
                .isSelected();
    }

    private List<WebElement> getPossibleOptions() {
        return getSelect()
                .getOptions();
    }

    private List<String> getPossibleValuesText() {
        List<WebElement> list = getSelect()
                .getOptions();
        return getValuesFromWebElements(list);
    }

    private int getIndexDropdownElementByValue(String value) {
        return getPossibleValuesText()
                .indexOf(value);
    }

    private Select getSelect() {
        return new Select(getWebElement());
    }

    private List<String> getValuesFromWebElements(List<WebElement> list) {
        List<String> output = new ArrayList<>();
        for (WebElement webElement : list) {
            output.add(webElement
                    .getText()
                    .trim());
        }
        return output;
    }
}