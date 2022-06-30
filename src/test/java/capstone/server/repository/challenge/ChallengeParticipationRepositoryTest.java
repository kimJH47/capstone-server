package capstone.server.repository.challenge;

import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.challenge.Challenge;
import capstone.server.domain.challenge.ChallengeParticipation;
import capstone.server.domain.challenge.JoinStatus;
import capstone.server.dto.challenge.ChallengeSaveRequestDto;
import capstone.server.repository.UserRepository;
import capstone.server.service.ChallengeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
@Transactional
class ChallengeParticipationRepositoryTest {


    @Autowired
    private ChallengeParticipationRepository challengeParticipationRepository;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    public void 참가장보조회_테스트() throws Exception{
        //given
        createUser();
        createChallenge();
        Challenge challenge = challengeRepository.findById(1L)
                                                 .get();
        //when
        List<ChallengeParticipation> finds = challengeParticipationRepository.findWithPagingByChallengeAndJoinStatus(challenge, JoinStatus.SUCCEEDED, PageRequest.of(0, 5));

        //then
        ChallengeParticipation challengeParticipation = finds.get(0);
        int size = finds.size();
        System.out.println("size = " + size);
        System.out.println("challengeParticipation = " + challengeParticipation.getChallenge().getTitle());


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

    private void createUser() {
        userRepository.save(User.builder()
                                .username("test")
                                .email("mail")
                                .build());
    }
    
}