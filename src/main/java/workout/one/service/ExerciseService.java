package workout.one.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import workout.one.domain.*;
import workout.one.domain.dto.request.DetailRequestDto;
import workout.one.domain.dto.request.ExerciseRequestDto;
import workout.one.domain.dto.response.ExerciseResponseDto;
import workout.one.exception.exceptions.*;
import workout.one.repository.*;
import workout.one.repository.ExerciseRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final MemberRepository memberRepository;
    private final ExerciseTypeRepository exerciseTypeRepository;
    private final DetailRepository detailRepository;

    @Transactional // 운동 데이터 저장, 최적화 필요
    public Long saveExercise(ExerciseRequestDto dto, Long memberId) { // 받아온 데이터를 통해 Exercise, Detail, Video 저장

        // 회원, 운동 타입 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원입니다"));
        ExerciseType exerciseType = exerciseTypeRepository.findById(dto.getExerciseTypeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 운동 타입입니다"));

        // Video 생성
        Video video = dto.getVideoRequestDto().toEntity();

        // Exercise 생성
        Exercise exercise = dto.toEntity(member, exerciseType, video);

        // Detail 생성
        List<DetailRequestDto> detailRequestDtos = dto.getDetails();
        detailRequestDtos.forEach(d -> d.toEntity(exercise));

        // cascade 통해 Exercise, Video, Detail 전부 저장 후 id 반환
        return exerciseRepository.save(exercise).getId();
    }

    /* 운동 결과 전체 조회 */
    public List<ExerciseResponseDto> findExercisesByMember(Long memberId) {

        List<Exercise> exercises = exerciseRepository.findAllByMemberId(memberId);
        return exercises.stream().map(e -> new ExerciseResponseDto(e)).collect(Collectors.toList());
    }

    /* 운동 결과 전체 조회, 페이징 */
    public Page<ExerciseResponseDto> findExercisesByMember_Page(Long memberId, Pageable pageable) {
        Page<Exercise> exercises = exerciseRepository.findAllByMemberId_Page(memberId, pageable);
        return exercises.map(e -> new ExerciseResponseDto(e));
    }

    /* month로 운동 결과 조회*/
    public List<ExerciseResponseDto> findExercisesByMemberMonth(Long memberId, LocalDate month) {

        LocalDateTime startDate = month.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = month.withDayOfMonth(month.lengthOfMonth()).atTime(LocalTime.MAX);

        List<Exercise> exercises = exerciseRepository.findAllByMemberMonth(memberId, startDate, endDate);
        return exercises.stream().map(ExerciseResponseDto::new).collect(Collectors.toList());
    }
    
    /* month로 운동 결과 조회, 페이징 */
    public Page<ExerciseResponseDto> findExercisesByMemberMonth_Page(Long memberId, LocalDate month, Pageable pageable) {

        LocalDateTime startDate = month.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = month.withDayOfMonth(month.lengthOfMonth()).atTime(LocalTime.MAX);

        Page<Exercise> exercises = exerciseRepository.findAllByMemberMonth_Page(memberId, startDate, endDate, pageable);
        return exercises.map(ExerciseResponseDto::new);
    }

    @Transactional // 운동 결과 삭제
    public void deleteExercise(Long exerciseId, Long memberId) {
        // id로 운동 조회
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 운동입니다"));
        
        // 본인의 데이터 삭제하는 건지 확인, 이때 getMember.getId()해도 FK를 이미 가지고 있기 때문에 쿼리 안나감
        if(!exercise.getMember().getId().equals(memberId)) {
            throw new UnauthorizedException("삭제 권한이 없습니다");
        }

        // detail은 벌크성 쿼리로 먼저 삭제
        detailRepository.deleteByExerciseId_bulk(exerciseId);
        exerciseRepository.delete(exercise);
    }

    @Transactional // 운동 결과 여러 개 삭제
    public void deleteExercises(List<Long> exerciseIds, Long memberId) {

        List<Exercise> exercises = exerciseRepository.findExercisesByExerciseMember(exerciseIds, memberId);

        // 본인의 데이터 삭제하는 건지 확인
        if(exercises.size() != exerciseIds.size()) {
            throw new UnauthorizedException("삭제 권한이 없습니다");
        }

        // detail은 벌크성 쿼리로 먼저 삭제
        detailRepository.deleteByExerciseIds_bulk(exerciseIds);
        exerciseRepository.deleteAll(exercises);
    }
}
