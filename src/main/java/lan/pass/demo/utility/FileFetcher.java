package lan.pass.demo.utility;

import cn.hutool.core.codec.Base64;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class FileFetcher {

    public void createFile(JsonNode node, String currentPath, String extension) throws IOException {
        // Determine the full path for the current node
        String fullPath = currentPath + "." + extension;

        if (node.isObject()) {
             if (extension != null && extension.equals("json")) {
                 // If it's a JSON file, just write the original content as a string
                 ObjectMapper objectMapper = new ObjectMapper();
                 String jsonText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
                 File jsonFile = new File(fullPath);
                 Files.write(jsonFile.toPath(), jsonText.getBytes(StandardCharsets.UTF_8));
            } else {
                 // If the node is an object, it represents a directory
                 File directory = new File(fullPath);
                 if (!directory.exists()) {
                     directory.mkdirs(); // Create directory
                 }
                 // Recursively process each child node
                 Iterator<String> fields = node.fieldNames();
                 while (fields.hasNext()) {
                     String fieldName = fields.next();
                     createFile(node.get(fieldName), fullPath, fieldName.contains(".") ? fieldName.substring(fieldName.lastIndexOf(".")) : null);
                 }
             }
        } else if (node.isTextual()) {
            // If the node is textual, it represents a file with base64 encoded content
            // Decode the base64 content
            byte[] fileContent = Base64.decode(node.asText());
            // If there's an extension, it's a file; otherwise, it's a directory name
            if (extension != null && !extension.isEmpty()) {
                File file = new File(fullPath + extension);
                Files.write(file.toPath(), fileContent);
            }
        }
    }

    public static JsonNode readFile(String path) {
        File file = new File(path);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        String fileName = file.getName();

        if (file.isDirectory()) {
            // If it's a directory, read all files and subdirectories
            File[] files = file.listFiles((dir, name) -> !name.equals(".svn") && !name.equals(".git")); // Ignore version control folders
            if (files != null) {
                for (File child : files) {
                    String childPath = child.getAbsolutePath();
                    JsonNode childNode = readFile(childPath); // Recursive call
                    rootNode.set(child.getName(), childNode);
                }
            }
        } else {
            // If it's a file, read its content
            try {
                String fileContent = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);

                // Check if the file is a JSON file by its extension
                if (fileName.endsWith(".json")) {
                    // Parse the file content as JSON
                    JsonNode jsonNode = objectMapper.readTree(fileContent);
                    rootNode.set(fileName, jsonNode);
                } else {
                    // For non-JSON files, encode the content in base64
                    String base64Content = Base64.encode(fileContent.getBytes(StandardCharsets.UTF_8));
                    rootNode.put(fileName, base64Content);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the error according to your needs, possibly re-throwing the exception
            }
        }

        return rootNode;
    }
}
