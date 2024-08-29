package lan.pass.demo.service;

import de.brendamour.jpasskit.*;
import lan.pass.demo.mapper.*;
import lan.pass.demo.model.*;
import lan.pass.demo.model.ChangeMessageType;
import lan.pass.demo.model.ChangeMessage;
import lan.pass.demo.request.ChangeMessageRequest;
import lan.pass.demo.request.PassRequest;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ChangeMessageService{

    @Resource
    private ChangeMessageMapper changeMessageMapper;
    @Resource
    private LocationMapper locationMapper;
    @Resource
    private AssetMapper assetMapper;
    @Resource
    private BeaconMapper beaconMapper;
    @Resource
    private RelevantDateMapper relevantDateMapper;
    @Resource
    private FieldService fieldService;

    public int deleteByPrimaryKey(Long id) {
        return changeMessageMapper.deleteByPrimaryKey(id);
    }

    public Object insert(ChangeMessageRequest request) {
        if (request.getOwnerId() == null){
            return "请输入ownerId";
        }
        ChangeMessage changeMessage = new ChangeMessage();
        changeMessage.setContent(request.getContent());
        changeMessage.setName(request.getName());
        if (request.getLocation() != null){
            changeMessage.setType(ChangeMessageType.assetTypeCode.get(ChangeMessageType.Locations));
        }
        else if (request.getBeacon() != null){
            changeMessage.setType(ChangeMessageType.assetTypeCode.get(ChangeMessageType.Beacons));
        }
        else if (request.getRelevantDate() != null){
            changeMessage.setType(ChangeMessageType.assetTypeCode.get(ChangeMessageType.RelevantDate));
        }
        else if (request.getField() != null){
            changeMessage.setType(ChangeMessageType.assetTypeCode.get(ChangeMessageType.BackFields));
        }
        //parent
        Asset asset = new Asset();
        asset.setName(request.getName());
        asset.setOwnerId(request.getOwnerId());
        asset.setType(changeMessage.getType());
        assetMapper.insertAsset(asset);
        changeMessage.setAssetId(asset.getId());
        int affectedRows = changeMessageMapper.insert(changeMessage);
        changeMessageMapper.afterInsert(changeMessage);
        if (request.getLocation() != null){
            Location location = request.getLocation();
            location.setChangeMessageId(changeMessage.getId());
            locationMapper.insertSelective(location);
        }
        else if (request.getBeacon() != null){
            Beacon beacon = request.getBeacon();
            beacon.setChangeMessageId(changeMessage.getId());
            beaconMapper.insertSelective(beacon);
        }
        else if (request.getRelevantDate() != null){
            RelevantDate relevantDate = request.getRelevantDate();
            relevantDate.setChangeMessageId(changeMessage.getId());
            relevantDateMapper.insertSelective(relevantDate);
        }
        else if (request.getField() != null){
            Field field = request.getField();
            field.setChangeMessageId(changeMessage.getId());
            fieldService.insertSelective(field);
        }
        return affectedRows;
    }
    
    public ChangeMessage selectByPrimaryKey(Long id) {
        ChangeMessage responseChangeMessage = changeMessageMapper.selectByPrimaryKey(id);
        selectFromFile(responseChangeMessage);
        collectChangeMessage(responseChangeMessage);
        return responseChangeMessage;
    }

    public ChangeMessage getLatest(Long id) {
        List<ChangeMessage> history = changeMessageMapper.selectHistory(id);
        ChangeMessage latest = history.get(0);
        selectFromFile(latest);
        collectChangeMessage(latest);
        return latest;
    }

    public List<ChangeMessage> getHistory(Long id) {
        List<ChangeMessage> history = changeMessageMapper.selectHistory(id);
        for (ChangeMessage cmi : history){
            collectChangeMessage(cmi);
        }
        selectFromFile(history);
        return history;
    }

    public List<ChangeMessage> selectByPrimaryKeys(List<Long> ids) {
        List<ChangeMessage> messages = changeMessageMapper.selectByPrimaryKeys(ids);
        selectFromFile(messages);
        for (ChangeMessage cmi : messages){
            collectChangeMessage(cmi);
        }
        return messages;
    }

    public List<ChangeMessage> getMessageByOwnerId(Long id, boolean beacon, boolean location, boolean field, boolean relevantDate){
        List<ChangeMessage> messages = changeMessageMapper.selectChangeMessageInfo(id, beacon, location, field, relevantDate);
        selectFromFile(messages);
        for (ChangeMessage cmi : messages){
            collectChangeMessage(cmi);
        }
        return messages;
    }

    public void selectFromFile(List<ChangeMessage> items){
        for (ChangeMessage item : items){
            selectFromFile(item);
        }
    }

    public void selectFromFile(ChangeMessage item) {
        if (item.getField() == null) return;
        fieldService.getPKField(item.getField());
    }
    
    public int updateByPrimaryKey(ChangeMessageRequest request) {
        int affectedRows = 0;
        //获得父表格的id
        HashMap<String, Long> params = new HashMap<>();
        if (request.getLocation() != null && request.getLocation().getId() != null){
            Location location = request.getLocation();
            params.put("locationId", location.getId());
        }
        else if (request.getBeacon() != null && request.getBeacon().getId() != null){
            Beacon beacon = request.getBeacon();
            params.put("beaconId", beacon.getId());
        }
        else if (request.getRelevantDate() != null && request.getRelevantDate().getId() != null){
            RelevantDate relevantDate = request.getRelevantDate();
            params.put("relevantDateId", relevantDate.getId());
        }
        else if (request.getField() != null && request.getField().getId() != null){
            Field field = request.getField();
            params.put("fieldId", field.getId());
        }
        ChangeMessage original = changeMessageMapper.selectChangeMessageByChildIds(params);
        ChangeMessage changeMessage = new ChangeMessage();
        changeMessage.setName(request.getName());
        changeMessage.setId(original.getChangeMessageId());
        changeMessage.setAssetId(original.getAssetId());
        changeMessage.setContent(request.getContent());
        changeMessage.setOriginalId(original.getOriginalId());
        changeMessage.setType(original.getType());
        changeMessageMapper.beforeUpdate(changeMessage);
        affectedRows += changeMessageMapper.insertEditRecord(changeMessage);
        if (request.getLocation() != null && request.getLocation().getId() != null){
            Location location = request.getLocation();
            location.setChangeMessageId(changeMessage.getId());
            locationMapper.insertSelective(location);
        }
        else if (request.getBeacon() != null && request.getBeacon().getId() != null){
            Beacon beacon = request.getBeacon();
            beacon.setChangeMessageId(changeMessage.getId());
            beaconMapper.insertSelective(beacon);
        }
        else if (request.getRelevantDate() != null && request.getRelevantDate().getId() != null){
            RelevantDate relevantDate = request.getRelevantDate();
            relevantDate.setChangeMessageId(changeMessage.getId());
            relevantDateMapper.insertSelective(relevantDate);
        }
        else if (request.getField() != null && request.getField().getId() != null){
            Field field = request.getField();
            field.setChangeMessageId(changeMessage.getId());
            fieldService.insertSelective(field);
        }
        return affectedRows;
    }

    public String insertRelationship(PassRequest passRequest, List<ChangeMessage> items, ChangeMessageType type){
        if (passRequest.getId() == null) return "pass不存在id";
        for (int i = 0; i < items.size(); i++){
            ChangeMessage item = items.get(i);
            Long chgId = null;
            if (item.getLocation() != null && item.getLocation().getChangeMessageId() != null){
                chgId = item.getLocation().getChangeMessageId();
            }
            else if (item.getBeacon() != null && item.getBeacon().getChangeMessageId() != null){
                chgId = item.getBeacon().getChangeMessageId();
            }
            else if (item.getRelevantDate() != null && item.getRelevantDate().getChangeMessageId() != null){
                chgId = item.getRelevantDate().getChangeMessageId();
            }
            else if (item.getField() != null && item.getField().getChangeMessageId() != null){
                chgId = item.getField().getChangeMessageId();
            }
            if (chgId != null){
                ChangeMessage changeMessage = changeMessageMapper.selectByPrimaryKey(chgId);
                if (changeMessage == null){
                    return "请插入存在的id";
                }
                changeMessageMapper.insertPassChangeMessage(passRequest.getId(), changeMessage.getOriginalId(), i, ChangeMessageType.fieldTypes.get(type));
            }
        }
        return "";
    }

    public void deletePassChangeMessageByPassId(PassRequest passRequest){
        changeMessageMapper.deletePassChangeMessageByPassId(passRequest.getId());
    }

    public HashMap<String, List<Object>> siftThrough(List<ChangeMessage> items){
        HashMap<String, List<Object>> result = new HashMap<>();
        result.put("beacons", new ArrayList<>());
        result.put("locations", new ArrayList<>());
        result.put("fields", new ArrayList<>());
        result.put("relevantDate", new ArrayList<>());
        for (int i = 0; i < items.size(); i++){
            ChangeMessage item = items.get(i);
            if (item.getPkBeacon() != null){
                result.get("beacons").add(item.getPkBeacon());
            } else if (item.getPkField() != null){
                result.get("fields").add(item.getPkField());
            } else if (item.getPkRelevantDate() != null){
                result.get("relevantDate").add(item.getPkRelevantDate());
            } else if (item.getPkLocation() != null){
                result.get("locations").add(item.getPkLocation());
            }
        }
        return result;
    }

    // Generic method with the second parameter as a type parameter
    public <T> List<T> siftThrough(List<ChangeMessage> items, Class<T> typeClass, boolean cast){
        if (items == null) return null;
        String typeKey = ChangeMessage.types.get(typeClass);
        if (typeKey == null) {
            throw new IllegalArgumentException("Type class not supported: " + typeClass.getName());
        }

        // Initialize the result list with the correct type
        List<T> resultList = new ArrayList<>();

        // Populate the result list with items of the specified type
        for (ChangeMessage item : items) {
            if (typeClass.equals(PKBeacon.class)){
                if (item.getPkBeacon() != null){
                    resultList.add(typeClass.cast(item.getPkBeacon()));
                } else if (cast && item.getBeacon() != null) {
                    ChangeMessage temp = selectByPrimaryKey(item.getChangeMessageId());
                    resultList.add(typeClass.cast(temp.toPKBeacon()));
                }
            } else if (typeClass.equals(PKLocation.class)){
                if (item.getPkLocation() != null){
                    resultList.add(typeClass.cast(item.getPkLocation()));
                } else if (cast && item.getLocation() != null) {
                    ChangeMessage temp = selectByPrimaryKey(item.getChangeMessageId());
                    resultList.add(typeClass.cast(temp.toPKLocation()));
                }
            } else if (typeClass.equals(PKField.class)){
                if (item.getPkField() != null){
                    resultList.add(typeClass.cast(item.getPkField()));
                } else if (cast && item.getField() != null) {
                    ChangeMessage temp = selectByPrimaryKey(item.getChangeMessageId());
                    resultList.add(typeClass.cast(temp.toPKField()));
                }
            } else if (typeClass.equals(String.class)){
                if (item.getPkRelevantDate() != null){
                    resultList.add(typeClass.cast(item.getPkRelevantDate()));
                } else if (cast && item.getRelevantDate() != null) {
                    ChangeMessage temp = selectByPrimaryKey(item.getChangeMessageId());
                    resultList.add(typeClass.cast(temp.toPKRelevanteDate()));
                }
            }
        }

        return resultList;
    }

    public void collectChangeMessage(ChangeMessage changeMessage){
        if (changeMessage.getLocation() != null){
            changeMessage.getLocation().setChangeMessageId(changeMessage.getChangeMessageId());
        }
        if (changeMessage.getBeacon() != null){
            changeMessage.getBeacon().setChangeMessageId(changeMessage.getChangeMessageId());
        }
        if (changeMessage.getRelevantDate() != null){
            changeMessage.getRelevantDate().setChangeMessageId(changeMessage.getChangeMessageId());
        }
        if (changeMessage.getField() != null){
            changeMessage.getField().setChangeMessageId(changeMessage.getChangeMessageId());
        }
    }

    public String loadChangeMessageItem(PassRequest passRequest){
        if (passRequest.getId() == null) return "pass不存在id";
        List<ChangeMessage> items = changeMessageMapper.selectChangeMessageItemsByPassId(passRequest.getId());
        for (ChangeMessage cmi : items){
            collectChangeMessage(cmi);
        }
        for (ChangeMessage cmi : pickChangeMessageItem(items, ChangeMessageType.Locations)){
            int index = cmi.getIndex();
            passRequest.getDataLocations().add(index, cmi);
        }
        for (ChangeMessage cmi : pickChangeMessageItem(items, ChangeMessageType.Beacons)){
            int index = cmi.getIndex();
            passRequest.getDataBeacons().add(index, cmi);
        }
        for (ChangeMessage cmi : pickChangeMessageItem(items, ChangeMessageType.RelevantDate)){
            passRequest.setDataRelevantDate(cmi);
        }
        ArrayList<ChangeMessageType> toLoad = new ArrayList<>();
        toLoad.add(ChangeMessageType.AuxiliaryFields);
        toLoad.add(ChangeMessageType.BackFields);
        toLoad.add(ChangeMessageType.PrimaryFields);
        toLoad.add(ChangeMessageType.SecondaryFields);
        toLoad.add(ChangeMessageType.HeaderFields);
        for (ChangeMessageType type : toLoad){
            for (ChangeMessage cmi : pickChangeMessageItem(items, type)){
                int index = cmi.getIndex();
                java.lang.reflect.Field field = ChangeMessageType.fieldHashMap.get(type);
                try {
                    field.setAccessible(true);
                    Object convertedResult = field.get(passRequest);
                    if (convertedResult != null){
                        ArrayList<ChangeMessage> targetItems = new ArrayList<>();
                        if (convertedResult instanceof List) {
                            targetItems = (ArrayList<ChangeMessage>)convertedResult;
                        }
                        targetItems.add(index, cmi);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        return "";
    }

    public <T> List<ChangeMessage> pickChangeMessageItem(List<ChangeMessage> items, ChangeMessageType type) {
        List<ChangeMessage> resultList = new ArrayList<>();
        java.lang.reflect.Field[] fields = ChangeMessage.class.getDeclaredFields();
        for (ChangeMessage item : items) {
            for (java.lang.reflect.Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object member = field.get(item);
                    if (member != null && ChangeMessageType.recognizedDataClass.get(type).isInstance(field.get(item))) {
                        int fieldType = ChangeMessageType.fieldTypes.get(type);
                        if (fieldType != -1 && item.getType() != fieldType){
                            continue;
                        }
                        resultList.add(item);
                        break;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultList;
    }

    public Object restore(Long id){
        changeMessageMapper.restore(id);
        ChangeMessage changeMessage = selectByPrimaryKey(id);
        return changeMessageMapper.deleteOtherHistory(changeMessage);
    }
}