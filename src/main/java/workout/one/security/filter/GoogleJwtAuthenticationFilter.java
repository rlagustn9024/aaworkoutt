/*package workout.one.security.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import workout.one.domain.dto.UserInfoDto;
import workout.one.exception.exceptions.UnauthorizedException;
import workout.one.service.AuthService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class GoogleJwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null && isGoogleToken(token)) { // Google OAuth 토큰인 경우 처리
            try {
                UserInfoDto userInfo = authService.authenticateWithGoogleToken(token); // Google 토큰 인증
                var authentication = new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UnauthorizedException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 Google 토큰입니다.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isGoogleToken(String token) {
        return token.length() > 1000; // Google 토큰인지 단순히 길이로 판단 (개발 중 구체적인 기준으로 변경 가능)
    }
}*/
