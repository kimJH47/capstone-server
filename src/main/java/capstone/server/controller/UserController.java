package capstone.server.controller;


import capstone.server.service.ImageStorageService;
import capstone.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public ResponseEntity<?> imageSave(@RequestParam("images") MultipartFile multipartFile) throws IOException {
//        if (bindingResult.hasErrors()) {
//            String defaultMessage = bindingResult.getAllErrors()
//                                                 .get(0)
//                                                 .getDefaultMessage();
//            System.out.println("defaultMessage = " + defaultMessage);
//            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
//        }
        String s = imageStorageService.ImageUploadtoS3(multipartFile);
        return ResponseEntity.ok()
                             .body(s);
    }

}
