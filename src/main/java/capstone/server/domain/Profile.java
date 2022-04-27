package capstone.server.domain;


import capstone.server.domain.image.ProfileImage;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Profile {

    @GeneratedValue
    @Id
    @Column(name = "profile_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private short age;

    @OneToMany(mappedBy = "profile")
    private List<ProfileImage> profileImages;






}
