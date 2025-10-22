package com.testlang;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.http.*;
import java.net.URI;
import java.time.Duration;

public class GeneratedTests {

    static String BASE_URL;
    static HttpClient client;

    @BeforeAll
    static void setup() {
        BASE_URL = "http://localhost:8080";
        client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    }

    @Test
    void test_Login() throws Exception {
        // POST request
        HttpRequest.Builder requestBuilderresponse1 = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{ \"username\": \"admin\", \"password\": \"1234\" }"));
        HttpRequest requestresponse1 = requestBuilderresponse1.build();
        HttpResponse<String> response1 = client.send(requestresponse1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode(), "Expected status code 200");
        assertTrue(response1.headers().firstValue("Content-Type").orElse("").contains("json"), "Expected header 'Content-Type' to contain 'json'");
        assertTrue(response1.body().contains("\"token\":"), "Expected body to contain '\"token\":'");
    }

    @Test
    void test_GetUser() throws Exception {
        // GET request
        HttpRequest.Builder requestBuilderresponse1 = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/users/42"))
            .GET();
        HttpRequest requestresponse1 = requestBuilderresponse1.build();
        HttpResponse<String> response1 = client.send(requestresponse1, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode(), "Expected status code 200");
        assertTrue(response1.headers().firstValue("Content-Type").orElse("").contains("json"), "Expected header 'Content-Type' to contain 'json'");
        assertTrue(response1.body().contains("\"username\":\"admin\""), "Expected body to contain '\"username\":\"admin\"'");
        assertTrue(response1.body().contains("\"id\":42"), "Expected body to contain '\"id\":42'");
    }

}
