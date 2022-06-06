package capstone.server.controller;


import capstone.server.service.ImageStorageService;
import capstone.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
