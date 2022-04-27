package capstone.server.domain.image;

import capstone.server.domain.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProfileImage {

    @GeneratedValue
    @Id
    private Long id;

    @JoinColumn(name = "profile_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;

    @Column
    private LocalDateTime uploadDate;



}