package capstone.server.service;


import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.challenge.Challenge;
import capstone.server.domain.challenge.ChallengeParticipation;
import capstone.server.domain.challenge.JoinStatus;
import capstone.server.domain.challenge.RoleType;
import capstone.server.dto.challenge.ChallengeJoinRequestDto;
import capstone.server.dto.challenge.ChallengeSaveRequestDto;
import capstone.server.repository.UserRepository;
import capstone.server.repository.challenge.ChallengeParticipationRepository;
import capstone.server.repository.challenge.ChallengeRepository;
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
        User findUser = userRepository.findById(requestDto.getUserId())
                                      .orElseThrow(() -> new IllegalArgumentException("테이블에 유저가 존재하지 않습니다"));
        Challenge challenge = requestDto.toEntity();
        challenge.changeUser(findUser);

        challengeRepository.save(challenge);


        //챌린지참가 정보에 바로 추가하기
        challengeParticipationRepository.save(ChallengeParticipation.builder()
                                                                    .challenge(challenge)
                                                                    .user(findUser)
                                                                    .joinTime(requestDto.getUploadTime())
                                                                    .requestTime(requestDto.getUploadTime())
                                                                    .joinStatus(JoinStatus.SUCCEEDED)
                                                                    .roleType(RoleType.ADMIN)
                                                                    .build());

    }

    @Transactional
    public void join(ChallengeJoinRequestDto requestDto) {

        Challenge findChallenge = challengeRepository.findById(requestDto.getChallengeId())
                                                     .orElseThrow((() -> new IllegalArgumentException("테이블에 챌린지가 존재하지 않습니다")));
        if (findChallenge.getChallengePrivacyStatus()
                         .equals(BucketPrivacyStatus.PRIVATE) && challengeParticipationRepository.isFullChallengeUsers(findChallenge)) {

            //챌린지에 참가자리가 없거나 비공개 일 때 예외 던지기(ControllerAdvice)
        }
        User findUser = userRepository.findById(requestDto.getUserId())
                                                .orElseThrow((() -> new IllegalArgumentException("테이블에 유저가 존재하지 않습니다")));
        challengeParticipationRepository.save(ChallengeParticipation.builder()
                                                                    .challenge(findChallenge)
                                                                    .user(findUser)
                                                                    .joinStatus(JoinStatus.WAIT)
                                                                    .requestTime(requestDto.getRequestTime())
                                                                    .roleType(RoleType.MEMBER)
                                                                    .build());



        /**
         * 검증 할 것
         * 1. 챌린지에 인원이 꽉찻는지
         * 2. 챌린지 공개 여부
         */


    }
}
