package capstone.server.domain.challenge;


import capstone.server.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallengeParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ChallengeParticipation_id")
    private Long id;


    private LocalDateTime requestTime; //참가요청 시간
    private LocalDateTime joinTime; // 참가시간


    //양방향 매핑 고민
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ChallengeRoleType challengeRoleType;
    @Enumerated(EnumType.STRING)
    private JoinStatus joinStatus;
    //버킷완료 여부 객체 뭘쓸까...,


    public void changeRoleType(ChallengeRoleType challengeRoleType) {
        this.challengeRoleType = challengeRoleType;
    }
    public void changeJoinStatus(JoinStatus joinStatus) {
        this.joinStatus = joinStatus;
    }





}
