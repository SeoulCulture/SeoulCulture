package seoul.culture.demo.controller.dto;

import lombok.Getter;
import seoul.culture.demo.domain.LocationDto;
import seoul.culture.demo.service.vo.MarkDto;

import java.util.List;

@Getter
public class MapDto {
    List<MarkDto> markerInfo;
    LocationDto currentLocation;
}
