package capstone.server.oauth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
//시큐리티 활성화
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //private final CustomOAuth2UserService customOAuth2UserService;


    //config 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {


    }
}
