package capstone.server.controller.sns;

import capstone.server.dto.sns.MemoDto;
import capstone.server.service.sns.CommentService;
import capstone.server.service.sns.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemoApiController {
    private final MemoService memoService;

    @PostMapping("/memo/{bucketId}")
    public ResponseEntity<String> addMemo(
            @RequestBody @Valid MemoDto memoDto,
            @PathVariable Long bucketId){

        boolean result = false;

        result = memoService.addMemo(memoDto.getUserSeq(),bucketId,memoDto.getContent());

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
