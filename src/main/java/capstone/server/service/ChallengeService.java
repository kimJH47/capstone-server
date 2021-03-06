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
import capstone.server.repository.challenge.SubChallengeParticipationRepository;
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
    private final SubChallengeParticipationRepository subChallengeParticipationRepository;

    @Transactional
    public ChallengeParticipationResponseDto save(ChallengeSaveRequestDto requestDto) {

        User findUser = createUser(requestDto.getUserId());

        Challenge challenge = requestDto.toEntity();
        challenge.changeUser(findUser);
        List<String> tagList = requestDto.getTagList();

        if (!tagList.isEmpty()) {
            List<ChallengeTag> collect = tagList.stream()
                                                .map(ChallengeTag::new)
                                                .collect(Collectors.toList());
            challenge.updateTagList(collect);
        }
        List<SubChallengeSaveRequestDto> subChallengeSaveRequestDtoList = requestDto.getSubChallengeSaveRequestDtoList();

        if (!subChallengeSaveRequestDtoList.isEmpty()) {

            List<SubChallenge> subChallengeList = subChallengeSaveRequestDtoList.stream()
                                                                       .map(SubChallengeSaveRequestDto::toEntity)
                                                                       .collect(Collectors.toList());

            for (SubChallenge subChallenge : subChallengeList) {
                subChallenge.changeChallenge(challenge);
                SubChallenge save = subChallengeRepository.save(subChallenge);
                SubChallengeParticipation subChallengeParticipation = SubChallengeParticipation.create(save, findUser, SubBucketStatus.ONGOING);
                subChallengeParticipationRepository.save(subChallengeParticipation);
            }

        }
        //??????????????? ????????? ?????? ????????????
        Challenge saveChallenge = challengeRepository.save(challenge);

        ChallengeParticipation challengeParticipation =
                challengeParticipationRepository.save(ChallengeParticipation.create(saveChallenge, findUser, JoinStatus.SUCCEEDED, ChallengeRoleType.ADMIN));

        return new ChallengeParticipationResponseDto(challengeParticipation);

    }




    /**
     * ?????? ??? ???
     * 1. ???????????? ????????? ????????????
     * 2. ????????? ?????? ??????
     */
    @Transactional
    public ChallengeParticipationResponseDto join(ChallengeJoinRequestDto requestDto) {

        Challenge findChallenge = challengeRepository.findById(requestDto.getChallengeId())
                                                     .orElseThrow((() -> new IllegalArgumentException("???????????? ???????????? ???????????? ????????????")));

        //???????????? ??????????????? ????????? ????????? ??? ??? ?????? ?????????(ControllerAdvice)
        if (findChallenge.getChallengePrivacyStatus()
                         .equals(BucketPrivacyStatus.PRIVATE)) {
            throw new CustomException(ErrorCode.CHALLENGE_NOT_PUBLIC);
        }
        if (isFullChallengeUsers(findChallenge)) {
            throw new CustomException(ErrorCode.CHALLENGE_FULL_USERS);
        }
        User findUser = createUser(requestDto.getUserId());
        ChallengeParticipation challengeParticipation = ChallengeParticipation.create(findChallenge, findUser, JoinStatus.WAIT, ChallengeRoleType.RESERVE);
        List<SubChallenge> subChallengeList = subChallengeRepository.findByChallenge(findChallenge);

        //????????? ???????????? ????????? ??????????????? ???????????? ??????
        for (SubChallenge subChallenge : subChallengeList) {
            SubChallengeParticipation subChallengeParticipation = SubChallengeParticipation.create(subChallenge, findUser, SubBucketStatus.ONGOING);
            subChallengeParticipationRepository.save(subChallengeParticipation);
        }

        return new ChallengeParticipationResponseDto(challengeParticipationRepository.save(challengeParticipation));

    }

    @Transactional
    public ChallengeParticipationResponseDto updateJoinStatus(ChallengeJoinStatusUpdateDto updateDto) {
        //???????????? ???????????? ????????????, ??????????????? ????????? ChallengeRoleType ??? ?????? ???????????????. dto??? userId ??? ????????? ???????????? User??? Id??? ??????(?????????)

        ChallengeParticipation adminParticipation = challengeParticipationRepository.findById(updateDto.getAdminParticipationId())
                                                                                      .orElseThrow(() -> new IllegalArgumentException("???????????? ??????????????? ???????????? ????????????"));
        if(adminParticipation.getChallengeRoleType().equals(ChallengeRoleType.ADMIN)){

            ChallengeParticipation participation = challengeParticipationRepository.findById(updateDto.getChallengeParticipationId())
                                                                                   .orElseThrow(() -> new IllegalArgumentException("???????????? ??????????????? ???????????? ????????????"));
            participation.changeJoinStatus(updateDto.getJoinStatus());
            participation.changeRoleType(ChallengeRoleType.MEMBER);

            return new ChallengeParticipationResponseDto(participation);
        }
        throw new CustomException(ErrorCode.CHALLENGE_ROLE_TYPE_NOT_ADIMIN);
    }
    //????????? ???????????? ????????????(??????)
    @Transactional(readOnly = true)
    public boolean isFullChallengeUsers(Challenge challenge) {
        Integer maxJoinNum = challenge.getMaxJoinNum();
        return challengeParticipationRepository.findWithPagingByChallengeAndJoinStatus(challenge, JoinStatus.SUCCEEDED,
                                                       PageRequest.of(0, maxJoinNum))
                                               .size() == maxJoinNum;
    }
    //????????? ?????? ????????????
    @Transactional(readOnly = true)
    public List<ChallengeParticipationResponseDto> findParticipationByChallengeId(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                                                 .orElseThrow(() -> new IllegalArgumentException("???????????? ???????????? ???????????? ????????????"));
        List<ChallengeParticipation> allByChallenge = challengeParticipationRepository.findAllByChallenge(challenge);

        return allByChallenge.stream()
                             .map(ChallengeParticipationResponseDto::new)
                             .collect(Collectors.toList());


    }
    //????????? ?????????????????? ??????( RoleType??? ADMIN ??? ????????????)
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
     * @param id ????????? Id
     * @return ????????? ???????????? dto
     */
    public ChallengeResponseDto findById(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                                                 .orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ???????????? ???????????? ????????????"));
        return new ChallengeResponseDto(challenge);
    }

    private User createUser(Long userId) {
        return userRepository.findById(userId)
                             .orElseThrow(() -> new IllegalArgumentException("???????????? ????????? ???????????? ????????????"));
    }
}
