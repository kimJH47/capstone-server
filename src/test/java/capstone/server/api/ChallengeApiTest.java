package capstone.server.api;


import capstone.server.controller.ChallengeController;
import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.domain.challenge.*;
import capstone.server.dto.challenge.*;
import capstone.server.oauth.config.SecurityConfig;
import capstone.server.oauth.config.properties.AppProperties;
import capstone.server.oauth.config.properties.CorsProperties;
import capstone.server.oauth.entity.ProviderType;
import capstone.server.oauth.entity.RoleType;
import capstone.server.oauth.handler.TokenAccessDeniedHandler;
import capstone.server.oauth.repository.UserRefreshTokenRepository;
import capstone.server.oauth.service.CustomOAuth2UserService;
import capstone.server.oauth.service.CustomUserDetailsService;
import capstone.server.oauth.token.AuthTokenProvider;
import capstone.server.service.ChallengeService;
import capstone.server.utll.WithMockCustomOAuth2Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Slf4j
@MockBeans({
        //?????????????????????
        //????????????
        @MockBean(CustomOAuth2UserService.class),
        @MockBean(CorsProperties.class),
        @MockBean(AppProperties.class),
        @MockBean(AuthTokenProvider.class),
        @MockBean(CustomUserDetailsService.class),
        @MockBean(TokenAccessDeniedHandler.class),
        @MockBean(UserRefreshTokenRepository.class),
})
@WebMvcTest(value = ChallengeController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
})
@ExtendWith(MockitoExtension.class)
public class ChallengeApiTest {


    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    ChallengeService challengeService;


