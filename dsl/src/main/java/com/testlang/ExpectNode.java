package com.testlang;

public class ExpectNode implements TestCommandNode {
    String type; // "status", "header", "body"
    String value1;
    String value2; // "contains" or the expected value

    // Constructor for 'expect status = 200'
    public ExpectNode(String type, String value1) {
        this.type = type;
        this.value1 = value1.replace("\"", "");
        this.value2 = null;
    }

    // Constructor for 'expect header "Content-Type" contains "json"'
    public ExpectNode(String type, String value1, String value2) {
        this.type = type;
        this.value1 = value1.replace("\"", "");
        this.value2 = value2.replace("\"", "");
    }

    // Getters for the CodeGenerator
    public String getType() { return type; }
    public String getValue1() { return value1; }
    public String getValue2() { return value2; }
}