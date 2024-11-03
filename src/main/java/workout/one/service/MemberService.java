package workout.one.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workout.one.domain.Member;
import workout.one.domain.RefreshToken;
import workout.one.domain.dto.UserInfoDto;
import workout.one.domain.dto.request.MemberSignupRequestDto;
import workout.one.domain.dto.request.MemberUpdateRequestDto;
import workout.one.domain.dto.response.AuthResponseDto;
import workout.one.domain.dto.response.MemberResponseDto;
import workout.one.exception.exceptions.DuplicateEmailException;
import workout.one.exception.exceptions.InvalidPasswordException;
import workout.one.exception.exceptions.NotFoundException;
import workout.one.repository.MemberRepository;
import workout.one.repository.RefreshTokenRepository;
import workout.one.security.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    /* 회원 조회 */
    public MemberResponseDto findMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다"));
        return new MemberResponseDto(member.getName(), member.getEmail(), member.getAuth());
    }
    
    
    @Transactional // 회원 정보 수정
    public void updateMember(Long memberId, MemberUpdateRequestDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다"));
        member.update(dto);
        memberRepository.save(member);
    }

    @Transactional // 회원 삭제
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다"));
        memberRepository.delete(member);
    }
}
