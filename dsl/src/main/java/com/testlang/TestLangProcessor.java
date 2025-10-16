package com.testlang;

import java.io.FileReader;
import java.io.IOException;

public class TestLangProcessor {
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TestLangProcessor <input-file>");
            System.exit(1);
        }
        
        String inputFile = args[0];
        
        try {
            // Create a FileReader for the input file
            FileReader reader = new FileReader(inputFile);
            
            // Create the scanner with the file reader
            TestLangScanner scanner = new TestLangScanner(reader);
            
            // Create the parser with the scanner
            parser parser = new parser(scanner);
            
            System.out.println("Parsing file: " + inputFile);
            System.out.println("Starting parse...");
            
            // Parse the input
            parser.parse();
            
            System.out.println("Parse completed successfully!");
            
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