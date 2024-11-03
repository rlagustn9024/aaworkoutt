package workout.one.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import workout.one.domain.Gender;
import workout.one.domain.Member;
import workout.one.domain.ScoreCriteria;

@Repository
public interface ScoreCriteriaRepository extends JpaRepository<ScoreCriteria, Long> {

    @Query("select sc from ScoreCriteria sc join fetch sc.test t where t.id = :testId and sc.gender = :gender and" +
            " :totalCount between sc.minRange and sc.maxRange")
    ScoreCriteria findScoreByTestAndGenderAndRange(@Param("testId") Long testId,
                                                   @Param("gender") Gender gender,
                                                   @Param("totalCount") Integer totalCount);
}
