package workout.one.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import workout.one.domain.Gender;
import workout.one.domain.Member;
import workout.one.domain.Test;
import workout.one.domain.TestResult;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestResultRequestDto {

    @NotNull(message = "testId 값은 필수입니다")
    private Long testId;

    @NotNull(message = "Gender 값은 필수입니다")
    private Gender gender;

    @Min(value = 0, message = "totalCount 값은 0 이상이어야 합니다")
    @NotNull(message = "totalCount 값은 필수입니다")
    private Integer totalCount;

    public TestResult toEntity(Member member, Test test, Integer score) {
        return TestResult.createTestResult(member, test, gender, totalCount, score);
    }
}
