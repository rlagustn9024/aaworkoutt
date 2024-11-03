package workout.one.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import workout.one.domain.Auth;
import workout.one.domain.Member;
import workout.one.domain.MemberStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignupRequestDto {

    @Size(max = 30, message = "이름은 30자를 넘길 수 없습니다")
    @NotBlank(message = "이름을 입력하세요")
    private String name;

    @Email(message = "이메일 형식이 올바르지 않습니다")
    @Size(max = 30, message = "이메일은 30자를 넘길 수 없습니다")
    @NotBlank(message = "이메일을 입력하세요")
    private String email;

    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    @NotBlank(message = "비밀번호를 입력하세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다")
    private String password;
    
    @NotBlank(message = "비밀번호 확인을 입력하세요")
    private String passwordConfirm;

    public Member toEntity(String encryptedPassword) {
        return Member.createMember(name, email, encryptedPassword, MemberStatus.ONLINE, Auth.USER);
    }
}
