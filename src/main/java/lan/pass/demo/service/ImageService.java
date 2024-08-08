package lan.pass.demo.service;

import de.brendamour.jpasskit.PKPass;
import de.brendamour.jpasskit.PKPassBuilder;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 提供设置通行证（Pass）Logo、Strip的服务。
 */
@Service
public class ImageService {

    /**
     * 设置通行证的Logo。
     *
     * @param passId 通行证的唯一标识符。
     * @param base64 以Base64编码的Logo文件内容。
     */
    public static void setLogo(long passId, String base64) {
        // Logo文件的存储路径
        Path path = Paths.get("pass", String.valueOf(passId), "logo.png");
        // 将Base64编码的Logo内容解码并写入文件
        try {
            Files.write(path, Base64.decodeBase64(base64.substring(base64.indexOf(',') + 1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置通行证的条带（Strip）。
     * 条带通常用于显示在通行证的顶部或底部的装饰性或信息性条带。
     *
     * @param passId 通行证的唯一标识符。
     * @param base64 以Base64编码的条带文件内容。
     */
    public static void setStrip(long passId, String base64) {
        // 条带文件的存储路径
        Path path = Paths.get("pass", String.valueOf(passId), "strip.png");
        // 将Base64编码的条带内容解码并写入文件
        try {
            Files.write(path, Base64.decodeBase64(base64.substring(base64.indexOf(',') + 1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置通行证的图标。
     *
     * @param passId 通行证的唯一标识符。
     * @param base64 以Base64编码的图标文件内容。
     */
    public static void setIcon(long passId, String base64) {
        // 图标文件的存储路径
        Path path = Paths.get("pass", String.valueOf(passId), "icon.png");
        // 将Base64编码的图标内容解码并写入文件
        try {
            Files.write(path, Base64.decodeBase64(base64.substring(base64.indexOf(',') + 1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置通行证的背景图片。
     *
     * @param passId 通行证的唯一标识符。
     * @param base64 以Base64编码的背景图片文件内容。
     */
    public static void setBackground(long passId, String base64) {
        // 背景图片文件的存储路径
        Path path = Paths.get("pass", String.valueOf(passId), "background.png");
        // 将Base64编码的背景图片内容解码并写入文件
        try {
            Files.write(path, Base64.decodeBase64(base64.substring(base64.indexOf(',') + 1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置通行证的缩略图。
     *
     * @param passId 通行证的唯一标识符。
     * @param base64 以Base64编码的缩略图文件内容。
     */
    public static void setThumbnail(long passId, String base64) {
        // 缩略图文件的存储路径
        Path path = Paths.get("pass", String.valueOf(passId), "thumbnail.png");
        // 将Base64编码的缩略图内容解码并写入文件
        try {
            Files.write(path, Base64.decodeBase64(base64.substring(base64.indexOf(',') + 1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}