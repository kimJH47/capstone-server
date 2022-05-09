package capstone.server.domain.image;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImage {

    @GeneratedValue
    @Id
    @Column(name = "profile_image_id")
    private Long id;
    private String location;
    private LocalDateTime uploadDate;



}