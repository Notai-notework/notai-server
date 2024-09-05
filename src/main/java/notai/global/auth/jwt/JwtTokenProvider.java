package notai.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import notai.global.enums.JwtType;
import notai.global.enums.Role;
import notai.global.exception.CustomException;
import notai.global.exception.errorCode.JwtErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey accessSecretKey;
    private final SecretKey refreshSecretKey;
    private final Long accessExpire;
    private final Long refreshExpire;

    public JwtTokenProvider(@Value("${jwt.access.secret}") String accessSecret,
        @Value("${jwt.access.secret}") String refreshSecret,
        @Value("${jwt.access.expire}") Long accessExpire,
        @Value("${jwt.refresh.expire}") Long refreshExpire) {

        accessSecretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encodeToString(accessSecret.getBytes()).getBytes());
        refreshSecretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encodeToString(refreshSecret.getBytes()).getBytes());
        this.accessExpire = accessExpire;
        this.refreshExpire = refreshExpire;
    }

    // 토큰 생성
    public String generate(String email, String name, Role role, JwtType jwtType) {

        Date now = new Date();

        return Jwts.builder()
            .claim("email", email)
            .claim("name", name)
            .claim("role", role.name())
            .setIssuedAt(now)
            .setExpiration(
                new Date(
                    now.getTime() + (jwtType == JwtType.ACCESS ? accessExpire : refreshExpire)))
            .signWith(jwtType == JwtType.ACCESS ? accessSecretKey : refreshSecretKey)
            .compact();
    }

    //토큰 검증
    public boolean isVerified(String token, JwtType jwtType) {
        try {
            extractClaims(token, jwtType);
        } catch (SignatureException e) {
            return false;
        }

        return true;
    }

    // 토큰 만료 여부
    public boolean isExpired(String token, JwtType jwtType) {
        try {
            extractClaims(token, jwtType);
        } catch (ExpiredJwtException e) {
            return true;
        }

        return false;
    }

    public String getEmail(String token, JwtType jwtType) {
        return extractClaims(token, jwtType).get("email", String.class);
    }

    public String getName(String token, JwtType jwtType) {
        return extractClaims(token, jwtType).get("name", String.class);
    }

    public String getRole(String token, JwtType jwtType) {
        return extractClaims(token, jwtType).get("role", String.class);
    }

    private Claims extractClaims(String token, JwtType jwtType)
        throws SignatureException, ExpiredJwtException {

        try {
            return Jwts.parserBuilder()
                .setSigningKey(jwtType == JwtType.ACCESS ? accessSecretKey : refreshSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomException(JwtErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (Exception e) {
            log.error("토큰 파싱 에러 !! == {}", e.getMessage());
            throw e;
        }
    }
}
