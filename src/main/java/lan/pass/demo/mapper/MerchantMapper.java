package lan.pass.demo.mapper;

import lan.pass.demo.model.Merchant;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MerchantMapper {

    void insertMerchant(Merchant merchant);
    Merchant selectMerchantById(Long id);
    Merchant findByEmailAndPassword(@Param("email") String email, @Param("password") String password, boolean ignorePassword);
    Merchant findByPhoneAndPassword(@Param("phone") String phone, @Param("password") String password, boolean ignorePassword);
    List<Merchant> selectAllMerchants();
    void updateMerchant(Merchant merchant);
    void deleteMerchant(Long id);
}