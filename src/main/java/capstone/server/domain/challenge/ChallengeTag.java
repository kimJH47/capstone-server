package capstone.server.domain.challenge;


import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ChallengeTag {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "challenge_tag_id")
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    public ChallengeTag(String content) {
        this.content = content;
    }

    public void changeChallenge(Challenge challenge){
        this.challenge = challenge;
    }

}
