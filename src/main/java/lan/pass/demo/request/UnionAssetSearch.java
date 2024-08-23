package lan.pass.demo.request;

import lan.pass.demo.model.AssetType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UnionAssetSearch {
    List<Long> unionIds;
    List<AssetType> types;
    Long merchantId;
};