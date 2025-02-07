package com.amazon.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.amazon.utils.ElementWrapper;

public class HomePage extends BasePage {
    private ElementWrapper elementWrapper;
    private By allMenu = By.xpath("//*[@id='nav-hamburger-menu']");
    private By seeAll = By.xpath("//li//*[text()='See all']");
    private By videoGamesLink = By.xpath("//li//*[text()='Video Games']");
    private By allVideoGamesLink = By.xpath("//li//a[text()='All Video Games']");

    public HomePage(WebDriver driver) {
        super(driver);
        this.elementWrapper = new ElementWrapper(driver, 10); // Wait for 10 seconds

    }

    public void openAllMenu() {
        elementWrapper.findElementWithWait(allMenu).click();
    }

    public void openSeeAllMenu() {
        elementWrapper.findElementWithWait(seeAll).click();
    }

    public void navigateToVideoGames() {
        elementWrapper.findElementWithWait(videoGamesLink).click();
    }

    public void navigateToAllVideoGamesLinks() {
        elementWrapper.forceClick(allVideoGamesLink);
    }
}
