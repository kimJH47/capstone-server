package capstone.server.controller.sns;

import capstone.server.dto.sns.CommentDto;
import capstone.server.dto.sns.HeartDto;
import capstone.server.service.sns.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/comment/{bucketId}")
    public ResponseEntity<String> addComment(
            @RequestBody @Valid CommentDto commentDto,
            @PathVariable Long bucketId){

        boolean result = false;

        result = commentService.addComment(commentDto.getUserSeq(),bucketId,commentDto.getContent());

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PutMapping("/comment/{bucketId}")
    public ResponseEntity<String> updateComment(
            @RequestBody @Valid CommentDto commentDto,
            @PathVariable Long bucketId){

        boolean result = false;

        result = commentService.updateComment(commentDto.getUserSeq(),commentDto.getCommentId(),commentDto.getContent());

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/comment/{bucketId}")
    public ResponseEntity<String> deleteComment(
            @RequestBody @Valid CommentDto commentDto,
            @PathVariable Long bucketId){

        boolean result = false;

        result = commentService.deleteComment(commentDto.getUserSeq(),commentDto.getCommentId());

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
