package lan.pass.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.brendamour.jpasskit.PKBeacon;
import de.brendamour.jpasskit.PKBeaconBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beacon {
    private Long id;

    private Short isActive;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long changeMessageId;

    private String proximityUUID;

    private Integer major;

    private Integer minor;
}