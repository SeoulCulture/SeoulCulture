package seoul.culture.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import seoul.culture.demo.datareader.SpotDto;
import seoul.culture.demo.entity.Spot;
import seoul.culture.demo.repository.SpotRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SpotController {

    // 실시간 변동 가능
    private final List<SpotDto> spotDtos;
    private final SpotRepository spotRepository;

    @GetMapping("/spots")
    public List<SpotDto> getSpots() {
        if (spotDtos == null || spotDtos.size() == 0) {
            return spotRepository.findAll().stream().map(Spot::toDto).toList();
        }
        return spotDtos;
    }
}