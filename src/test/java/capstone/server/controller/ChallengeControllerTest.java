package capstone.server.controller;

import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.challenge.*;
import capstone.server.dto.challenge.ChallengeJoinRequestDto;
import capstone.server.dto.challenge.ChallengeJoinStatusUpdateDto;
import capstone.server.dto.challenge.ChallengeSaveRequestDto;
import capstone.server.repository.UserRepository;
import capstone.server.repository.challenge.ChallengeParticipationRepository;
import capstone.server.repository.challenge.ChallengeRepository;
import capstone.server.service.ChallengeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @Autowired
    ChallengeService challengeService;

    @BeforeEach
    public void 테스트유저_생성() {
        User save = userRepository.save(User.builder()
                                            .username("Test")
                                            .email("emailTest")
                                            .build());
    }

    @Test
    @DisplayName("챌린지 생성 api 사용시 챌린지 생성이 완료되어야함")
    @WithMockUser
    public void 챌린지생성_테스트() throws Exception{

        ArrayList<String > tags = new ArrayList<>();
        tags.add("여행");
        //given
        ChallengeSaveRequestDto requestDto = ChallengeSaveRequestDto.builder()
                                                                    .content("챌린지 1")
                                                                    .maxJoinNum(2)
                                                                    .uploadTime(LocalDateTime.now())
                                                                    .modifiedTime(LocalDateTime.now())
                                                                    .title("챌린지 제목")
                                                                    .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                                    .userId(1L)
                                                                    .tagList(tags)
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
            assertEquals(challengeParticipation.getChallengeRoleType(), ChallengeRoleType.ADMIN);
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
            assertEquals(challengeParticipation.getChallengeRoleType(), ChallengeRoleType.MEMBER);
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
                                            .username("참가자")
                                            .build());

        ChallengeParticipation save1 = challengeParticipationRepository.save(ChallengeParticipation.builder()
                                                                                                   .joinStatus(JoinStatus.WAIT)
                                                                                                   .challengeRoleType(ChallengeRoleType.MEMBER)
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

    @Test
    @DisplayName("챌린지 검색 테스트")
    public void 챌린지검색API() throws Exception{
        //given
        createChallenge();
        List<String> tagList = new ArrayList<>();
        tagList.add("여행");
        ChallengeSearch challengeSearch = new ChallengeSearch();
        challengeSearch.setTagList(tagList);
        //when
        //then
        mvc.perform(get("/api/challenge/search").contentType(MediaType.APPLICATION_JSON)
                                               .content(objectMapper.writeValueAsString(challengeSearch)))
           .andExpect(status().isOk())
           .andDo(print());

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

    private void createChallenge() {
        ArrayList<String > tags = new ArrayList<>();
        tags.add("여행");
        challengeService.save(ChallengeSaveRequestDto.builder()
                                                     .content("챌린지 1")
                                                     .maxJoinNum(5)
                                                     .uploadTime(LocalDateTime.now())
                                                     .modifiedTime(LocalDateTime.now())
                                                     .title("챌린지 제목")
                                                     .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                     .userId(1L)
                                                     .tagList(tags)
                                                     .build());


        challengeService.save(ChallengeSaveRequestDto.builder()
                                                     .content("챌린지 2")
                                                     .maxJoinNum(5)
                                                     .uploadTime(LocalDateTime.now())
                                                     .modifiedTime(LocalDateTime.now())
                                                     .title("여행")
                                                     .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                     .userId(1L)
                                                     .tagList(tags)
                                                     .build());

        challengeService.save(ChallengeSaveRequestDto.builder()
                                                     .content("챌린지 1")
                                                     .maxJoinNum(5)
                                                     .uploadTime(LocalDateTime.now())
                                                     .modifiedTime(LocalDateTime.now())
                                                     .title("낚시")
                                                     .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                     .userId(1L)
                                                     .tagList(tags)
                                                     .build());

    }

}