package capstone.server.dto.challenge;


import capstone.server.domain.challenge.ChallengeParticipation;
import capstone.server.domain.challenge.ChallengeRoleType;
import capstone.server.domain.challenge.JoinStatus;
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
    private ChallengeRoleType challengeRoleType;
    private JoinStatus joinStatus;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime requestTime;

    public ChallengeParticipationResponseDto(ChallengeParticipation challengeParticipation) {

        this.userId = challengeParticipation.getUser()
                                            .getUserId();
        this.challengeId = challengeParticipation.getChallenge()
                                                 .getId();

        this.userName = challengeParticipation.getUser()
                                              .getName();

        this.challengeRoleType = challengeParticipation.getChallengeRoleType();
        this.joinStatus = challengeParticipation.getJoinStatus();


    }
}
