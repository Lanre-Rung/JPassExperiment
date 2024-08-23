package lan.pass.demo.request;

import lan.pass.demo.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeMessageRequest {
    Long id;
    Location location;
    Beacon beacon;
    Field field;
    RelevantDate relevantDate;
    String name;
    String content;
    Long ownerId;
}
