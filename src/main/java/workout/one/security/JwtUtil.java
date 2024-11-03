package workout.one.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import workout.one.domain.dto.UserInfoDto;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;


@Slf4j
@Component
public class JwtUtil { // JWT 생성, 검증, 파싱

    private final SecretKey key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    // 생성자
    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.access_expiration_time}") long accessTokenExpTime,
                   @Value("${jwt.refresh_expiration_time}") long refreshTokenExpTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }


    public String createAccessToken(UserInfoDto member) {
        return createToken(member, accessTokenExpTime, "access");
    }


    public String createRefreshToken(UserInfoDto userInfo) {
        return createToken(userInfo, refreshTokenExpTime, "refresh");
    }


    private String createToken(UserInfoDto member, long expireTime, String tokenType) {

        Claims claims = Jwts.claims();
        claims.put("memberId", member.getMemberId());
        claims.put("auth", member.getAuth());
        claims.put("tokenType", tokenType);

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 비밀번호 확인 완료 후, 확인 상태 포함하는 verify 토큰 발급
    public String createVerifyToken(UserInfoDto member) {
        Claims claims = Jwts.claims();
        claims.put("memberId", member.getMemberId());
        claims.put("auth", member.getAuth());
        claims.put("tokenType", "verify");
        claims.put("passwordVerified", true);

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusMinutes(10);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    // 주어진 JWT 토큰이 유효한지 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    /* JWT 토큰에서 비밀번호 확인 상태 검증 */
    public boolean isPasswordVerified(String token) {
        Claims claims = parseClaims(token);
        Boolean passwordVerified = claims.get("passwordVerified", Boolean.class);
        return passwordVerified != null && passwordVerified;
    }

    /* refresh 토큰의 만료기간이 얼마 남지 않았거나 만료된 경우 true */
    public boolean isRefreshTokenExpiringSoon(String token) {
        Claims claims = parseClaims(token);
        Date expiration = claims.getExpiration();
        long timeToExpire = expiration.getTime() - System.currentTimeMillis();
        return timeToExpire < 600_000; // 10분 이하로 남았을 때 true 반환
    }


    /* refresh 토큰이 만료된 경우 true */
    public boolean isRefreshTokenExpired(String token) {
        Claims claims = parseClaims(token);
        Date expiration = claims.getExpiration();
        long timeToExpire = expiration.getTime() - System.currentTimeMillis();
        return timeToExpire <= 0; // 10분 이하로 남았을 때 true 반환
    }


    // parseClaimsJws() 함수 -> JWT 토큰의 클레임 정보를 파싱하여 반환
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    } 


    // JWT 토큰에서 사용자 ID 추출
    public Long getMemberId(String token) {
        return parseClaims(token).get("memberId", Long.class);
    }

    // JWT 토큰에서 사용자 권한 추출
    public String getRole(String token) {
        return parseClaims(token).get("auth", String.class);
    }

    // JWT 토큰에서 토큰 종류 추출
    public String getTokenType(String token) {
        return parseClaims(token).get("tokenType", String.class);
    }

    // refresh Token의 만료일 추출
    public LocalDateTime getExpiration(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // authentication 헤더에서 검증된 사용자 정보 추출
    public UserInfoDto getUserInfo(Authentication authentication) {
        return (UserInfoDto) authentication.getPrincipal();
    }
    
}
