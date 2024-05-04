package seoul.culture.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoul.culture.demo.entity.Location;
import seoul.culture.demo.entity.mark.Mark;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    boolean existsByLocation(Location location);

    boolean existsByTitleAndLocation(String title, Location location);
}