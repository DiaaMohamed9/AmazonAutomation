package com.amazon.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private By emailField = By.id("ap_email");
    private By passwordField = By.id("ap_password");
    private By loginButton = By.id("signInSubmit");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String email, String password) {
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }
}
