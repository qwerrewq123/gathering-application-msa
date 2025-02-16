package my.userservice.async;

import lombok.RequiredArgsConstructor;
import my.userservice.dto.request.EmailCertificationRequest;
import my.userservice.provider.EmailProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncService {

    private final EmailProvider emailProvider;

    @Async("customAsyncExecutor")
    public void asyncTask(EmailCertificationRequest emailCertificationRequest){

        emailCertification(emailCertificationRequest);
    }


    private void emailCertification(EmailCertificationRequest emailCertificationRequest) {

        emailProvider.sendCertificationMail(emailCertificationRequest.getEmail(), certificationNumber());

    }


    private String certificationNumber(){
        int number = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(number);
    }
}
