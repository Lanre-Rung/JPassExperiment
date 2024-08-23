package lan.pass.demo.utility;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 此类提供了将目录压缩成ZIP文件的功能。
 */
public class Compressor {
    public static byte[] readInputStreamToByteArray(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int nRead;
        while ((nRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, nRead);
        }
        return outputStream.toByteArray();
    }
    /**
     * 将指定目录及其所有子目录和文件压缩成一个ZIP文件。
     *
     * @param directoryPath 需要压缩的目录的路径。
     * @param outputZipFilePath 输出ZIP文件的路径。
     */
    public static void zipDirectory(String directoryPath, String outputZipFilePath) {
        // 创建用于写入ZIP文件的FileOutputStream
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outputZipFilePath))) {
            // 创建目录的File对象
            File directoryToZip = new File(directoryPath);
            // 确保目录路径以斜杠结束
            String baseDir = directoryToZip.getPath().endsWith(File.separator) ?
                    directoryToZip.getPath() : directoryToZip.getPath() + File.separator;
            // 遍历目录
            Files.walk(directoryToZip.toPath())
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        try {
                            // 为每个文件创建ZIP条目
                            String entryPath = path.toString().substring(baseDir.length()).replace("\\", "/");
                            ZipEntry zipEntry = new ZipEntry(entryPath);
                            zipOut.putNextEntry(zipEntry);
                            // 将文件内容复制到ZIP条目
                            try (InputStream is = Files.newInputStream(path)) {
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = is.read(buffer)) > 0) {
                                    zipOut.write(buffer, 0, len);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Extracts a ZIP file to a specified directory.
     *
     * @param zipFilePath The path to the ZIP file.
     * @param targetDirectoryPath The path to the target directory where the ZIP will be extracted.
     */
    public static void unzipFile(String zipFilePath, String targetDirectoryPath) {
        File destDir = new File(targetDirectoryPath);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destDir.getAbsolutePath() + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }
}