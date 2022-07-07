package capstone.server.domain;


import capstone.server.domain.image.ProfileImage;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Profile {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "profile_id")
    private Long id;
    private Integer age;
    private String nickName;

    @OneToOne
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;


    @Builder
    public Profile(Integer age, ProfileImage profileImage) {
        this.age = age;
        this.profileImage = profileImage;
    }
}
