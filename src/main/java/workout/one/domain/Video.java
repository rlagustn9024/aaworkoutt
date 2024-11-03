package workout.one.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Video extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_seq_generator")
    @SequenceGenerator(
            name = "video_seq_generator",
            initialValue = 3,
            allocationSize = 1
    )
    @Column(name = "video_id")
    private Long id;

    private String originalName;
    private String savedName;
    private Long size;
    private String type;
    private String url;

    /* 생성자 */
    @Builder
    public Video(String originalName, String savedName, Long size, String type, String url) {
        this.originalName = originalName;
        this.savedName = savedName;
        this.size = size;
        this.type = type;
        this.url = url;
    }
}
