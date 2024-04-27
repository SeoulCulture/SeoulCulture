package seoul.culture.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import seoul.culture.demo.entity.Mood;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MoodResponseDto {
    private List<String> mood;
    public MoodResponseDto(List<Mood> moods){
        this.mood = moods.stream()
                        .map(mood -> mood.getMood().toString())
                        .collect(Collectors.toList());
    }
}
