package com.testlang;

// We implement StatementNode to mark this as a valid top-level statement
public class ConfigNode implements StatementNode {

    // This field will hold our base URL
    String baseUrl;

    public ConfigNode(String url) {
        // We remove the quotes (") from the string token
        // so we store the clean URL.
        this.baseUrl = url.replace("\"", "");
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}