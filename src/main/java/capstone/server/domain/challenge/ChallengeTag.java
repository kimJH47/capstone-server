package capstone.server.domain.challenge;


import javax.persistence.*;

@Entity
public class ChallengeTag {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;


}
