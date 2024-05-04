package seoul.culture.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.culture.demo.entity.mark.CulturePlace;
import seoul.culture.demo.entity.Location;

@Repository
public interface CulturePlaceRepository extends JpaRepository<CulturePlace, Long> {
    boolean existsByTitleAndLocation(String title, Location location);
}