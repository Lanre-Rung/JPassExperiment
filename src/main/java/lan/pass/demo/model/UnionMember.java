package lan.pass.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnionMember {
    private Long unionId;

    private Long merchantId;

    private Short isAdministrator;
}