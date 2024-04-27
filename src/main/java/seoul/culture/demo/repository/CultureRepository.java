package seoul.culture.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoul.culture.demo.entity.Culture;
import seoul.culture.demo.entity.Location;

public interface CultureRepository extends JpaRepository<Culture, Long> {
    boolean existsByTitleAndLocation(String title, Location location);

    boolean existsByLocation(Location location);
}