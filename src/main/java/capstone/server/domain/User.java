package capstone.server.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class User {

    @GeneratedValue
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column
    @OneToOne(mappedBy = "user")
    private Profile profile;

    @Column
    private String email;

    @Column
    private String nickName;

}
