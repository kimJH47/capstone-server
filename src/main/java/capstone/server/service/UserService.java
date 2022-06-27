package capstone.server.service;

import capstone.server.domain.User;
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

    public User getUser(String userId) {
        return userRepository.findByUserId(userId)
                             .orElseThrow(() -> new IllegalArgumentException("테이블에 유저가 없습니다"));
    }
}



