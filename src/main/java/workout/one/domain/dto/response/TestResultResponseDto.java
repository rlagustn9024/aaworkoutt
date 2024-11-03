package workout.one.domain.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import workout.one.domain.Gender;
import workout.one.domain.TestResult;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestResultResponseDto {

    private Long testResultId;
    private String memberName;
    private String testName;
    private String exerciseName;
    private Gender gender;
    private Integer totalCount;
    private Integer score;

    public TestResultResponseDto(TestResult testResult) {
        this.testResultId = testResult.getId();
        this.memberName = testResult.getMember().getName();
        this.testName = testResult.getTest().getName();
        this.exerciseName = testResult.getTest().getExerciseType().getName();
        this.gender = testResult.getGender();
        this.totalCount = testResult.getTotalCount();
        this.score = testResult.getScore();
    }
}
