package capstone.server.controller;


import capstone.server.dto.bucket.*;
import capstone.server.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BucketController {

    private final BucketService bucketService;
   // private final ImageStorageService imageStorageService;

    //서버주소/api/buckets
//    @PostMapping(value = "/buckets",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<?> save(@RequestBody @Valid BucketSaveRequestDto requestDto, BindingResult bindingResult) {
//
//        if (bindingResult.hasErrors()) {
//            String defaultMessage = bindingResult.getAllErrors()
//                                                 .get(0)
//                                                 .getDefaultMessage();
//            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
//        }
//        bucketService.saveBucket(requestDto);
//        ResponseEntity<String> responseEntity = ResponseEntity.ok()
//                                                              .body("버킷리스트 등록완료");
//
//        return responseEntity;
//
//    }

    //버킷저장 + 이미지 저장 api
    @PostMapping("/buckets")
    public ResponseEntity<?> save(@RequestBody @Valid BucketSaveRequestDto requestDto,
                                  BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        System.out.println("requestDto.getContent() = " + requestDto.getContent());
        Long saveId = bucketService.saveBucket(requestDto);
        
        return ResponseEntity.ok()
                             .body(saveId);
    }
    //이미지 저장 api
    @PostMapping("/buckets/{id}/images")
    public ResponseEntity<?> save(@PathVariable("id")Long id,@RequestParam("images") List<MultipartFile> multipartFiles) throws IOException {
        List<String> uploadUrls = new ArrayList<>();
        //uploadUrls =imageStorageService.BucketImageUploadToS3(id, multipartFiles);
        return ResponseEntity.ok()
                             .body(uploadUrls);
    }
    //조회
    @GetMapping("/buckets/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {

        BucketResponseDto findBucket = bucketService.findOne(id);
        return ResponseEntity.ok()
                             .body(findBucket);
    }

    @GetMapping("/buckets/user/{id}")
    public ResponseEntity<?> findBucketsByUserId(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                             .body(bucketService.findBucketsByUserId(id));
    }
    @PutMapping("/buckets/contents")
    public ResponseEntity<?> updateBucketContents(@Valid @RequestBody BucketContentUpdateDto updateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        bucketService.updateBucketContent(updateDto);

        return ResponseEntity.ok()
                             .body("버킷 내용이 업데이트 되었습니다");

    }
    @PutMapping("/buckets/status")
    public ResponseEntity<?> updateBucketStatus( @Valid @RequestBody BucketStatusUpdateDto updateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        bucketService.updateBucketStatus(updateDto);
        return ResponseEntity.ok()
                             .body("버킷 내용이 업데이트 되었습니다");
    }

    @PutMapping("/buckets/{id}/like")
    public ResponseEntity<?> updateBucketLike
            (@PathVariable("id") Long id, @Valid @RequestBody BucketStatusUpdateDto updateDto,BindingResult bindingResult) {
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
