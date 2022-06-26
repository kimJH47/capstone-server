package capstone.server.service;

import capstone.server.domain.User;
import capstone.server.dto.UserSaveRequestDto;
import capstone.server.oauth.entity.RoleType;
import capstone.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class UserServiceTest {


    @Autowired
    private UserService service;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void 유저등록_테스트() throws Exception{
        //Oauth x
        //given


        UserSaveRequestDto dto = new UserSaveRequestDto("myName", "kmr2644@gmail.com");

        //when
        Long saveId = service.save(dto);

        User findUser = userRepository.findById(saveId)
                                   .orElse(new User());
        //then
        assertThat(findUser.getName()).isEqualTo("myName");
        assertThat(findUser.getRoleType()).isEqualTo(RoleType.GUEST);
        assertThat(findUser.getEmail()).isEqualTo("kmr2644@gmail.com");
    }

}