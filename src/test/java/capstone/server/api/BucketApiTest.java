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
        //버킷컨트롤러
        //시큐리티
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
    @DisplayName("버킷저장 dto를 POST 방식으로 요청,저장된 버킷 ID가 반환된다")
    public void 버킷저장() throws Exception{
        //given
        BucketSaveRequestDto saveDto = getBucketSaveRequestDto("버킷내용");
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
                  .isEqualTo("1"); // 서비스로직 신경 x

        then(bucketService).should(times(1))
                           .saveBucket(any(BucketSaveRequestDto.class));


    }

    @Test
    @WithMockCustomOAuth2Account
    @DisplayName("버킷아이디 하나를 GET 요청하면 단건조회가 완료되어야함")
    public void 버킷_단건조회() throws Exception{
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
    @DisplayName("유저 ID를 Get 으로 보내면 해당하는 버킷전체가 조회완료 되어야함")
    public void 유저_버킷_전체조회() throws Exception{
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


    //테스트용 유저 생성

    @Test
    @WithMockCustomOAuth2Account
    @DisplayName("버킷상태 업데이트 dto를 put 하면 업데이트가 완료되어야함")
    public void 버킷상태_업데이트() throws Exception{
        //given
        User user = getUser();
        Bucket bucket = getBucketWithSubBucket(1L, user, "버킷상태업데이트~");

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
    //조회용 버킷생성

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
                             .build();
        bucket.addSubBucket(SubBucket.builder()
                                     .content("세부 꿈나무 야후~")
                                     .modifiedTime(LocalDateTime.now())
                                     .uploadTime(LocalDateTime.now())
                                     .build());
        bucket.addSubBucket(SubBucket.builder()
                                     .content("세부 꿈나무 야후후~~")
                                     .modifiedTime(LocalDateTime.now())
                                     .uploadTime(LocalDateTime.now())
                                     .build());
        return bucket;
    }

    private BucketSaveRequestDto getBucketSaveRequestDto(String content) {
        List<SubBucketSaveRequestDto> list =new ArrayList<>();
        for(int i = 0;i<5;i++) {
            list.add(SubBucketSaveRequestDto.builder()
                                            .content("세부목표1")
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
