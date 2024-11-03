package workout.one.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import workout.one.domain.RefreshToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

    @Modifying
    @Query("delete from RefreshToken rt" +
            " where rt.expirationDate < :currentDate")
    void deleteExpiredTokens(LocalDateTime currentDate);
}
