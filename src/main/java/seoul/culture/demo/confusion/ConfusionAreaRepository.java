package seoul.culture.demo.confusion;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfusionAreaRepository extends JpaRepository<ConfusionArea, Long> {
    boolean existsByPoi(String poi);

    ConfusionArea getByPoi(String poi);
}