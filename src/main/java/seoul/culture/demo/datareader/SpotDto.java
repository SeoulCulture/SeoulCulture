package seoul.culture.demo.datareader;

import lombok.Builder;
import lombok.NoArgsConstructor;
import seoul.culture.demo.entity.Spot;

@Builder
@NoArgsConstructor
public class SpotDto {
    String spotName;
    String sido;
    double latitude;
    double longitude;

    public static SpotDto of(String spotName, String sido, double latitude, double longitude) {
        return new SpotDto(spotName, sido, latitude, longitude);
    }

    private SpotDto(String spotName, String sido, double latitude, double longitude) {
        this.spotName = spotName;
        this.sido = sido;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Spot toEntity() {
        return Spot.of(this.spotName, this.sido, this.latitude, this.longitude);
    }
}
