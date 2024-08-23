package lan.pass.demo.service;

import lan.pass.demo.mapper.MerchantMapper;
import lan.pass.demo.model.Merchant;
import lan.pass.demo.request.LogInRequest;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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

    public Merchant findByEmail(LogInRequest logInRequest) {
        return merchantMapper.findByEmailAndPassword(logInRequest.getEmailOrPhone(), logInRequest.getPassword(), false);
    }

    public Merchant findByPhone(LogInRequest logInRequest) {
        return merchantMapper.findByPhoneAndPassword(logInRequest.getEmailOrPhone(), logInRequest.getPassword(), false);
    }

    public Object signup(Merchant merchant) {
        Merchant existingEmail = merchantMapper.findByEmailAndPassword(merchant.getEmail(), null, true);
        Merchant existingPhone = merchantMapper.findByPhoneAndPassword(merchant.getPhone(), null, true);

        if (existingEmail != null) {
            return "email exists";
        } else if (existingPhone != null){
            return "phone exists";
        } else {
            merchantMapper.insertMerchant(merchant);
            return merchant;
        }
    }

    public Object changePassword(Merchant merchant) {
        Merchant existingEmail = merchantMapper.findByEmailAndPassword(merchant.getEmail(), null, true);
        Merchant existingPhone = merchantMapper.findByPhoneAndPassword(merchant.getPhone(), null, true);

        if (existingEmail != null) {
            return "email exists";
        } else if (existingPhone != null){
            return "phone exists";
        } else {
            merchantMapper.updateMerchant(merchant);
            return merchant;
        }
    }

}