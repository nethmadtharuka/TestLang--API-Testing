package com.testlang;

public class LetNode implements StatementNode {
    String variableName;
    String value;

    public LetNode(String variableName, String value) {
        this.variableName = variableName;
        // We'll clean up the quotes from the string token
        this.value = value.replace("\"", "");
    }
}