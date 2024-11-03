package workout.one.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Detail extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detail_seq_generator")
    @SequenceGenerator(
            name = "detail_seq_generator",
            initialValue = 1
    )
    @Column(name = "detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private Integer count;
    private Duration passedTime;

    /* 연관관계 편의 메서드 */
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
        exercise.getDetails().add(this);
    }

    /* 생성 메서드 */
    public static Detail createDetail(Exercise exercise, Integer count, Duration passedTime) {

        Detail detail = new Detail();
        detail.setExercise(exercise);
        detail.count = count;
        detail.passedTime = passedTime;
        return detail;
    }
}
