package capstone.server.repository.challenge;

import capstone.server.domain.challenge.Challenge;
import capstone.server.domain.challenge.ChallengeSearch;

import java.util.List;

public interface ChallengeRepositoryCustom {


    List<Challenge> searchChallenge(ChallengeSearch challengeSearch);
}
