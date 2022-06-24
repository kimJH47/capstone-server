package capstone.server.service;

import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.challenge.*;
import capstone.server.dto.challenge.*;
import capstone.server.exception.CustomException;
import capstone.server.repository.UserRepository;
import capstone.server.repository.challenge.ChallengeParticipationRepository;
import capstone.server.repository.challenge.ChallengeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@SpringBootTest
@Transactional
class ChallengeServiceTest {

    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private ChallengeParticipationRepository challengeParticipationRepository;


    @BeforeEach
    public void 테스트유저_생성() {

        List<User> collect = Stream.of(1, 2, 3, 4, 5)
                                   .map(integer -> User.builder()

                                                       .email("email@naver.com")
                                                       .name("test" + integer)
                                                       .build())
                                   .map(user -> userRepository.save(user))
                                   .collect(Collectors.toList());

    }
    @Test
    @DisplayName("ChallengeSearch 를 넘기면 조건에 맞는 ChallengeList 가 반환 되어야함")
    public void challengeBasicSearch() throws Exception{
        //given
        List<String> tagList = new ArrayList<>();
        tagList.add("여행");
        tagList.add("힉압");
        tagList.add("낚시");
        ChallengeSearch challengeSearch = new ChallengeSearch();
        challengeSearch.setTitle("여행");
        challengeSearch.setTagList(tagList);
        createChallenge();
        //when
        List<ChallengeResponseDto> challengeResponseDtos = challengeService.searchChallenges(challengeSearch);

        //then
        challengeResponseDtos.stream()
                             .map(challenge -> challenge.getTitle())
                             .allMatch(s -> s.equals("여행"));

    }
    @Test
    @DisplayName("ChallengeSearch 와 태그리스트를 넘기면 조건에 맞는 ChallengeList 가 반환 되어야함")
    public void challengeTagSearch() throws Exception{

        //given
        List<String> tagList = new ArrayList<>();
        tagList.add("여행");
        ChallengeSearch challengeSearch = new ChallengeSearch();
        challengeSearch.setTagList(tagList);
        createChallenge();
        //when
        List<Challenge> challenges = challengeRepository.searchToTag(challengeSearch);
        //then
        challenges.stream()
                  .map(Challenge::getTitle)
                  .allMatch(s -> s.equals("여행"));
        Assertions.assertEquals(challenges.size(), 3);

    }
    @Test
    @DisplayName("챌린지가 성공적로 저장되어야함")
    public void 챌린지저장_테스트() throws Exception{
        //given
        ChallengeSaveRequestDto requestDto = ChallengeSaveRequestDto.builder()
                                                                    .content("챌린지 1")
                                                                    .maxJoinNum(2)
                                                                    .uploadTime(LocalDateTime.now())
                                                                    .modifiedTime(LocalDateTime.now())
                                                                    .title("챌린지 제목")
                                                                    .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                                    .userId(1L)
                                                                    .build();
        challengeService.save(requestDto);
        //when
        List<Challenge> challenges = challengeRepository.findAll();
        //then
        Challenge challenge = challenges.get(0);
        Assertions.assertEquals(challenge.getId(), 1L);
        Assertions.assertEquals(challenge.getTitle(), requestDto.getTitle());


    }
    @Test
    @DisplayName("태그들과 함깨하는 챌린지저장")
    public void 챌린지저장_테스트_With_태그() throws Exception{
        //given
        List<String> tagList = new ArrayList<>();
        tagList.add("여행");
        tagList.add("도구");
        tagList.add("test");
        tagList.add("사랑");
        ChallengeSaveRequestDto requestDto = ChallengeSaveRequestDto.builder()
                                                                    .content("챌린지 1")
                                                                    .maxJoinNum(2)
                                                                    .uploadTime(LocalDateTime.now())
                                                                    .modifiedTime(LocalDateTime.now())
                                                                    .title("챌린지 제목")
                                                                    .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                                    .userId(1L)
                                                                    .tagList(tagList)
                                                                    .build();

        challengeService.save(requestDto);
        //when
        List<Challenge> challenges = challengeRepository.findAll();
        //then
        Challenge challenge = challenges.get(0);
        Assertions.assertEquals(challenge.getId(), 1L);
        Assertions.assertEquals(challenge.getTitle(), requestDto.getTitle());

        List<ChallengeTag> tagList1 = challenge.getTagList();
        List<String> strings = tagList1.stream()
                                       .map(ChallengeTag::getContent)
                                       .collect(Collectors.toList());

        assertThat(strings, containsInAnyOrder(tagList.toArray()));



    }


