# Amazon Selenium Automation (Java)

## Overview

This project automates an Amazon shopping flow using Selenium WebDriver with Java and includes API tests using RestAssured.

## Prerequisites

- Install **Java (JDK 8 or later)**
- Install **Maven**
- Install **VS Code** with the Java extension

## Installation

1. Clone or unzip this project.
2. Open a terminal in the project root.
3. Run:
   ```sh
   mvn clean install
   ```

## Running UI Tests

To run the UI tests, use:

```sh
mvn test -Dsurefire.suiteXmlFiles=ui-tests.xml
```

## Running API Tests

To run the API tests, use:

```sh
mvn test -Dsurefire.suiteXmlFiles=api-tests.xml
```

## Test Flow

### UI Tests:

1. Open Amazon Egypt and log in.
2. Navigate to Video Games from the "All" menu.
3. Apply filters (Free Shipping & New).
4. Sort items by price (High to Low).
5. Add items under 15,000 EGP to the cart.
6. Verify total price.

### API Tests:

- **Login API Test:** Validates login functionality and retrieves an authentication token.
- **WhoAmI API Test:** Ensures correct user details are returned for authenticated users.
- **Invalid Login Test:** Confirms incorrect credentials return a proper error response.

## Notes

- This test uses **WebDriverManager** to automatically manage ChromeDriver.
- All local dependencies are bundled within the project to ensure compatibility across machines.
