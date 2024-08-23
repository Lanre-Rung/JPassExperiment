package lan.pass.demo.service;

import lan.pass.demo.mapper.AssetMapper;
import lan.pass.demo.model.Asset;
import lan.pass.demo.model.AssetType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AssetService {
    @Resource
    private AssetMapper assetMapper;

    public Asset createAsset(String name, Long ownerId, AssetType assetType){
        Asset asset = new Asset();
        asset.setName(name);
        asset.setOwnerId(ownerId);
        asset.setType(AssetType.getCode(assetType));
        assetMapper.insertAsset(asset);
        return asset;
    }
}
