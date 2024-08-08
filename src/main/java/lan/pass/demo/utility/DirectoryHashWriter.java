package lan.pass.demo.utility;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 此类用于计算指定目录下所有文件的SHA-1哈希值，并将结果以JSON格式写入到文件中。
 * 它排除了名为"manifest.json"和"signature"的文件。
 */
public class DirectoryHashWriter {

    /**
     * 排除文件集合，存储不需要计算哈希值的文件名。
     */
    private static final Set<String> EXCLUDE_FILES = new HashSet<>(Arrays.asList(
            "manifest.json", "signature"
    ));

    /**
     * 计算指定目录下所有文件的SHA-1哈希值，并将结果以JSON格式写入到指定的文件中。
     *
     * @param directoryPath 需要计算哈希值的目录路径。
     * @param outputFilePath 输出JSON文件的路径。
     */
    public static void calculateHashesForDirectoryAndWriteToFile(String directoryPath, String outputFilePath) {
        File directory = new File(directoryPath);
        StringBuilder hashData = new StringBuilder("{\n");

        // 确保提供的路径是一个目录
        if (!directory.isDirectory()) {
            System.out.println("提供的路径不是一个目录。");
            return;
        }

        // 遍历目录
        File[] files = directory.listFiles((dir, name) -> !EXCLUDE_FILES.contains(name) && !name.endsWith("DS_Store"));
        if (files == null) {
            return;
        }

        boolean isFirst = true;
        for (File file : files) {
            if (file.isFile()) {
                try {
                    String sha1Hash = calculateSHA1(file);
                    if (!isFirst) {
                        hashData.append(",\n");
                    } else {
                        isFirst = false;
                    }
                    hashData.append("  \"").append(file.getName()).append("\": \"").append(sha1Hash).append("\"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        hashData.append("\n}");

        // 将结果写入到文件中
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(hashData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算单个文件的SHA-1哈希值。
     *
     * @param file 需要计算哈希值的文件。
     * @return 文件的SHA-1哈希值。
     * @throws IOException 如果文件读取过程中发生I/O错误。
     */
    private static String calculateSHA1(File file) throws IOException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        try (InputStream is = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = is.read(buffer)) != -1) {
                md.update(buffer, 0, numRead);
            }
            byte[] digest = md.digest();
            return bytesToHex(digest);
        }
    }

    /**
     * 将字节数组转换为十六进制字符串。
     *
     * @param bytes 需要转换的字节数组。
     * @return 转换后的十六进制字符串。
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}