package lan.pass.demo.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.brendamour.jpasskit.enums.PKPassPersonalizationField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize
public class Personalization {
    private List<PKPassPersonalizationField> requiredPersonalizationFields;
    private String description;
    private String termsAndConditions;
}
