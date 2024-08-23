package lan.pass.demo.service;

import lan.pass.demo.model.*;
import lan.pass.demo.request.UnionAssetSearch;
import lombok.var;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import lan.pass.demo.mapper.UnionAssetMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnionAssetService{

    @Resource
    private UnionAssetMapper unionAssetMapper;
    @Resource
    private UnionMemberService unionMemberService;
    @Resource
    private ChangeMessageService changeMessageService;
    @Resource
    private ImageService imageService;
    
    public int deleteByPrimaryKey(Long id) {
        return unionAssetMapper.deleteByPrimaryKey(id);
    }

    
    public int insert(UnionAsset record) {
        return unionAssetMapper.insert(record);
    }

    
    public int insertSelective(UnionAsset record) {
        return unionAssetMapper.insertSelective(record);
    }

    
    public UnionAsset selectByPrimaryKey(Long id) {
        return unionAssetMapper.selectByPrimaryKey(id);
    }

    
    public int updateByPrimaryKeySelective(UnionAsset record) {
        return unionAssetMapper.updateByPrimaryKeySelective(record);
    }
    
    public int updateByPrimaryKey(UnionAsset record) {
        return unionAssetMapper.updateByPrimaryKey(record);
    }
    /**
     * Selects an asset based on the unionId.
     *
     * @param search unionAssetSearch params
     * @return the UnionAsset object if found, otherwise null
     */
    public List<Asset> selectAssetByUnionId(UnionAssetSearch search) {
        List<Long> unionIds = search.getUnionIds();
        if (search.getMerchantId() != null){
            unionIds = unionMemberService.selectUnionsByMemberId(search.getMerchantId(), true).stream()  // Convert the list to a Stream
                    .map(Union::getId)  // Extract the ID from each Union object
                    .collect(Collectors.toList());
        }
        List<AssetType> assetTypes = search.getTypes();
        ArrayList<Integer> types = new ArrayList<>();
        for (var assetType : assetTypes){
            if (assetType.equals(AssetType.ChangeMessage)){
                types.add(AssetType.getCode(AssetType.Location));
                types.add(AssetType.getCode(AssetType.Field));
                types.add(AssetType.getCode(AssetType.Beacon));
                types.add(AssetType.getCode(AssetType.RelevantDate));
            } else {
                types.add(AssetType.getCode(assetType));
            }
        }
        List<Asset> assets = unionAssetMapper.selectAssetByUnionIds(unionIds, types);
        List<Long> ids = new ArrayList<>();
        for (var asset : assets){
            if (asset.getChangeMessageItem() != null){
                var cmi = asset.getChangeMessageItem();
                ids.add(cmi.getChangeMessageId());
                //remove current asset
            } else if (asset.getImage() != null){
                imageService.packImage(asset.getImage());
            }
        }
        if (!ids.isEmpty()){
            List<ChangeMessage> changeMessages = changeMessageService.selectByPrimaryKeys(ids);
            HashMap<Long, ChangeMessage> temp = new HashMap<>();
            for (var changeMessage : changeMessages){
                temp.put(changeMessage.getChangeMessageId(), changeMessage);
            }
            for (var asset : assets){
                if (asset.getChangeMessageItem() != null && temp.containsKey(asset.getChangeMessageItem().getChangeMessageId())){
                    asset.setChangeMessageItem(temp.get(asset.getChangeMessageItem().getChangeMessageId()));
                }
            }
        }
        return assets;
    }
}
