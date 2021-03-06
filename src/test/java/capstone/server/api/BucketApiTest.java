package capstone.server.api;


import capstone.server.controller.BucketController;
import capstone.server.domain.User;
import capstone.server.domain.bucket.*;
import capstone.server.dto.bucket.BucketResponseDto;
import capstone.server.dto.bucket.BucketSaveRequestDto;
import capstone.server.dto.bucket.BucketStatusUpdateDto;
import capstone.server.dto.bucket.SubBucketSaveRequestDto;
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
import capstone.server.service.BucketService;
import capstone.server.utll.WithMockCustomOAuth2Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@Slf4j
@MockBeans({
        //??????????????????
        //????????????
        @MockBean(CustomOAuth2UserService.class),
        @MockBean(CorsProperties.class),
        @MockBean(AppProperties.class),
        @MockBean(AuthTokenProvider.class),
        @MockBean(CustomUserDetailsService.class),
        @MockBean(TokenAccessDeniedHandler.class),
        @MockBean(UserRefreshTokenRepository.class),
})
@WebMvcTest(value = BucketController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
})
@ExtendWith(MockitoExtension.class)
public class BucketApiTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    BucketService bucketService;


    @Test
    @WithMockCustomOAuth2Account
    @DisplayName("???????????? dto??? POST ???????????? ??????,????????? ?????? ID??? ????????????")
    public void ????????????() throws Exception{
        //given
        BucketSaveRequestDto saveDto = getBucketSaveRequestDto("????????????");
        given(bucketService.saveBucket(any(BucketSaveRequestDto.class))).willReturn(1L);

        //when
        //then
        String request = mvc.perform(post("/api/buckets").contentType(MediaType.APPLICATION_JSON)
                                                         .content(objectMapper.writeValueAsString(saveDto))
                                                         .with(csrf()))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
        Assertions.assertThat(request)
                  .isEqualTo("1"); // ??????????????? ?????? x

        then(bucketService).should(times(1))
                           .saveBucket(any(BucketSaveRequestDto.class));


    }

    @Test
    @WithMockCustomOAuth2Account
    @DisplayName("??????????????? ????????? GET ???????????? ??????????????? ??????????????????")
    public void ??????_????????????() throws Exception{
        //given
        User user = getUser();
        Bucket bucket = getBucketWithSubBucket(1L,user, "bucket content");

        given(bucketService.findOne(anyLong())).willReturn(BucketResponseDto.create(bucket));

        //when
        //then

        mvc.perform(get("/api/buckets/1").accept(MediaType.APPLICATION_JSON))
           .andDo(print())
           .andExpect(status().isOk());

        then(bucketService).should(times(1))
                           .findOne(anyLong());



    }



    @Test
    @WithMockCustomOAuth2Account
    @DisplayName("?????? ID??? Get ?????? ????????? ???????????? ??????????????? ???????????? ????????????")
    public void ??????_??????_????????????() throws Exception{
        //given
        User user = getUser();
        Bucket bucket1 = getBucketWithSubBucket(1L,user, "bucket content1");

        Bucket bucket2 = getBucketWithSubBucket(2L,user, "bucket content2" );

        List<BucketResponseDto> dtos = new ArrayList<>();
        dtos.add(BucketResponseDto.create(bucket1));
        dtos.add(BucketResponseDto.create(bucket2));


        given(bucketService.findBucketsByUserId(anyLong())).willReturn(dtos);
        //when
        //then
        mvc.perform(get("/api/buckets/user/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andDo(print());
        then(bucketService).should(times(1))
                           .findBucketsByUserId(anyLong());
    }


    //???????????? ?????? ??????

    @Test
    @WithMockCustomOAuth2Account
    @DisplayName("???????????? ???????????? dto??? put ?????? ??????????????? ??????????????????")
    public void ????????????_????????????() throws Exception{
        //given
        User user = getUser();
        Bucket bucket = getBucketWithSubBucket(1L, user, "????????????????????????~");

        BucketStatusUpdateDto updateDto = BucketStatusUpdateDto.builder()
                                                               .BucketId(1L)
                                                               .status(BucketStatus.COMPLETED)
                                                               .build();

        given(bucketService.updateBucketStatus(updateDto)).willReturn(bucket.getId());
        //when
        //then
        mvc.perform(put("/api/buckets/status").contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(updateDto))
                                              .with(csrf()))
           .andExpect(status().isOk())
           .andDo(print());

        then(bucketService).should(times(1))
                           .updateBucketStatus(any(BucketStatusUpdateDto.class));
    }

    private User getUser() {
        return User.builder()
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
    //????????? ????????????

    private Bucket getBucketWithSubBucket(long id, User user, String content) {
        Bucket bucket = Bucket.builder()
                              .modifiedTime(LocalDateTime.now())
                              .uploadTime(LocalDateTime.now())
                              .id(id)
                              .bucketStatus(BucketStatus.ONGOING)
                              .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                              .content(content)
                              .user(user)
                              .subBucketList(new ArrayList<>())
                              .targetDate(LocalDateTime.now())
                              .build();
        bucket.addSubBucket(SubBucket.builder()
                                     .content("?????? ????????? ??????~")
                                     .modifiedTime(LocalDateTime.now())
                                     .uploadTime(LocalDateTime.now())
                                     .build());
        bucket.addSubBucket(SubBucket.builder()
                                     .content("?????? ????????? ?????????~~")
                                     .modifiedTime(LocalDateTime.now())
                                     .uploadTime(LocalDateTime.now())
                                     .build());
        return bucket;
    }

    private BucketSaveRequestDto getBucketSaveRequestDto(String content) {
        List<SubBucketSaveRequestDto> list =new ArrayList<>();
        for(int i = 0;i<5;i++) {
            list.add(SubBucketSaveRequestDto.builder()
                                            .content("????????????1")
                                            .subBucketStatus(SubBucketStatus.ONGOING)
                                            .build());
        }
        BucketSaveRequestDto dto = BucketSaveRequestDto.builder()
                                                       .userId(1L)
                                                       .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                       .content(content)
                                                       .subBucketSaveRequestDtoList(list)
                                                       .bucketStatus(BucketStatus.ONGOING)
                                                       .build();
        return dto;
    }
}
