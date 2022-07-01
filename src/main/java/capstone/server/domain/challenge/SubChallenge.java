package capstone.server.domain.challenge;


import javax.persistence.*;

@Entity
public class SubChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_challenge_id")
    private Long id;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    public void changeChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

}
