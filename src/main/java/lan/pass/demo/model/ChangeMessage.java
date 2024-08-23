package lan.pass.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.brendamour.jpasskit.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangeMessage {

    Long id;

    int index;

    int type;

    Long changeMessageId;

    Long originalId;

    Date lastModified;

    String content;

    String name;

    Long assetId;

    Location location;

    Beacon beacon;

    RelevantDate relevantDate;

    Field field;

    Asset asset;

    PKLocation pkLocation;

    PKBeacon pkBeacon;

    String pkRelevantDate;

    PKField pkField;

    public static HashMap<Class<?>, String> types = new HashMap<>();
    static {
        types.put(PKBeacon.class, "beacons");
        types.put(PKLocation.class, "locations");
        types.put(PKField.class, "fields");
        types.put(String.class, "relevantDate");
    }

    public PKBeacon toPKBeacon(){
        PKBeaconBuilder beaconBuilder = PKBeacon.builder();
        beaconBuilder.major(beacon.getMajor());
        beaconBuilder.minor(beacon.getMinor());
        beaconBuilder.proximityUUID(beacon.getProximityUUID());
        beaconBuilder.relevantText(content);
        return beaconBuilder.build();
    }
    public PKLocation toPKLocation(){
        PKLocationBuilder locationBuilder = PKLocation.builder();
        locationBuilder.altitude(location.getAltitude());
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        locationBuilder.relevantText(content);
        return locationBuilder.build();
    }
    public String toPKRelevanteDate(){
        return relevantDate.getDate();
    }
    public PKField toPKField(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.treeToValue(field.getFileContent(), PKField.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
