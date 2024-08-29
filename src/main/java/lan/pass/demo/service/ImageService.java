package lan.pass.demo.service;

import lan.pass.demo.mapper.ImageMapper;
import lan.pass.demo.model.Asset;
import lan.pass.demo.model.AssetType;
import lan.pass.demo.model.Image;
import lan.pass.demo.request.ImageRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.jni.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageService {

    ImageMapper imageMapper;
    @Autowired
    public void setImageMapper(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    private AssetService assetService;
    @Autowired
    public void setAssetService(AssetService assetService) {
        this.assetService = assetService;
    }

    public int insertImage(ImageRequest imageRequest) {
        Asset asset = assetService.createAsset(imageRequest.getName(), imageRequest.getOwnerId(), AssetType.Image);
        Image image = new Image();
        image.setName(imageRequest.getName());
        image.setType(imageRequest.getType());
        image.setAssetId(asset.getId());
        int affectedRows = imageMapper.insertImage(image);
        setImageFile(image.getId(), imageRequest.getContent());
        return affectedRows;
    }

    public int updateImage(ImageRequest imageRequest) {
        Image image = new Image();
        image.setId(imageRequest.getId());
        image.setName(imageRequest.getName());
        image.setType(imageRequest.getType());
        int affectedRows = imageMapper.updateImage(image);
        setImageFile(image.getId(), imageRequest.getContent());
        return affectedRows;
    }

    public Image packImage(Image image){
        String content = getImageFile(image.getId());
        image.setContent(content);
        return image;
    }

    public Image getImageById(Long id) {
        Image image = imageMapper.selectImageById(id);
        return packImage(image);
    }

    public List<Image> getImagesByOwnerId(Long id, Long pageIndex, Long pageCount){
        Long offset = pageIndex > 0 && pageCount > 0? (pageIndex - 1) * pageCount : -1;
        List<Image> images = imageMapper.getImagesByOwnerId(id, offset, pageCount);
        for (Image image : images){
            packImage(image);
        }
        return images;
    }

    public int deleteImageById(Long id) {
        return imageMapper.deleteImageById(id);
    }
    /**
     * 设置通行证的模板图片。
     *
     * @param passId 通行证的唯一标识符。
     * @param base64 以Base64编码的图片文件内容。
     */
    public static void setImageFile(long passId, String base64, String type) {
        // Logo文件的存储路径
        Path path = Paths.get("pass", String.valueOf(passId), type + ".png");
        // 将Base64编码的Logo内容解码并写入文件
        try {
            Files.write(path, Base64.decodeBase64(base64.substring(base64.indexOf(',') + 1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 设置图片模板的位置
     *
     * @param imageId 图片标识符
     * @param base64 以Base64编码的图片文件内容。
     */
    public static void setImageFile(long imageId, String base64) {
        // 图片文件的存储路径
        Path path = Paths.get("image", imageId + ".png");
        // 获取文件的父路径并确保其存在
        Path dir = path.getParent();
        if (dir != null && !Files.exists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the error, possibly by throwing a custom exception or returning a status
                return;
            }
        }
        try {
            //在写入之前把原来的文件删掉
            if (Files.exists(path)) {
                Files.delete(path);
            }
            // 将内容写入文件
            Files.write(path, Base64.decodeBase64(base64.substring(base64.indexOf(',') + 1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Retrieves an image by its identifier and returns its Base64 encoded content.
     *
     * @param imageId 图片标识符
     * @return Base64编码的图片文件内容，如果找不到文件则返回null。
     */
    public static String getImageFile(long imageId) {
        // 图片文件的存储路径
        Path path = Paths.get("image", imageId + ".png");
        // 检查文件是否存在
        if (!Files.exists(path)) {
            // 文件不存在，返回null
            return null;
        }
        return "data:image/png;base64," + cn.hutool.core.codec.Base64.encode(new File(String.valueOf(path)));
    }
}