package capstone.server.dto.challenge;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class ChallengeJoinRequestDto {

    @NotNull
    private Long userId;
    @NotNull
    private Long challengeId;
    private LocalDateTime requestTime;

}
