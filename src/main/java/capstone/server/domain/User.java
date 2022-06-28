package capstone.server.domain;


import capstone.server.oauth.entity.ProviderType;
import capstone.server.oauth.entity.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
public class User extends BaseTimeEntity{

    @JsonIgnore
    @Id
    @Column(name = "user_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(name = "user_id")
    private String userId;

    @Column(length = 100)
    @Size(max = 100)
    private String username;

    @JsonIgnore
    @Column(length = 128)
    @Size(max = 128)
    private String password;

    @Column(length = 512)
    private String email;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(length = 1)
    @Size(min = 1, max = 1)
    private String emailVerifiedYn;

    @Column(length = 512)
    @Size(max = 512)
    private String profileImageUrl;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column
    private String name;

    public void changeProfile(Profile profile) {
        this.profile = profile;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    //생성자 대신 빌더패턴 이용

}
