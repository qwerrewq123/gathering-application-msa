package dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {

    private String code;
    private String message;

    public static Response of(String code, String message) {
        return new Response(code, message);
    }
}
