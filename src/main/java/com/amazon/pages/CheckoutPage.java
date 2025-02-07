package com.amazon.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {
    private By cashOnDeliveryOption = By.xpath("//input[@value='CashOnDelivery']");
    private By totalPrice = By.id("order-total");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void proceedToCheckout() {
        // Placeholder: Click on checkout button
    }

    public void selectCashOnDelivery() {
        driver.findElement(cashOnDeliveryOption).click();
    }

    public boolean verifyTotalPrice() {
        // Placeholder: Implement logic to verify total price
        return true;
    }
}
