package lan.pass.demo.controller;

import lan.pass.demo.model.Merchant;
import lan.pass.demo.request.LogInRequest;
import lan.pass.demo.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

    private MerchantService merchantService;
    @Autowired
    private void setMerchantService(MerchantService merchantService){
        this.merchantService = merchantService;
    }

    @PostMapping
    public Merchant createMerchant(@RequestBody Merchant merchant) {
        return merchantService.saveMerchant(merchant);
    }

    @GetMapping("/{id}")
    public Merchant getMerchantById(@PathVariable Long id) {
        return merchantService.getMerchantById(id);
    }

    @GetMapping
    public List<Merchant> getAllMerchants() {
        return merchantService.getAllMerchants();
    }

    @PutMapping("/{id}")
    public Merchant updateMerchant(@PathVariable Long id, @RequestBody Merchant merchantDetails) {
        return merchantService.updateMerchant(id, merchantDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteMerchant(@PathVariable Long id) {
        merchantService.deleteMerchant(id);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Merchant merchant) {
        Object result = merchantService.signup(merchant);
        if (result instanceof Merchant){
            return ResponseEntity.ok((((Merchant) result).getId()));
        } else {
            return ResponseEntity.ok(result);
        }
    }

    //电子邮箱或手机号码+密码登录
    @PostMapping("/login")
    public ResponseEntity<?> loginByEPAndPassword(@RequestBody LogInRequest logInRequest) {
        Merchant result = null;
        if (logInRequest.getEmailOrPhone() != null ){
            result = merchantService.findByEmail(logInRequest);
            if (result == null){
                result = merchantService.findByPhone(logInRequest);
            }
        }
        if (result != null){
            return ResponseEntity.ok(result.getId());
        } else {
            return ResponseEntity.ok("Login failed");
        }
    }
}