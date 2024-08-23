package lan.pass.demo.mapper;

import lan.pass.demo.model.RelevantDate;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RelevantDateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RelevantDate record);

    int insertSelective(RelevantDate record);

    RelevantDate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RelevantDate record);

    int updateByPrimaryKey(RelevantDate record);
}