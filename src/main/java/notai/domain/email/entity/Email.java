package notai.domain.email.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import notai.global.entity.BaseTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Email extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(precision = 6)
    private Integer code;

    @Transient
    private Boolean isExpired;

    @PostLoad
    public void isExpired() {
        isExpired = Duration.between(getUpdatedAt(), LocalDateTime.now()).toMinutes() > 3;
    }

    public void updateCode(Integer code) {
        this.code = code;
    }
}
