package lan.pass.demo.mapper;

import lan.pass.demo.model.Asset;
import lan.pass.demo.model.UnionAsset;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UnionAssetMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UnionAsset record);

    int insertSelective(UnionAsset record);

    UnionAsset selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UnionAsset record);

    int updateByPrimaryKey(UnionAsset record);

    List<Asset> selectAssetByUnionIds(List<Long> unionIds, List<Integer> types);
}