package capstone.server.controller;

import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.dto.BucketSaveRequestDto;
import capstone.server.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class BucketApiTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("버킷저장 API 테스트")
    public void 버킷저장() throws Exception {
        //given
        User save = userRepository.save(User.builder()
                                            .nickName("Test")
                                            .email("emailTest")
                                            .build());

        BucketSaveRequestDto requestDto = BucketSaveRequestDto.builder()
                                                              .content("버킷이름")
                                                              .userId(1L)
                                                              .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                              .modifiedTime(LocalDateTime.now())
                                                              .uploadTime(LocalDateTime.now())
                                                              .build();


        //when
        ResultActions perform = mockMvc.perform(post("/api/buckets").contentType(MediaType.APPLICATION_JSON)
                                                                    .content(objectMapper.writeValueAsString(requestDto)))
                                       .andExpect(status().isOk())
                                       .andDo(print());
        //then

        perform.andExpect(status().isOk());


    }

}