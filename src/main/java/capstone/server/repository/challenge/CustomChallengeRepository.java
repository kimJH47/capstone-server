package capstone.server.repository.challenge;

import capstone.server.domain.challenge.Challenge;
import capstone.server.domain.challenge.ChallengeSearch;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomChallengeRepository {


    List<Challenge> searchChallenge(ChallengeSearch challengeSearch);
}
