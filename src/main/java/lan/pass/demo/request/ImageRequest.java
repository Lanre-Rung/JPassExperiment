package lan.pass.demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest {
    Long id;
    String content;
    Long ownerId;
    Integer type;
    String name;
}
