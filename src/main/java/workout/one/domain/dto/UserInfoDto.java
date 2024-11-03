package workout.one.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import workout.one.domain.Auth;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserInfoDto { // 여기에 있는 정보들이 토큰에 들어감

    private Long memberId;
    private Auth auth;

    // 권한 이름을 String 타입으로 반환, 토큰 만들 때 String 타입으로 바꿔서 제작
    public String getAuth() {
        return "ROLE_" + auth.name();
    }
}
