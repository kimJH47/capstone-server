package capstone.server.service;

import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.challenge.Challenge;
import capstone.server.dto.challenge.ChallengeJoinRequestDto;
import capstone.server.dto.challenge.ChallengeParticipationResponseDto;
import capstone.server.dto.challenge.ChallengeSaveRequestDto;
import capstone.server.repository.UserRepository;
import capstone.server.repository.challenge.ChallengeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@Transactional
class ChallengeServiceTest {

    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChallengeRepository challengeRepository;

    @BeforeEach
    public void 테스트유저_생성() {

        List<User> collect = Stream.of(1, 2, 3, 4, 5)
                                   .map(integer -> User.builder()

                                                       .email("email@naver.com")
                                                       .nickName("test" + integer)
                                                       .build())
                                   .map(user -> userRepository.save(user))
                                   .collect(Collectors.toList());

    }

    @Test
    @DisplayName("챌린지가 성공저긍로 저장되어야함")
    public void 챌린지저장_테스트() throws Exception{
        //given
        ChallengeSaveRequestDto requestDto = ChallengeSaveRequestDto.builder()
                                                                    .content("챌린지 1")
                                                                    .maxJoinNum(2)
                                                                    .uploadTime(LocalDateTime.now())
                                                                    .modifiedTime(LocalDateTime.now())
                                                                    .title("챌린지 제목")
                                                                    .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                                    .userId(1L)
                                                                    .build();
        challengeService.save(requestDto);
        //when
        List<Challenge> challenges = challengeRepository.findAll();
        //then
        Challenge challenge = challenges.get(0);
        Assertions.assertEquals(challenge.getId(), 1L);
        Assertions.assertEquals(challenge.getTitle(), requestDto.getTitle());


    }



    @Test
    @DisplayName("챌린지에 참가(또는 대기) 유저들을 조회하는 테스트")
    public void 챌린지참가_유저_조회테스트() throws Exception{
        //given
        createChallenge();
        List<User> all = userRepository.findAll();

        List<ChallengeJoinRequestDto> dtos = Stream.of(1, 2, 3, 4, 5)
                                                      .map(value -> ChallengeJoinRequestDto.builder()
                                                                                           .userId(Long.valueOf(value))
                                                                                           .requestTime(LocalDateTime.now())
                                                                                           .challengeId(1L)
                                                                                           .build())
                                                      .collect(Collectors.toList());

        //when
        for (ChallengeJoinRequestDto dto : dtos) {
            challengeService.join(dto);
        }
        List<ChallengeParticipationResponseDto> users = challengeService.findUsers(1L);
        //then
        for (ChallengeParticipationResponseDto user : users) {
            System.out.println(user.getUserId());
            System.out.println(user.getChallengeId());
            System.out.println(user.getJoinStatus());

        }

    }

    @Test
    @DisplayName("챌린지에 남은자리가 있는지 확인하는 테스트,boolean 으로 반환됨")
    public void challengeFullUsers_test() throws Exception {

        //given

        //when

        //then
    }

    private void createChallenge() {
        challengeService.save(ChallengeSaveRequestDto.builder()
                                                     .content("챌린지 1")
                                                     .maxJoinNum(5)
                                                     .uploadTime(LocalDateTime.now())
                                                     .modifiedTime(LocalDateTime.now())
                                                     .title("챌린지 제목")
                                                     .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                     .userId(1L)
                                                     .build());
    }
}