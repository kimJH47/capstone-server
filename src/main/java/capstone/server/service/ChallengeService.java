package capstone.server.service;


import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.SubBucketStatus;
import capstone.server.domain.challenge.*;
import capstone.server.dto.challenge.*;
import capstone.server.exception.CustomException;
import capstone.server.exception.ErrorCode;
import capstone.server.repository.UserRepository;
import capstone.server.repository.challenge.ChallengeParticipationRepository;
import capstone.server.repository.challenge.ChallengeRepository;
import capstone.server.repository.challenge.SubChallengeRepository;
import capstone.server.repository.challenge.UserSubChallengeInfoRepository;
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
    private final SubChallengeRepository subChallengeRepository;
    private final UserSubChallengeInfoRepository userSubChallengeInfoRepository;

    @Transactional
    public ChallengeParticipationResponseDto save(ChallengeSaveRequestDto requestDto) {

        User findUser = userRepository.findById(requestDto.getUserId())
                                      .orElseThrow(() -> new IllegalArgumentException("테이블에 유저가 존재하지 않습니다"));
        Challenge challenge = requestDto.toEntity();
        challenge.changeUser(findUser);
        List<String> tagList = requestDto.getTagList();

        if (!tagList.isEmpty()) {
            List<ChallengeTag> collect = tagList.stream()
                                                .map(s -> new ChallengeTag(s))
                                                .collect(Collectors.toList());
            challenge.updateTagList(collect);
        }
        List<SubChallengeSaveRequestDto> subChallengeSaveRequestDtoList = requestDto.getSubChallengeSaveRequestDtoList();
        if (!subChallengeSaveRequestDtoList.isEmpty()) {
            subChallengeSaveRequestDtoList.stream()
                                          .map(SubChallengeSaveRequestDto::toEntity)
                                          .forEach(subChallenge -> {
                                              subChallenge.changeChallenge(challenge);
                                              subChallengeRepository.save(subChallenge);
                                              UserSubChallengeInfo challengeInfo = UserSubChallengeInfo.builder()
                                                                                                       .subChallenge(subChallenge)
                                                                                                       .user(findUser)
                                                                                                       .subBucketStatus(SubBucketStatus.ONGOING)
                                                                                                       .build();
                                              userSubChallengeInfoRepository.save(challengeInfo);
                                          });
        }
        //챌린지참가 정보에 바로 추가하기
        Challenge save = challengeRepository.save(challenge);
        ChallengeParticipation challengeParticipation = challengeParticipationRepository.save(ChallengeParticipation.builder()
                                                                                                  .challenge(save)
                                                                                                  .user(findUser)
                                                                                                  .joinTime(save.getUploadTime())
                                                                                                  .requestTime(save.getUploadTime())
                                                                                                  .joinStatus(JoinStatus.SUCCEEDED)
                                                                                                  .challengeRoleType(ChallengeRoleType.ADMIN)
                                                                                                  .build());

        return new ChallengeParticipationResponseDto(challengeParticipation);

    }


    /**
     * 검증 할 것
     * 1. 챌린지에 인원이 꽉찻는지
     * 2. 챌린지 공개 여부
     */
    @Transactional
    public ChallengeParticipationResponseDto join(ChallengeJoinRequestDto requestDto) {

        Challenge findChallenge = challengeRepository.findById(requestDto.getChallengeId())
                                                     .orElseThrow((() -> new IllegalArgumentException("테이블에 챌린지가 존재하지 않습니다")));

        //챌린지에 참가자리가 없거나 비공개 일 때 예외 던지기(ControllerAdvice)
        if (findChallenge.getChallengePrivacyStatus()
                         .equals(BucketPrivacyStatus.PRIVATE)) {
            throw new CustomException(ErrorCode.CHALLENGE_NOT_PUBLIC);
        } else if (isFullChallengeUsers(findChallenge)) {
            throw new CustomException(ErrorCode.CHALLENGE_FULL_USERS);
        }

        User findUser = userRepository.findById(requestDto.getUserId())
                                      .orElseThrow((() -> new IllegalArgumentException("테이블에 유저가 존재하지 않습니다")));

        ChallengeParticipation save = challengeParticipationRepository.save(ChallengeParticipation.builder()
                                                                                                  .challenge(findChallenge)
                                                                                                  .user(findUser)
                                                                                                  .joinStatus(JoinStatus.WAIT)
                                                                                                  .requestTime(requestDto.getRequestTime())
                                                                                                  .challengeRoleType(ChallengeRoleType.RESERVE)
                                                                                                  .build());
        return new ChallengeParticipationResponseDto(save);


    }

    @Transactional
    public ChallengeParticipationResponseDto updateJoinStatus(ChallengeJoinStatusUpdateDto updateDto) {
        //어드민만 참가정보 변경가능, 참가정보가 바뀌면 ChallengeRoleType 도 같이 변경되야함. dto의 userId 는 변경을 요청하는 User의 Id를 말함(어드민)

        ChallengeParticipation adminarticipation = challengeParticipationRepository.findById(updateDto.getAdminParticipationId())
                                                                                      .orElseThrow(() -> new IllegalArgumentException("테이블에 참가정보가 존재하지 않습니다"));
        if(adminarticipation.getChallengeRoleType().equals(ChallengeRoleType.ADMIN)){

            ChallengeParticipation participation = challengeParticipationRepository.findById(updateDto.getChallengeParticipationId())
                                                                                   .orElseThrow(() -> new IllegalArgumentException("테이블에 참가정보가 존재하지 않습니다"));
            participation.changeJoinStatus(updateDto.getJoinStatus());
            participation.changeRoleType(ChallengeRoleType.MEMBER);

            return new ChallengeParticipationResponseDto(participation);
        }

        throw new CustomException(ErrorCode.CHALLENGE_ROLE_TYPE_NOT_ADIMIN);
    }

    //챌린지 참가가능 여부확인(인원)
    @Transactional(readOnly = true)
    public boolean isFullChallengeUsers(Challenge challenge) {
        Integer maxJoinNum = challenge.getMaxJoinNum();
        return challengeParticipationRepository.findWithPagingByChallengeAndJoinStatus(challenge, JoinStatus.SUCCEEDED,
                                                       PageRequest.of(0, maxJoinNum))
                                               .size() == maxJoinNum;
    }

    //챌린지 참가 유저확인
    @Transactional(readOnly = true)
    public List<ChallengeParticipationResponseDto> findParticipationByChallengeId(Long id) {

        Challenge challenge = challengeRepository.findById(id)
                                                 .orElseThrow(() -> new IllegalArgumentException("테이블에 챌린지가 존재하지 않습니다"));

        List<ChallengeParticipation> allByChallenge = challengeParticipationRepository.findAllByChallenge(challenge);

        return allByChallenge.stream()
                             .map(ChallengeParticipationResponseDto::new)
                             .collect(Collectors.toList());


    }

    @Transactional(readOnly = true)
    public List<ChallengeResponseDto> searchChallenges(ChallengeSearch challengeSearch) {

        if (challengeSearch.getTagList()
                           .isEmpty()) {
            return challengeRepository.searchChallenge(challengeSearch)
                                      .stream()
                                      .map(ChallengeResponseDto::new)
                                      .collect(Collectors.toList());
        }else {
            return challengeRepository.searchToTag(challengeSearch)
                               .stream()
                               .map(ChallengeResponseDto::new).collect(Collectors.toList());
        }
    }

    /**
     *
     * @param id 챌린지 Id
     * @return 챌린지 단건조회 dto
     */
    public ChallengeResponseDto findById(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                                                 .orElseThrow(() -> new IllegalArgumentException("테이블에 해당 챌린지가 존재하지 않습니다"));
        return new ChallengeResponseDto(challenge);
    }
}
