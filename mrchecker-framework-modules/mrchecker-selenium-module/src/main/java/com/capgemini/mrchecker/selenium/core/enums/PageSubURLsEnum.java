package com.capgemini.mrchecker.selenium.core.enums;

import com.capgemini.mrchecker.selenium.core.base.environment.GetEnvironmentParam;

public enum PageSubURLsEnum implements ISubUrl {
	
	WWW_FONT_URL("") {
		@Override
		public String subURL() {
			return GetEnvironmentParam.WWW_FONT_URL.getValue();
		}
	},
	TOOLS_QA("") {
		@Override
		public String subURL() {
			return GetEnvironmentParam.TOOLS_QA.getValue();
		}
	},
	
	WEB_SERVICE("") {
		@Override
		public String subURL() {
			return GetEnvironmentParam.WEB_SERVICE.getValue();
		}
	},
	
	REGISTRATION("registration/"),
	FRAMEANDWINDOWS("frames-and-windows"),
	MAIN_PAGE(""),
	FAQ("content/apps/static/faqpopup/index.html"),
	AUTOMATION_PRACTICE_FORM("automation-practice-form"),
	TABS("tabs/"),
	TOOLTIP("tooltip/"),
	MENU("menu/"),
	SLIDER("slider/");
	
	/*
	 * Sub urls are used as real locations in Bank test environment
	 */
	private final String subURL;
	
	PageSubURLsEnum(String subURL) {
		this.subURL = subURL;
	}
	
	@Override
	public String subURL() {
		return subURL;
	}
	
	@Override
	public String toString() {
		return subURL();
	}
}
