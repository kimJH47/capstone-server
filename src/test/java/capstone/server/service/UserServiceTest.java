package capstone.server.service;

import capstone.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class UserServiceTest {


    @Autowired
    private UserService service;
    @Autowired
    private UserRepository userRepository;


}