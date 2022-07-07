package capstone.server.dto.challenge;

import capstone.server.domain.challenge.JoinStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@Builder
public class ChallengeJoinStatusUpdateDto {

    private Long userId;
    private Long challengeParticipationId;
    @JsonSerialize(using = ToStringSerializer.class)
    private JoinStatus JoinStatus;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime updateTime;

}
