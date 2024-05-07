package seoul.culture.demo.datareader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ConfusionDto {
    private String name;
    private String code;
    private String confusion;
    private String confusionMsg;
    private double maleRate;
    private double femaleRate;
    private double rateIn0to10;
    private double rateIn10;
    private double rateIn20;
    private double rateIn30;
    private double rateIn40;
    private double rateIn50;
    private double rateIn60;
    private double rateIn70;
    private double resentRate;
    private double nonResentRate;
    private LocalDateTime updateTime;
}
