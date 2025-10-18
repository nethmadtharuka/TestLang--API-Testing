package com.testlang;

import java.io.FileReader;
import java.io.IOException;
// Import the ProgramNode class you created
import com.testlang.ProgramNode;

public class TestLangProcessor {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TestLangProcessor <input-file>");
            System.exit(1);
        }

        String inputFile = args[0];

        try {
            FileReader reader = new FileReader(inputFile);
            TestLangScanner scanner = new TestLangScanner(reader);
            // NOTE: By Java convention, class names should be uppercase (Parser)
            // But we will use the lowercase name 'parser' that CUP generated
            parser parser = new parser(scanner);

            System.out.println("Parsing file: " + inputFile);
            System.out.println("Starting parse...");

            // --- KEY CHANGE HERE ---
            // 1. Call parser.parse() and get its 'value' field.
            // 2. Cast the returned Object into your ProgramNode class.
            ProgramNode astRoot = (ProgramNode) parser.parse().value;

            // --- UPDATED SUCCESS MESSAGE ---
            // Check if the AST root was successfully created
            if (astRoot != null) {
                System.out.println("Parsing successful!");
                // 3. Print how many statements it found.
                System.out.println("Found " + astRoot.getStatements().size() + " statements.");
            } else {
                System.out.println("Parsing failed to produce an AST.");
            }

            reader.close();

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Parse error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}