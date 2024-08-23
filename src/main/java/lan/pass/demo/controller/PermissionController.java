package lan.pass.demo.controller;

import lan.pass.demo.model.Merchant;
import lan.pass.demo.service.PassService;
import lan.pass.demo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    PermissionService permissionService;

    @Autowired
    public void setPassService(PermissionService permissionService){
        this.permissionService = permissionService;
    }
    @GetMapping
    public ResponseEntity<?> setPassPermissions(@RequestParam Long id) {
        return ResponseEntity.ok( permissionService.getPermissions(id));
    }
    @PostMapping
    public ResponseEntity<?> setPassPermissions(@RequestParam Long id, @RequestBody List<Merchant> merchants) {
        permissionService.setPermissions(id, merchants);
        return ResponseEntity.ok("update succeeded");
    }
}
