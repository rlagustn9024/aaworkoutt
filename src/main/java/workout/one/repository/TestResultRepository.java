package workout.one.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import workout.one.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface TestResultRepository extends JpaRepository<TestResult, Long>{

    @Query("select tr from TestResult tr" +
            " join fetch tr.member m" +
            " join fetch tr.test t" +
            " join fetch t.exerciseType et" +
            " where m.id = :memberId")
    List<TestResult> findAllByMemberId(@Param("memberId") Long memberId);

    @Query(value = "select tr from TestResult tr" +
            " join fetch tr.member m" +
            " join fetch tr.test t" +
            " join fetch t.exerciseType et" +
            " where m.id = :memberId",
            countQuery = "select count(tr) from TestResult tr" +
                    " where tr.member.id = :memberId")
    Page<TestResult> findAllByMemberId_Page(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select tr from TestResult tr" +
            " join fetch tr.member m" +
            " join fetch tr.test t" +
            " join fetch t.exerciseType et" +
            " where m.id = :memberId" +
            " and tr.createdDate between :startDate and :endDate")
    List<TestResult> findAllByMemberIdAndMonth(@Param("memberId") Long memberId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate);

    @Query(value = "select tr from TestResult tr" +
            " join fetch tr.member m" +
            " join fetch tr.test t" +
            " join fetch t.exerciseType et" +
            " where m.id = :memberId" +
            " and tr.createdDate between :startDate and :endDate",
            countQuery = "select count(tr) from TestResult tr" +
                    " where tr.member.id = :memberId" +
                    " and tr.createdDate between :startDate and :endDate")
    Page<TestResult> findAllByMemberIdAndMonth_Page(@Param("memberId") Long memberId,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate,
                                                    Pageable pageable);

    @EntityGraph(attributePaths = {"member", "test", "test.exerciseType"})
    Optional<TestResult> findTestResultById(Long testId);

    @Query("select tr from TestResult tr" +
            " where tr.member.id = :memberId" +
            " and tr.id in :testResultIds")
    List<TestResult> findTestResults(List<Long> testResultIds, Long memberId);
}
