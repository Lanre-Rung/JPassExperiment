package lan.pass.demo.mapper;

import lan.pass.demo.model.Merchant;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MerchantMapper {

    @Insert("INSERT INTO merchants (name) VALUES (#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertMerchant(Merchant merchant);

    @Select("SELECT * FROM merchants WHERE id = #{id}")
    Merchant selectMerchantById(Long id);

    @Select("SELECT * FROM merchants")
    List<Merchant> selectAllMerchants();

    @Update("UPDATE merchants SET name = #{name} WHERE id = #{id}")
    void updateMerchant(Merchant merchant);

    @Delete("DELETE FROM merchants WHERE id = #{id}")
    void deleteMerchant(Long id);

    @Delete({
            "<script>",
            "DELETE FROM combine_passes",
            "WHERE pass_id = #{passId}",
            "AND merchant_id IN",
            "<foreach item='item' collection='toDeletes' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "AND owner_id = #{ownerId}",
            "</script>"
    })
    void deleteMerchantPermissions(
            @Param("passId") Long passId,
            @Param("toDeletes") List<Long> toDeletes,
            @Param("ownerId") Long ownerId);
}