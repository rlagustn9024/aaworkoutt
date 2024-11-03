package workout.one.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workout.one.repository.RefreshTokenRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenCleanupService {

    private final RefreshTokenRepository refreshTokenRepository;

    // 매일 자정에 실행 (cron 표현식으로 설정 가능)
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteExpiredTokens() {
        LocalDateTime currentDate = LocalDateTime.now();
        refreshTokenRepository.deleteExpiredTokens(currentDate);
        System.out.println("유효기간이 지난 refresh 토큰들이 삭제되었습니다.");
    }
}
