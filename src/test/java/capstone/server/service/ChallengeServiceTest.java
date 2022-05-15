package capstone.server.service;

import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.challenge.Challenge;
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
        User save = userRepository.save(User.builder()
                                            .nickName("Test")
                                            .email("emailTest")
                                            .build());
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
}