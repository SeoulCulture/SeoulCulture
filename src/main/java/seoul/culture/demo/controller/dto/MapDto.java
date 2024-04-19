package seoul.culture.demo.controller.dto;

import lombok.Getter;
import seoul.culture.demo.domain.LocationDto;
import seoul.culture.demo.service.vo.Mark;

import java.util.List;

@Getter
public class MapDto {
    List<Mark> markerInfo;
    LocationDto currentLocation;
}
