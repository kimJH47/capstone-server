package capstone.server.controller;


import capstone.server.dto.bucket.BucketStatusUpdateDto;
import capstone.server.dto.bucket.BucketContentUpdateDto;
import capstone.server.dto.bucket.BucketResponseDto;
import capstone.server.dto.bucket.BucketSaveRequestDto;
import capstone.server.dto.bucket.BucketUpdateDto;
import capstone.server.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
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

    @GetMapping("/buckets/user/{id}")
    public ResponseEntity<?> findBucketsByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                             .body(bucketService.findBucketsByUserId(id));

    }

    @PutMapping("/buckets/{id}/contents")
    public ResponseEntity<?> updateBucketContents(@PathVariable("id") Long id, @Valid @RequestBody BucketContentUpdateDto updateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        bucketService.updateBucketContent(updateDto, id);
        return ResponseEntity.ok()
                             .body("버킷 내용이 업데이트 되었습니다");
    }

    @PutMapping("/buckets/{id}/status")
    public ResponseEntity<?> updateBucketStatus(@PathVariable("id") Long id, @Valid @RequestBody BucketStatusUpdateDto updateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        bucketService.updateBucketStatus(updateDto, id);
        return ResponseEntity.ok()
                             .body("버킷 내용이 업데이트 되었습니다");
    }

    //업데이트 api 를 하나로 처리하는게 정상아닌가?
    @PutMapping("/buckets/{id}/update")
    public ResponseEntity<?> updateBucket(@PathVariable("id") Long id, BucketUpdateDto updateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        bucketService.updateBucket(updateDto, id);
        return  ResponseEntity.ok()
                              .body("버킷이 업데이트 되었습니다");
    }


}
