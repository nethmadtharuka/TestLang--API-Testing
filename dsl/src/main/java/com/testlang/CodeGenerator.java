package com.testlang;

import java.util.HashMap;
import java.util.Map;

// This is the class that will "walk" the AST and build the Java code.
public class CodeGenerator {

    // A map to store variables from 'let' statements (e.g., user = "admin")
    private Map<String, String> variables = new HashMap<>();

    // The main method that does all the work.
    // It takes the ProgramNode (your AST) as input.
    public String generate(ProgramNode program) {

        // StringBuilder is an efficient way to build a long string piece by piece.
        StringBuilder sb = new StringBuilder();

        // --- 1. Add the "Boilerplate" Code ---
        // These are the imports and setup code that every generated file will need.

        // Add all the necessary Java imports for the test file
        sb.append("package com.testlang;\n\n"); // Put it in the same package
        sb.append("import org.junit.jupiter.api.*;\n");
        sb.append("import static org.junit.jupiter.api.Assertions.*;\n");
        sb.append("import java.net.http.*;\n");
        sb.append("import java.net.URI;\n");
        sb.append("import java.time.Duration;\n");
        sb.append("import java.util.Map;\n\n");

        // Create the main test class
        sb.append("public class GeneratedTests {\n\n");

        // Add static fields for the base URL and HTTP client
        sb.append("    static String BASE_URL = \"http://localhost:8080\";\n");
        sb.append("    static HttpClient client;\n\n");

        // Add the @BeforeAll setup method
        sb.append("    @BeforeAll\n");
        sb.append("    static void setup() {\n");
        sb.append("        client = HttpClient.newBuilder()\n");
        sb.append("            .connectTimeout(Duration.ofSeconds(5))\n");
        sb.append("            .build();\n");
        sb.append("    }\n\n");

        // --- 2. Walk the AST and Generate Dynamic Code ---
        // This loop checks each statement from your AST.

        for (StatementNode statement : program.getStatements()) {

            if (statement instanceof LetNode) {
                // If it's a 'let' statement, save the variable for later.
                LetNode letNode = (LetNode) statement;

                // We store the variable name (e.g., "user") and its value (e.g., "admin")
                variables.put(letNode.variableName, letNode.value);

            } else if (statement instanceof TestNode) {
                // If it's a 'test' block, generate a new @Test method
                TestNode testNode = (TestNode) statement;

                sb.append("    @Test\n");
                // Create a valid Java method name from the test name
                sb.append("    void test_").append(testNode.testName.replaceAll("\\s+", "_")).append("() throws Exception {\n");

                // --- THIS IS WHERE STEP 5 WILL GO ---
                // In the next step, we will add logic here to generate
                // the HTTP requests (POST, GET) and assertions (expect)
                // that go inside this test block.

                // For now, add a placeholder test to make it a valid Java file.
                sb.append("        System.out.println(\"Running test: ").append(testNode.testName).append("\");\n");
                sb.append("        assertTrue(true);\n");

                sb.append("    }\n\n");
            }

            // We will add 'else if (statement instanceof ConfigNode)' here later
        }

        // --- 3. Finish the File ---
        // Add the final closing brace for the class
        sb.append("}\n");

        // Return the complete string.
        return sb.toString();
    }
}