package lan.pass.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Asset {

    private Long id;

    private String name;

    private Boolean isActive;

    private Long ownerId;

    private int type;

    private Merchant owner;

    private Image image;

    private Pass pass;

    private ChangeMessage changeMessageItem;
}
