package seoul.culture.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoul.culture.demo.entity.Spot;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    boolean existsBySidoAndSpotName(String sido, String spotName);

    Spot findBySpotName(String place);

    boolean existsBySpotName(String spotName);
}