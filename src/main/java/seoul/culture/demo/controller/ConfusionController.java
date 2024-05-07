package seoul.culture.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import seoul.culture.demo.개발중.ConfusionResponseDto;
import seoul.culture.demo.개발중.ConfusionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConfusionController {

    private final ConfusionService confusionService;

    @GetMapping("/confusion")
    public List<ConfusionResponseDto> getConfusions() {
        return confusionService.getRealTimeConfusions();
    }
}
