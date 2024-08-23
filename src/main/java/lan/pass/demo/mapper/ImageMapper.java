package lan.pass.demo.mapper;

import lan.pass.demo.model.Image;
import lan.pass.demo.model.Pass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ImageMapper {
    int insertImage(Image image);
    Image selectImageById(Long id);
    int updateImage(Image image);
    int deleteImageById(Long id);
    List<Image> getImagesByOwnerId(Long id, Long offset, Long pageCount);
}