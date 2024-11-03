package workout.one.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import workout.one.domain.dto.UserInfoDto;
import workout.one.domain.dto.request.LoginRequestDto;
import workout.one.domain.dto.request.MemberSignupRequestDto;
import workout.one.domain.dto.request.MemberVerificationRequestDto;
import workout.one.domain.dto.response.AuthResponseDto;
import workout.one.domain.dto.response.MemberVerificationResponseDto;
import workout.one.exception.exceptions.InvalidPasswordException;
import workout.one.exception.exceptions.UnauthorizedException;
import workout.one.security.JwtUtil;
import workout.one.service.AuthService;

import java.time.Duration;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @PostMapping("/register") // 회원 가입
    public ResponseEntity<AuthResponseDto> registerMember(@Valid @RequestBody MemberSignupRequestDto memberRequestDto) {

        Map<String, String> tokens = authService.joinMember(memberRequestDto);

        ResponseCookie cookie = ResponseCookie.from("Refresh-Token", tokens.get("refreshToken"))
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/refresh")
                .maxAge(Duration.ofDays(14))
                .build();

        AuthResponseDto responseDto = new AuthResponseDto(tokens.get("accessToken"), "Bearer");

        return ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", "/")
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(responseDto);
    }

    @PostMapping("/login") // 로그인
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {

        Map<String, String> tokens = authService.login(loginRequestDto);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("Refresh-Token", tokens.get("refreshToken"))
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/refresh")
                .maxAge(Duration.ofDays(14))
                .build();

        AuthResponseDto responseDto = new AuthResponseDto(tokens.get("accessToken"), "Bearer");
        
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(responseDto);
    }


    @PostMapping("/refresh") // access 토큰 재발급
    public ResponseEntity<AuthResponseDto> refreshToken(@CookieValue(value = "Refresh-Token", required = false) String refreshToken) {

        if(refreshToken == null) {
            throw new UnauthorizedException("Refresh token이 유효하지 않습니다");
        }
        
        Map<String, String> tokens = authService.refreshAccessToken(refreshToken);

        AuthResponseDto responseDto = new AuthResponseDto(tokens.get("accessToken"), "Bearer");

        if(tokens.get("refreshToken") == null) {
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } else {
            ResponseCookie refreshTokenCookie = ResponseCookie.from("Refresh-Token", tokens.get("refreshToken"))
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .path("/refresh")
                    .maxAge(Duration.ofDays(14))
                    .build();

            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body(responseDto);
        }
    }

    @PostMapping("/member/verify") // 비밀번호 확인
    public MemberVerificationResponseDto verifyPassword_r(Authentication authentication,
                                                                          @Valid @RequestBody MemberVerificationRequestDto dto) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        if(!authService.verifyPassword(userInfo.getMemberId(), dto.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다");
        }

        MemberVerificationResponseDto responseDto = new MemberVerificationResponseDto(jwtUtil.createVerifyToken(userInfo), "Bearer");
        return responseDto;
    }

    // 로그아웃
    @PostMapping("/refresh/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "Refresh-Token", required = false) String refreshToken, Authentication authentication) {

        if (refreshToken != null) {
            UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
            authService.deleteRefreshToken(userInfo.getMemberId());
        }

        ResponseCookie cookie = ResponseCookie.from("Refresh-Token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/refresh")
                .maxAge(0)
                .build();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).header("Location", "/")
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
