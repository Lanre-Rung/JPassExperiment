package lan.pass.demo.controller;

import lan.pass.demo.model.Pass;
import lan.pass.demo.request.PassRequest;
import lan.pass.demo.service.PassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
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
    public ResponseEntity<Object> createPass(@RequestBody PassRequest request) {
        Object pass = passService.createPass(request);
        return ResponseEntity.ok(pass);
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
        // Call the method that now returns byte[] directly
        byte[] passFileContent = passService.getPassFileContent(id); // Assuming this is the new method name

        if (passFileContent != null) {
            // Create an InputStreamResource from the byte array
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(passFileContent));

            // Build the response entity with the resource
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + id + ".pkpass")
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        }

        // Return a bad request response if there's an issue
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<?> getEditContent(@PathVariable("id") Long id) {
        Pass pass = passService.getPassById(id);
        HashMap<String, Object> response = new HashMap<>();
        response.put("passData", pass);
        response.put("fileData", passService.getEditContent(id));
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<Object> editPass(@RequestBody PassRequest request) {
        Object pass = passService.editPass(request, null);
        return ResponseEntity.ok(pass);
    }

    @PutMapping("/multiple")
    public ResponseEntity<Object> editMultiplePasses(@RequestBody PassRequest request) {
        Object result = passService.editMultiplePasses(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public Pass getPassById(@PathVariable Long id) {
        return passService.getPassById(id);
    }

    @GetMapping("/owner_id/{id}")
    public List<Pass> getPassesByOwnerId(@PathVariable("id") Long id, @RequestParam(value = "template", required = false) boolean isTemplate) {
        return passService.selectPassByOwnerId(id, isTemplate);
    }

    @GetMapping("/merchant_id/{id}")
    public List<Pass> getPassesByMerchantId(@PathVariable("id") Long id, @RequestParam(value = "template", required = false) boolean isTemplate) {
        return passService.selectPassByMerchantId(id, isTemplate);
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
    public int deletePass(@PathVariable Long id, @RequestParam("ownerId") Long ownerId) {
        return passService.deletePass(id, ownerId);
    }
}
