package seoul.culture.demo.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MapDto {
    List<MarkDto> markerInfo;
    LocationDto currentLocation;
}
