package capstone.server.controller;


import capstone.server.domain.User;
import capstone.server.domain.bucket.*;
import capstone.server.dto.bucket.SubBucketSaveRequestDto;
import capstone.server.repository.UserRepository;
import capstone.server.repository.bucket.BucketRepository;
import capstone.server.repository.bucket.SubBucketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class SubBucketApiTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BucketRepository bucketRepository;
    @Autowired
    private SubBucketRepository subBucketRepository;

    @BeforeEach
    public void 테스트유저_버킷_생성() {
        User save = userRepository.save(User.builder()
                                            .username("Test")
                                            .email("emailTest")
                                            .build());
        Bucket bucket = Bucket.builder()
                              .bucketStatus(BucketStatus.ONGOING)
                              .modifiedTime(LocalDateTime.now())
                              .uploadTime(LocalDateTime.now())
                              .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                              .content("버킷제목")
                              .build();
        bucket.changeUser(save);
        bucketRepository.save(bucket);
    }

    @Test
    @DisplayName("서브버킷 저장 API 테스트")
    public void 서브버킷저장() throws Exception {
        //given
        SubBucketSaveRequestDto request = SubBucketSaveRequestDto.builder()
                                                                 .bucketId(1L)
                                                                 .subBucketStatus(SubBucketStatus.ONGOING)
                                                                 .modifiedTime(LocalDateTime.now())
                                                                 .uploadTime(LocalDateTime.now())
                                                                 .content("세부목표 내용입네다")
                                                                 .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/sub-bucket").contentType(MediaType.APPLICATION_JSON)
                                                                             .content(objectMapper.writeValueAsString(request)))
                                             .andExpect(status().isOk())
                                             .andDo(print());
        //then
        resultActions.andExpect(status().isOk());

    }

    @Test
    @DisplayName("버컷아이디를 보내면 서브버킷이 전부 조회되야함")
    public void 서브버킷_조회() throws Exception {
        //given
        Bucket bucket = bucketRepository.findById(1L)
                                        .get();
        for(int i = 1; i<5;i++){

            subBucketRepository.save(SubBucket.builder()
                                              .content("세부목표" + i)
                                              .uploadTime(LocalDateTime.now())
                                              .subBucketStatus(SubBucketStatus.ONGOING)
                                              .modifiedTime(LocalDateTime.now())
                                              .bucket(bucket)
                                               .build());
        }

        //when
        //then
        mockMvc.perform(get("/api/sub-bucket/1").accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andDo(print());


    }
}
