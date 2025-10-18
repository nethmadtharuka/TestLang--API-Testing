package com.testlang;

public class TestNode implements StatementNode {
    String testName;
    // Later, we will add a list of requests and assertions here

    public TestNode(String testName) {
        this.testName = testName;
    }
}