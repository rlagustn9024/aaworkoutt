package workout.one.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import workout.one.domain.dto.UserInfoDto;
import workout.one.domain.dto.response.DetailResponseDto;
import workout.one.security.JwtUtil;
import workout.one.service.DetailService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exercises/{exerciseId}/details")
public class DetailController { // Detail 저장은 Exercise와 같이 됨

    private final DetailService detailService;
    private final JwtUtil jwtUtil;

    @GetMapping // 운동 디테일 전체 조회
    public List<DetailResponseDto> getAllDetails(Authentication authentication, @PathVariable Long exerciseId) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return detailService.findDetailsByExerciseId(exerciseId, userInfo.getMemberId());
    }

    @GetMapping("/page") // 운동 디테일 전체 조회, 페이징
    public Page<DetailResponseDto> getAllDetails_Page(Authentication authentication, @PathVariable Long exerciseId,
                                                      Pageable pageable) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return detailService.findDetailsByExerciseId_Page(exerciseId, pageable, userInfo.getMemberId());
    }

    @GetMapping("/month") // month 기준으로 조회
    public List<DetailResponseDto> getDetailsByExerciseIdMonth(Authentication authentication, @PathVariable Long exerciseId,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return detailService.findDetailsByExerciseIdMonth(exerciseId, month, userInfo.getMemberId());
    }

    @GetMapping("/month/page") // month 기준으로 조회, 페이징
    public Page<DetailResponseDto> getDetailsByExerciseIdMonth_Page(Authentication authentication, @PathVariable Long exerciseId,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month,
                                                                    Pageable pageable) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return detailService.findDetailsByExerciseIdMonth_Page(exerciseId, month, pageable, userInfo.getMemberId());
    }

    @DeleteMapping // 운동 데이터는 놔두고, Detail 들만 삭제
    public void deteteDetails(@PathVariable Long exerciseId, Authentication authentication) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        detailService.deleteDetails(exerciseId, userInfo.getMemberId());
    }
}
