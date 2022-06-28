package capstone.server.repository;

import capstone.server.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Test
    public void 유저등록_테스트() throws Exception {

        //given
        User save = userRepository.save(User.builder()
                                            .name("Test")
                                            .email("emailTest")
                                            .build());
        //when
        Long id = save.getUserSeq();
        //then

        User byId = userRepository.getById(id);

        System.out.println("byId = " + byId.getName());
        Assertions.assertThat(userRepository.getById(id))
                  .isSameAs(save);

    }
}