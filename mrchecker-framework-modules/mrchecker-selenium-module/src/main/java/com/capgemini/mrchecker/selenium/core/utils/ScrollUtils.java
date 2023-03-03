package com.capgemini.mrchecker.selenium.core.utils;

import com.capgemini.mrchecker.selenium.core.BasePage;
import com.capgemini.mrchecker.selenium.core.newDrivers.INewWebDriver;
import com.capgemini.mrchecker.test.core.logger.BFLogger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * This class contains methods related to scrolling the page. Some methods were moved here from BasePage since they
 * operated on the whole page not on a particular component.
 *
 * @author
 */
public class ScrollUtils {
    private static final String SCROLL_TOP = "arguments[0].scrollIntoView(true);";
    private static final String SCROLL_BOTTOM = "arguments[0].scrollIntoView(false);";
    private static final String SCROLL_MIDDLE = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);" + "var elementTop = arguments[0].getBoundingClientRect().top;" + "window.scrollBy(0, elementTop-(viewPortHeight/2));";

    private ScrollUtils() {
    }

    public enum Position {
        TOP,
        BOTTOM,
        MIDDLE
    }

    /**
     * Determines if the scroll bar is positioned at its lowest possible position by comparing its position before and
     * after scrolling 250 points to the bottom
     *
     * @return true if we can't scroll to bottom of the page anymore, false otherwise
     * @author
     */
    public static boolean isScrolledToTheBottomOfThePage() {
        JavascriptExecutor executor = (JavascriptExecutor) BasePage.getDriver();
        Long scrollPositionBefore = (Long) executor.executeScript("return window.scrollY;");
        executor.executeScript("window.scrollBy(0,250);");
        Long scrollPositionAfter = (Long) executor.executeScript("return window.scrollY;");
        BFLogger.logDebug(scrollPositionBefore + " " + scrollPositionAfter);
        return scrollPositionBefore.equals(scrollPositionAfter);
    }

    public static void scrollPage(INewWebDriver driver, Position position) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        switch (position) {
            case TOP:
                javascriptExecutor.executeScript("window.scrollTo(0, 0);");
                break;
            case BOTTOM:
                javascriptExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
                break;
            case MIDDLE:
            default:
                throw new AssertionError("Unable to scroll page into: " + position);
        }
    }

    public static void scrollPage(Position position) {
        scrollPage(BasePage.getDriver(), position);
    }

    public static void scrollPageToBottom() {
        scrollPage(Position.BOTTOM);
    }

    public static void scrollPageToTop() {
        scrollPage(Position.TOP);
    }

    public static void scrollPage(INewWebDriver driver, int xOffset, int yOffset) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(" + xOffset + "," + yOffset + ")", "");
    }

    /**
     * Scrolls page by offsets given by parameters Negative xOffset scrolls left, negative yOffset scrolls up
     *
     * @param xOffset horizontal offset
     * @param yOffset vertical offset
     */
    public static void scrollPage(int xOffset, int yOffset) {
        scrollPage(BasePage.getDriver(), xOffset, yOffset);
    }

    /**
     * This method moves view to the given element and tries to center it horizontally and vertically if possible Its
     * useful especially with Chrome, when driver cant click on element which is not visible on screen, becouse of for
     * example: docked-ticket-trade-inner at the bottom of the screen
     *
     * @param element WebElement which we want to be displayd on center of the screen
     */
    public static void scrollElementIntoView(WebElement element) {
        scrollElementIntoView(element, Position.MIDDLE);
    }

    public static void scrollElementIntoView(WebElement element, Position position) {
        scrollElementIntoView(BasePage.getDriver(), element, position);
        BasePage.getDriver().waitForPageLoaded();
    }

    public static void scrollElementIntoView(INewWebDriver driver, WebElement element, Position position) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        switch (position) {
            case TOP:
                javascriptExecutor.executeScript(SCROLL_TOP, element);
                break;
            case BOTTOM:
                javascriptExecutor.executeScript(SCROLL_BOTTOM, element);
                break;
            case MIDDLE:
                javascriptExecutor.executeScript(SCROLL_MIDDLE, element);
                break;
            default:
                throw new AssertionError("Unable to scroll element into: " + position);
        }
    }

    /**
     * @return true if element is presented in the middle of the page; false otherwise
     * @author
     */
    public static boolean isElementLocatedInTheMiddle(WebElement element) {
        JavascriptExecutor jse = (JavascriptExecutor) BasePage.getDriver();
        long verticalOffset = (long) jse.executeScript("return window.scrollY;");
        long elementTopPosition = element.getLocation()
                .getY();
        long elementBottomPosition = elementTopPosition + element.getSize().height;
        long windowHeight = BasePage.getDriver()
                .manage()
                .window()
                .getSize()
                .getHeight();
        long windowHeightCenter = (windowHeight / 2) + verticalOffset;
        return elementTopPosition < windowHeightCenter && elementBottomPosition > windowHeightCenter;
    }
}