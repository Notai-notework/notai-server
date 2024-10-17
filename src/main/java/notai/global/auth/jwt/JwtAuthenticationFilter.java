package notai.global.auth.jwt;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notai.global.auth.CustomUserDetails;
import notai.global.enums.JwtType;
import notai.global.enums.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            @SuppressWarnings("unchecked")
            Map<String, String> data = objectMapper.readValue(request.getInputStream(), Map.class);

            String email = data.get("email");
            String password = data.get("password");

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password);

            return authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            log.error("인증 에러 {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        CustomUserDetails principal = (CustomUserDetails) authResult.getPrincipal();

        String email = principal.getEmail();
        String name = principal.getName();
        Role role = principal.getRole();

        String accessToken = jwtTokenProvider.generate(email, name, role, JwtType.ACCESS);
        String refreshToken = jwtTokenProvider.generate(email, name, role, JwtType.REFRESH);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("refresh", "Bearer " + refreshToken);
    }
}
