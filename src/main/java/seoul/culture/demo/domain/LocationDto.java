package seoul.culture.demo.domain;

import lombok.Getter;

@Getter
public class LocationDto {
    private final String latitude;
    private final String longitude;

    public LocationDto(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
