package capstone.server.controller;


import capstone.server.dto.bucket.BucketResponseDto;
import capstone.server.dto.bucket.BucketSaveRequestDto;
import capstone.server.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class BucketController {

    private final BucketService bucketService;

    @PostMapping("/buckets")
    public ResponseEntity<?> save(@RequestBody @Valid BucketSaveRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            System.out.println("BED REQUEST");
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        bucketService.saveBucket(requestDto);
        ResponseEntity<String> responseEntity = ResponseEntity.ok()
                                                          .body("버킷리스트 등록완료");

        return responseEntity;

    }

    @GetMapping("/buckets/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {

        BucketResponseDto findBucket = bucketService.findById(id);


        return ResponseEntity.ok()
                             .body(findBucket);

    }

}
