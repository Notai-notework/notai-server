package notai.domain.email.service;

import notai.domain.email.dto.request.EmailCheckCodeRequest;

public interface EmailService {

    void sendEmailCode(String email);

    void checkEmailCode(EmailCheckCodeRequest request);
}
