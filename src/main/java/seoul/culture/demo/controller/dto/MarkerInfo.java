package seoul.culture.demo.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@RequiredArgsConstructor
public class MarkerInfo {
    private final String category;
    private final BigDecimal latitude;
    private final BigDecimal longitude;
    private final String name;
    private final int price;

}
