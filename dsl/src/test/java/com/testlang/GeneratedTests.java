package com.testlang;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.http.*;
import java.net.URI;
import java.time.Duration;
import java.util.Map;

public class GeneratedTests {

    static String BASE_URL = "http://localhost:8080";
    static HttpClient client;

    @BeforeAll
    static void setup() {
        client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    }

    @Test
    void test_Login() throws Exception {
        System.out.println("Running test: Login");
        assertTrue(true);
    }

}
