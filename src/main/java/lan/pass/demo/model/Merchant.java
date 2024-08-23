package lan.pass.demo.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {
    private Long id;

    @NonNull
    private String name;

    private String password;

    private String phone;

    private String email;

    // Method to create a new Merchant with only id and name
    public Merchant idAndNameOnly() {
        return new Merchant(this.id, this.name, null, null, null);
    }
}