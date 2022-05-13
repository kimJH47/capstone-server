package capstone.server.controller;

import capstone.server.dto.challenge.ChallengeSaveRequestDto;
import capstone.server.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChallengeController {

    private final ChallengeService challengeService;


    //챌린지 생성
    @PostMapping("/challenge")
    public ResponseEntity<?> save(@Valid ChallengeSaveRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            System.out.println("BED REQUEST");
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        challengeService.save(requestDto);
        return ResponseEntity.ok()
                             .body("챌린지 생성이 완료되었습니다");
    }
    //챌린지 참가 요청
    //유저 챌린지 참가 수락(또는 거절)
    //챌린지 참가 여부 조호
    //챌린지 참가 유저 조회
    //기본 챌린지 조회

}
