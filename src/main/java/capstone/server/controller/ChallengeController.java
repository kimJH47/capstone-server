package capstone.server.controller;

import capstone.server.domain.challenge.Challenge;
import capstone.server.dto.challenge.ChallengeJoinRequestDto;
import capstone.server.dto.challenge.ChallengeSaveRequestDto;
import capstone.server.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChallengeController {

    private final ChallengeService challengeService;


    //챌린지 생성
    @PostMapping("/challenge")
    public ResponseEntity<?> createChallenge(@RequestBody @Valid ChallengeSaveRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();

            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        challengeService.save(requestDto);
        return ResponseEntity.ok()
                             .body("챌린지 생성이 완료되었습니다");
    }

    //챌린지 참가 요청
    @PostMapping("/challenge/join")
    public ResponseEntity<?> challengeJoinRequest(@RequestBody @Valid ChallengeJoinRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();

            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        challengeService.join(requestDto);
        return ResponseEntity.ok()
                             .body("챌린지 참가요청이 완료되었습니다");


    }

    //기본 챌린지 조회
    @GetMapping("/challenge")
    public ResponseEntity<?> findAll() {
        List<Challenge> challenges = challengeService.findAll();
    }

    @GetMapping("/challenge/{id}/users")
    public ResponseEntity<?> findChallengeUsersById(@PathVariable Long id) {

    }
    //유저 챌린지 참가 수락(또는 거절)
    //챌린지 참가 여부 조호
    //챌린지 참가 유저 조회


}
