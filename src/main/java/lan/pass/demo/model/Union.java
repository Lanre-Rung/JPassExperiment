package lan.pass.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Union {
    private Long id;

    private String name;

    private Long ownerId;

    private Short isActive;
}