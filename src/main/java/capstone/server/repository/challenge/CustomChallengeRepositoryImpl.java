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


@RequiredArgsConstructor
public class CustomChallengeRepositoryImpl implements CustomChallengeRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /*
        꼭 컬랙션을 셔널로감싸서 옵 반환을 해야하나?
     */
    @Override
    public List<Challenge> searchChallenge(ChallengeSearch challengeSearch) {
        return jpaQueryFactory.selectFrom(challenge)
                              .where(challenge.challengePrivacyStatus.eq(BucketPrivacyStatus.PUBLIC), eqTitle(challengeSearch.getTitle()), eqStatus(challengeSearch.getChallengeStatus()))
                              .limit(100)
                              .fetch();
    }


    public BooleanExpression eqTitle(String title) {
        return title != null ? challenge.title.eq(title) : null;
    }

    public BooleanExpression eqStatus(BucketStatus challengeStatus) {
        return challengeStatus != null ? challenge.challengeStatus.eq(challengeStatus) : null;
    }



}
