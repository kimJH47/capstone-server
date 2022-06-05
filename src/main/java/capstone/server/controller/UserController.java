package capstone.server.controller;


import capstone.server.service.ImageStorageService;
import capstone.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final ImageStorageService imageStorageService;

    @GetMapping("/users")
    public ResponseEntity<?> findByUsers() {
        return ResponseEntity.ok()
                             .body(userService.findByUsers());
    }

    @PostMapping("/image")
    public ResponseEntity<?> imageSave(@RequestParam("images") List<MultipartFile> multipartFiles) throws IOException {
//        if (bindingResult.hasErrors()) {
//            String defaultMessage = bindingResult.getAllErrors()
//                                                 .get(0)
//                                                 .getDefaultMessage();
//            System.out.println("defaultMessage = " + defaultMessage);
//            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
//        }
        List<String> uploadUrls = imageStorageService.ImageUploadToS3(multipartFiles);
        return ResponseEntity.ok()
                             .body(uploadUrls);
    }

}
