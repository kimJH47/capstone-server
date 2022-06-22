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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    public ChallengeTag(String content, Challenge challenge) {
        this.content = content;
        challenge.getTagList()
                 .add(this);
    }

    public ChallengeTag() {

    }

    public void changeChallenge(Challenge challenge){
        this.challenge = challenge;
    }

}
