package dto.response.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AlarmResponsePage {

    private String code;
    private String message;
//    private Page<AlarmResponse> page;

//    public static AlarmResponsePage of(String code, String message, Page<AlarmResponse> page) {
//        return new AlarmResponsePage(code,message,page);
//    }
}
