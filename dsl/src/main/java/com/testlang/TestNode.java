package com.testlang;

import java.util.ArrayList;
import java.util.List;

public class TestNode implements StatementNode {
    String testName;
    // THIS IS THE UPGRADE: A list to hold commands
    List<TestCommandNode> commands;

    public TestNode(String testName) {
        this.testName = testName;
        this.commands = new ArrayList<>(); // Initialize the list
    }

    // A method to add a command (a request or expect)
    public void addCommand(TestCommandNode command) {
        this.commands.add(command);
    }

    // Getters for the CodeGenerator
    public String getTestName() { return testName; }
    public List<TestCommandNode> getCommands() { return commands; }
}