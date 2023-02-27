package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import org.openqa.selenium.By;

import java.util.Collections;
import java.util.List;

/**
 * Created by LKURZAJ on 06.03.2017.
 */
public class TabElement extends RadioButtonElement implements IBasicElement {
    /**
     * @param selector - selector of Tab element's set
     **/
    public TabElement(By selector) {
        this(selector, By.cssSelector("li"));
    }

    /**
     * @param selector      - selector of Tab element's set
     * @param inputSelector - selector of relative path from Tab element's set to basic input element
     **/
    public TabElement(By selector, By inputSelector) {
        super(ElementType.TAB, selector, inputSelector, Collections.singletonList("ui-tabs-active ui-state-active"));
    }

    /**
     * @param selector               - selector of Tab element's set
     * @param inputSelector          - selector of relative path from Tab element's set to basic input element
     * @param listSelectedAttributes - list of class name describing selected item
     **/
    public TabElement(By selector, By inputSelector, List<String> listSelectedAttributes) {
        super(ElementType.TAB, selector, inputSelector, listSelectedAttributes);
    }
}