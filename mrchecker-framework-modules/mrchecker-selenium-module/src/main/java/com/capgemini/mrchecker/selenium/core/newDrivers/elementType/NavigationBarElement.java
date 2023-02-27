package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;
import com.capgemini.mrchecker.test.core.exceptions.BFInputDataException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LKURZAJ on 07.03.2017.
 */
public class NavigationBarElement extends BasicElement {

    private final By inputChildrenSelector;

    /**
     * @param selector - selector of Navigation Bar element's set
     **/
    public NavigationBarElement(By selector) {
        this(selector, By.cssSelector("li"));
    }

    /**
     * @param selector - selector of Navigation Bar element's set
     **/
    public NavigationBarElement(By selector, By inputChildrenSelector) {
        super(ElementType.NAVIGATION_BAR, selector);
        this.inputChildrenSelector = inputChildrenSelector;
    }

    public List<String> getItemsTextList() {
        List<WebElement> listElements = getItems();
        List<String> out = new ArrayList<>();

        for (WebElement listElem : listElements) {
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
        throw new BFElementNotFoundException("Any active item was found in " + getWebElement());
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
        return getWebElement()
                .findElements(inputChildrenSelector);
    }
}