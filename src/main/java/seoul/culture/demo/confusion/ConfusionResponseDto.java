package seoul.culture.demo.confusion;

import lombok.Builder;
import lombok.Getter;
import seoul.culture.demo.datareader.ConfusionDto;

import java.util.List;

@Getter
@Builder
public class ConfusionResponseDto {
    private String poi;
    private String name;
    private String eng;
    private List<String> areas;
    private ConfusionDto confusionData;
}
