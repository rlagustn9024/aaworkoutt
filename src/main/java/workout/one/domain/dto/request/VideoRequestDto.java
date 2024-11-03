package workout.one.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import workout.one.domain.Video;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoRequestDto {

    @NotNull(message = "originalName 값은 필수입니다")
    private String originalName;

    @NotNull(message = "savedName 값은 필수입니다")
    private String savedName;

    @Min(value = 0, message = "값은 0 이상이어야 합니다")
    @NotNull(message = "size 값은 필수입니다")
    private Long size;

    @NotNull(message = "videoType 값은 필수입니다")
    private String videoType;

    @NotNull(message = "url 값은 필수입니다")
    private String url;

    public Video toEntity() {
        return Video.builder()
                .originalName(originalName)
                .savedName(savedName)
                .size(size)
                .type(videoType)
                .url(url)
                .build();
    }
}
