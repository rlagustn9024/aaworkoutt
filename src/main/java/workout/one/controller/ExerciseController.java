package workout.one.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import workout.one.domain.dto.UserInfoDto;
import workout.one.domain.dto.request.ExerciseRequestDto;
import workout.one.domain.dto.response.ExerciseResponseDto;
import workout.one.security.JwtUtil;
import workout.one.service.ExerciseService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final JwtUtil jwtUtil;


    @PostMapping("/exercises") // 운동 결과 저장
    public ResponseEntity<Long> createExercise(@Valid @RequestBody ExerciseRequestDto exerciseRequestDto, Authentication authentication) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        Long response = exerciseService.saveExercise(exerciseRequestDto, userInfo.getMemberId());
        return ResponseEntity.status(HttpStatus.SEE_OTHER)//.header("Location", "/")
                .body(response);
    }

    @GetMapping("/exercises") // 운동 결과 전체 조회
    public List<ExerciseResponseDto> getAllExercises(Authentication authentication) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return exerciseService.findExercisesByMember(userInfo.getMemberId());
    }

    @GetMapping("/exercises/page") // 운동 결과 전체 조회, 페이징
    public Page<ExerciseResponseDto> getAllExercises_Page(Authentication authentication, Pageable pageable) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return exerciseService.findExercisesByMember_Page(userInfo.getMemberId(), pageable);
    }

    @GetMapping("/exercises/month") // month 기준으로 조회, 리스트로 반환하지 말고 제네릭으로 마지막에 감싸야 함
    public List<ExerciseResponseDto> getExercisesByMemberMonth(Authentication authentication,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return exerciseService.findExercisesByMemberMonth(userInfo.getMemberId(), month);
    }

    @GetMapping("/exercises/month/page") // month 기준으로 조회, 페이징
    public Page<ExerciseResponseDto> getExercisesByMemberMonth_Page(Authentication authentication,
                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month,
                                                                    Pageable pageable) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return exerciseService.findExercisesByMemberMonth_Page(userInfo.getMemberId(), month, pageable);
    }

    @DeleteMapping("/exercises/{exerciseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 운동 삭제, 최적화 필요
    public void deleteExercise(@PathVariable Long exerciseId, Authentication authentication) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        exerciseService.deleteExercise(exerciseId, userInfo.getMemberId());
    }

    @DeleteMapping("/exercises")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 운동 여러 개 삭제, 최적화 필요
    public void deleteExercises(@RequestBody List<Long> exerciseIds, Authentication authentication) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        exerciseService.deleteExercises(exerciseIds, userInfo.getMemberId());
    }
}
