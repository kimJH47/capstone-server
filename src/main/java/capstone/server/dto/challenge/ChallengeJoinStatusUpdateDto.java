package capstone.server.dto.challenge;

import capstone.server.domain.challenge.JoinStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChallengeJoinStatusUpdateDto {

    //유저 id 정보
    private Long userId;
    private Long challengeId;
    private Long adminParticipationId;

    //변경해야하는 참가정보 id
    private Long challengeParticipationId;
    @JsonSerialize(using = ToStringSerializer.class)
    private JoinStatus JoinStatus;

}
