package lan.pass.demo.mapper;

import lan.pass.demo.model.Union;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UnionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Union record);

    int insertSelective(Union record);

    Union selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Union record);

    int updateByPrimaryKey(Union record);

    List<Union> selectByOwnerId(Long ownerId);
}