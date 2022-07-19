package capstone.server.controller.sns;

import capstone.server.dto.sns.CommentDto;
import capstone.server.dto.sns.HeartDto;
import capstone.server.service.sns.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/comment/{bucketId}")
    public ResponseEntity<String> addHeart(
            @RequestBody @Valid CommentDto commentDto,
            @PathVariable Long bucketId){

        boolean result = false;

        result = commentService.addComment(commentDto.getUserSeq(),bucketId,commentDto.getContent());

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
