package capstone.server.service;


import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.challenge.Challenge;
import capstone.server.domain.challenge.ChallengeParticipation;
import capstone.server.domain.challenge.JoinStatus;
import capstone.server.domain.challenge.RoleType;
import capstone.server.dto.challenge.ChallengeJoinStatusUpdateDto;
import capstone.server.dto.challenge.ChallengeJoinRequestDto;
import capstone.server.dto.challenge.ChallengeParticipationResponseDto;
import capstone.server.dto.challenge.ChallengeSaveRequestDto;
import capstone.server.exception.CustomException;
import capstone.server.exception.ErrorCode;
import capstone.server.repository.UserRepository;
import capstone.server.repository.challenge.ChallengeParticipationRepository;
import capstone.server.repository.challenge.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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


    /**
     * 검증 할 것
     * 1. 챌린지에 인원이 꽉찻는지
     * 2. 챌린지 공개 여부
     */
    @Transactional
    public void join(ChallengeJoinRequestDto requestDto) {

        Challenge findChallenge = challengeRepository.findById(requestDto.getChallengeId())
                                                     .orElseThrow((() -> new IllegalArgumentException("테이블에 챌린지가 존재하지 않습니다")));
        if (findChallenge.getChallengePrivacyStatus()
                         .equals(BucketPrivacyStatus.PRIVATE) || isFullChallengeUsers(findChallenge)) {
            //챌린지에 참가자리가 없거나 비공개 일 때 예외 던지기(ControllerAdvice)
            throw new CustomException(ErrorCode.CHALLENGE_FULL_USERS);
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





    }

    @Transactional(readOnly = true)
    public boolean isFullChallengeUsers(Challenge challenge) {
        Integer maxJoinNum = challenge.getMaxJoinNum();
        return challengeParticipationRepository.findWithPagingByChallengeAndJoinStatus(challenge, JoinStatus.SUCCEEDED,
                PageRequest.of(0, maxJoinNum)).size()==maxJoinNum;
    }
    @Transactional(readOnly = true)
    public List<Challenge> findAll() {

        return challengeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ChallengeParticipationResponseDto> findUsers(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                                                 .orElseThrow(() -> new IllegalArgumentException("테이블에 챌린지가 존재하지 않습니다"));
        List<ChallengeParticipation> allByChallenge = challengeParticipationRepository.findAllByChallenge(challenge);
        return allByChallenge.stream()
                             .map(ChallengeParticipationResponseDto::new)
                             .collect(Collectors.toList());


    }

    @Transactional
    public void updateJoinStatus(ChallengeJoinStatusUpdateDto updateDto) {
        ChallengeParticipation participation = challengeParticipationRepository.findById(updateDto.getChallengeParticipationId())
                                                                               .orElseThrow(() -> new IllegalArgumentException("테이블에 참가정보가 존재하지 않습니다"));
        participation.changeJoinStatus(updateDto.getJoinStatus());


    }
}
