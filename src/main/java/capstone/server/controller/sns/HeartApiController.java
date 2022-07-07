package capstone.server.controller.sns;

import capstone.server.domain.User;
import capstone.server.dto.sns.HeartDto;
import capstone.server.service.sns.HeartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class HeartApiController {
    private final HeartService heartService;

    @PostMapping("/heart/{bucketId}")
    public ResponseEntity<String> addHeart(
            @RequestBody @Valid HeartDto heartDto,
            @PathVariable Long bucketId){

        boolean result = false;

        result = heartService.addHeart(heartDto.getUserSeq(),bucketId);

        return result ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
