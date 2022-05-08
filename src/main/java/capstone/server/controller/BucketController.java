package capstone.server.controller;


import capstone.server.dto.BucketSaveRequestDto;
import capstone.server.service.BucketService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@Api(consumes = )
@Controller
@RequiredArgsConstructor
public class BucketController {

    private final BucketService bucketService;

    @PostMapping("/api/v1/users")
    public ResponseEntity<?> save(@RequestBody @Valid BucketSaveRequestDto requestDto, BindingResult bindingResult) {

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
