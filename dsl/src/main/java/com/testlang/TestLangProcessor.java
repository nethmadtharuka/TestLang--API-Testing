package com.testlang;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TestLangProcessor {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TestLangProcessor <input-file>");
            System.exit(1);
        }

        String inputFile = args[0];

        try {
            // DEBUG: Print what lexer reads
            System.out.println("\n=== DEBUG: Token values ===");
            FileReader debugReader = new FileReader(inputFile);
            TestLangScanner debugScanner = new TestLangScanner(debugReader);
            int count = 0;
            while (count < 25) {
                java_cup.runtime.Symbol sym = debugScanner.next_token();
                if (sym.sym == 0) break;
                if (sym.value != null) {
                    System.out.println("Token: " + sym.sym + " | Value: [" + sym.value + "]");
                }
                count++;
            }
            debugReader.close();
            System.out.println("=== End debug ===\n");

            // --- 1. PARSE THE FILE ---
            FileReader reader = new FileReader(inputFile);
            TestLangScanner scanner = new TestLangScanner(reader);
            parser parser = new parser(scanner);
            System.out.println("Parsing file: " + inputFile);
            ProgramNode astRoot = (ProgramNode) parser.parse().value;
            reader.close();

            if (astRoot == null) {
                System.err.println("Parsing failed to produce an AST.");
                System.exit(1);
            }
            System.out.println("Parsing successful! Found " + astRoot.getStatements().size() + " statements.");

            // --- 2. GENERATE THE CODE ---
            CodeGenerator generator = new CodeGenerator();
            String javaCode = generator.generate(astRoot);
            System.out.println("Code generation complete.");

            // --- 3. SAVE THE FILE ---
            String outputFilePath = "src/test/java/com/testlang/GeneratedTests.java";
            try (FileWriter writer = new FileWriter(outputFilePath)) {
                writer.write(javaCode);
            }
            System.out.println("SUCCESS: GeneratedTests.java was created!");
            System.out.println("You can now run 'mvn clean test' to execute it.");

        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}