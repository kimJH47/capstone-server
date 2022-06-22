package capstone.server.domain.challenge;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeRoleType {
    ADMIN("ROLE_ADMIN","챌린지 관리자"),
    MEMBER("ROLE_MEMBER","일반맴버");

    private final String key;
    private final String role;
}
