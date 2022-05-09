package capstone.server.controller;


import capstone.server.dto.BucketSaveRequestDto;
import capstone.server.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;



@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class BucketController {

    private final BucketService bucketService;

    @PostMapping("/users")
    public ResponseEntity<?> save(@RequestBody  @Valid BucketSaveRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        bucketService.saveBucket(requestDto);
        return ResponseEntity.ok()
                             .body("버킷리스트 등록완료");

    }

}