    @Test
    @DisplayName("챌린지에 참가(또는 대기) 유저들을 조회하는 테스트")
    public void 챌린지참가_유저_조회테스트() throws Exception{
        //given

        createChallenge();
        List<User> all = userRepository.findAll();

        List<ChallengeJoinRequestDto> dtos = Stream.of(1, 2, 3, 4, 5)
                                                      .map(value -> ChallengeJoinRequestDto.builder()
                                                                                           .userId(Long.valueOf(value))
                                                                                           .requestTime(LocalDateTime.now())
                                                                                           .challengeId(1L)
                                                                                           .build())
                                                      .collect(Collectors.toList());

        for (ChallengeJoinRequestDto dto : dtos) {
            challengeService.join(dto);
        }
        //when
        List<ChallengeParticipationResponseDto> users = challengeService.findUsers(1L);
        //then
        ChallengeParticipationResponseDto responseDto = users.get(1);
        Assertions.assertEquals(responseDto.getChallengeId(), 1L);
        Assertions.assertEquals(responseDto.getChallengeRoleType(), ChallengeRoleType.MEMBER);
        Assertions.assertEquals(responseDto.getUserName(),"test1");




    }

    @Test
    @DisplayName("챌린지에 자리가 없으면 예외가 발생하는 테스트")
    public void challengeFullUsers_test() throws Exception {
        //given
        createUser("test");
        createChallenge();
        Challenge challenge = challengeRepository.findById(1L)
                                                 .get();


        for (int i = 0 ; i<4;i++){
            User save = userRepository.save(new User());
            challengeParticipationRepository.save(ChallengeParticipation.builder()
                                                                        .user(save)
                                                                        .challengeRoleType(ChallengeRoleType.MEMBER)
                                                                        .joinStatus(JoinStatus.SUCCEEDED)
                                                                        .challenge(challenge)
                                                                        .joinTime(LocalDateTime.now())
                                                                        .build());
        }
        //테스트 챌린지 최대인원 5명 / 유저 1명 추가참가
        //when
        //then
        Assertions.assertThrows(CustomException.class, () -> {
            challengeService.join(ChallengeJoinRequestDto.builder()
                                                         .challengeId(1L)
                                                         .userId(1L)
                                                         .build());
        });



    }
    @Test
    @DisplayName("참가요청된 유저의 joinStatus 가 wait 에서 succeeded 로 변경되어야함")
    public void 챌린지참가_상태변경_테스트() throws Exception{
        //given
        createUser("test");
        createUser("참가자");
        createChallenge();
        challengeService.join(ChallengeJoinRequestDto.builder()
                                                     .userId(2L)
                                                     .challengeId(1L)
                                                     .build());
        //when
        challengeService.updateJoinStatus(new ChallengeJoinStatusUpdateDto(2L, 2L, JoinStatus.SUCCEEDED, LocalDateTime.now()));
        //then
        ChallengeParticipation challenge = challengeParticipationRepository.findById(2L)
                                                 .get();
        //Assertions.assertEquals(challenge.getJoinStatus(), JoinStatus.WAIT);
        Assertions.assertEquals(challenge.getJoinStatus(), JoinStatus.SUCCEEDED);
    }
    private void createChallenge() {
        ArrayList<String > tags = new ArrayList<>();
        tags.add("여행");
        challengeService.save(ChallengeSaveRequestDto.builder()
                                                     .content("챌린지 1")
                                                     .maxJoinNum(5)
                                                     .uploadTime(LocalDateTime.now())
                                                     .modifiedTime(LocalDateTime.now())
                                                     .title("챌린지 제목")
                                                     .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                     .userId(1L)
                                                     .tagList(tags)
                                                     .build());


        challengeService.save(ChallengeSaveRequestDto.builder()
                                                     .content("챌린지 2")
                                                     .maxJoinNum(5)
                                                     .uploadTime(LocalDateTime.now())
                                                     .modifiedTime(LocalDateTime.now())
                                                     .title("여행")
                                                     .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                     .userId(1L)
                                                     .tagList(tags)
                                                     .build());

        challengeService.save(ChallengeSaveRequestDto.builder()
                                                     .content("챌린지 1")
                                                     .maxJoinNum(5)
                                                     .uploadTime(LocalDateTime.now())
                                                     .modifiedTime(LocalDateTime.now())
                                                     .title("낚시")
                                                     .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                     .userId(1L)
                                                     .tagList(tags)
                                                     .build());

    }
    private void createUser(String test) {
        userRepository.save(User.builder()
                                .name(test)
                                .email("mail")
                                .build());
    }
}