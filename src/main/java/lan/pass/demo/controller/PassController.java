package lan.pass.demo.controller;

import de.brendamour.jpasskit.PKPass;
import lan.pass.demo.model.Pass;
import lan.pass.demo.request.PassRequest;
import lan.pass.demo.service.PassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/pass")
public class PassController {

    PassService passService;

    @Autowired
    public void setPassService(PassService passService){
        this.passService = passService;
    }

    @PostMapping
    public ResponseEntity<PKPass> createPass(@RequestBody PassRequest request) {
        PKPass pass = passService.createPass(request);
        if (pass != null) {
            return ResponseEntity.ok(pass);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public Pass getPassById(@PathVariable Long id) {
        return passService.getPassById(id);
    }

    @GetMapping
    public List<Pass> getAllPasses() {
        return passService.getAllPasses();
    }

    @PutMapping("/{id}")
    public Pass updatePass(@PathVariable Long id, @RequestBody Pass passDetails) {
        return passService.updatePass(id, passDetails);
    }

    @DeleteMapping("/{id}")
    public void deletePass(@PathVariable Long id) {
        passService.deletePass(id);
    }
}
