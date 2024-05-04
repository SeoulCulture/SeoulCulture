package seoul.culture.demo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import seoul.culture.demo.entity.mark.CultureEvent;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CultureMood {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "culture_mood_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CultureEvent cultureEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mood mood;
}
