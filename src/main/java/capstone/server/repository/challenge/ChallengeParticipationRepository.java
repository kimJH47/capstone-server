package capstone.server.repository.challenge;

import capstone.server.domain.challenge.Challenge;
import capstone.server.domain.challenge.ChallengeParticipation;
import capstone.server.domain.challenge.JoinStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeParticipationRepository extends JpaRepository<ChallengeParticipation, Long> {


    //limit 걸어야함

    @Query("select chp from ChallengeParticipation chp " +
            "where chp.challenge =:challenge " +
            "and chp.joinStatus=:joinStatus")
    List<ChallengeParticipation> findWithPagingByChallengeAndJoinStatus(@Param("challenge") Challenge challenge,
                                                                        @Param("joinStatus") JoinStatus joinStatus, Pageable pageable);
    @Query("select chp from ChallengeParticipation chp join fetch chp.challenge where chp.challenge=:challenge")
    List<ChallengeParticipation> findAllByChallenge(@Param("challenge") Challenge challenge);
    default boolean isFullChallengeUsers(Challenge challenge) {
        int maxUsers = challenge.getMaxJoinNum();
        return findWithPagingByChallengeAndJoinStatus(challenge, JoinStatus.SUCCEEDED,
                PageRequest.of(0, maxUsers)).size() == maxUsers;
    }





}
