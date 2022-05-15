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


    //필드가 3개인데 빌더패턴 고려해야할까 (4개이상일때 고려- 이펙티브자바)


}
