package capstone.server.controller;

import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.challenge.Challenge;
import capstone.server.domain.challenge.ChallengeParticipation;
import capstone.server.domain.challenge.JoinStatus;
import capstone.server.domain.challenge.RoleType;
import capstone.server.dto.challenge.ChallengeSaveRequestDto;
import capstone.server.repository.UserRepository;
import capstone.server.repository.challenge.ChallengeParticipationRepository;
import capstone.server.repository.challenge.ChallengeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
                                            .nickName("Test")
                                            .email("emailTest")
                                            .build());
    }

    @Test
    @DisplayName("챌린지 생성 api 요청시 챌린지 생성이 완료되어야함")
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


}