package notai.global.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import notai.domain.user.entity.User;
import notai.global.auth.CustomUserDetails;
import notai.global.enums.JwtType;
import notai.global.enums.Role;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.JwtErrorCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (!Pattern.matches("/api/*", requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().equals("/refresh-token")) {

        }

        String authorization = request.getHeader("Authorization");

        // 액세스 토큰이 없는 경우
        if (authorization == null) {
            throw new CustomException(JwtErrorCode.ACCESS_TOKEN_MISSING);
        }

        String token = authorization.split(" ")[1];

        // 액세스 토큰이 유효하지 않은 경우
        if (!jwtTokenProvider.isVerified(token, JwtType.ACCESS)) {
            throw new CustomException(JwtErrorCode.ACCESS_TOKEN_INVALID_SIGNATURE);
        }

        // 액세스 토큰 만료된 경우
//        if (jwtTokenProvider.isExpired(token, JwtType.ACCESS)) {
//
//            // 리프레시 토큰 가져오기
//            String refreshToken = extractRefreshToken(request.getCookies());
//
//            // 리프레시 토큰이 없는 경우
//            if (refreshToken == null) {
//                throw new CustomException(JwtErrorCode.REFRESH_TOKEN_MISSING);
//            }
//
//            refreshToken = refreshToken.split(" ")[1];
//
//            // 리프레시 토큰이 유효하지 않은 경우
//            if (!jwtTokenProvider.isVerified(refreshToken, JwtType.REFRESH)) {
//                throw new CustomException(JwtErrorCode.REFRESH_TOKEN_INVALID_SIGNATURE);
//            }
//
//            String email = jwtTokenProvider.getEmail(refreshToken, JwtType.REFRESH);
//            String name = jwtTokenProvider.getName(refreshToken, JwtType.REFRESH);
//            String role = jwtTokenProvider.getRole(refreshToken, JwtType.REFRESH);
//
//            // 액세스 토큰 재발급
//            String newAccessToken = jwtTokenProvider.generate(email, name, Role.valueOf(role),
//                JwtType.ACCESS);
//
//            response.setHeader("Authorization", "Bearer " + newAccessToken);
//        }

        User user = User.builder()
            .email(jwtTokenProvider.getEmail(token, JwtType.ACCESS))
            .name(jwtTokenProvider.getName(token, JwtType.ACCESS))
            .role(Role.valueOf(jwtTokenProvider.getRole(token, JwtType.ACCESS))).build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            customUserDetails, null,
            customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    // 요청 쿠키에서 리프레시 토큰 추출
    private String extractRefreshToken(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
