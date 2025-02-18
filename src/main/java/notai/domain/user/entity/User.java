package notai.domain.user.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import notai.domain.chat.room.entity.ChatRoom;
import notai.domain.file.entity.File;
import notai.global.entity.BaseTime;
import notai.global.enums.Role;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String phoneNumber;

    @Column(unique = true)
    private String nickname;

    private String address;

    @OneToOne(mappedBy = "user")
    private ChatRoom chatRoom;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_file_id")
    private File imageFile;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updateImageFile(File imageFile) {
        this.imageFile = imageFile;
    }
}
