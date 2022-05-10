package capstone.server.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class BucketServiceTest {

    @Autowired
    private BucketService bucketService;
    @Autowired
    private UserService userService;




    @Test
    @DisplayName("버킷등록이 완료되어야함")

    public void 버킷등록() throws Exception {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("버킷아이디 하나를 보내면 버킷조회가 완료되어야함")
    public void 버킷조회() throws Exception {
        //given

        //when

        //then
    }

}