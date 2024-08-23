package lan.pass.demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LogInRequest {
    String emailOrPhone;
    String password;
}
