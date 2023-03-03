package com.capgemini.mrchecker.selenium.core.newDrivers.elementType;

public enum ElementType implements IElementType {
    BUTTON("Button"),
    LIST("List"),
    INPUT_TEXT("Input text element"),
    RADIO("Radio"),
    DROPDOWN("Dropdown"),
    CHECKBOX("Checkbox"),
    LABEL("Label"),
    TAB("Tab"),
    TOOLTIP("Tooltip"),
    NAVIGATION_BAR("Navigation Bar"),
    MENU("Menu"),
    IFRAME("iFrame"),
    HORIZONTAL_SLIDER("Horizontal Slider"),
    IMAGE("Image");

    private final String value;

    ElementType(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return this.value;
    }

    @Override
    public String toString() {
        return getName();
    }
}