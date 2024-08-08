package lan.pass.demo.controller;

import lan.pass.demo.model.Merchant;
import lan.pass.demo.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @DeleteMapping("/pass/{passId}/permissions")
    public ResponseEntity<?> deleteMerchantPermissions(
            @PathVariable("passId") Long passId,
            @RequestParam List<Long> toDeletes,
            @RequestParam("ownerId") Long ownerId) {

        merchantService.deleteMerchantPermissions(passId, toDeletes, ownerId);
        return ResponseEntity.ok().build();
    }
}