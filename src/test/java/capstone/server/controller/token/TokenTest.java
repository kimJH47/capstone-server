package capstone.server.controller.token;

import capstone.server.controller.BucketController;
import capstone.server.oauth.service.CustomOAuth2UserService;
import capstone.server.repository.bucket.BucketRepository;
import capstone.server.service.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@Transactional
@AutoConfigureMockMvc
@Slf4j
@WebMvcTest(BucketController.class)
@MockBeans({
        @MockBean(BucketService.class),
        @MockBean(BucketRepository.class),
        @MockBean(CustomOAuth2UserService.class)

})
public class TokenTest {


    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser(username = "ADMIN")
    public void 토큰() throws Exception {
        //given
        //when
        //then

        String clientId = "foo";
        String clientSecret = "bar";
        String username = "id";
        String password = "password";

        ResultActions perform = this.mvc.perform(post("/oauth/token")
                                            .with(httpBasic(clientId, clientSecret))
                                            .with(oauth2Login())
                                            .param("username", username)
                                            .param("password", password)
                                            .param("grant_type", "password"))
                                        .andDo(print());


    }


}
