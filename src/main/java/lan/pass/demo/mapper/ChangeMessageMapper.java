package lan.pass.demo.mapper;

import lan.pass.demo.model.ChangeMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChangeMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ChangeMessage record);

    ChangeMessage selectByPrimaryKey(Long id);

    List<ChangeMessage> selectHistory(Long id);

    List<ChangeMessage> selectByPrimaryKeys(List<Long> ids);

    int afterInsert(ChangeMessage record);

    int beforeUpdate(ChangeMessage record);

    int insertEditRecord(ChangeMessage record);

    ChangeMessage selectChangeMessageByChildIds(Map<String, Long> params);

    List<ChangeMessage> selectChangeMessageInfo(
            @Param("owner_id") Long ownerId,
            @Param("beacon") Boolean beacon,
            @Param("field") Boolean field,
            @Param("location") Boolean location,
            @Param("relevant_date") Boolean relevantDate);

    int insertPassChangeMessage(@Param("passId") Long passId, @Param("chgMessageId") Long chgMessageId,
                                @Param("index") int index, @Param("type") int type);

    int deletePassChangeMessageByPassId(Long passId);

    List<ChangeMessage> selectChangeMessageItemsByPassId(Long passId);

    int restore(Long id);

    int deleteOtherHistory(ChangeMessage record);
}