    @Test
    @WithMockCustomOAuth2Account
    @DisplayName("????????? ??????dto ??? post ??? ????????? ????????? ?????? ??? ???????????? dto ??? ???????????????")
    public void ???????????????() throws Exception {
        //given
        User user = getUser();
        //???????????? ???????????????..
        Challenge challenge = getChallengeWithTag(1L, "????????????", "????????? content", user);

        ArrayList<String> list = new ArrayList<>();
        list.add("??????");
        list.add("????????????");
        list.add("??????");

        ChallengeSaveRequestDto requestDto = ChallengeSaveRequestDto.builder()
                                                                    .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                                    .tagList(list)
                                                                    .content("?????????~")
                                                                    .maxJoinNum(10)
                                                                    .title("????????????")
                                                                    .userId(1L)
                                                                    .build();

        ChallengeParticipationResponseDto responseDto = ChallengeParticipationResponseDto.builder()
                                                                                         .challengeRoleType(ChallengeRoleType.ADMIN)
                                                                                         .requestTime(challenge.getUploadTime())
                                                                                         .challengeId(challenge.getId())
                                                                                         .joinStatus(JoinStatus.SUCCEEDED)
                                                                                         .userName(user.getUsername())
                                                                                         .build();

        given(challengeService.save(any(ChallengeSaveRequestDto.class))).willReturn(responseDto);

        //when
        //then
        mvc.perform(post("/api/challenges").contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(requestDto))
                                           .with(csrf()))
           .andExpect(status().isOk())
           .andDo(print());
        then(challengeService).should(times(1))
                              .save(any(ChallengeSaveRequestDto.class));

    }
    @Test
    @DisplayName("????????? ??????????????? post ?????? ????????????dto??? ??????????????????")
    @WithMockCustomOAuth2Account
    public void ?????????_??????() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        ChallengeJoinRequestDto requestDto = ChallengeJoinRequestDto.builder()
                                                                    .challengeId(1L)
                                                                    .userId(1L)
                                                                    .requestTime(now)
                                                                    .build();
        ChallengeParticipationResponseDto responseDto = ChallengeParticipationResponseDto.builder()
                                                                                         .challengeId(1L)
                                                                                         .requestTime(now)
                                                                                         .userName("testname")
                                                                                         .joinStatus(JoinStatus.WAIT)
                                                                                         .challengeRoleType(ChallengeRoleType.RESERVE)
                                                                                         .build();

        given(challengeService.join(any(ChallengeJoinRequestDto.class))).willReturn(responseDto);
        //when
        //then
        mvc.perform(post("/api/challenges/join").contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(requestDto))
                                                .with(csrf()))
           .andExpect(status().isOk())
           .andDo(print());
        then(challengeService).should(times(1))
                              .join(any(ChallengeJoinRequestDto.class));


    }

    @Test
    @DisplayName("????????? Id ??? GET ?????? ????????? ???????????? ????????? ????????? ??????????????????")
    @WithMockCustomOAuth2Account
    public void ?????????_????????????() throws Exception{
        //given
        Challenge challengeWithTag = getChallengeWithTag(1L, "???????????? ?????????", "??????", getUser());
        ChallengeResponseDto responseDto = new ChallengeResponseDto(challengeWithTag);

        given(challengeService.findById(anyLong())).willReturn(responseDto);

        //when
        //then
        mvc.perform(get("/api/challenges/1").contentType(MediaType.APPLICATION_JSON))
           .andDo(print())
           .andExpect(status().isOk());

        then(challengeService).should(times(1))
                              .findById(anyLong());
    }
    @Test
    @DisplayName("????????? ???????????? dto??? post ?????? ????????? ?????? ????????? ????????? ??????????????? ??????????????????")
    @WithMockCustomOAuth2Account
    public void ????????????_??????() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        ChallengeParticipationResponseDto responseDto = ChallengeParticipationResponseDto.builder()
                                                                                         .id(1L)
                                                                                         .challengeId(1L)
                                                                                         .requestTime(now)
                                                                                         .userName("testname")
                                                                                         .joinStatus(JoinStatus.SUCCEEDED)
                                                                                         .challengeRoleType(ChallengeRoleType.MEMBER)
                                                                                         .build();

        ChallengeJoinStatusUpdateDto updateDto = new ChallengeJoinStatusUpdateDto(1L, 1L, 1L, 2L, JoinStatus.SUCCEEDED);
        given(challengeService.updateJoinStatus(any(ChallengeJoinStatusUpdateDto.class))).willReturn(responseDto);


        //when
        //then
        mvc.perform(post("/api/challenges/join-status").contentType(MediaType.APPLICATION_JSON)
                                                       .content(objectMapper.writeValueAsString(updateDto))
                                                       .with(csrf()))
           .andExpect(status().isOk())
           .andDo(print());
        then(challengeService).should(times(1))
                              .updateJoinStatus(any(ChallengeJoinStatusUpdateDto.class));

    }

    //????????? ?????? ???????????????
    @Test
    @DisplayName("????????? ???????????? GET ?????? ?????? ???????????? ?????????????????? ??????????????????")
    @WithMockCustomOAuth2Account
    public void ?????????_????????????_??????() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.now();
        List<ChallengeParticipationResponseDto> dtos = new ArrayList<>();
        dtos.add(ChallengeParticipationResponseDto.builder()
                                                  .id(1L)
                                                  .challengeId(1L)
                                                  .requestTime(now)
                                                  .userName("testname")
                                                  .joinStatus(JoinStatus.SUCCEEDED)
                                                  .challengeRoleType(ChallengeRoleType.MEMBER)
                                                  .build());
        dtos.add(ChallengeParticipationResponseDto.builder()
                                                  .id(2L)
                                                  .challengeId(1L)
                                                  .requestTime(now)
                                                  .userName("testname")
                                                  .joinStatus(JoinStatus.SUCCEEDED)
                                                  .challengeRoleType(ChallengeRoleType.MEMBER)
                                                  .build());

        given(challengeService.findParticipationByChallengeId(1L)).willReturn(dtos);
        //when
        //then
        mvc.perform(get("/api/challenges/1/users").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andDo(print());
        then(challengeService).should(times(1))
                              .findParticipationByChallengeId(anyLong());
    }
    @Test
    @DisplayName("????????? ?????? dto??? post ?????? ???????????? ????????? ?????? ???????????? ??????????????????")
    @WithMockCustomOAuth2Account
    public void ?????????_??????() throws Exception{
        //given
        User user = getUser();
        List<ChallengeResponseDto> list = new ArrayList<>();

        list.add(new ChallengeResponseDto(getChallengeWithTag(1L, "????????? ?????????1", "???????????????1", user)));
        list.add(new ChallengeResponseDto(getChallengeWithTag(2L, "????????? ?????????2", "???????????????2", user)));
        list.add(new ChallengeResponseDto(getChallengeWithTag(3L, "????????? ?????????3", "???????????????3", user)));

        ChallengeSearch challengeSearch = new ChallengeSearch();
        List<String> tagList = new ArrayList<>();
        tagList.add("??????");
        tagList.add("??????");
        challengeSearch.setTagList(tagList);
        challengeSearch.setTitle("?????????");

        given(challengeService.searchChallenges(any(ChallengeSearch.class))).willReturn(list);
        //when
        //then
        mvc.perform(post("/api/challenges/search").contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(challengeSearch))
                                                  .with(csrf()))
           .andDo(print())
           .andExpect(status().isOk());
        then(challengeService).should(times(1))
                              .searchChallenges(any(ChallengeSearch.class));

    }

    private Challenge getChallengeWithTag(Long id, String title, String content, User user) {
        Challenge challenge = Challenge.builder()
                                       .tagList(new ArrayList<>())
                                       .uploadTime(LocalDateTime.now())
                                       .modifiedTime(LocalDateTime.now())
                                       .maxJoinNum(10)
                                       .challengeStatus(BucketStatus.ONGOING)
                                       .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                       .id(id)
                                       .title(title)
                                       .content(content)
                                       .user(user)
                                       .build();
        ChallengeTag challengeTag1 = new ChallengeTag(1L, "??????", challenge);
        ChallengeTag challengeTag2 = new ChallengeTag(2L, "????????????", challenge);
        ChallengeTag challengeTag3 = new ChallengeTag(3L, "??????", challenge);
        List<ChallengeTag> challengeTagList = new ArrayList<>();
        challengeTagList.add(challengeTag1);
        challengeTagList.add(challengeTag2);
        challengeTagList.add(challengeTag3);
        challenge.updateTagList(challengeTagList);

        return challenge;
    }

    private User getUser() {
        return User.builder()
                   .userSeq(1L)
                   .email("email@naver.com")
                   .username("testname")
                   .roleType(RoleType.USER)
                   .providerType(ProviderType.KAKAO)
                   .profileImageUrl("/test/image")
                   .emailVerifiedYn("Y")
                   .password("testPass")
                   .userId("1")
                   .build();
    }
}
