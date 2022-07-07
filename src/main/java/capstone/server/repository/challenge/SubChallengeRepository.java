package capstone.server.repository.challenge;

import capstone.server.domain.challenge.Challenge;
import capstone.server.domain.challenge.SubChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubChallengeRepository extends JpaRepository<SubChallenge,Long> {

    List<SubChallenge> findByChallenge(Challenge challenge);
}
