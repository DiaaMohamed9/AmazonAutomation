package com.amazon.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import com.amazon.utils.ElementWrapper;
import java.time.Duration;

public class VideoGamesPage extends BasePage {
    private ElementWrapper elementWrapper;
    private By freeShippingFilter = By.xpath("(//span[text()='Free Shipping'])[last()]");
    private By newConditionFilter = By.xpath("//a[*[text()='New']]");
    private By sortDropdown = By.xpath("//span[text()='Sort by:']");
    private By sortHighToLow = By.xpath("//a[text()='Price: High to Low']");
    private By theItemslessThanLimitAndHasStock = By.xpath(
            "//span[contains(text(), 'EGP') and number(translate(translate(translate(text(), 'EGP', ''), ',', ''), ' ', '') ) < 15000]/ancestor\t::div[@role='listitem' and descendant::button[text()='Add to cart']]");
    private By nextPageButton = By.xpath("//a[contains(text(), 'Next')]"); // Example for next page button
    private By addToCartDialog = By.xpath("(//div[@role='dialog']//*[@data-cy=\"add-to-cart\"])[1]");

    public VideoGamesPage(WebDriver driver) {
        super(driver);
        this.elementWrapper = new ElementWrapper(driver, 10); // Wait for 10 seconds

    }

    public void applyFilters() {
        elementWrapper.findElementWithWait(freeShippingFilter).click();
        elementWrapper.findElementWithWait(newConditionFilter).click();
    }

    public void sortByPriceHighToLow() {
        elementWrapper.findElementWithWait(sortDropdown).click();
        elementWrapper.findElementWithWait(sortHighToLow).click();
    }

    public Map<String, Double> addAffordableItemsToCart() throws InterruptedException {
        Map<String, Double> addedItems = new HashMap<>(); // Store item name and price for assertion
        boolean hasNextPage = true;
        Integer limitForTest = 5;
        Integer counter = 1;
        Integer itemsCounter = 0;

        while (hasNextPage && counter <= limitForTest) {
            counter += 1;
            elementWrapper.findElementWithWait(By.xpath("//*[@role=\"listitem\"]"));

            // Find all affordable items (with price check in XPath)
            List<WebElement> affordableItems = driver.findElements(theItemslessThanLimitAndHasStock);

            // Loop through each affordable item
            if (affordableItems.size() > 0)
                for (WebElement item : affordableItems) {
                    try {
                        // int initialCartCount = getCartCount();

                        // Get item name
                        WebElement nameElement = item.findElement(By.xpath(".//h2"));

                        String itemName = nameElement.getText();
                        String itemAsin = item.getAttribute("data-asin"); // Get the data-asin attribute

                        // Get item price
                        WebElement priceElement = item.findElement(By.xpath(".//*[@class='a-price-whole']"));
                        String priceText = priceElement.getText();
                        double price = parsePrice(priceText);

                        // Click "Add to Cart" button
                        WebElement addToCartButton = item.findElement(By.xpath(".//button[text()='Add to cart']"));
                        addToCartButton.click();
                        // Check if the dialog appears within 2 seconds
                        int oldCartCount = itemsCounter;
                        try {
                            // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
                            // wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartDialog));
                            itemsCounter = getCartCount(itemsCounter + 1);
                            Thread.sleep(400);

                            // If the dialog appears, re-click the "Add to Cart" button
                            // System.out.println("Dialog appeared. Re-clicking Add to Cart.");
                            // elementWrapper.findElementWithWait(addToCartDialog).click();
                        } catch (TimeoutException e) {
                            // If the dialog doesn't appear, continue normally
                            System.out.println("Dialog did not appear, continuing normally.");
                        }
                        // Thread.sleep(1000);

                        // Store item details in the Map (name and price)
                        // If cart count increased, add item data to the dictionary
                        if (itemsCounter > oldCartCount) {
                            // Get item name and price from the element
                            // Add the item to the dictionary
                            addedItems.put(itemAsin, price);
                            System.out.println("Added item: " + itemName + " with price: " + price);
                        }

                    } catch (Exception e) {
                        System.out.println("Failed to process item: " + e.getMessage());
                    }
                }

            // Check if next page is available, and navigate to it if so
            hasNextPage = isNextPageAvailable();
            if (hasNextPage) {
                WebElement nextButton = driver.findElement(nextPageButton);
                nextButton.click();
                Thread.sleep(4000);
                if (affordableItems.size() > 0)
                    elementWrapper.waitUntilElementDisappear(affordableItems.get(0));
            }

        }

        // Print all added items in the dictionary (Map)
        System.out.println("All items added to cart:");
        for (Map.Entry<String, Double> entry : addedItems.entrySet()) {
            System.out.println("Item Name: " + entry.getKey() + ", Price: " + entry.getValue());
        }
        return addedItems; // Return the Map containing item names and prices
    }

    private double parsePrice(String priceText) {
        // Remove non-numeric characters (like "EGP" and commas)
        String numericText = priceText.replaceAll("[^0-9.]", "");
        return Double.parseDouble(numericText);
    }

    private boolean isNextPageAvailable() {
        try {
            WebElement nextButton = driver.findElement(nextPageButton);
            return nextButton.isDisplayed() && nextButton.isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private int getCartCount(int count) {
        try {

            // Use String.format to insert the expected count into the XPath
            String cartCountXPath = String.format("//*[@id='nav-cart-count' and text()='%d']", count);

            WebElement cartCountElement = elementWrapper.findElementWithWait(By.xpath(cartCountXPath));
            String cartCountText = cartCountElement.getText();
            return Integer.parseInt(cartCountText.trim());
        } catch (NoSuchElementException e) {
            return 0; // If cart count element is not found, assume count is 0
        }
    }
}
