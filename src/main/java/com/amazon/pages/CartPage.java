package com.amazon.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.amazon.utils.ElementWrapper;
import java.util.List;
import java.util.Map;

public class CartPage extends BasePage {
    private ElementWrapper elementWrapper;

    // Locators
    private By cartButton = By.xpath("//*[@id='nav-cart-count-container']");
    private By cartItems = By.id("sc-active-cart");
    private By theItemsInCart = By.xpath("//*[@data-asin]");
    private By cartSubtotal = By.xpath("//*[@id=\"sc-subtotal-amount-buybox\"]");
    private By itemNameLocator = By.xpath(".//h4");
    private By itemPriceLocator = By.xpath(".//span[contains(text(),'EGP')][2]");

    // Constructor
    public CartPage(WebDriver driver) {
        super(driver);
        this.elementWrapper = new ElementWrapper(driver, 10); // Wait for 10 seconds
    }

    // Verify if items exist in the cart
    public void assertItemsInCart() {
        int cartItemCount = driver.findElements(cartItems).size();
        Assert.assertTrue(cartItemCount > 0, "Cart is empty, but expected items to be present.");
    }

    // Navigate to the cart page
    public void goToCart() {
        driver.get("https://www.amazon.eg/-/en/gp/cart/view.html?ref_=nav_cart");
        // elementWrapper.forceClick(cartButton);
    }

    // Verify the items in the cart with the expected data
    public void assertCartItems(Map<String, Double> expectedItems) {
        elementWrapper.findElementWithWait(theItemsInCart);

        // Get the list of cart items
        List<WebElement> itemsInCart = driver.findElements(theItemsInCart);
        Assert.assertFalse(itemsInCart.isEmpty(), "No items found in the cart.");

        double totalPrice = 0;

        for (WebElement item : itemsInCart) {
            // Get the name and price of the item in the cart
            String cartItemName = item.findElement(itemNameLocator).getText();
            String cartItemPriceText = item.findElement(itemPriceLocator).getText();
            double cartItemPrice = Double.parseDouble(cartItemPriceText.replace("EGP", "").replace(",", "").trim());
            String itemAsin = item.getAttribute("data-asin"); // Get the data-asin attribute

            // Assert that the expected item exists in the cart
            Assert.assertTrue(expectedItems.containsKey(itemAsin), "Unexpected item in cart: " + itemAsin);

            // Assert price match
            double expectedPrice = expectedItems.get(itemAsin);
            Assert.assertEquals(cartItemPrice, expectedPrice,
                    "Price mismatch for item " + itemAsin + ". Expected: " + expectedPrice + ", Found: "
                            + cartItemPrice);

            totalPrice += cartItemPrice;
        }

        // Assert subtotal
        double expectedSubtotal = expectedItems.values().stream().mapToDouble(Double::doubleValue).sum();
        String cartSubtotalText = driver.findElement(cartSubtotal).getText();
        double actualSubtotal = Double.parseDouble(cartSubtotalText.replace("EGP", "").replace(",", "").trim());

        Assert.assertEquals(actualSubtotal, expectedSubtotal,
                "Subtotal mismatch. Expected: " + expectedSubtotal + ", Found: " + actualSubtotal);
    }
}
