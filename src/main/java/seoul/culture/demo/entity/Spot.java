package seoul.culture.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import seoul.culture.demo.datareader.SpotDto;

@Entity
@RequiredArgsConstructor
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long spotId;

    String spotName;

    String sido;

    double latitude;
    double longitude;

    public static Spot of(String spotName, String sido, double latitude, double longitude) {
        return new Spot(spotName, sido, latitude, longitude);
    }

    private Spot(String spotName, String sido, double latitude, double longitude) {
        this.spotName = spotName;
        this.sido = sido;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static SpotDto toDto(Spot entity) {
        return SpotDto.of(entity.spotName, entity.sido, entity.latitude, entity.longitude);
    }
}