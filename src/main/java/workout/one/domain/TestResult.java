package workout.one.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestResult extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_result_seq_generator")
    @SequenceGenerator(
            name = "test_result_seq_generator",
            initialValue = 16
    )
    @Column(name = "test_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private Test test;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer totalCount;
    private Integer score;

    /* 연관관계 편의 메서드 */
    public void setMember(Member member) {
        this.member = member;
        member.getTestResults().add(this);
    }

    /* 생성 메서드 */
    public static TestResult createTestResult(Member member, Test test, Gender gender,
                                              Integer totalCount, Integer score) {
        TestResult testResult = new TestResult();
        testResult.setMember(member);
        testResult.test = test;
        testResult.gender = gender;
        testResult.totalCount = totalCount;
        testResult.score = score;
        return testResult;
    }
}
