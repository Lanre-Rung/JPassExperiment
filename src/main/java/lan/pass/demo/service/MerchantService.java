package lan.pass.demo.service;

import lan.pass.demo.mapper.MerchantMapper;
import lan.pass.demo.model.Merchant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MerchantService {

    private MerchantMapper merchantMapper;

    @Autowired
    private void setMerchantMapper(MerchantMapper merchantMapper){
        this.merchantMapper = merchantMapper;
    }

    public Merchant saveMerchant(Merchant merchant) {
        merchantMapper.insertMerchant(merchant);
        return merchant;
    }

    public Merchant getMerchantById(Long id) {
        return merchantMapper.selectMerchantById(id);
    }

    public List<Merchant> getAllMerchants() {
        return merchantMapper.selectAllMerchants();
    }

    public Merchant updateMerchant(Long id, Merchant merchantDetails) {
        merchantDetails.setId(id);
        merchantMapper.updateMerchant(merchantDetails);
        return merchantDetails;
    }

    public void deleteMerchant(Long id) {
        merchantMapper.deleteMerchant(id);
    }

    public void deleteMerchantPermissions(Long passId, List<Long> toDeletes, Long ownerId) {
        merchantMapper.deleteMerchantPermissions(passId, toDeletes, ownerId);
    }
}