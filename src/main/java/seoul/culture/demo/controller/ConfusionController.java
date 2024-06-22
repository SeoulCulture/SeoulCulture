package seoul.culture.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import seoul.culture.demo.confusion.ConfusionResponseDto;
import seoul.culture.demo.confusion.ConfusionService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConfusionController {

    private final ConfusionService confusionService;

    @GetMapping("/confusion")
    public List<ConfusionResponseDto> getConfusions() throws IOException {
        return confusionService.getRealTimeConfusions();
    }
}
