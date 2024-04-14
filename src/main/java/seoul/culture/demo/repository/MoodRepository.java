package seoul.culture.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoul.culture.demo.domain.Mood;
import seoul.culture.demo.domain.MoodType;

public interface MoodRepository extends JpaRepository<Mood, Long> {
    boolean existsByMood(MoodType mood);
}
