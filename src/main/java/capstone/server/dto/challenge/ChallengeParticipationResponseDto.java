package capstone.server.dto.challenge;


import capstone.server.domain.challenge.ChallengeParticipation;
import capstone.server.domain.challenge.JoinStatus;
import capstone.server.domain.challenge.RoleType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ChallengeParticipationResponseDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long challengeId;
    private String userName;
    private RoleType roleType;
    private JoinStatus joinStatus;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime requestTime;

    public ChallengeParticipationResponseDto(ChallengeParticipation challengeParticipation) {

        this.userId = challengeParticipation.getUser()
                                            .getId();
        this.challengeId = challengeParticipation.getChallenge()
                                                 .getId();

        this.userName = challengeParticipation.getUser()
                                              .getNickName();

        this.roleType = challengeParticipation.getRoleType();
        this.joinStatus = challengeParticipation.getJoinStatus();


    }
}
