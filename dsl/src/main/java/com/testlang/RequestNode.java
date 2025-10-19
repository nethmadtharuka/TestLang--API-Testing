package com.testlang;

public class RequestNode implements TestCommandNode {
    String method; // "POST", "GET", etc.
    String url;
    String body; // Can be null if there's no body

    public RequestNode(String method, String url, String body) {
        this.method = method;
        // Don't clean the URL - keep it as-is
        this.url = url;

        // Don't clean the body - keep it exactly as received from the parser
        this.body = body;
    }

    // Getters so the CodeGenerator can read this data
    public String getMethod() { return method; }
    public String getUrl() { return url; }
    public String getBody() { return body; }
    public boolean hasBody() { return body != null; }
}