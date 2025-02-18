package notai.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

        accessSecretKey = Keys.hmacShaKeyFor(
            Base64.getEncoder().encodeToString(accessSecret.getBytes()).getBytes());
        refreshSecretKey = Keys.hmacShaKeyFor(
            Base64.getEncoder().encodeToString(refreshSecret.getBytes()).getBytes());
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
//        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtType == JwtType.ACCESS ? accessSecretKey : refreshSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
//        } catch (ExpiredJwtException e) {
//            return true;
//        }
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

    public Map<String, Object> extractPayload(String token) {
        // JWT를 "."으로 분리
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        // 페이로드 추출
        String payload = parts[1];

        // Base64Url 디코딩
        String decodedPayload = new String(Base64.getUrlDecoder().decode(payload),
            StandardCharsets.UTF_8);

        return convertToMap(decodedPayload);
    }

    // JSON 문자열을 Map으로 변환하는 메서드
    private static Map<String, Object> convertToMap(String json) {
        Map<String, Object> map = new HashMap<>();

        // JSON 문자열을 수동으로 파싱하여 Map으로 변환
        String[] pairs = json.replace("{", "").replace("}", "").replace("\"", "").split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        return map;
    }
}
