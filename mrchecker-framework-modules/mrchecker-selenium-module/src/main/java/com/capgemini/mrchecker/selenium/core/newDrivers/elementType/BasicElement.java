package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.exceptions.BFElementNotFoundException;

public abstract class BasicElement implements IBasicElement {
	
	private final ElementType	type;
	private By					cssSelector;
	
	public BasicElement(ElementType type, By cssSelector) {
		this.type = type;
		this.setCssSelector(cssSelector);
		
		load();
	}
	
	@Override
	public WebElement load() {
		return getElement();
	}
	
	@Override
	public String getElementTypeName() {
		return type.toString();
	}
	
	public WebElement getElement() throws BFElementNotFoundException {
		return BasePage.getDriver()
				.findElementDynamic(getCssSelector());
	}
	
	public String getClassName() {
		return getElement().getAttribute("class");
	}
	
	public String getValue() {
		return getElement().getAttribute("value");
	}
	
	public String getText() {
		return getElement().getText();
	}
	
	public Boolean isDisplayed() {
		return BasePage.isElementDisplayed(getCssSelector());
	}
	
	private By getCssSelector() {
		return cssSelector;
	}
	
	private void setCssSelector(By cssSelector) {
		this.cssSelector = cssSelector;
	}
}
