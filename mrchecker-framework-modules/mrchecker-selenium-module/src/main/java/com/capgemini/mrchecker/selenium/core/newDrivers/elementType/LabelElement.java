package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import org.openqa.selenium.By;

/**
 * Created by LKURZAJ on 03.03.2017.
 */
public class LabelElement extends BasicElement {
    public LabelElement(By selector) {
        super(ElementType.LABEL, selector);
    }
}