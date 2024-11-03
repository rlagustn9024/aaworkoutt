package workout.one.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScoreCriteria {

    @Id @GeneratedValue
    @Column(name = "score_criteria_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private Test test;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer minRange;
    private Integer maxRange;
    private Integer score;

    /* 연관관계 편의 메서드 */
    public void setTest(Test test) {
        this.test = test;
        test.getScoreCriterias().add(this);
    }
}
