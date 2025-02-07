package com.amazon.test.ui;

import com.amazon.pages.*;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AmazonTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    private VideoGamesPage videoGamesPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @BeforeClass
    public void setUp() {

        WebDriverManager.chromedriver().clearResolutionCache();
        WebDriverManager.chromedriver().clearDriverCache();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.amazon.eg/-/en");
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        videoGamesPage = new VideoGamesPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @Test
    public void testAmazonShoppingFlow() throws InterruptedException {
        // loginPage.login("your-email@example.com", "your-password");
        System.out.println("Starting addAffordableItemsToCart test...");

        homePage.openAllMenu();
        homePage.openSeeAllMenu();
        homePage.navigateToVideoGames();
        homePage.navigateToAllVideoGamesLinks();
        videoGamesPage.applyFilters();
        videoGamesPage.sortByPriceHighToLow();
        Map<String, Double> data = videoGamesPage.addAffordableItemsToCart();
        cartPage.goToCart();
        cartPage.assertCartItems(data);

        // Assert.assertTrue(cartPage.verifyItemsInCart());

        // checkoutPage.proceedToCheckout();
        // checkoutPage.selectCashOnDelivery();
        // Assert.assertTrue(checkoutPage.verifyTotalPrice());
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}