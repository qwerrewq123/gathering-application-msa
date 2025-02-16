package my.alarmservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;

public class UserFeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return null;
    }
}
