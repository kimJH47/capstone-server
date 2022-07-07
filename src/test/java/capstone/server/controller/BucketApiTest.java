package capstone.server.controller;

import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.dto.bucket.BucketContentUpdateDto;
import capstone.server.dto.bucket.BucketSaveRequestDto;
import capstone.server.repository.UserRepository;
import capstone.server.repository.bucket.BucketRepository;
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
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class BucketApiTest {
/*

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BucketRepository bucketRepository;

    @BeforeEach
    public void 테스트유저생성() {
        User save = userRepository.save(User.builder()
                                            .username("Test")
                                            .email("emailTest")
                                            .build());
    }
    @Test
    @DisplayName("버킷저장 API 테스트")
    public void 버킷저장() throws Exception {

        //given


        BucketSaveRequestDto requestDto = BucketSaveRequestDto.builder()
                                                              .content("버킷이름")
                                                              .userId(1L)
                                                              .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                              .modifiedTime(LocalDateTime.now())
                                                              .uploadTime(LocalDateTime.now())
                                                              .build();


        //when
        //then
        ResultActions resultActions = mockMvc.perform(post("/api/buckets").contentType(MediaType.APPLICATION_JSON)
                                                                          .content(objectMapper.writeValueAsString(requestDto)))
                                             .andExpect(status().isOk())
                                             .andDo(print());

        String s = bucketRepository.findById(1L)
                                   .stream()
                                   .map(Bucket::getContent)
                                   .collect(Collectors.joining());
        Assertions.assertEquals(s,"버킷이름");



    }

    @Test
    @DisplayName("유저 아이디로 버킷들이 조회되어야함")
    public void 버킷전체조희() throws Exception{
        //given
        User user = userRepository.findById(1L)
                                  .get();

        for (int i = 0; i < 10; i++) {
            Bucket bucket1 = Bucket.builder()
                                   .content("버킷"+i)
                                   .bucketStatus(BucketStatus.ONGOING)
                                   .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                   .user(user)
                                   .uploadTime(LocalDateTime.now())
                                   .modifiedTime(LocalDateTime.now())
                                   .build();
            bucketRepository.save(bucket1);
        }
        //when
        //then
        mockMvc.perform(get("/api/buckets/user/1"))
               .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void bucket_content_update_test() throws Exception{
        //given
        User user = userRepository.findById(1L)
                                  .get();

        for (int i = 0; i < 10; i++) {
            Bucket bucket1 = Bucket.builder()
                                   .content("버킷"+i)
                                   .bucketStatus(BucketStatus.ONGOING)
                                   .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                   .user(user)
                                   .uploadTime(LocalDateTime.now())
                                   .modifiedTime(LocalDateTime.now())
                                   .build();
            bucketRepository.save(bucket1);
        }
        //when
        //then
        BucketContentUpdateDto build = BucketContentUpdateDto.builder()
                                                             .content("수정된버킷")
                                                             .updateTime(LocalDateTime.now())
                                                             .build();
        mockMvc.perform(put("/api/buckets/1/contents").contentType(MediaType.APPLICATION_JSON)
                                                      .content(objectMapper.writeValueAsString(build)))
               .andExpect(status().isOk())
               .andDo(print());
        String content = bucketRepository.findById(1L)
                                         .get()
                                         .getContent();
        Assertions.assertEquals(content, "수정된버킷");
    }

*/
}