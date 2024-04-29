package seoul.culture.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import seoul.culture.demo.datareader.SpotDto;

import java.util.List;

@RestController
public class SpotController {

    // 실시간 변동 가능
    private List<SpotDto> spotDtos;

    public void setSpotDtos(List<SpotDto> spotDtos) {
        this.spotDtos = spotDtos;
    }

    @GetMapping("/spots")
    public List<SpotDto> getSpots() {
        return spotDtos;
    }
}
