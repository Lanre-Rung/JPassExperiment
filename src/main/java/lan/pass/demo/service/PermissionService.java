package lan.pass.demo.service;

import lan.pass.demo.mapper.MerchantMapper;
import lan.pass.demo.mapper.PassMapper;
import lan.pass.demo.mapper.PermissionMapper;
import lan.pass.demo.model.Merchant;
import lan.pass.demo.model.Pass;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private PermissionMapper permissionMapper;

    @Autowired
    public void setPermissionMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    private MerchantMapper merchantMapper;

    @Autowired
    private void setMerchantMapper(MerchantMapper merchantMapper){
        this.merchantMapper = merchantMapper;
    }


    private PassMapper passMapper;

    @Autowired
    private void setPassMapper(PassMapper passMapper){
        this.passMapper = passMapper;
    }


    public Map<String, List<Merchant>> getPermissions(Long passId) {
        Pass pass = passMapper.selectPassById(passId);
        List<Merchant> merchantsWithPermission = permissionMapper.selectMerchantsByPassId(passId);
        Set<Long> permittedIds = merchantsWithPermission.stream()
                .map(Merchant::getId)
                .collect(Collectors.toSet());

        List<Merchant> allMerchants = merchantMapper.selectAllMerchants().stream()
                .filter(merchant -> !merchant.getId().equals(pass.getOwnerId()))
                .collect(Collectors.toList());;
        List<Merchant> merchantsWithoutPermission = allMerchants.stream()
                .filter(merchant -> !permittedIds.contains(merchant.getId()))
                .map(Merchant::idAndNameOnly)
                .collect(Collectors.toList());

        Map<String, List<Merchant>> result = new HashMap<>();
        result.put("exist", merchantsWithPermission);
        result.put("rest", merchantsWithoutPermission);
        return result;
    }

    public void setPermissions(Long passId, List<Merchant> merchants){
        permissionMapper.deletePassPermissions(passId);
        Pass pass = passMapper.selectPassById(passId);
        for (Merchant merchant : merchants){
            if (pass.getOwnerId().equals(merchant.getId())) continue;
            permissionMapper.grantPassPermission(passId, merchant.getId());
        }
    }
}
