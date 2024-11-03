package workout.one.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(unique = true)
    private Long memberId;
    
    private String refreshToken;
    private LocalDateTime expirationDate;

    public RefreshToken(Long memberId, String refreshToken, LocalDateTime expirationDate) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
        this.expirationDate = expirationDate;
    }
}
