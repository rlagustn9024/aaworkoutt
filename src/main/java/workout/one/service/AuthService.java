package workout.one.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workout.one.domain.Auth;
import workout.one.domain.Member;
import workout.one.domain.RefreshToken;
import workout.one.domain.dto.UserInfoDto;
import workout.one.domain.dto.request.LoginRequestDto;
import workout.one.domain.dto.request.MemberSignupRequestDto;
import workout.one.exception.exceptions.*;
import workout.one.repository.MemberRepository;
import workout.one.repository.RefreshTokenRepository;
import workout.one.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Transactional // 회원 가입
    public Map<String, String> joinMember(MemberSignupRequestDto memberRequestDto) {

        // 중복 이메일 검사
        if(memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new DuplicateEmailException("이미 사용중인 이메일입니다");
        }

        // 비밀번호, 비밀번호 확인 일치 여부 검증
        if(!memberRequestDto.getPassword().equals(memberRequestDto.getPasswordConfirm())) {
            throw new InvalidPasswordException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // Spring Security의 BCryptPasswordEncoder 사용하여 비밀번호 해싱
        String encryptedPasword = passwordEncoder.encode(memberRequestDto.getPassword());

        // Member 저장
        Member member = memberRequestDto.toEntity(encryptedPasword);
        Member savedMember = memberRepository.save(member);

        // JWT 토큰에 넣을 유저정보 Dto 생성
        UserInfoDto userInfoDto = new UserInfoDto(savedMember.getId(), savedMember.getAuth());

        // JWT 토큰 생성
        String accessToken = jwtUtil.createAccessToken(userInfoDto);
        String refreshToken = jwtUtil.createRefreshToken(userInfoDto);
        refreshTokenRepository.save(new RefreshToken(member.getId(), refreshToken, jwtUtil.getExpiration(refreshToken)));

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }


    @Transactional /* 로그인 */
    public Map<String, String> login(LoginRequestDto dto) {
        
        // 사용자 조회
        Member member = memberRepository.findMemberByEmail(dto.getEmail())
                .orElseThrow(() -> new MismatchException("잘못된 이메일 또는 비밀번호입니다"));

        // 비밀번호 검증
        if(!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new MismatchException("잘못된 이메일 또는 비밀번호입니다");
        }

        // JWT 토큰에 넣을 유저정보 Dto 생성
        UserInfoDto userInfo = new UserInfoDto(member.getId(), member.getAuth());
        
        // access Token은 유효기간이 짧기 때문에 무조건 줌
        String accessToken = jwtUtil.createAccessToken(userInfo);
        String refreshToken;

        Map<String, String> tokens = new HashMap<>();
        RefreshToken storedRefreshToken = refreshTokenRepository.findByMemberId(member.getId()).orElse(null);
        
        if (storedRefreshToken != null) { // DB에 refresh 토큰 존재하는 경우
            // 만료일이 지났거나 얼마 남지 않은 경우 refresh 토큰 삭제 후 재발급
            if (jwtUtil.isRefreshTokenExpiringSoon(storedRefreshToken.getRefreshToken())) {
                refreshTokenRepository.delete(storedRefreshToken); // 만료된 토큰 삭제
            } else { // 만료가 임박하지 않았다면 그대로 사용
                refreshToken = storedRefreshToken.getRefreshToken();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                return tokens;
            }
        }

        // DB에 refresh Token 존재하지 않으면 새로 발급 후 저장
        refreshToken = jwtUtil.createRefreshToken(userInfo);
        refreshTokenRepository.save(new RefreshToken(member.getId(), refreshToken, jwtUtil.getExpiration(refreshToken)));

        // accessToken과 refreshToken을 담아 반환
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    /* 비밀번호 검증 */
    public boolean verifyPassword(Long memberId, String password) {
        
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다"));

        return passwordEncoder.matches(password, member.getPassword());
    }


    /* 사용자 ID를 통해 사용자 조회 및 인증 객체 생성 */
    public UserInfoDto loadUserByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다"));
        return new UserInfoDto(member.getId(), member.getAuth());
    }


    /* refresh 토큰을 통해 access 토큰 재발급 */
    @Transactional
    public Map<String, String> refreshAccessToken(String refreshToken) {
        // 이 함수 들어왔으면 일단 refresh 토큰이 있긴 한거임, 대신 검증은 안되었고 기간이 얼마남았는지 모름
        // -> 여기서 검증한 후에 기간 얼마 안남아 있으면 refresh 토큰도 재발급 해줌

        // 해당 사용자의 refresh Token이 맞는지 확인
        Long memberId = jwtUtil.getMemberId(refreshToken);
        RefreshToken storedRefreshToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException("유효하지 않은 refresh Token 입니다"));

        // 여기서 만료되었는지만 먼저 확인하고 만료되었으면 DB에서 삭제
        // 만료된 채로 db에 저장되어 있는 refresh 토큰을 삭제하는 과정임
        if(jwtUtil.isRefreshTokenExpired(refreshToken)) {
            refreshTokenRepository.delete(storedRefreshToken);
        }
        
        // 토큰이 유효하지 않거나 refreshToken이 아닌 경우 예외 발생
        // 여기서도 만료되었는지를 확인하기 때문에 앞에서 먼저 확인하고 삭제 -> 만료된 refresh 토큰이라면 삭제된 후 예외 터짐
        if(!jwtUtil.validateToken(refreshToken) || !"refresh".equals(jwtUtil.getTokenType(refreshToken))) {
            throw new UnauthorizedException("유효하지 않은 refresh Token 입니다");
        }

        // UserInfoDto 생성
        UserInfoDto userInfo = new UserInfoDto(memberId,
                Auth.valueOf(jwtUtil.getRole(refreshToken).replace("ROLE_", "")));

        // access Token 재발급
        String newAccessToken = jwtUtil.createAccessToken(userInfo);
        String newRefreshToken = null;


        // refresh 토큰의 만료일이 얼마 남지 않은 경우 삭제 후 재발급
        if (jwtUtil.isRefreshTokenExpiringSoon(refreshToken)) {
            refreshTokenRepository.delete(storedRefreshToken);
            newRefreshToken = jwtUtil.createRefreshToken(userInfo);
        }

        // accessToken과 refreshToken을 담아 반환
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);
        return tokens;
    }


    @Transactional /* 로그아웃 */
    public void deleteRefreshToken(Long memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }
}
