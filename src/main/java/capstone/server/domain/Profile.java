package capstone.server.domain;


import capstone.server.domain.image.ProfileImage;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Profile {

    @GeneratedValue
    @Id
    @Column(name = "profile_id")
    private Long id;
    @Column
    private short age;

    @OneToOne
    @JoinColumn(name = "profile_image_id")
    private ProfileImage profileImage;


    @Builder
    public Profile(short age, ProfileImage profileImage) {
        this.age = age;
        this.profileImage = profileImage;
    }
}
