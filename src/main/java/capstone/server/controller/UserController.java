package capstone.server.controller;


import capstone.server.commons.ApiResponse;
import capstone.server.domain.User;
import capstone.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
   //private final ImageStorageService imageStorageService;

    @GetMapping("/users")
    public ResponseEntity<?> findByUsers() {
        return ResponseEntity.ok()
                             .body(userService.findByUsers());
    }
    @GetMapping("/v1/users")
    public ApiResponse getUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                                                                                                                                                 .getAuthentication()
                                                                                                                                                 .getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ApiResponse.success("user", user);
    }
}
