package capstone.server.service;

import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.domain.challenge.*;
import capstone.server.dto.challenge.ChallengeParticipationResponseDto;
import capstone.server.dto.challenge.ChallengeResponseDto;
import capstone.server.dto.challenge.ChallengeSaveRequestDto;
import capstone.server.oauth.entity.ProviderType;
import capstone.server.oauth.entity.RoleType;
import capstone.server.repository.UserRepository;
import capstone.server.repository.challenge.ChallengeParticipationRepository;
import capstone.server.repository.challenge.ChallengeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class  ChallengeServiceTest {


    @InjectMocks
    private ChallengeService challengeService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChallengeRepository challengeRepository;
    @Mock
    private ChallengeParticipationRepository challengeParticipationRepository;


    @BeforeEach
    public void init() {
        User user = User.builder()
                        .email("email@naver.com")
                        .username("testname")
                        .roleType(RoleType.USER)
                        .providerType(ProviderType.KAKAO)
                        .profileImageUrl("/test/image")
                        .emailVerifiedYn("Y")
                        .password("testPass")
                        .userId("1")
                        .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    }
    @Test
    @DisplayName("챌린지 저장 dto를 받으면 챌린지 생성 후 저장을 완료해야함")
    public void 챌린지_저장() throws Exception{
        //given
        User user = getUser();
        Challenge challenge = getChallengeWithTag(1L, "새로운 챌린지", "내용드갑니다", user);
        ChallengeParticipation challengeParticipation = getChallengeParticipation(user, challenge, 1L, ChallengeRoleType.ADMIN, JoinStatus.SUCCEEDED);
        ChallengeSaveRequestDto requestDto = ChallengeSaveRequestDto.builder()
                                                                    .userId(1L)
                                                                    .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                                    .content("내용드갑니다")
                                                                    .maxJoinNum(10)
                                                                    .tagList(new ArrayList<>())
                                                                    .subChallengeSaveRequestDtoList(new ArrayList<>())
                                                                    .title("새로운 챌린지")
                                                                    .build();

        given(challengeRepository.save(any(Challenge.class))).willReturn(challenge);
        given(challengeParticipationRepository.save(any(ChallengeParticipation.class))).willReturn(challengeParticipation);

        //when
        ChallengeParticipationResponseDto responseDto = challengeService.save(requestDto);
        //then
        then(challengeRepository).should(times(1))
                                 .save(any());
        then(challengeParticipationRepository).should(times(1))
                                              .save(any());

        assertThat(responseDto.getChallengeRoleType()).isEqualTo(ChallengeRoleType.ADMIN);
        assertThat(responseDto.getJoinStatus()).isEqualTo(JoinStatus.SUCCEEDED);
        assertThat(responseDto.getUserName()).isEqualTo("testname");


    }



    @Test
    @DisplayName("챌린지에 참가(또는 대기) 유저들을 조회하는 테스트")
    public void 챌린지참가_유저_조회테스트() throws Exception {
        //given
        User user = getUser();
        Challenge challenge = getChallengeWithTag(1L, "챌린지", "내용", user);

        //유저는 테스트 편의상 같은 유저임
        List<ChallengeParticipation> list = new ArrayList<>();

        list.add(getChallengeParticipation(user, challenge, 1L, ChallengeRoleType.ADMIN, JoinStatus.SUCCEEDED));
        list.add(getChallengeParticipation(user, challenge, 2L, ChallengeRoleType.MEMBER, JoinStatus.SUCCEEDED));
        list.add(getChallengeParticipation(user, challenge, 3L, ChallengeRoleType.MEMBER, JoinStatus.SUCCEEDED));
        list.add(getChallengeParticipation(user, challenge, 4L, ChallengeRoleType.RESERVE, JoinStatus.WAIT));
        list.add(getChallengeParticipation(user, challenge, 5L, ChallengeRoleType.RESERVE, JoinStatus.WAIT));

        given(challengeRepository.findById(anyLong())).willReturn(Optional.of(challenge));
        given(challengeParticipationRepository.findAllByChallenge(any(Challenge.class))).willReturn(list);
        //when
        List<ChallengeParticipationResponseDto> responseDtos = challengeService.findParticipationByChallengeId(1L);
        //then
        then(challengeParticipationRepository).should(times(1))
                                              .findAllByChallenge(any(Challenge.class));

        assertThat(responseDtos.size()).isEqualTo(5);
        assertThat(true).isEqualTo(responseDtos.stream()
                                               .map(ChallengeParticipationResponseDto::getChallengeRoleType)
                                               .anyMatch(type -> type.equals(ChallengeRoleType.MEMBER)));


    }
    @Test
    @DisplayName("ChallengeSearch 를 넘기면 검색조건에 맞는 ChallengeList 가 반환 되어야함")
    public void 챌린지_검색_기본검색() throws Exception{
        //given
        ChallengeSearch challengeSearch = new ChallengeSearch();
        List<String> tagList = new ArrayList<>();
        challengeSearch.setTagList(tagList);
        challengeSearch.setTitle("챌린지");

        User user = getUser();
        Challenge challenge1 = getChallengeWithTag(1L, "찾는챌린지", "poo", user);
        Challenge challenge2 = getChallengeWithTag(1L, "찾는챌린지", "poo", user);

        List<Challenge> list = new ArrayList<>();
        list.add(challenge1);
        list.add(challenge2);

        given(challengeRepository.searchChallenge(any(ChallengeSearch.class))).willReturn(list);

        //when
        List<ChallengeResponseDto> dtos = challengeService.searchChallenges(challengeSearch);

        //then
        then(challengeRepository).should(times(1))
                                 .searchChallenge(any(ChallengeSearch.class));
        then(challengeRepository).should(times(0))
                                 .searchToTag(any(ChallengeSearch.class));
        assertThat(dtos.size()).isEqualTo(2);
        assertThat(dtos.get(0)
                       .getTitle()).isEqualTo("찾는챌린지");

    }
    @Test
    @DisplayName("ChallengeSearch 와 태그리스트를 넘기면 검색조건에 맞는 ChallengeList 가 반환 되어야함")
    public void 챌린지_태그검색() throws Exception{
        //given
        User user = getUser();
        Challenge challenge1 = getChallengeWithTag(1L, "찾는챌린지", "poo", user);
        Challenge challenge2 = getChallengeWithTag(1L, "찾는챌린지", "poo", user);

        List<Challenge> list = new ArrayList<>();
        list.add(challenge1);
        list.add(challenge2);

        ChallengeSearch challengeSearch = new ChallengeSearch();
        List<String> tagList = new ArrayList<>();
        tagList.add("여행");
        tagList.add("휴가");
        challengeSearch.setTagList(tagList);
        challengeSearch.setTitle("찾는챌린지");

        given(challengeRepository.searchToTag(any())).willReturn(list);
        //when
        List<ChallengeResponseDto> dtos = challengeService.searchChallenges(challengeSearch);
        //then
        then(challengeRepository).should(times(1))
                                 .searchToTag(any(ChallengeSearch.class));
        then(challengeRepository).should(times(0))
                                 .searchChallenge(any(ChallengeSearch.class));
        assertThat(dtos.size()).isEqualTo(2);
        assertThat(dtos.get(0)
                       .getTitle()).isEqualTo("찾는챌린지");


    }
    @Test
    @DisplayName("챌린지 join 시 남는자리가 없으면 예외가 발생함")
    public void 챌린지_풀방_예외() throws Exception {
        //given
        getUser();
        //when

        //then


    }
    @Test
    @DisplayName("참가요청된 유저의 joinStatus 가 wait 에서 succeeded 로 변경되어야함")
    public void 챌린지참가_상태변경_테스트() throws Exception{
        //given
        getUser();
        //when
        //then
    }

    private Challenge getChallengeWithTag(Long id, String title, String content, User user) {
        Challenge challenge = Challenge.builder()
                                       .tagList(new ArrayList<>())
                                       .uploadTime(LocalDateTime.now())
                                       .modifiedTime(LocalDateTime.now())
                                       .maxJoinNum(10)
                                       .challengeStatus(BucketStatus.ONGOING)
                                       .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                       .id(id)
                                       .title(title)
                                       .content(content)
                                       .user(user)
                                       .build();
        ChallengeTag challengeTag1 = new ChallengeTag(1L, "여행", challenge);
        ChallengeTag challengeTag2 = new ChallengeTag(2L, "해외여행", challenge);
        ChallengeTag challengeTag3 = new ChallengeTag(3L, "휴가", challenge);
        List<ChallengeTag> challengeTagList = new ArrayList<>();
        challengeTagList.add(challengeTag1);
        challengeTagList.add(challengeTag2);
        challengeTagList.add(challengeTag3);
        challenge.updateTagList(challengeTagList);

        return challenge;
    }

    private ChallengeParticipation getChallengeParticipation(User user, Challenge challenge, long id, ChallengeRoleType roleType, JoinStatus joinStatus) {
        ChallengeParticipation challengeParticipation = ChallengeParticipation.builder()
                                                                              .challenge(challenge)
                                                                              .challengeRoleType(roleType)
                                                                              .requestTime(LocalDateTime.now())
                                                                              .id(id)
                                                                              .joinStatus(joinStatus)
                                                                              .joinTime(LocalDateTime.now())
                                                                              .user(user)
                                                                              .build();
        return challengeParticipation;
    }

    private User getUser() {
        return userRepository.findById(1L)
                             .orElseThrow(() -> new IllegalArgumentException("Mock 유저 존재하지않음"));
    }

}