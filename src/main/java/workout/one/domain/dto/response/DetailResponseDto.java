package workout.one.domain.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import workout.one.domain.Detail;

import java.time.Duration;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailResponseDto {

    private Long detailId;
    private Integer count;
    private Duration passedTime;

    public DetailResponseDto(Detail detail) {
        this.detailId = detail.getId();
        this.count = detail.getCount();
        this.passedTime = detail.getPassedTime();
    }
}
