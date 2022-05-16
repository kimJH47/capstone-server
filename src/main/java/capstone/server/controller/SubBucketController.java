package capstone.server.controller;


import capstone.server.dto.bucket.SubBucketResponseDto;
import capstone.server.dto.bucket.SubBucketSaveRequestDto;
import capstone.server.service.SubBucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@Controller
public class SubBucketController {

    private final SubBucketService subBucketService;

    @PostMapping("/sub-bucket")
    public ResponseEntity<?> save(@RequestBody @Valid SubBucketSaveRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }

        subBucketService.saveSubBucket(requestDto);
        ResponseEntity<String> responseEntity = ResponseEntity.ok()
                                                              .body("서브 버킷리스트 등록완료");
        return responseEntity;
    }

    @GetMapping("/sub-bucket/{bucket-id}")
    public ResponseEntity<?> findByBucketId(@PathVariable("bucket-id") Long id) {
        List<SubBucketResponseDto> responseDtos = subBucketService.findByBucketId(id);
        return ResponseEntity.ok()
                             .body(responseDtos);
    }
}
