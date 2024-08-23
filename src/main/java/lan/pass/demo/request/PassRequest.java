package lan.pass.demo.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.brendamour.jpasskit.*;
import de.brendamour.jpasskit.enums.PKPassType;
import de.brendamour.jpasskit.enums.PKTransitType;
import lan.pass.demo.model.ChangeMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassRequest {
    private Long id;
    private List<Long> ids;
    private String name;
    private Long ownerId;
    private Boolean isTemplate;
    private Integer formatVersion;
    private String passTypeIdentifier;
    private PKTransitType transitType;
    private PKPassType passType;
    private List<ChangeMessage> dataHeaderFields;
    private List<ChangeMessage> dataPrimaryFields;
    private List<ChangeMessage> dataSecondaryFields;
    private List<ChangeMessage> dataAuxiliaryFields;
    private List<ChangeMessage> dataBackFields;
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
    private List<ChangeMessage> dataBeacons;
    private List<ChangeMessage> dataLocations;
    private List<PKBarcode> barcodes;
    private String appLaunchURL;
    private List<Long> associatedStoreIdentifiers;
    private List<PWAssociatedApp> associatedApps;
    private Map<String, Object> userInfo;
    private Long maxDistance;
    private ChangeMessage dataRelevantDate;
    private String expirationDate;
    private Boolean voided;
    private Boolean sharingProhibited;
    private PKSemantics semantics;
    private String logo;
    private String icon;
    private String strip;
    private Personalization personalization;
    public void coverBy(PassRequest toCover) {
        if (toCover == null) {
            return;
        }
        setName(toCover.getName() != null ? toCover.getName() : this.getName());
        setOwnerId(toCover.getOwnerId() != null ? toCover.getOwnerId() : this.getOwnerId());
        setIsTemplate(toCover.getIsTemplate() != null ? toCover.getIsTemplate() : this.getIsTemplate());
        setFormatVersion(toCover.getFormatVersion() != null ? toCover.getFormatVersion() : this.getFormatVersion());
        setPassTypeIdentifier(toCover.getPassTypeIdentifier() != null ? toCover.getPassTypeIdentifier() : this.getPassTypeIdentifier());
        setTransitType(toCover.getTransitType() != null ? toCover.getTransitType() : this.getTransitType());
        setPassType(toCover.getPassType() != null ? toCover.getPassType() : this.getPassType());
        setDataHeaderFields(toCover.getDataHeaderFields() != null ? toCover.getDataHeaderFields() : this.getDataHeaderFields());
        setDataPrimaryFields(toCover.getDataPrimaryFields() != null ? toCover.getDataPrimaryFields() : this.getDataPrimaryFields());
        setDataSecondaryFields(toCover.getDataSecondaryFields() != null ? toCover.getDataSecondaryFields() : this.getDataSecondaryFields());
        setDataAuxiliaryFields(toCover.getDataAuxiliaryFields() != null ? toCover.getDataAuxiliaryFields() : this.getDataAuxiliaryFields());
        setDataBackFields(toCover.getDataBackFields() != null ? toCover.getDataBackFields() : this.getDataBackFields());
        setSerialNumber(toCover.getSerialNumber() != null ? toCover.getSerialNumber() : this.getSerialNumber());
        setTeamIdentifier(toCover.getTeamIdentifier() != null ? toCover.getTeamIdentifier() : this.getTeamIdentifier());
        setOrganizationName(toCover.getOrganizationName() != null ? toCover.getOrganizationName() : this.getOrganizationName());
        setLogoText(toCover.getLogoText() != null ? toCover.getLogoText() : this.getLogoText());
        setDescription(toCover.getDescription() != null ? toCover.getDescription() : this.getDescription());
        setBackgroundColor(toCover.getBackgroundColor() != null ? toCover.getBackgroundColor() : this.getBackgroundColor());
        setForegroundColor(toCover.getForegroundColor() != null ? toCover.getForegroundColor() : this.getForegroundColor());
        setLabelColor(toCover.getLabelColor() != null ? toCover.getLabelColor() : this.getLabelColor());
        setNfc(toCover.getNfc() != null ? toCover.getNfc() : this.getNfc());
        setWebServiceURL(toCover.getWebServiceURL() != null ? toCover.getWebServiceURL() : this.getWebServiceURL());
        setAuthenticationToken(toCover.getAuthenticationToken() != null ? toCover.getAuthenticationToken() : this.getAuthenticationToken());
        setDataBeacons(toCover.getDataBeacons() != null ? toCover.getDataBeacons() : this.getDataBeacons());
        setDataLocations(toCover.getDataLocations() != null ? toCover.getDataLocations() : this.getDataLocations());
        setBarcodes(toCover.getBarcodes() != null ? toCover.getBarcodes() : this.getBarcodes());
        setAppLaunchURL(toCover.getAppLaunchURL() != null ? toCover.getAppLaunchURL() : this.getAppLaunchURL());
        setAssociatedStoreIdentifiers(toCover.getAssociatedStoreIdentifiers() != null ? toCover.getAssociatedStoreIdentifiers() : this.getAssociatedStoreIdentifiers());
        setAssociatedApps(toCover.getAssociatedApps() != null ? toCover.getAssociatedApps() : this.getAssociatedApps());
        setUserInfo(toCover.getUserInfo() != null ? toCover.getUserInfo() : this.getUserInfo());
        setMaxDistance(toCover.getMaxDistance() != null ? toCover.getMaxDistance() : this.getMaxDistance());
        setDataRelevantDate(toCover.getDataRelevantDate() != null ? toCover.getDataRelevantDate() : this.getDataRelevantDate());
        setExpirationDate(toCover.getExpirationDate() != null ? toCover.getExpirationDate() : this.getExpirationDate());
        setVoided(toCover.getVoided() != null ? toCover.getVoided() : this.getVoided());
        setSharingProhibited(toCover.getSharingProhibited() != null ? toCover.getSharingProhibited() : this.getSharingProhibited());
        setSemantics(toCover.getSemantics() != null ? toCover.getSemantics() : this.getSemantics());
        setLogo(toCover.getLogo() != null ? toCover.getLogo() : this.getLogo());
        setIcon(toCover.getIcon() != null ? toCover.getIcon() : this.getIcon());
        setStrip(toCover.getStrip() != null ? toCover.getStrip() : this.getStrip());
        setPersonalization(toCover.getPersonalization() != null ? toCover.getPersonalization() : this.getPersonalization());
    }
}