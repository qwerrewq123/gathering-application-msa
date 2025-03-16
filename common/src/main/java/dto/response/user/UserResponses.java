package dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserResponses {
    private String code;
    private String message;
    private List<UserResponsesElement> elements = new ArrayList<>();
}
