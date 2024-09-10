package notai.global.config;

import jakarta.mail.MessagingException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Slf4j
@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private Integer port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private Boolean auth;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private Integer timeout;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private Boolean starttlsEnable;

    @Bean
    public JavaMailSender javaMailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setDefaultEncoding(CharEncoding.UTF_8);
        javaMailSender.setJavaMailProperties(properties());

        try {
            javaMailSender.testConnection();
        } catch (MessagingException e) {
            log.error("mail 연결 테스트 실패 == " + e.getMessage());
        }

        return javaMailSender;
    }

    private Properties properties() {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.timeout", timeout);
        properties.put("mail.smtp.starttls.enable", starttlsEnable);

        return properties;
    }
}
