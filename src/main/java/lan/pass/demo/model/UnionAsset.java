package lan.pass.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnionAsset {
    private Long id;
    private Long unionId;
    private Long assetId;
}