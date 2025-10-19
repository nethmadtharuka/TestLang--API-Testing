package com.testlang;

import java.util.HashMap;
import java.util.Map;

public class CodeGenerator {

    // A map to store variables from 'let' statements (e.g., user = "admin")
    private Map<String, String> variables = new HashMap<>();

    // Store the base URL from config block
    private String baseUrl = "http://localhost:8080";

    public String generate(ProgramNode program) {
        StringBuilder sb = new StringBuilder();

        // --- 1. Add the "Boilerplate" Code ---
        sb.append("package com.testlang;\n\n");
        sb.append("import org.junit.jupiter.api.*;\n");
        sb.append("import static org.junit.jupiter.api.Assertions.*;\n");
        sb.append("import java.net.http.*;\n");
        sb.append("import java.net.URI;\n");
        sb.append("import java.time.Duration;\n\n");

        sb.append("public class GeneratedTests {\n\n");

        // We'll set BASE_URL dynamically if there's a config block
        sb.append("    static String BASE_URL;\n");
        sb.append("    static HttpClient client;\n\n");

        sb.append("    @BeforeAll\n");
        sb.append("    static void setup() {\n");

        // --- 2. First Pass: Process Config and Let Statements ---
        for (StatementNode statement : program.getStatements()) {
            if (statement instanceof ConfigNode) {
                ConfigNode configNode = (ConfigNode) statement;
                baseUrl = configNode.baseUrl;
            } else if (statement instanceof LetNode) {
                LetNode letNode = (LetNode) statement;
                variables.put(letNode.variableName, letNode.value);
            }
        }

        // Set the BASE_URL in the setup method
        sb.append("        BASE_URL = \"").append(baseUrl).append("\";\n");
        sb.append("        client = HttpClient.newBuilder()\n");
        sb.append("            .connectTimeout(Duration.ofSeconds(5))\n");
        sb.append("            .build();\n");
        sb.append("    }\n\n");

        // --- 3. Second Pass: Generate Test Methods ---
        for (StatementNode statement : program.getStatements()) {
            if (statement instanceof TestNode) {
                TestNode testNode = (TestNode) statement;
                generateTestMethod(testNode, sb);
            }
        }

        // --- 4. Finish the File ---
        sb.append("}\n");

        return sb.toString();
    }

    private void generateTestMethod(TestNode testNode, StringBuilder sb) {
        sb.append("    @Test\n");

        // Create a valid Java method name from the test name
        String methodName = testNode.getTestName().replaceAll("\\s+", "_");
        sb.append("    void test_").append(methodName).append("() throws Exception {\n");

        // Track the last response variable name
        String lastResponseVar = null;
        int requestCount = 0;

        // Generate code for each command in the test
        for (TestCommandNode command : testNode.getCommands()) {
            if (command instanceof RequestNode) {
                requestCount++;
                lastResponseVar = "response" + requestCount;
                generateRequest((RequestNode) command, lastResponseVar, sb);
                sb.append("\n");
            } else if (command instanceof ExpectNode) {
                generateAssertion((ExpectNode) command, lastResponseVar, sb);
            }
        }

        sb.append("    }\n\n");
    }

    private void generateRequest(RequestNode requestNode, String responseVar, StringBuilder sb) {
        String method = requestNode.getMethod();
        String url = requestNode.getUrl();
        String body = requestNode.getBody();

        // Replace variables in URL (e.g., $user -> actual value)
        url = replaceVariables(url);

        sb.append("        // ").append(method).append(" request\n");
        sb.append("        HttpRequest.Builder requestBuilder").append(responseVar).append(" = HttpRequest.newBuilder()\n");
        sb.append("            .uri(URI.create(BASE_URL + \"").append(url).append("\"))\n");

        // Add body if present
        if (body != null && !body.isEmpty()) {
            // Replace variables in body
            body = replaceVariables(body);



            // Escape backslashes and quotes for Java string literal
            body = body.replace("\\", "\\\\").replace("\"", "\\\"");


            sb.append("            .header(\"Content-Type\", \"application/json\")\n");
            sb.append("            .").append(method).append("(HttpRequest.BodyPublishers.ofString(\"");
            sb.append(body).append("\"));\n");
        } else {
            // No body (GET, DELETE)
            sb.append("            .").append(method).append("();\n");
        }

        sb.append("        HttpRequest request").append(responseVar).append(" = requestBuilder").append(responseVar).append(".build();\n");
        sb.append("        HttpResponse<String> ").append(responseVar).append(" = client.send(request");
        sb.append(responseVar).append(", HttpResponse.BodyHandlers.ofString());\n");
    }

    private void generateAssertion(ExpectNode expectNode, String responseVar, StringBuilder sb) {
        String type = expectNode.getType();
        String value1 = expectNode.getValue1();
        String value2 = expectNode.getValue2();

        if (responseVar == null) {
            sb.append("        // WARNING: No request before this assertion!\n");
            return;
        }

        switch (type) {
            case "status":
                sb.append("        assertEquals(").append(value1).append(", ");
                sb.append(responseVar).append(".statusCode(), \"Expected status code ").append(value1).append("\");\n");
                break;

            case "header":
                sb.append("        assertTrue(").append(responseVar).append(".headers().firstValue(\"");
                sb.append(value1).append("\").orElse(\"\").contains(\"").append(value2);
                sb.append("\"), \"Expected header '").append(value1).append("' to contain '").append(value2).append("'\");\n");
                break;

            case "body":
                // value1 is "contains", value2 is the content
                if (value2 != null) {
                    sb.append("        assertTrue(").append(responseVar).append(".body().contains(\"");
                    sb.append(escapeJavaString(value2)).append("\"), \"Expected body to contain '");
                    sb.append(escapeJavaString(value2)).append("'\");\n");
                }
                break;

            default:
                sb.append("        // Unknown assertion type: ").append(type).append("\n");
        }
    }

    // Replace $variable with actual values from the 'let' statements
    private String replaceVariables(String text) {
        if (text == null) return null;

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String varName = "$" + entry.getKey();
            String varValue = entry.getValue();
            text = text.replace(varName, varValue);
        }
        return text;
    }

    // Escape special characters for Java strings
    private String escapeJavaString(String str) {
        if (str == null) return "";
        // Escape backslashes first, then quotes
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}