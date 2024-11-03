package workout.one.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import workout.one.domain.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
