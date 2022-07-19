package capstone.server.controller.sns;

import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.domain.bucket.reactions.Comment;
import capstone.server.domain.bucket.reactions.Heart;
import capstone.server.dto.sns.CommentDto;
import capstone.server.dto.sns.HeartDto;
import capstone.server.oauth.entity.ProviderType;
import capstone.server.oauth.entity.RoleType;
import capstone.server.repository.UserRepository;
import capstone.server.repository.bucket.BucketRepository;

import capstone.server.repository.sns.CommentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
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
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class CommentApiControllerTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    BucketRepository bucketRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("커멘트 1개 테스트")
    @WithMockUser
    @Test
    void testCreateComment() throws Exception{

        User user = addUser();

        Bucket bucket = addBucket(user);

        String test = "테스트 문구";

        CommentDto commentDto = CommentDto.builder()
                .userSeq(user.getUserSeq())
                .content(test)
                .build();

        mockMvc.perform(post("/comment/"+bucket.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto))
                .with(csrf())).andExpect(status().isOk());

        Comment comment = commentRepository.findAll().get(0);

        assertNotNull(comment);
        assertNotNull(comment.getUser().getUserSeq());
        Assertions.assertThat(comment.getContent()).isEqualTo(test);
        assertNotNull(comment.getBucket().getId());
    }

    @DisplayName("다중 커멘트 테스트")
    @WithMockUser
    @Test
    void testCreateMultipleComment() throws Exception{

        User user = addUser();

        Bucket bucket = addBucket(user);

        String test = "테스트 문구";

        CommentDto commentDto = CommentDto.builder()
                .userSeq(user.getUserSeq())
                .content(test)
                .build();

        mockMvc.perform(post("/comment/"+bucket.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto))
                .with(csrf())).andExpect(status().isOk());
        mockMvc.perform(post("/comment/"+bucket.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto))
                .with(csrf())).andExpect(status().isOk());

        List<Comment> comments = commentRepository.findAll();


        Assertions.assertThat(comments.size()).isEqualTo(2);
        Assertions.assertThat(comments.get(0).getId()).isNotEqualTo(comments.get(1).getId());
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
