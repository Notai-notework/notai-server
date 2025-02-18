package notai.domain.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notai.domain.email.dto.request.EmailCheckCodeRequest;
import notai.domain.email.entity.Email;
import notai.domain.email.repository.EmailRepository;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.EmailErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;

    // 메일 인증번호 생성 및 전송
    @Override
    public void sendEmailCode(String email) {

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            messageHelper.setTo(email);
            messageHelper.setFrom(senderEmail, "Notai-notework");
            messageHelper.setSubject("[Notai] 이메일 인증번호 발송");
            messageHelper.setText(generateEmailMessage(email), true);

            ClassPathResource img = new ClassPathResource(
                "static/img/logo.png");
            messageHelper.addInline("logo", img);
        } catch (MessagingException e) {
            log.error("이메일 인증번호 발송 에러 == {}", e.getMessage());
            throw new CustomException(EmailErrorCode.EMAIL_CODE_SENDING_ERROR);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
    }

    // 메일 인증번호 확인
    @Override
    public void checkEmailCode(EmailCheckCodeRequest request) {

        Email foundEmail = emailRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new CustomException(EmailErrorCode.EMAIL_NOT_FOUND));

        System.out.println(foundEmail.getIsExpired());
        // 인증번호 만료
        if (foundEmail.getIsExpired()) {
            throw new CustomException(EmailErrorCode.EMAIL_CODE_EXPIRED);
        }

        // 인증번호 불일치
        if (!Objects.equals(foundEmail.getCode(), request.getCode())) {
            throw new CustomException(EmailErrorCode.EMAIL_CODE_VALID_ERROR);
        }
    }

    // 랜덤 인증번호 생성
    private Integer generateEmailCode() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }

    // 이메일 메시지(HTML) 생성
    private String generateEmailMessage(String email) {

        Integer code = generateEmailCode();

        // 이메일 및 인증번호 저장
        Email foundEmail = emailRepository.findByEmail(email)
            .orElse(Email.builder().email(email).build());
        foundEmail.updateCode(code);

        emailRepository.save(foundEmail);

        return "<html lang=\"ko\">\n"
            + "<body style=\"font-family: Arial, sans-serif; color: #333; background-color: #f5f5f5; padding: 20px; text-align: center;\">\n"
            + "    <div style=\"max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; padding: 20px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\">\n"
//            + "        <h2 style=\"color: #4CAF50;\">Notai</h2>\n"
            + "        <img src='cid:logo' width='600'>"
            + "        <p style=\"font-size: 16px; line-height: 1.5; margin-bottom: 20px;\">\n"
            + "            안녕하세요,<br><br>\n"
            + "            요청하신 인증번호는 아래와 같습니다:\n"
            + "        </p>\n"
            + "        <div style=\"font-size: 24px; font-weight: bold; color: #4CAF50; margin: 20px 0;\">\n"
            + "            " + code + "\n"
            + "        </div>\n"
            + "        <p style=\"font-size: 16px; line-height: 1.5;\">\n"
            + "            이 인증번호는 3분 내에 입력해 주세요.<br>\n"
            + "            인증번호 입력이 늦어질 경우, 재전송 요청을 하셔야 할 수 있습니다.\n"
            + "        </p>\n"
            + "        <p style=\"font-size: 14px; color: #777;\">\n"
            + "            이 메일은 자동으로 발송된 것입니다. 질문이 있으시면 고객 지원팀에 문의해 주세요.\n"
            + "        </p>\n"
            + "    </div>\n"
            + "</body>\n"
            + "</html>\n";
    }
}
