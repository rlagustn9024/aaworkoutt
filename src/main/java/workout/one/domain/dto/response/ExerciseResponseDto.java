package workout.one.domain.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import workout.one.domain.Exercise;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExerciseResponseDto {

    private Long exerciseId;
    private String exerciseName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer targetCount;
    private Integer totalCount;

    public ExerciseResponseDto(Exercise exercise) {
        this.exerciseId = exercise.getId();
        this.exerciseName = exercise.getExerciseType().getName();
        this.startTime = exercise.getStartTime();
        this.endTime = exercise.getEndTime();
        this.targetCount = exercise.getTargetCount();
        this.totalCount = exercise.getTotalCount();
    }
}
