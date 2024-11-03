package workout.one.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workout.one.domain.Detail;
import workout.one.domain.dto.response.DetailResponseDto;
import workout.one.exception.exceptions.NotFoundException;
import workout.one.exception.exceptions.UnauthorizedException;
import workout.one.repository.DetailRepository;
import workout.one.repository.ExerciseRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DetailService {

    private final DetailRepository detailRepository;
    private final ExerciseRepository exerciseRepository;


    /* 운동 디테일 전체 조회 */
    public List<DetailResponseDto> findDetailsByExerciseId(Long exerciseId, Long memberId) {
        validateAuthorization(exerciseId, memberId);
        List<Detail> details = detailRepository.findAllByExerciseId(exerciseId);
        return details.stream().map(d -> new DetailResponseDto(d)).collect(Collectors.toList());
    }

    /* 운동 디테일 전체 조회, 페이징 */
    public Page<DetailResponseDto> findDetailsByExerciseId_Page(Long exerciseId, Pageable pageable, Long memberId) {
        validateAuthorization(exerciseId, memberId);
        Page<Detail> details = detailRepository.findAllByExerciseId_Page(exerciseId, pageable);
        return details.map(DetailResponseDto::new);
    }


    /* month로 운동 디테일 조회 */
    public List<DetailResponseDto> findDetailsByExerciseIdMonth(Long exerciseId, LocalDate month, Long memberId) {
        validateAuthorization(exerciseId, memberId);

        LocalDateTime startDate = month.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = month.withDayOfMonth(month.lengthOfMonth()).atTime(LocalTime.MAX);

        List<Detail> details = detailRepository.findDetailsByExerciseIdMonth(exerciseId, startDate, endDate);
        return details.stream().map(DetailResponseDto::new).collect(Collectors.toList());
    }

    /* month로 운동 디테일 조회, 페이징 */
    public Page<DetailResponseDto> findDetailsByExerciseIdMonth_Page(Long exerciseId, LocalDate month, Pageable pageable,
                                                                     Long memberId) {
        validateAuthorization(exerciseId, memberId);

        LocalDateTime startDate = month.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = month.withDayOfMonth(month.lengthOfMonth()).atTime(LocalTime.MAX);

        Page<Detail> details = detailRepository.findDetailsByExerciseIdMonth_Page(exerciseId, startDate, endDate, pageable);
        return details.map(DetailResponseDto::new);
    }

    @Transactional // 운동 디테일 삭제 (exerciseId에 묶인거 전부 삭제)
    public void deleteDetails(Long exerciseId, Long memberId) {
        // 이걸 삭제하는 사람이 본인인지 확인, 이거 프로젝션으로 조회 하는게 나을듯(memberId만 딱 필요함)
        validateAuthorization(exerciseId, memberId);
        
        // 벌크성 삭제 쿼리 끝난 후에 바로 종료되기 때문에 flush, clear 굳이 안해도 됨
        detailRepository.deleteByExerciseId_bulk(exerciseId);
    }


    // 이걸 삭제, 조회하는 사람이 본인인지 확인, 이거 프로젝션으로 조회 하는게 나을듯(memberId만 딱 필요함)
    // 다른데에서는 Authentication 객체로 자신의 id값이 와서 100% 본인이라는게 확실한데,
    // 여기서는 경로변수만 바꾸면 남의 것 볼 수 있기 때문에 인증 절차 필요
    private void validateAuthorization(Long exerciseId, Long memberId) {

        Long foundMemberId = exerciseRepository.findMemberIdByExerciseId(exerciseId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 운동입니다"));

        if(!foundMemberId.equals(memberId)) {
            throw new UnauthorizedException("권한이 없습니다");
        }
    }
}
