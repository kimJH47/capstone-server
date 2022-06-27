package capstone.server.oauth.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserRefreshToken {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenSeq;

    @Column(length = 64)
    @Size(max = 64)
    private String userId;

    @Column(length = 256)
    @Size(max = 256)
    private String refreshToken;

    public UserRefreshToken(@Size(max = 64) String userId,@Size(max = 256) String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;

    }
}
