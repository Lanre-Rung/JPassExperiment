package lan.pass.demo.mapper;

import lan.pass.demo.model.Asset;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AssetMapper {
    int insertAsset(Asset asset);
    Asset selectAssetById(Long id);
    int updateAsset(Asset asset);
    int deleteAssetById(Long id);
}
