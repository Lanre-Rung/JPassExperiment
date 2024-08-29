package lan.pass.demo.utility;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import de.brendamour.jpasskit.*;
import de.brendamour.jpasskit.enums.PKPassType;
import lan.pass.demo.model.ChangeMessage;
import lan.pass.demo.model.ChangeMessageType;
import lan.pass.demo.request.PassRequest;
import lan.pass.demo.request.Personalization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassConverter {

    static HashMap<String, String> types = new HashMap<>();
    static ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, Class<?>> fieldsMap = new HashMap<>();
    static HashMap<String, String> images = new HashMap<>();
    static {
        types.put("generic", "PKGenericPass");
        types.put("boardingPass", "PKBoardingPass");
        types.put("coupon", "PKCoupon");
        types.put("eventTicket", "PKEventTicket");
        types.put("storeCard", "PKStoreCard");

        fieldsMap.put("beacons", PKBeacon.class);
        fieldsMap.put("locations", PKLocation.class);
        fieldsMap.put("barcodes", PKBarcode.class);
        fieldsMap.put("associatedApps", PWAssociatedApp.class);
    }
    public static PassRequest populatePassRequest(HashMap<String, JsonNode> map) throws IOException {
        PassRequest request;
        JsonNode passNode = map.get("pass.json");
        request = objectMapper.treeToValue(passNode, PassRequest.class);
        findType(request, passNode);
        request.setDataBeacons(convertListToCMIList(passNode.get("beacons"), PKBeacon.class));
        request.setDataLocations(convertListToCMIList(passNode.get("locations"), PKLocation.class));
        request.setDataRelevantDate(convertNodeToCMI(passNode.get("relevantDate"), String.class));
        JsonNode personalizationNode = map.get("personalization.json");
        if (personalizationNode != null){
            request.setPersonalization(objectMapper.treeToValue(personalizationNode, Personalization.class));
        }
        JsonNode iconNode = map.get("icon.png");
        if (iconNode != null){
            request.setIcon(objectMapper.treeToValue(iconNode, String.class));
        }
        JsonNode stripNode = map.get("strip.png");
        if (stripNode != null){
            request.setStrip(objectMapper.treeToValue(stripNode, String.class));
        }
        JsonNode logoNode = map.get("logo.png");
        if (logoNode != null){
            request.setLogo(objectMapper.treeToValue(logoNode, String.class));
        }
        return request;
    }
    public static void findType(PassRequest request, JsonNode passNode) throws IOException {
        for (String typeKey : types.keySet()) {
            if (passNode.has(typeKey)) {
                String enumName = types.get(typeKey);
                try {
                    request.setPassType(PKPassType.valueOf(enumName));
                } catch (IllegalArgumentException e) {
                    System.err.println("Pass type not recognized: " + enumName);
                }
                FieldContainer fieldContainer = objectMapper.treeToValue(passNode.get(typeKey), FieldContainer.class);
                List<ChangeMessage> primaryCMIList = convertListToCMIList(fieldContainer.getPrimaryFields(), PKField.class);
                List<ChangeMessage> headerCMIList = convertListToCMIList(fieldContainer.getHeaderFields(), PKField.class);
                List<ChangeMessage> secondaryCMIList = convertListToCMIList(fieldContainer.getSecondaryFields(), PKField.class);
                List<ChangeMessage> auxiliaryCMIList = convertListToCMIList(fieldContainer.getAuxiliaryFields(), PKField.class);
                List<ChangeMessage> backCMIList = convertListToCMIList(fieldContainer.getBackFields(), PKField.class);
                request.setDataPrimaryFields(primaryCMIList);
                request.setDataSecondaryFields(secondaryCMIList);
                request.setDataHeaderFields(headerCMIList);
                request.setDataAuxiliaryFields(auxiliaryCMIList);
                request.setDataBackFields(backCMIList);
                break;
            }
        }
    }
    public static <T, C> ChangeMessage convertNodeToCMI(C nodeItem, Class<T> targetClass) {
        ChangeMessage item = new ChangeMessage();
        if (nodeItem == null) return null;
        // Check if the targetClass is one of the supported types in ChangeMessage
        if (!ChangeMessageType.recognizedPKClass.containsValue(targetClass)) {
            throw new IllegalArgumentException("Unsupported target class: " + targetClass.getName());
        }

        // Set the corresponding field in ChangeMessage based on the type of nodeItem
        if (nodeItem.getClass().isAssignableFrom(PKBeacon.class)) {
            item.setPkBeacon((PKBeacon) nodeItem);
        } else if (nodeItem.getClass().isAssignableFrom(PKLocation.class)) {
            item.setPkLocation((PKLocation) nodeItem);
        } else if (nodeItem.getClass().isAssignableFrom(PKField.class)) {
            item.setPkField((PKField) nodeItem);
        } else if (nodeItem.getClass() == TextNode.class) {
            // Assuming the nodeItem is a String and corresponds to pkRelevantDate
            item.setPkRelevantDate(((TextNode) nodeItem).asText());
        } else {
            throw new IllegalArgumentException("nodeItem is not an instance of a supported type.");
        }

        return item;
    }
    public static <T, C> List<ChangeMessage> convertListToCMIList(List<C> toConvertList, Class<T> targetClass) {
        List<ChangeMessage> cmiList = new ArrayList<>();
        for (C toConvert : toConvertList) {
            ChangeMessage cmi = convertNodeToCMI(toConvert, targetClass);
            cmiList.add(cmi);
        }
        return cmiList;
    }
    public static <T> List<ChangeMessage> convertListToCMIList(JsonNode toConvertList, Class<T> targetClass) {
        List<ChangeMessage> cmiList = new ArrayList<>();
        for (JsonNode node : toConvertList) {
            try {
                T convertedObject = objectMapper.treeToValue(node, targetClass);
                ChangeMessage cmi = new ChangeMessage();
                // Set the converted object to the appropriate field in ChangeMessage
                if (PKBeacon.class.isAssignableFrom(targetClass)) {
                    cmi.setPkBeacon((PKBeacon) convertedObject);
                } else if (PKLocation.class.isAssignableFrom(targetClass)) {
                    cmi.setPkLocation((PKLocation) convertedObject);
                }
                // Add the ChangeMessage to the list
                cmiList.add(cmi);
            } catch (IOException e) {
                // Handle the exception as appropriate for your application
                e.printStackTrace();
            }
        }
        return cmiList;
    }
}
@Data
@NoArgsConstructor
@AllArgsConstructor
class FieldContainer {
    private List<PKField> headerFields;
    private List<PKField> primaryFields;
    private List<PKField> secondaryFields;
    private List<PKField> auxiliaryFields;
    private List<PKField> backFields;
}