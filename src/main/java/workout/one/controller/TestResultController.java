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
import workout.one.domain.dto.request.TestResultRequestDto;
import workout.one.domain.dto.response.TestResultResponseDto;
import workout.one.security.JwtUtil;
import workout.one.service.TestResultService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestResultController {

    private final TestResultService testResultService;
    private final JwtUtil jwtUtil;


    @PostMapping("/test-results") // 시험 결과 저장
    public ResponseEntity<Long> createTestResult(@Valid @RequestBody TestResultRequestDto testResultRequestDto, Authentication authentication) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        Long response = testResultService.saveTestResult(testResultRequestDto, userInfo.getMemberId());
        return ResponseEntity.status(HttpStatus.SEE_OTHER)//.header("Location", "/")
                .body(response);
    }

    // 만들어진 테스트 id가지고 testResult 바로 볼 수 있게함 + 단건 조회
    @GetMapping("/test-results/{testResultId}")
    public TestResultResponseDto getTestResult(@PathVariable Long testResultId) {
        return testResultService.findTestResultById(testResultId);
    }

    @GetMapping("/test-results") // 제네릭으로 감싸야 함, 시험 결과 전체 조회
    public List<TestResultResponseDto> getAllTestResults(Authentication authentication) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return testResultService.findAllByMember(userInfo.getMemberId());
    }

    @GetMapping("/test-results/page") // 시험 결과 전체 조회, 페이징
    public Page<TestResultResponseDto> getAllTestResults_Page(Authentication authentication, Pageable pageable) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return testResultService.findAllByMember_Page(userInfo.getMemberId(), pageable);
    }

    @GetMapping("/test-results/month") // 제네릭으로 감싸야 함, month 기준으로 조회
    public List<TestResultResponseDto> getTestResultsByMemberIdAndMonth(Authentication authentication,
                                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return testResultService.findTestResultsByMemberAndMonth(userInfo.getMemberId(), month);
    }

    @GetMapping("/test-results/month/page") // month 기준으로 조회, 페이징
    public Page<TestResultResponseDto> getTestResultsByMemberAndMonth_Page(Authentication authentication,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month,
                                                                           Pageable pageable) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        return testResultService.findTestResultsByMemberMonth_Page(userInfo.getMemberId(), month, pageable);
    }

    @DeleteMapping("/test-results/{testResultId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 시험 결과 1개 삭제
    public void deleteTestResult(@PathVariable Long testResultId, Authentication authentication) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        testResultService.deleteTestResult(testResultId, userInfo.getMemberId());
    }

    @DeleteMapping("/test-results")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 시험 결과 여러 개 삭제
    public void deleteTestResults(@RequestBody List<Long> testResultIds, Authentication authentication) {
        UserInfoDto userInfo = jwtUtil.getUserInfo(authentication);
        testResultService.deleteTestResults(testResultIds, userInfo.getMemberId());
    }
}
