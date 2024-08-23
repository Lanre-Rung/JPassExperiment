package lan.pass.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Image {
    private Long id;
    private String name;
    private Integer isActive;
    private Long assetId;
    private Integer type;
    private Asset asset;
    private String content;
}
