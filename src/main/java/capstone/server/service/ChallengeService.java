package capstone.server.service;


import capstone.server.domain.User;
import capstone.server.domain.challenge.Challenge;
import capstone.server.dto.challenge.ChallengeSaveRequestDto;
import capstone.server.repository.ChallengeRepository;
import capstone.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;

    public void save(ChallengeSaveRequestDto requestDto) {
        Challenge challenge = requestDto.toEntity();
        User findUser = userRepository.findById(requestDto.getUserId())
                                      .orElseThrow(() -> new IllegalArgumentException("테이블에 유저가 존재하지 않습니다"));
        challenge.changeUser(findUser);

        challengeRepository.save(challenge);
        //챌린지참가 정보에 바로 추가하기


    }
}
