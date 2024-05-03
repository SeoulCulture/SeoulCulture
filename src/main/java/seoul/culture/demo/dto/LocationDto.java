package seoul.culture.demo.dto;

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
