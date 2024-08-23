package lan.pass.demo.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

public enum AssetType {
    Pass,
    Image,
    ChangeMessage,
    Location,
    Beacon,
    Field,
    RelevantDate;

    // Map to store the forward direction: AssetType to Integer
    private static final Map<AssetType, Integer> SQLTypeCode = new TreeMap<>();

    // Static initializer block to populate the forward map
    static {
        SQLTypeCode.put(Pass, 0);
        SQLTypeCode.put(Image, 1);
        SQLTypeCode.put(ChangeMessage, 2);
        SQLTypeCode.put(Location, 3);
        SQLTypeCode.put(Beacon, 4);
        SQLTypeCode.put(Field, 5);
        SQLTypeCode.put(RelevantDate, 6);
    }

    // Method to get the integer code for an AssetType
    public static Integer getCode(AssetType type) {
        return SQLTypeCode.get(type);
    }

    // Reverse map: Integer to AssetType
    private static final Map<Integer, AssetType> reverseSQLTypeCode = new TreeMap<>();

    // Static initializer block to populate the reverse map
    static {
        for (Map.Entry<AssetType, Integer> entry : SQLTypeCode.entrySet()) {
            reverseSQLTypeCode.put(entry.getValue(), entry.getKey());
        }
    }

    // Method to get the AssetType from an integer code
    public static AssetType getType(Integer code) {
        return reverseSQLTypeCode.get(code);
    }
}