package capstone.server.controller.token;

import capstone.server.utll.WithMockCustomOAuth2Account;
import capstone.server.controller.BucketController;
import capstone.server.oauth.config.SecurityConfig;
import capstone.server.oauth.config.properties.AppProperties;
import capstone.server.oauth.config.properties.CorsProperties;
import capstone.server.oauth.handler.TokenAccessDeniedHandler;
import capstone.server.oauth.repository.UserRefreshTokenRepository;
import capstone.server.oauth.service.CustomOAuth2UserService;
import capstone.server.oauth.service.CustomUserDetailsService;
import capstone.server.oauth.token.AuthTokenProvider;
import capstone.server.repository.bucket.BucketRepository;
import capstone.server.service.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@Slf4j
@MockBeans({
        @MockBean(BucketService.class),
        @MockBean(BucketRepository.class),
        @MockBean(CustomOAuth2UserService.class),
        @MockBean(CorsProperties.class),
        @MockBean(AppProperties.class),
        @MockBean(AuthTokenProvider.class),
        @MockBean(CustomUserDetailsService.class),
        @MockBean(TokenAccessDeniedHandler.class),
        @MockBean(UserRefreshTokenRepository.class),


})
@WebMvcTest(value = BucketController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
public class TokenTest {


    @Autowired
    MockMvc mvc;

    @Test
    @WithMockCustomOAuth2Account
    public void 토큰() throws Exception {
        //given
        //when
        //then
        mvc.perform(get("/api/buckets/user/1"))
                .andExpect(status().isOk())
           .andDo(print());


    }


}
