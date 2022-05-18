package capstone.server.dto;

import capstone.server.domain.challenge.JoinStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
public class ChallengeJoinStatusRequestDto {

    @NotNull
    private Long userId;
    @NotNull
    private Long challengeParticipationId;
    @NotNull
    private JoinStatus updateJoinStatus;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime updateTime;

}
