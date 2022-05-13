package capstone.server.service;


import capstone.server.domain.User;
import capstone.server.domain.challenge.Challenge;
import capstone.server.dto.challenge.ChallengeJoinRequestDto;
import capstone.server.dto.challenge.ChallengeSaveRequestDto;
import capstone.server.repository.challenge.ChallengeParticipationRepository;
import capstone.server.repository.challenge.ChallengeRepository;
import capstone.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final ChallengeParticipationRepository challengeParticipationRepository;

    @Transactional
    public void save(ChallengeSaveRequestDto requestDto) {
        Challenge challenge = requestDto.toEntity();
        User findUser = userRepository.findById(requestDto.getUserId())
                                      .orElseThrow(() -> new IllegalArgumentException("테이블에 유저가 존재하지 않습니다"));
        challenge.changeUser(findUser);

        challengeRepository.save(challenge);
        //챌린지참가 정보에 바로 추가하기


    }

    @Transactional
    public void join(Long challengeId, ChallengeJoinRequestDto requestDto) {
        /**
         * 검증 할 것
         * 1. 챌린지에 인원이 꽉찻는지
         * 2. 챌린지 공개 여부
         */

    }
}
