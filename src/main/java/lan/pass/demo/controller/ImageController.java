package lan.pass.demo.controller;

import lan.pass.demo.model.Image;
import lan.pass.demo.request.ImageRequest;
import lan.pass.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    private ImageService imageService;

    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    // Create a new image
    @PostMapping
    public ResponseEntity<Integer> createImage(@RequestBody ImageRequest imageRequest) {
        int affectedRows = imageService.insertImage(imageRequest);
        return new ResponseEntity<>(affectedRows, HttpStatus.CREATED);
    }

    // Get an image by ID
    @GetMapping("/owner_id/{id}")
    public ResponseEntity<?> getImagesByOwnerId(@PathVariable Long id, @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Long pageIndex,
                                                @RequestParam(value = "pageCount", required = false, defaultValue = "10") Long pageCount) {
        List<Image> images = imageService.getImagesByOwnerId(id, pageIndex, pageCount);
        return ResponseEntity.ok(images);
    }

    // Get an image by owner ID
    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable Long id) {
        Image image = imageService.getImageById(id);
        if (image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    // Update an existing image
    @PutMapping
    public ResponseEntity<?> updateImage(@RequestBody ImageRequest imageRequest) {
        int updateResult = imageService.updateImage(imageRequest);
        if (updateResult == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    // Delete an image by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        int deleteResult = imageService.deleteImageById(id);
        if (deleteResult == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}