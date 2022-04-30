package capstone.server.domain.image;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProfileImage {

    @GeneratedValue
    @Id
    @Column(name = "profile_image_id")
    private Long id;
    private String location;
    @Column
    private LocalDateTime uploadDate;



}