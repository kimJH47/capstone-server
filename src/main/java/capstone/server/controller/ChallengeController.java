package capstone.server.controller;

import capstone.server.domain.challenge.ChallengeSearch;
import capstone.server.dto.challenge.*;
import capstone.server.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChallengeController {

    private final ChallengeService challengeService;


    //챌린지 생성
    @PostMapping("/challenges")
    public ResponseEntity<?> createChallenge(@RequestBody @Valid ChallengeSaveRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();

            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        ChallengeParticipationResponseDto save = challengeService.save(requestDto);
        return ResponseEntity.ok()
                             .body(save);
    }

    //챌린지 참가 요청
    @PostMapping("/challenges/join")
    public ResponseEntity<?> challengeJoinRequest(@RequestBody @Valid ChallengeJoinRequestDto requestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        ChallengeParticipationResponseDto responseDto = challengeService.join(requestDto);
        System.out.println("responseDto = " + responseDto.getUserName());
        return ResponseEntity.ok()
                             .body(responseDto);

    }

    //기본 챌린지 조회
/*    @GetMapping("/challenge")
    public ResponseEntity<?> findAll() {
        List<Challenge> challenges = challengeService.findAll();
        return null;
    }*/

    /**
     * 챌린지에 참가하는 유저 조회
     *
     * @param id 챌린지 ID
     * @return 조회된 참가정보 리스트
     * <p>
     * 추가할것 :
     * -동적쿼리이용 : 하는 참가상태를 바디로 받아서 api 하나로 처리하기.
     */
    @GetMapping("/challenges/{id}/users")
    public ResponseEntity<?> findChallengeParticipation(@PathVariable Long id) {
        List<ChallengeParticipationResponseDto> responseDtos = challengeService.findParticipationByChallengeId(id);
        return ResponseEntity.ok()
                             .body(responseDtos);
    }

    @PostMapping("/challenges/join-status")
    public ResponseEntity<?> updateJoinStatus(@RequestBody @Valid ChallengeJoinStatusUpdateDto updateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        ChallengeParticipationResponseDto responseDto = challengeService.updateJoinStatus(updateDto);
        return ResponseEntity.ok()
                             .body(responseDto);
    }

    //챌린지 검색
    @PostMapping("/challenges/search")
    public ResponseEntity<?> searchChallenge(@RequestBody @Valid ChallengeSearch challengeSearch,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String defaultMessage = bindingResult.getAllErrors()
                                                 .get(0)
                                                 .getDefaultMessage();
            return new ResponseEntity<>(defaultMessage, HttpStatus.BAD_REQUEST);
        }
        List<ChallengeResponseDto> challengeResponseDtos = challengeService.searchChallenges(challengeSearch);
        return ResponseEntity.ok()
                             .body(challengeResponseDtos);
    }
    //유저 챌린지 참가 수락(또는 거절)
    //챌린지 참가 여부 조회



    //챌린지 단건조회
    @GetMapping("/challenges/{id}")
    public ResponseEntity<?> findOne(@PathVariable("id") Long id) {

        ChallengeResponseDto responseDto = challengeService.findById(id);

        return ResponseEntity.ok()
                             .body(responseDto);
    }



}
