package workout.one.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workout.one.domain.*;
import workout.one.domain.dto.request.TestResultRequestDto;
import workout.one.domain.dto.response.TestResultResponseDto;
import workout.one.exception.exceptions.*;
import workout.one.repository.MemberRepository;
import workout.one.repository.ScoreCriteriaRepository;
import workout.one.repository.TestRepository;
import workout.one.repository.TestResultRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TestResultService {

    private final TestResultRepository testResultRepository;
    private final ScoreCriteriaRepository scoreCriteriaRepository;
    private final MemberRepository memberRepository;
    private final TestRepository testRepository;

    @Transactional // 테스트 결과 저장
    public Long saveTestResult(TestResultRequestDto dto, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다"));
        Test test = testRepository.findById(dto.getTestId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테스트입니다"));

        ScoreCriteria sc = scoreCriteriaRepository.findScoreByTestAndGenderAndRange(dto.getTestId(),
                dto.getGender(), dto.getTotalCount());

        if(sc == null) {
            throw new InvalidScoreCriteriaException("해당 조건에 맞는 점수를 찾을 수 없습니다");
        }

        TestResult testResult = dto.toEntity(member, test, sc.getScore());
        return testResultRepository.save(testResult).getId();
    }

    // 전체 테스트 결과 조회
    public List<TestResultResponseDto> findAllByMember(Long memberId) {
        List<TestResult> testResults = testResultRepository.findAllByMemberId(memberId);
        return testResults.stream().map(tr -> new TestResultResponseDto(tr))
                .collect(Collectors.toList());
    }

    // 전체 테스트 결과 조회, 페이징
    public Page<TestResultResponseDto> findAllByMember_Page(Long memberId, Pageable pageable) {
        Page<TestResult> testResults = testResultRepository.findAllByMemberId_Page(memberId, pageable);
        return testResults.map(tr -> new TestResultResponseDto(tr));
    }

    // Month로 테스트 결과 조회
    public List<TestResultResponseDto> findTestResultsByMemberAndMonth(Long memberId, LocalDate month) {

        LocalDateTime startDate = month.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = month.withDayOfMonth(month.lengthOfMonth()).atTime(LocalTime.MAX);
        
        List<TestResult> testResults = testResultRepository.findAllByMemberIdAndMonth(memberId, startDate, endDate);
        return testResults.stream().map(tr -> new TestResultResponseDto(tr))
                .collect(Collectors.toList());
    }

    // Month로 테스트 결과 조회, 페이징
    public Page<TestResultResponseDto> findTestResultsByMemberMonth_Page(Long memberId, LocalDate month, Pageable pageable) {

        LocalDateTime startDate = month.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = month.withDayOfMonth(month.lengthOfMonth()).atTime(LocalTime.MAX);

        Page<TestResult> testResults = testResultRepository.findAllByMemberIdAndMonth_Page(memberId, startDate, endDate, pageable);
        return testResults.map(tr -> new TestResultResponseDto(tr));
    }

    // 테스트 결과 1개 조회
    public TestResultResponseDto findTestResultById(Long testResultId) {
        TestResult testResult = testResultRepository.findTestResultById(testResultId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테스트 결과입니다"));
        return new TestResultResponseDto(testResult);
    }

    @Transactional // 테스트 결과 1개 삭제
    public void deleteTestResult(Long testResultId, Long memberId) {
        TestResult testResult = testResultRepository.findById(testResultId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테스트 결과입니다"));
        
        if(!testResult.getMember().getId().equals(memberId)) {
            throw new UnauthorizedException("삭제 권한이 없습니다");
        }
        testResultRepository.delete(testResult);
    }

    @Transactional // 테스트 결과 여러 개 삭제
    public void deleteTestResults(List<Long> testResultIds, Long memberId) {

        List<TestResult> testResults = testResultRepository.findTestResults(testResultIds, memberId);
        if(testResults.size() != testResultIds.size()) {
            throw new UnauthorizedException("삭제 권한이 없습니다");
        }
        testResultRepository.deleteAll(testResults);
    }
}
