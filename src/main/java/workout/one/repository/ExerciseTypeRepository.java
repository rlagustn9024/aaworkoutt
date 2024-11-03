package workout.one.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import workout.one.domain.ExerciseType;

public interface ExerciseTypeRepository extends JpaRepository<ExerciseType, Long> {
}
