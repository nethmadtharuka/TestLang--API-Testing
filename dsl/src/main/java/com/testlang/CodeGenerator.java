package com.testlang;

import java.util.HashMap;
import java.util.Map;

public class CodeGenerator {

    private Map<String, String> variables = new HashMap<>();
    private String baseUrl = "http://localhost:8080"; // Default value

    public String generate(ProgramNode program) {
        StringBuilder sb = new StringBuilder();

        // --- 1. FIRST PASS: Find Config and Variables ---
        // This loop finds the config first, so the baseUrl is set correctly.
        for (StatementNode statement : program.getStatements()) {
            if (statement instanceof ConfigNode) {
                ConfigNode config = (ConfigNode) statement;
                this.baseUrl = config.getBaseUrl();
            } else if (statement instanceof LetNode) {
                LetNode letNode = (LetNode) statement;
                variables.put(letNode.variableName, letNode.value);
            }
        }

        // --- 2. BUILD THE FILE ---

        // Add package and imports
        sb.append("package com.testlang;\n\n");
        sb.append("import org.junit.jupiter.api.*;\n");
        sb.append("import static org.junit.jupiter.api.Assertions.*;\n");
        sb.append("import java.net.http.*;\n");
        sb.append("import java.net.URI;\n");
        sb.append("import java.time.Duration;\n");
        sb.append("import java.util.Map;\n\n");

        // Create the main test class
        sb.append("public class GeneratedTests {\n\n");

        // Use the baseUrl variable we stored
        sb.append("    static String BASE_URL = \"").append(this.baseUrl).append("\";\n");
        sb.append("    static HttpClient client;\n\n");

        // Add the @BeforeAll setup method
        sb.append("    @BeforeAll\n");
        sb.append("    static void setup() {\n");
        sb.append("        client = HttpClient.newBuilder()\n");
        sb.append("            .connectTimeout(Duration.ofSeconds(5))\n");
        sb.append("            .build();\n");
        sb.append("    }\n\n");

        // --- 3. SECOND PASS: Generate Tests ---
        // This loop now only generates the @Test methods.
        for (StatementNode statement : program.getStatements()) {
            if (statement instanceof TestNode) {
                TestNode testNode = (TestNode) statement;
                sb.append("    @Test\n");
                sb.append("    void test_").append(testNode.testName.replaceAll("\\s+", "_")).append("() throws Exception {\n");
                sb.append("        System.out.println(\"Running test: ").append(testNode.testName).append("\");\n");
                sb.append("        assertTrue(true);\n");
                sb.append("    }\n\n");
            }
        }

        // Close the class
        sb.append("}\n");

        return sb.toString();
    }
}