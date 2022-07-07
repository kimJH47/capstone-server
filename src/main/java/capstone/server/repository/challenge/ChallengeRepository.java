package capstone.server.repository.challenge;

import capstone.server.domain.challenge.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeRepositoryCustom {

}
