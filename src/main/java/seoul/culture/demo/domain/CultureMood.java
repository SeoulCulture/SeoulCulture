package seoul.culture.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CultureMood {
    @Id @GeneratedValue
    @Column(name = "culture_mood_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Culture culture;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mood mood;
}
