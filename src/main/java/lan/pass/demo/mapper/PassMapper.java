package lan.pass.demo.mapper;

import lan.pass.demo.model.Merchant;
import lan.pass.demo.model.Pass;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface PassMapper {

    @Insert("INSERT INTO pass (name, owner_id, last_modified, created_date, is_template, asset_id) VALUES (#{name}, #{ownerId}, NOW(), NOW(), #{isTemplate}, #{assetId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPass(Pass pass);

    @Select("SELECT * FROM pass_info WHERE id = #{id} AND is_active = 1")
    Pass selectPassById(Long id);

    @Select("SELECT * FROM pass_info AND is_active = 1")
    List<Pass> selectAllPasses();

    @Select("<script>" +
            "SELECT * FROM pass_info WHERE owner_id = #{id}" +
            "<if test='isTemplate'>" +
            "AND is_template = 1" +
            "</if>" +
            "</script>")
    List<Pass> selectPassByOwnerId(Long id, boolean isTemplate);

    @Select("<script>" +
            "SELECT id, name, owner_id, is_active, last_modified, created_date, is_template " +
            "FROM pass_info WHERE owner_id = #{id} " +
            "<if test='isTemplate'>" +
            "AND is_template = 1" +
            "</if>" +
            "UNION ALL " +
            "SELECT pass_id AS id, pass_name AS name, owner_id, is_active, last_modified, created_date, is_template " +
            "FROM combined_pass WHERE merchant_id = #{id}" +
            "<if test='isTemplate'>" +
            "AND is_template = 1" +
            "</if>" +
            "</script>")
    List<Pass> selectPassByMerchantId(Long id, boolean isTemplate);

    @Update("UPDATE pass SET name = #{name}, owner_id = #{ownerId}, last_modified = NOW() WHERE id = #{id}")
    void updatePass(Pass pass);

    @Delete("UPDATE pass SET is_active = 0 WHERE id = #{id} AND owner_id = #{ownerId}")
    int deletePass(Long id, Long ownerId);
}