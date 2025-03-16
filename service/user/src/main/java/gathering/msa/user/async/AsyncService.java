package gathering.msa.user.async;

import dto.request.user.EmailCertificationRequest;
import gathering.msa.user.provider.EmailProvider;
import gathering.msa.user.service.fail.FailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncService {

    private final EmailProvider emailProvider;
    private final FailService failService;

    @Async("customAsyncExecutor")
    public void asyncTask(EmailCertificationRequest emailCertificationRequest){
        try {
            emailCertification(emailCertificationRequest);
        }catch (MessagingException e){
            failService.send(emailCertificationRequest.getClientId());
        }
    }

    private void emailCertification(EmailCertificationRequest emailCertificationRequest) throws MessagingException {
        emailProvider.sendCertificationMail(emailCertificationRequest.getEmail(), certificationNumber());
    }

    private String certificationNumber(){
        int number = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(number);
    }
}
