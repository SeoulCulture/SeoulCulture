package seoul.culture.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mood {
    @Id @GeneratedValue
    @Column(name = "mood_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private MoodType mood;

    public Mood(MoodType mood) {
        this.mood = mood;
    }
}
