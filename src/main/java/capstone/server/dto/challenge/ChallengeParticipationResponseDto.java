package capstone.server.dto.challenge;


import capstone.server.domain.challenge.ChallengeParticipation;
import capstone.server.domain.challenge.ChallengeRoleType;
import capstone.server.domain.challenge.JoinStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallengeParticipationResponseDto {

    private Long challengeId;
    private Long id;
    private String userName;
    private ChallengeRoleType challengeRoleType;
    private JoinStatus joinStatus;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime requestTime;

    public ChallengeParticipationResponseDto(ChallengeParticipation challengeParticipation) {

        this.challengeId = challengeParticipation.getChallenge()
                                                 .getId();
        this.id = challengeParticipation.getId();
        this.userName = challengeParticipation.getUser()
                                              .getUsername();
        this.challengeRoleType = challengeParticipation.getChallengeRoleType();
        this.joinStatus = challengeParticipation.getJoinStatus();
        this.requestTime = challengeParticipation.getRequestTime();

    }
}
