package capstone.server.domain.challenge;


import capstone.server.domain.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class ChallengeParticipation {

    @Id
    @GeneratedValue
    @Column(name = "ChallengeParticipation_id")
    private Long id;


    private LocalDateTime requestTime; //참가요청 시간
    private LocalDateTime joinTime; // 참가시간


    //양방향 매핑 고민
    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @Enumerated(EnumType.STRING)
    private JoinStatus joinStatus;

    //버킷완료 여부 객체 뭘쓸까...







}
