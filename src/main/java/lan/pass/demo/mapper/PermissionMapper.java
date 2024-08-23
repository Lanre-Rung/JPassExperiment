package lan.pass.demo.mapper;

import lan.pass.demo.model.Merchant;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PermissionMapper {
    @Select("SELECT merchant_id, merchant_name " +
            "FROM passcombined_pass " +
            "WHERE pass_id = #{id}")
    @Results({
            @Result(property = "id", column = "merchant_id"),
            @Result(property = "name", column = "merchant_name")
    })
    List<Merchant> selectMerchantsByPassId(Long id);

    @Delete("DELETE FROM merchant_pass WHERE pass_id = #{id}")
    void deletePassPermissions(Long id);

    @Delete("INSERT INTO merchant_pass(pass_id, merchant_id) VALUES(#{passId}, #{merchantId})")
    void grantPassPermission(Long passId, Long merchantId);
}
