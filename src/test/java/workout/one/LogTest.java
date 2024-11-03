package workout.one;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import workout.one.service.ExerciseService;

@SpringBootTest
@ActiveProfiles("test")
public class LogTest {

    @Autowired
    private ExerciseService exerciseService;

    @Test
    public void testExecutionTimeAspect() {
        System.out.println("Starting test for execution time aspect...");


        System.out.println("Finished test for execution time aspect.");
    }
}