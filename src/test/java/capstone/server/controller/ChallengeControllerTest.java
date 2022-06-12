package capstone.server.controller;

import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.challenge.Challenge;
import capstone.server.domain.challenge.ChallengeParticipation;
import capstone.server.domain.challenge.JoinStatus;
import capstone.server.domain.challenge.RoleType;
import capstone.server.dto.challenge.ChallengeJoinRequestDto;
import capstone.server.dto.challenge.ChallengeJoinStatusUpdateDto;
import capstone.server.dto.challenge.ChallengeSaveRequestDto;
import capstone.server.repository.UserRepository;
import capstone.server.repository.challenge.ChallengeParticipationRepository;
import capstone.server.repository.challenge.ChallengeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class ChallengeControllerTest {


    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private ChallengeParticipationRepository challengeParticipationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void 테스트유저_생성() {
        User save = userRepository.save(User.builder()
                                            .name("Test")
                                            .email("emailTest")
                                            .build());
    }

    @Test
    @DisplayName("챌린지 생성 api 사용시 챌린지 생성이 완료되어야함")
    public void 챌린지생성_테스트() throws Exception{
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
        //when
        //then
        mvc.perform(post("/api/challenge").contentType(MediaType.APPLICATION_JSON)
                                          .content(objectMapper.writeValueAsString(requestDto)))
           .andExpect(status().isOk())
           .andDo(print());

        assertEquals(challengeRepository.findById(1L)
                                                   .stream()
                                                   .map(Challenge::getTitle)
                                                   .collect(Collectors.joining()), requestDto.getTitle());


        List<ChallengeParticipation> all = challengeParticipationRepository.findAll();
        ChallengeParticipation challengeParticipation = all.get(0);

        assertAll(() -> {
            assertEquals(challengeParticipation.getId(),1L);
            assertEquals(challengeParticipation.getRoleType(), RoleType.ADMIN);
            assertEquals(challengeParticipation.getJoinStatus(), JoinStatus.SUCCEEDED);

        });

    }
    @Test
    @DisplayName("챌린지 참가 api 사용시 챌린지 참가가 완료되어야함")
    public void 챌린지참가_테스트() throws Exception{
        //given
        createChallenge(10, BucketPrivacyStatus.PUBLIC, "챌린지 제목", "챌린지 내용");
        ChallengeJoinRequestDto requestDto = ChallengeJoinRequestDto.builder()
                                                               .challengeId(1L)
                                                               .requestTime(LocalDateTime.now())
                                                               .userId(1L)
                                                               .build();
        //when
        //then
        mvc.perform(post("/api/challenge/join").contentType(MediaType.APPLICATION_JSON)
                                               .content(objectMapper.writeValueAsString(requestDto)))
           .andExpect(status().isOk())
           .andDo(print());

        List<ChallengeParticipation> all = challengeParticipationRepository.findAll();
        ChallengeParticipation challengeParticipation = all.get(0);

        assertAll(() -> {
            assertEquals(challengeParticipation.getId(),1L);
            assertEquals(challengeParticipation.getRoleType(), RoleType.MEMBER);
            assertEquals(challengeParticipation.getJoinStatus(), JoinStatus.WAIT);

        });
    }

    /**
     * 예외테스트 작성하기
     * -참가인원이 풀방일 때
     * -챌린지가 비공개일 때
     *
     */
    @Test
    @DisplayName("챌린지 참가인원이 꽉찾을때 예외 테스트")
    public void challengeFullUsers_test() throws Exception{
        //given

        createChallenge(1, BucketPrivacyStatus.PUBLIC, "챌린지 제목", "챌린지 내용");
        ChallengeJoinRequestDto requestDto = ChallengeJoinRequestDto.builder()
                                                                    .challengeId(1L)
                                                                    .requestTime(LocalDateTime.now())
                                                                    .userId(1L)
                                                                    .build();
        //when
        //then
        mvc.perform(post("/api/challenge/join").contentType(MediaType.APPLICATION_JSON)
                                               .content(objectMapper.writeValueAsString(requestDto)))
           .andExpect(status().isOk())
           .andDo(print());
        //조회 Api 만들고 다시 테스트
    }


    @Test
    @DisplayName("챌린지 참가정보 변경 api 사용시 변경되어야함")
    public void 챌린지참가정보변경_테스트() throws Exception {

        //given

        Challenge challenge = createChallenge(5, BucketPrivacyStatus.PUBLIC, "챌린지 제목", "챌린지 내용");
        User save = userRepository.save(User.builder()
                                            .email("email")
                                            .name("참가자")
                                            .build());

        ChallengeParticipation save1 = challengeParticipationRepository.save(ChallengeParticipation.builder()
                                                                                                   .joinStatus(JoinStatus.WAIT)
                                                                                                   .roleType(RoleType.MEMBER)
                                                                                                   .user(save)
                                                                                                   .challenge(challenge)
                                                                                                   .build());
        System.out.println("save1 = " + save1.getId());
        //when
        //then
        ChallengeJoinStatusUpdateDto build = ChallengeJoinStatusUpdateDto.builder()
                                                                         .updateTime(LocalDateTime.now())
                                                                         .challengeParticipationId(1L)
                                                                         .userId(2L)
                                                                         .JoinStatus(JoinStatus.SUCCEEDED)
                                                                         .build();
        mvc.perform(post("/api/challenge/join-status").contentType(MediaType.APPLICATION_JSON)
                                                      .content(objectMapper.writeValueAsString(build)))
           .andExpect(status().isOk())
           .andDo(print());


        System.out.println(" ==========");
        List<ChallengeParticipation> all = challengeParticipationRepository.findAll();
        for (ChallengeParticipation challengeParticipation : all) {
            System.out.println("challengeParticipation.getId() = " + challengeParticipation.getId());
            System.out.println("challengeParticipation.getJoinStatus() = " + challengeParticipation.getJoinStatus());
            
        }
        Assertions.assertEquals(save1.getJoinStatus(), JoinStatus.SUCCEEDED);


    }
    private Challenge createChallenge(int maxJoinNum, BucketPrivacyStatus privacyStatus, String title, String content) {
        User user = userRepository.findById(1L)
                                  .get();
        Challenge challenge = Challenge.builder()
                                   .maxJoinNum(maxJoinNum)
                                   .challengePrivacyStatus(privacyStatus)
                                   .title(title)
                                   .content(content)
                                   .uploadTime(LocalDateTime.now())
                                   .build();
        challenge.changeUser(user);
        return challengeRepository.save(challenge);
    }




}