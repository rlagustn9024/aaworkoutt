package workout.one.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import workout.one.domain.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
}
