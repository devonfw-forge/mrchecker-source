package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import com.capgemini.mrchecker.selenium.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class ListElements extends BasicElement implements IBasicElement {
    public ListElements(By selector) {
        super(ElementType.LIST, selector);
    }

    public Integer getSize() {
        return BasePage.getDriver()
                .findElementDynamics(getSelector())
                .size();
    }

    public List<WebElement> getList() {
        return BasePage.getDriver()
                .findElementDynamics(getSelector());
    }

    public List<String> getTextList() {
        return getList()
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
}