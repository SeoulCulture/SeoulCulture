package seoul.culture.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoul.culture.demo.domain.Mood;

public interface MoodRepository extends JpaRepository<Mood, Long> {
}
