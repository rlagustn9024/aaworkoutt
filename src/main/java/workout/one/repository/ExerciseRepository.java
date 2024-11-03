package workout.one.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import workout.one.domain.Exercise;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @Query("select e from Exercise e" +
            " join fetch e.exerciseType et" +
            " where e.member.id = :memberId")
    List<Exercise> findAllByMemberId(Long memberId);

    @Query(value = "select e from Exercise e" +
            " join fetch e.exerciseType et" +
            " where e.member.id = :memberId",
            countQuery = "select count(e) from Exercise e" +
                    " where e.member.id = :memberId")
    Page<Exercise> findAllByMemberId_Page(Long memberId, Pageable pageable);

    @Query("select e from Exercise e" +
            " join fetch e.exerciseType et" +
            " where e.member.id = :memberId" +
            " and e.createdDate between :startDate and :endDate")
    List<Exercise> findAllByMemberMonth(Long memberId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "select e from Exercise e" +
            " join fetch e.exerciseType et" +
            " where e.member.id = :memberId" +
            " and e.createdDate between :startDate and :endDate",
            countQuery = "select count(e) from Exercise e" +
                    " where e.member.id = :memberId" +
                    " and e.createdDate between :startDate and :endDate")
    Page<Exercise> findAllByMemberMonth_Page(Long memberId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);


    @Query("select e from Exercise e" +
            " join fetch e.member" +
            " where e.id = :exerciseId")
    Optional<Exercise> findExerciseByExerciseId(Long exerciseId);


    @Query("select e from Exercise e" +
            " where e.member.id = :memberId" +
            " and e.id in :exerciseIds")
    List<Exercise> findExercisesByExerciseMember(List<Long> exerciseIds, Long memberId);

    @Query("select e.member.id from Exercise e" +
            " where e.id = :exerciseId")
    Optional<Long> findMemberIdByExerciseId(Long exerciseId);
}
