package com.capgemini.mrchecker.playwright.core;

import com.microsoft.playwright.Page;

public interface IBasePage {

    boolean isLoaded();

    void load();

    BasePage getParent();

    void setParent(BasePage parent);

    void setPage(Page page);

    Page getPage();
}