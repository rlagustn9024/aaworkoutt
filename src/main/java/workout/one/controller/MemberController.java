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
import workout.one.domain.dto.request.MemberSignupRequestDto;
import workout.one.domain.dto.request.MemberUpdateRequestDto;
import workout.one.domain.dto.response.AuthResponseDto;
import workout.one.domain.dto.response.MemberResponseDto;
import workout.one.exception.exceptions.UnauthorizedException;
import workout.one.security.JwtUtil;
import workout.one.service.MemberService;

import java.time.Duration;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @GetMapping("/members") // 회원 정보 조회
    public MemberResponseDto getMember(Authentication authentication) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return memberService.findMemberById(userInfo.getMemberId());
    }

    @PatchMapping("/members") // 회원 정보 수정, 할거 없으면 비밀번호 확인하고 비밀번호 변경까지 구현
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMember(@RequestHeader("Verify") String authorizationHeader,
                             @Valid @RequestBody MemberUpdateRequestDto memberUpdateRequestDto,
                             Authentication authentication) {

        String token = authorizationHeader.substring(7);

        if (!jwtUtil.isPasswordVerified(token)) {
            throw new UnauthorizedException("비밀번호 확인이 필요합니다.");
        }

        Long memberId_verify = jwtUtil.getMemberId(token);
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);

        if(!memberId_verify.equals(userInfo.getMemberId())) {
            throw new UnauthorizedException("본인만 수정할 수 있습니다");
        }

        memberService.updateMember(memberId_verify, memberUpdateRequestDto);
    }

    @DeleteMapping("/members")  // 회원 정보 삭제
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@RequestHeader("Verify") String authorizationHeader,
                             Authentication authentication) {

        String token = authorizationHeader.substring(7);

        if(!jwtUtil.isPasswordVerified(token)) {
            throw new UnauthorizedException("비밀번호 확인이 필요합니다.");
        }

        Long memberId_verify = jwtUtil.getMemberId(token);
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        
        if(!memberId_verify.equals(userInfo.getMemberId())) {
            throw new UnauthorizedException("본인만 삭제할 수 있습니다");
        }
        memberService.deleteMember(memberId_verify);
    }
}
