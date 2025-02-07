package com.amazon.test.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class ApiTests {

    private String token;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://pay2.foodics.dev";

        // Set login credentials
        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("email", "merchant@foodics.com");
        loginPayload.put("password", "123456");
        loginPayload.put("token", "Lyz22cfYKMetFhKQybx5HAmVimF1i0xO");

        // Login Request
        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .when()
                .post("/cp_internal/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        token = response.jsonPath().getString("token"); // Extract token
        Assert.assertNotNull(token, "Token should not be null");
        System.out.println("Login successful, Token: " + token);
    }

    @Test
    public void testWhoAmI() {
        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/cp_internal/whoami")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String email = response.jsonPath().getString("email");
        Assert.assertEquals(email, "merchant@foodics.com", "Email mismatch in whoami response");

        System.out.println("Verified WhoAmI API successfully.");
    }

    @Test
    public void testInvalidLogin() {
        Map<String, String> invalidPayload = new HashMap<>();
        invalidPayload.put("email", "wrong@foodics.com");
        invalidPayload.put("password", "wrongpass");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(invalidPayload)
                .when()
                .post("/cp_internal/login")
                .then()
                .statusCode(401) // Unauthorized
                .extract()
                .response();

        String errorMessage = response.jsonPath().getString("message");
        Assert.assertTrue(errorMessage.contains("invalid"), "Error message mismatch");

        System.out.println("Invalid login test passed.");
    }
}
