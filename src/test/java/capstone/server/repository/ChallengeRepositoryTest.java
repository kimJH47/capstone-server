package capstone.server.repository;


import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.domain.challenge.*;
import capstone.server.oauth.entity.RoleType;
import capstone.server.repository.challenge.ChallengeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(JpaConfig.class)
public class ChallengeRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private ChallengeRepository challengeRepository;

    @Test
    @DisplayName("ChallengeSearch 를 넘기면 필드조건에 맞는 Challenge List 가 반환되야함")
    public void 챌린지_기본검색() throws Exception{
        //given

        //when

        //then
    }

    @Test
    @DisplayName("ChallengeSearch 를 넘기면 필드조건에 맞는 Challenge List 가 반환되야함")
    public void 챌린지_태그검색() throws Exception{
        //given
        User user = getUser("1", "email@tesat.com", "testName");
        em.persist(user);
        Challenge challenge = getChallenge("testTitle", "testContent", user);

        List<ChallengeTag> list = new ArrayList<>();
        list.add(new ChallengeTag("여행"));
        list.add(new ChallengeTag("휴가"));
        challenge.updateTagList(list);

        challengeRepository.save(challenge);


        //검색되면 안되는 챌린지(태그는 일치하나 컨탠츠는 일치 x
        Challenge otherChallenge = getChallenge("other", "other", user);
        List<ChallengeTag> list2 = new ArrayList<>();
        list2.add(new ChallengeTag("여행"));
        otherChallenge.updateTagList(list2);
        challengeRepository.save(otherChallenge);


        ChallengeSearch challengeSearch = new ChallengeSearch();
        challengeSearch.setTitle("test");
        List<String> tags = new ArrayList<>();
        tags.add("여행");
        challengeSearch.setTagList(tags);


        //when
        List<Challenge> challenges = challengeRepository.searchToTag(challengeSearch);
        //then
        assertThat(true).isEqualTo(challenges.stream()
                                             .map(Challenge::getContent)
                                             .allMatch(content -> content.matches("(.*)test(.*)")));
        assertThat(challenges.size()).isEqualTo(1);
    }
    @Test
    @DisplayName("ChallengeSearch 를 이용해서 검색하지만 조건에 만족하는 챌린지가 없어 0개가 반환되어야함")
    public void 챌린지_태그검색_0개() throws Exception{
        //given
        User user = getUser("1", "email@tesat.com", "testName");
        em.persist(user);
        Challenge challenge = getChallenge("otherTitle", "other1", user);

        List<ChallengeTag> list = new ArrayList<>();
        list.add(new ChallengeTag("여행"));
        list.add(new ChallengeTag("휴가"));
        challenge.updateTagList(list);

        challengeRepository.save(challenge);

        Challenge otherChallenge = getChallenge("otherTitle2", "other2", user);
        List<ChallengeTag> list2 = new ArrayList<>();
        list2.add(new ChallengeTag("여행"));
        otherChallenge.updateTagList(list2);
        challengeRepository.save(otherChallenge);

        ChallengeSearch challengeSearch = new ChallengeSearch();
        List<String> tags = new ArrayList<>();
        tags.add("여행");
        challengeSearch.setTagList(tags);
        //타이틀에 test가 포함되는 챌린지 검색
        challengeSearch.setTitle("test");



        //when
        List<Challenge> challenges = challengeRepository.searchToTag(challengeSearch);
        //then
        assertThat(challenges.size()).isEqualTo(0);


    }
    private Challenge getChallenge(String title, String content, User user) {

        return Challenge.builder()
                        .tagList(new ArrayList<>())
                        .maxJoinNum(10)
                        .challengeStatus(BucketStatus.ONGOING)
                        .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                        .title(title)
                        .content(content)
                        .user(user)
                        .build();
    }

    private ChallengeParticipation getChallengeParticipation(User user, Challenge challenge,  ChallengeRoleType roleType, JoinStatus joinStatus) {
        return ChallengeParticipation.builder()
                                     .challenge(challenge)
                                     .challengeRoleType(roleType)
                                     .requestTime(LocalDateTime.now())
                                     .joinTime(LocalDateTime.now())
                                     .joinStatus(joinStatus)
                                     .user(user)
                                     .build();
    }

    private ChallengeTag challengeTag(String cotent){
        return new ChallengeTag(cotent);
    }
    private User getUser(String userId, String email, String testName) {
        return User.builder()
                   .userId(userId)
                   .password("test")
                   .email(email)
                   .profileImageUrl("/user/image")
                   .emailVerifiedYn("Y")
                   .username(testName)
                   .roleType(RoleType.USER)
                   .build();
    }
}

