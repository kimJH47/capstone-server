package capstone.server.dto.challenge;


import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ChallengeJoinRequestDto {

    @NotNull
    private Long userId;
    @NotNull
    private Long challengeId;
    private LocalDateTime requestTime;


}
