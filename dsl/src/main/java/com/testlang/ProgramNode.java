package com.testlang;

import java.util.ArrayList;
import java.util.List;

public class ProgramNode {
    // A list to hold all our statements
    private List<StatementNode> statements;

    public ProgramNode() {
        this.statements = new ArrayList<>();
    }

    // A method to add a statement to the list
    public void addStatement(StatementNode statement) {
        this.statements.add(statement);
    }

    // This lets us "get" the list later
    public List<StatementNode> getStatements() {
        return statements;
    }
}