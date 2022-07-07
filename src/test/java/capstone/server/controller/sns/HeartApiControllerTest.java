package capstone.server.controller.sns;

import capstone.server.basic.WithMockCustomOAuth2Account;
import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.domain.bucket.reactions.Heart;
import capstone.server.dto.bucket.BucketSaveRequestDto;
import capstone.server.dto.sns.HeartDto;
import capstone.server.oauth.entity.ProviderType;
import capstone.server.oauth.entity.RoleType;
import capstone.server.repository.UserRepository;
import capstone.server.repository.bucket.BucketRepository;
import capstone.server.repository.sns.HeartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Assert;
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
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class HeartApiControllerTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    HeartRepository heartRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    BucketRepository bucketRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("좋아요 테스트")
    @WithMockUser
    @Test
    void testCreateLike() throws Exception{

        User user = addUser();

        Bucket bucket = addBucket(user);

        HeartDto heartDto = HeartDto.builder()
                .userSeq(user.getUserSeq())
                .build();

        mockMvc.perform(post("/heart/"+bucket.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(heartDto))
                .with(csrf())).andExpect(status().isOk());

        Heart heart = heartRepository.findAll().get(0);

        assertNotNull(heart);
        assertNotNull(heart.getUser().getUserSeq());
        assertNotNull(heart.getBucket().getId());
    }

    @DisplayName("좋아요 취소 테스트")
    @WithMockUser
    @Test
    void testDuplicatedLike() throws Exception{

        User user = addUser();

        Bucket bucket = addBucket(user);

        HeartDto heartDto = HeartDto.builder()
                .userSeq(user.getUserSeq())
                .build();

        mockMvc.perform(post("/heart/"+bucket.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(heartDto))
                .with(csrf())).andExpect(status().isOk());

        mockMvc.perform(post("/heart/"+bucket.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(heartDto))
                .with(csrf())).andExpect(status().isOk());
        /*
        Heart heart = heartRepository.findAll().get(0);

        assertNotNull(heart);
        assertNotNull(heart.getUser().getUserSeq());
        assertNotNull(heart.getBucket().getId());
        */

        Assert.isTrue(heartRepository.findAll().isEmpty());
    }

    private User addUser(){

        User user = User.builder()
                .email("email@naver.com")
                .username("testname")
                .roleType(RoleType.USER)
                .providerType(ProviderType.KAKAO)
                .profileImageUrl("/test/image")
                .emailVerifiedYn("Y")
                .password("testPass")
                .userId("1")
                .build();

        User save = userRepository.save(user);

        return save;
    }

    private Bucket addBucket(User user){

        Bucket bucket = Bucket.builder()
                .content("버킷")
                .bucketStatus(BucketStatus.ONGOING)
                .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                .user(user)
                .uploadTime(LocalDateTime.now())
                .modifiedTime(LocalDateTime.now())
                .build();

        Bucket save = bucketRepository.save(bucket);

        return save;
    }
}
