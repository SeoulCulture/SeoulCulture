package seoul.culture.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul.culture.demo.controller.dto.MoodResponseDto;
import seoul.culture.demo.domain.Mood;
import seoul.culture.demo.repository.MoodRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoodService {

    private final MoodRepository moodRepository;

    public MoodResponseDto getMoods(){
        List<Mood> moods = moodRepository.findAll();
        return new MoodResponseDto(moods);
    }
}
