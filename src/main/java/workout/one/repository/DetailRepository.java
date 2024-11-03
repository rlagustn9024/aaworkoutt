package workout.one.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import workout.one.domain.Detail;

import java.time.LocalDateTime;
import java.util.List;

public interface DetailRepository extends JpaRepository<Detail, Long> {

    @Query("select d from Detail d" +
            " where d.exercise.id = :exerciseId")
    List<Detail> findAllByExerciseId(@Param("exerciseId") Long exerciseId);

    @Query(value = "select d from Detail d" +
            " where d.exercise.id = :exerciseId")
    Page<Detail> findAllByExerciseId_Page(Long exerciseId, Pageable pageable);

    @Query("select d from Detail d" +
            " where d.exercise.id = :exerciseId" +
            " and d.createdDate between :startDate and :endDate")
    List<Detail> findDetailsByExerciseIdMonth(Long exerciseId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "select d from Detail d" +
            " where d.exercise.id = :exerciseId" +
            " and d.createdDate between :startDate and :endDate")
    Page<Detail> findDetailsByExerciseIdMonth_Page(Long exerciseId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Modifying
    @Query("delete from Detail d" +
            " where d.exercise.id = :exerciseId")
    void deleteByExerciseId_bulk(Long exerciseId);

    @Modifying
    @Query("delete from Detail d" +
            " where d.exercise.id in :exerciseIds")
    void deleteByExerciseIds_bulk(List<Long> exerciseIds);
}
