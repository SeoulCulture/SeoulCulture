package seoul.culture.demo.domain;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
public class Location {
    private BigDecimal latitude;
    private BigDecimal longitude;
}
