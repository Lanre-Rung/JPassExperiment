package lan.pass.demo.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {
    private Long id;

    @NonNull
    private String name;
}