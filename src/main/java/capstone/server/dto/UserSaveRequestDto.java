package capstone.server.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserSaveRequestDto {

    private String nickName;
    private String email;

}
