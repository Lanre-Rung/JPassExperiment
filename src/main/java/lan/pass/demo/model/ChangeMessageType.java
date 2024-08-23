package lan.pass.demo.model;

import de.brendamour.jpasskit.PKBeacon;
import de.brendamour.jpasskit.PKField;
import de.brendamour.jpasskit.PKLocation;
import lan.pass.demo.request.PassRequest;


import java.util.HashMap;

public enum ChangeMessageType {
    Locations,
    Beacons,
    RelevantDate,
    PrimaryFields,
    HeaderFields,
    SecondaryFields,
    AuxiliaryFields,
    BackFields;
    public static final HashMap<ChangeMessageType, Integer> fieldTypes = new java.util.HashMap<>();
    static{
        fieldTypes.put(ChangeMessageType.Locations, -1);
        fieldTypes.put(ChangeMessageType.Beacons, -1);
        fieldTypes.put(ChangeMessageType.RelevantDate, -1);
        fieldTypes.put(ChangeMessageType.PrimaryFields, 0);
        fieldTypes.put(ChangeMessageType.HeaderFields, 1);
        fieldTypes.put(ChangeMessageType.SecondaryFields, 2);
        fieldTypes.put(ChangeMessageType.AuxiliaryFields, 3);
        fieldTypes.put(ChangeMessageType.BackFields, 4);
    }
    public static final HashMap<ChangeMessageType, Class<?>> recognizedDataClass = new java.util.HashMap<>();
    static {
        recognizedDataClass.put(ChangeMessageType.Locations, Location.class);
        recognizedDataClass.put(ChangeMessageType.Beacons, Beacon.class);
        recognizedDataClass.put(ChangeMessageType.RelevantDate, RelevantDate.class);
        recognizedDataClass.put(ChangeMessageType.PrimaryFields, Field.class);
        recognizedDataClass.put(ChangeMessageType.HeaderFields, Field.class);
        recognizedDataClass.put(ChangeMessageType.SecondaryFields, Field.class);
        recognizedDataClass.put(ChangeMessageType.AuxiliaryFields, Field.class);
        recognizedDataClass.put(ChangeMessageType.BackFields, Field.class);
    }
    public static final HashMap<ChangeMessageType, Class<?>> recognizedPKClass = new java.util.HashMap<>();
    static{
        recognizedPKClass.put(ChangeMessageType.Locations, PKLocation.class);
        recognizedPKClass.put(ChangeMessageType.Beacons, PKBeacon.class);
        recognizedPKClass.put(ChangeMessageType.RelevantDate, String.class);
        recognizedPKClass.put(ChangeMessageType.PrimaryFields, PKField.class);
        recognizedPKClass.put(ChangeMessageType.HeaderFields, PKField.class);
        recognizedPKClass.put(ChangeMessageType.SecondaryFields, PKField.class);
        recognizedPKClass.put(ChangeMessageType.AuxiliaryFields, PKField.class);
        recognizedPKClass.put(ChangeMessageType.BackFields, PKField.class);
    }
    public static final HashMap<ChangeMessageType, java.lang.reflect.Field> fieldHashMap = new HashMap<>();
    static{
        try {
            fieldHashMap.put(ChangeMessageType.Locations, PassRequest.class.getDeclaredField("dataLocations"));
            fieldHashMap.put(ChangeMessageType.Beacons, PassRequest.class.getDeclaredField("dataBeacons"));
            fieldHashMap.put(ChangeMessageType.RelevantDate, PassRequest.class.getDeclaredField("dataRelevantDate"));
            fieldHashMap.put(ChangeMessageType.PrimaryFields, PassRequest.class.getDeclaredField("dataPrimaryFields"));
            fieldHashMap.put(ChangeMessageType.AuxiliaryFields, PassRequest.class.getDeclaredField("dataAuxiliaryFields"));
            fieldHashMap.put(ChangeMessageType.BackFields, PassRequest.class.getDeclaredField("dataBackFields"));
            fieldHashMap.put(ChangeMessageType.SecondaryFields, PassRequest.class.getDeclaredField("dataSecondaryFields"));
            fieldHashMap.put(ChangeMessageType.HeaderFields, PassRequest.class.getDeclaredField("dataHeaderFields"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    public static final HashMap<ChangeMessageType, Integer> assetTypeCode = new HashMap<>();
    static {
        assetTypeCode.put(ChangeMessageType.Locations, 3);
        assetTypeCode.put(ChangeMessageType.Beacons, 4);
        assetTypeCode.put(ChangeMessageType.RelevantDate, 6);
        assetTypeCode.put(ChangeMessageType.PrimaryFields, 5);
        assetTypeCode.put(ChangeMessageType.AuxiliaryFields, 5);
        assetTypeCode.put(ChangeMessageType.BackFields, 5);
        assetTypeCode.put(ChangeMessageType.SecondaryFields, 5);
        assetTypeCode.put(ChangeMessageType.HeaderFields, 5);
    }
}
