package workout.one.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import workout.one.domain.Detail;
import workout.one.domain.Exercise;

import java.time.Duration;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailRequestDto {

    @Min(value = 0, message = "count 값은 0 이상이어야 합니다")
    @NotNull(message = "count 값은 필수입니다")
    private Integer count;

    @NotNull(message = "passedTime 값은 필수입니다")
    private Duration passedTime;

    public Detail toEntity(Exercise exercise) {
        return Detail.createDetail(exercise, count, passedTime);
    }
}
