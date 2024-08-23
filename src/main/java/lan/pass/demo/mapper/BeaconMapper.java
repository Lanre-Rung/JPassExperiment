package lan.pass.demo.mapper;

import lan.pass.demo.model.Beacon;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BeaconMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Beacon record);

    int insertSelective(Beacon record);

    Beacon selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Beacon record);

    int updateByPrimaryKey(Beacon record);
}