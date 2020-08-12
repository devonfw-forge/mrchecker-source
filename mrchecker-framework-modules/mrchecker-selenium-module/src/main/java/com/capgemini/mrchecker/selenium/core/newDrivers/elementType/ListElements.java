package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.capgemini.mrchecker.selenium.core.BasePage;

public class ListElements extends BasicElement implements IBasicElement {
	
	private final By cssSelector;
	
	public ListElements(By cssSelector) {
		super(ElementType.LIST, cssSelector);
		this.cssSelector = cssSelector;
	}
	
	public Integer getSize() {
		return BasePage.getDriver()
				.findElementDynamics(cssSelector)
				.size();
		
	}
	
	public List<WebElement> getList() {
		return BasePage.getDriver()
				.findElementDynamics(cssSelector);
	}
	
	public List<String> getTextList() {
		return getList()
				.stream()
				.map(WebElement::getText)
				.collect(Collectors.toList());
	}
}
