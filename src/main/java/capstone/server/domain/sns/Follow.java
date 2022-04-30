package capstone.server.domain.sns;


import capstone.server.domain.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Follow {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;
    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

    private LocalDateTime flowerTime;

}
