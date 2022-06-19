package capstone.server.dto;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResponseDto <T>{

    //private int count;
    private T data;
}
