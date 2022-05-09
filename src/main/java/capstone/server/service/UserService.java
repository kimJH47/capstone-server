package capstone.server.service;

import capstone.server.domain.User;
import capstone.server.domain.UserRole;
import capstone.server.dto.UserSaveRequestDto;
import capstone.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<User> findByUsers() {
        return userRepository.findAll();
    }


    @Transactional
    public Long save(UserSaveRequestDto requestDto) {
        //파라미터를 dto 쓸지 유저를 쓸지 고민
        User user = User.builder()
                        .email(requestDto.getEmail())
                        .nickName(requestDto.getNickName())
                        .userRole(UserRole.GUEST)
                        .build();

        return userRepository.save(user).getId();


    }
}
