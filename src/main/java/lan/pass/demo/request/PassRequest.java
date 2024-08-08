package lan.pass.demo.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.brendamour.jpasskit.*;
import de.brendamour.jpasskit.enums.PKPassPersonalizationField;
import de.brendamour.jpasskit.enums.PKPassType;
import de.brendamour.jpasskit.enums.PKTransitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassRequest {
    private Long id;
    private String name;
    private Long ownerId;
    private Integer formatVersion;
    private String passTypeIdentifier;
    private PKTransitType transitType;
    private PKPassType passType;
    private List<PKField> headerFields;
    private List<PKField> primaryFields;
    private List<PKField> secondaryFields;
    private List<PKField> auxiliaryFields;
    private List<PKField> backFields;
    private String serialNumber;
    private String teamIdentifier;
    private String organizationName;
    private String logoText;
    private String description;
    private String backgroundColor;
    private String foregroundColor;
    private String labelColor;
    private PKNFC nfc;
    private URL webServiceURL;
    private String authenticationToken;
    private List<PKBeacon> beacons;
    private List<PKLocation> locations;
    private List<PKBarcode> barcodes;
    private String appLaunchURL;
    private List<Long> associatedStoreIdentifiers;
    private List<PWAssociatedApp> associatedApps;
    private Map<String, Object> userInfo;
    private Long maxDistance;
    private String relevantDate;
    private String expirationDate;
    private Boolean voided;
    private Boolean sharingProhibited;
    private PKSemantics semantics;
    private String logo;
    private String icon;
    private String strip;
    private Personalization personalization;

}