package workout.one.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExerciseType extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "exercise_type_id")
    private Long id;

    @Column(unique = true)
    private String name;
}
