package seoul.culture.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.culture.demo.entity.mark.CultureEvent;
import seoul.culture.demo.entity.Location;

@Repository
public interface CultureEventRepository extends JpaRepository<CultureEvent, Long> {
    boolean existsByTitleAndLocation(String title, Location location);

    boolean existsByLocation(Location location);
}