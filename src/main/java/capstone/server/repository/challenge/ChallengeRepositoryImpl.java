package capstone.server.repository.challenge;

import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.domain.challenge.Challenge;
import capstone.server.domain.challenge.ChallengeSearch;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static capstone.server.domain.challenge.QChallenge.challenge;
import static capstone.server.domain.challenge.QChallengeTag.challengeTag;


@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /*
        꼭 컬랙션을 셔널로감싸서 옵 반환을 해야하나?
     */
    @Override
    public List<Challenge> searchChallenge(ChallengeSearch challengeSearch) {
        return jpaQueryFactory.selectFrom(challenge)
                              .where(challenge.challengePrivacyStatus.eq(BucketPrivacyStatus.PUBLIC),
                                      containsTitle(challengeSearch.getTitle()),
                                      eqStatus(challengeSearch.getChallengeStatus()))
                              .limit(100)
                              .fetch();
    }
    @Override
    public List<Challenge> searchToTag(ChallengeSearch challengeSearch) {
        //join 왼쪽 외래키
        return jpaQueryFactory.select(challenge)
                              .from(challenge)
                              .leftJoin(challenge.tagList, challengeTag)
                              .on(challengeTag.content.in(challengeSearch.getTagList()))
                              .where(challenge.challengePrivacyStatus.eq(BucketPrivacyStatus.PUBLIC),
                                      containsTitle(challengeSearch.getTitle()),
                                      eqStatus(challengeSearch.getChallengeStatus()),
                                      eqMaxJoinNum(challengeSearch.getMaxJoinNum()))
                              .distinct()
                              .fetch();
    }

    public BooleanExpression containsTitle(String title) {
        return title != null ? challenge.title.contains(title) : null;
    }

    public BooleanExpression eqStatus(BucketStatus challengeStatus) {
        return challengeStatus != null ? challenge.challengeStatus.eq(challengeStatus) : null;
    }
    public BooleanExpression eqMaxJoinNum(Integer num) {
        return num != null ? challenge.maxJoinNum.loe(num) : null;
    }

}
