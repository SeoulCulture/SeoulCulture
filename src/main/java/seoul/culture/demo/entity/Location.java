package seoul.culture.demo.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Location {
    private Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location of(double latitude, double longitude){
        return new Location(latitude, longitude);
    }

    private double latitude;
    private double longitude;
}