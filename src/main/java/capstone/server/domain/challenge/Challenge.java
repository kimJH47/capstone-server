package capstone.server.domain.challenge;


import capstone.server.domain.BaseTimeEntity;
import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Challenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private BucketPrivacyStatus challengePrivacyStatus;
    @Enumerated(EnumType.STRING)
    private BucketStatus challengeStatus;
    private Integer maxJoinNum;
    private LocalDateTime targetDate;
    //태그검색시 사용
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "id",cascade = CascadeType.ALL)
    private List<ChallengeTag> tagList = new ArrayList<>();


    public void changeUser(User user) {
        this.user = user;
    }

    public void updateTagList(List<ChallengeTag> tagList) {
        tagList.stream()
                .forEach(this::addTag);
    }

    private void addTag(ChallengeTag challengeTag) {
        this.tagList.add(challengeTag);
        challengeTag.changeChallenge(this);
    }

}
