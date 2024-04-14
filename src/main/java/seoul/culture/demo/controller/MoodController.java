package seoul.culture.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import seoul.culture.demo.controller.dto.MoodResponseDto;
import seoul.culture.demo.service.MoodService;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MoodController {
    private final MoodService moodService;

    @GetMapping("/moods")
    @ResponseBody
    public MoodResponseDto moods(){
        return moodService.getMoods();
    }
}
