package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.capgemini.mrchecker.selenium.core.exceptions.BFComponentStateException;

public class DropdownListElement extends BasicElement implements IBasicElement {
	
	public DropdownListElement(By cssSelector) {
		super(ElementType.DROPDOWN, cssSelector);
	}
	
	public void selectDropdownByIndex(int index) {
		getObject()
				.selectByIndex(index);
		List<WebElement> list = getObject()
				.getOptions();
		
		if (!list.get(index)
				.isSelected()) {
			throw new BFComponentStateException(ElementType.DROPDOWN.toString(), "select", "Option with index: "
					+ index + " should be set from in " + getObject()
							.toString());
		}
	}
	
	public boolean isDropdownElementSelectedByIndex(int index) {
		return getPossibleOptions()
				.get(index)
				.isSelected();
	}
	
	public void selectDropdownByValue(String value) {
		value = value.trim();
		getObject()
				.selectByValue(value);
		if (!isDropdownElementSelectedByValue(value)) {
			throw new BFComponentStateException(ElementType.DROPDOWN.toString(), "select", "Option with value: "
					+ value + " should be set from in " + getObject()
							.toString());
		}
	}
	
	public void selectDropdownByVisibleText(String value) {
		boolean flag = false;
		
		getObject()
				.selectByVisibleText(value);
		
		List<String> list = getAllSelectedOptionsText();
		for (String s : list) {
			if (s.equals(value)) {
				flag = true;
				break;
			}
		}
		
		if (!flag) {
			throw new RuntimeException(
					"Option with text: " + value + " wasn't selected in " + getObject()
							.toString());
		}
	}
	
	public List<String> getAllSelectedOptionsText() {
		List<WebElement> list = getObject()
				.getAllSelectedOptions();
		return getValuesFromWebElements(list);
	}
	
	public String getFirstSelectedOptionText() {
		return getObject()
				.getFirstSelectedOption()
				.getText()
				.trim();
	}
	
	public int getAmountOfPossibleValues() {
		List<WebElement> list = getObject()
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
		return getObject()
				.getOptions();
	}
	
	private List<String> getPossibleValuesText() {
		List<WebElement> list = getObject()
				.getOptions();
		return getValuesFromWebElements(list);
	}
	
	private int getIndexDropdownElementByValue(String value) {
		return getPossibleValuesText()
				.indexOf(value);
	}
	
	private Select getObject() {
		return new Select(getElement());
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
