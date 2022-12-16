package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import org.openqa.selenium.By;

import java.util.Collections;
import java.util.List;

/**
 * Created by LKURZAJ on 06.03.2017.
 */
public class TabElement extends RadioButtonElement implements IBasicElement {

    /**
     * @param cssSelector - selector of Tab element's set
     **/
    public TabElement(By cssSelector) {
        this(cssSelector, By.cssSelector("li"));
    }

    /**
     * @param cssSelector         - selector of Tab element's set
     * @param inputChildsSelector - selector of relative path from Tab element's set to basic input element
     **/
    public TabElement(By cssSelector, By inputChildsSelector) {
        super(ElementType.TAB, cssSelector, inputChildsSelector, Collections.singletonList("ui-tabs-active ui-state-active"));
    }

    /**
     * @param cssSelector            - selector of Tab element's set
     * @param inputChildsSelector    - selector of relative path from Tab element's set to basic input element
     * @param listSelectedAttributes - list of class name describing selected item
     **/
    public TabElement(By cssSelector, By inputChildsSelector, List<String> listSelectedAttributes) {
        super(ElementType.TAB, cssSelector, inputChildsSelector, listSelectedAttributes);
    }
}
