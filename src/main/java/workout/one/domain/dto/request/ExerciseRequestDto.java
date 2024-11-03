package workout.one.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import workout.one.domain.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExerciseRequestDto {

    @NotNull(message = "exerciseTypeId 값은 필수입니다")
    private Long exerciseTypeId;

    @NotNull(message = "startTime 값은 필수입니다")
    private LocalDateTime startTime;

    @NotNull(message = "endTime 값은 필수입니다")
    private LocalDateTime endTime;

    @NotNull(message = "targetTime 값은 필수입니다")
    private LocalTime targetTime;

    @Min(value = 0, message = "totalCount 값은 0 이상이어야 합니다")
    @NotNull(message = "totalCount 값은 필수입니다")
    private Integer totalCount;

    @Min(value = 0, message = "targetCount 값은 0 이상이어야 합니다")
    @NotNull(message = "targetCount 값은 필수입니다")
    private Integer targetCount;

    @Valid
    private VideoRequestDto videoRequestDto;

    @Valid
    private List<DetailRequestDto> details;

    public Exercise toEntity(Member member, ExerciseType exerciseType, Video video) {
        return Exercise.createExercise(member, exerciseType, video, startTime, endTime, targetTime
        , totalCount, targetCount);
    }
}
