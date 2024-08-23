package lan.pass.demo.mapper;

import lan.pass.demo.model.Field;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FieldMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Field record);

    int insertSelective(Field record);

    Field selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Field record);

    int updateByPrimaryKey(Field record);
}