package com.testlang;

public class RequestNode implements TestCommandNode {
    String method; // "POST", "GET", etc.
    String url;
    String body; // Can be null if there's no body

    public RequestNode(String method, String url, String body) {
        this.method = method;
        this.url = url.replace("\"", ""); // Clean quotes

        if (body != null) {
            // Clean quotes and escaped characters from the body
            this.body = body.replace("\"", "")
                    .replace("\\\"", "\"");
        } else {
            this.body = null;
        }
    }

    // Getters so the CodeGenerator can read this data
    public String getMethod() { return method; }
    public String getUrl() { return url; }
    public String getBody() { return body; }
    public boolean hasBody() { return body != null; }
}