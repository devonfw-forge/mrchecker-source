package com.capgemini.mrchecker.selenium.core;

import com.capgemini.mrchecker.test.core.IPage;

public interface IBasePage extends IPage {
	
	boolean isLoaded();
	
	void load();
	
	BasePage getParent();
	
	void setParent(BasePage parent);
}
