package workout.one.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import workout.one.domain.dto.request.MemberUpdateRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
    @SequenceGenerator(
            name = "member_seq_generator",
            initialValue = 4
    )
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    private Auth auth;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exercise> exercises = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestResult> testResults = new ArrayList<>();


    /* 생성자 */
    public Member(Long id, String password, Auth auth) {
        this.id = id;
        this.password = password;
        this.auth = auth;
    }

    /* 생성 메서드 */
    public static Member createMember(String name, String email, String password,
                                      MemberStatus status, Auth auth) {
        Member member = new Member();
        member.name = name;
        member.email = email;
        member.password = password;
        member.status = status;
        member.auth = auth;
        return member;
    }

    /* 비즈니스 로직 */
    public void update(MemberUpdateRequestDto dto) {
        name = dto.getName();
    }
}
