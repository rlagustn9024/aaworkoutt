package workout.one.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Exercise extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_seq_generator")
    @SequenceGenerator(
            name = "exercise_seq_generator",
            initialValue = 11
    )
    @Column(name = "exercise_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_type_id")
    private ExerciseType exerciseType;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detail> details = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "video_id")
    private Video video;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalTime targetTime;
    private Integer totalCount;
    private Integer targetCount;

    /* 연관관계 편의 메서드 */
    public void setMember(Member member) {
        this.member = member;
        member.getExercises().add(this);
    }

    /* 생성 메서드 */
    public static Exercise createExercise(Member member, ExerciseType exerciseType, Video video,
                                          LocalDateTime startTime, LocalDateTime endTime,
                                          LocalTime targetTime, Integer totalCount,
                                          Integer targetCount) {
        Exercise exercise = new Exercise();
        exercise.setMember(member);
        exercise.exerciseType = exerciseType;
        exercise.video = video;
        exercise.startTime = startTime;
        exercise.endTime = endTime;
        exercise.targetTime = targetTime;
        exercise.totalCount = totalCount;
        exercise.targetCount = targetCount;
        return exercise;
    }
}
