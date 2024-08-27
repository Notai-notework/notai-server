package notai.global.auth;

import lombok.RequiredArgsConstructor;
import notai.domain.user.entity.User;
import notai.domain.user.repository.UserRepository;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.UserErrorCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User foundUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        return CustomUserDetails.builder().user(foundUser).build();
    }
}
