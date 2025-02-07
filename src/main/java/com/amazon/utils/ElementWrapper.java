package com.amazon.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class ElementWrapper {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor jsExecutor;

    // Constructor to initialize the WebDriver, WebDriverWait, and JavaScript
    // Executor
    public ElementWrapper(WebDriver driver, int waitTimeInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeInSeconds));
        this.jsExecutor = (JavascriptExecutor) driver;
    }

    // Method to find an element with explicit wait
    public WebElement findElementWithWait(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            System.out.println("Element not found within the given wait time: " + locator);
            throw e; // Rethrow the exception
        }
    }

    public Boolean waitUntilElementDisappear(WebElement element) {
        try {
            return wait.until(ExpectedConditions.stalenessOf(element));
        } catch (TimeoutException e) {
            System.out.println("Element not dissappeared " + element);
            throw e; // Rethrow the exception
        }
    }

    // Method to find an element that is clickable with wait
    public WebElement findElementToClickWithWait(By locator) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            System.out.println("Element not found within the given wait time: " + locator);
            throw e; // Rethrow the exception
        }
    }

    // **Force Click Using JavaScript**
    public void forceClick(By locator) {
        try {
            WebElement element = findElementWithWait(locator);
            element.click(); // Try normal click first
        } catch (Exception e) {
            System.out.println("Normal click failed. Trying JavaScript click for: " + locator);
            WebElement element = driver.findElement(locator);
            jsExecutor.executeScript("arguments[0].click();", element);
        }
    }
}
